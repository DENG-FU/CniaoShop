package cniao5shop.com.cniao5.cniaoshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import cniao5shop.com.cniao5.cniaoshop.bean.User;
import cniao5shop.com.cniao5.cniaoshop.http.BaseCallback;
import cniao5shop.com.cniao5.cniaoshop.http.OkHttpHelper;
import cniao5shop.com.cniao5.cniaoshop.http.SpotsCallback;
import cniao5shop.com.cniao5.cniaoshop.msg.LoginRespMsg;
import cniao5shop.com.cniao5.cniaoshop.utils.CountTimerView;
import cniao5shop.com.cniao5.cniaoshop.utils.DESUtil;
import cniao5shop.com.cniao5.cniaoshop.utils.ManifestUtil;
import cniao5shop.com.cniao5.cniaoshop.utils.ToastUtils;
import cniao5shop.com.cniao5.cniaoshop.widget.CNiaoToolBar;
import cniao5shop.com.cniao5.cniaoshop.widget.ClearEditText;
import dmax.dialog.SpotsDialog;

public class RegSecondActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private CNiaoToolBar mToolBar;
    @ViewInject(R.id.txtTip)
    private TextView mTxtTip;
    @ViewInject(R.id.btn_reSend)
    private Button mBtnResend;
    @ViewInject(R.id.edittxt_code)
    private ClearEditText mEtCode;

    private String phone;
    private String pwd;
    private String countryCode;

    private CountTimerView countTimerView;
    private SMSEventHandler eventHandler;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_second);
        ViewUtils.inject(this);

        initToolBar();
        dialog = new SpotsDialog(this,"正在注册...");

        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);
        String text = "已发送<font color=#209526>验证码</font>短信到这个号码：" + formatedPhone;
        mTxtTip.setText(Html.fromHtml(text));

        CountTimerView timerView = new CountTimerView(mBtnResend);
        timerView.start();

        SMSSDK.initSDK(this, ManifestUtil.getMetaDataValue(this, "mob_sms_appKey"),
                ManifestUtil.getMetaDataValue(this, "mob_sms_appSecrect"));

        eventHandler = new SMSEventHandler();
        SMSSDK.registerEventHandler(eventHandler);
    }

    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }

    private void initToolBar() {
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCode();
            }
        });
    }

    private void submitCode() {
        String vCode = mEtCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.show(this, "请填写验证码");
            return;
        }
        dialog.show();
        SMSSDK.submitVerificationCode(countryCode, phone, vCode);
    }

    @OnClick(R.id.btn_reSend)
    public void reSendCode(View view){
        SMSSDK.getVerificationCode("+"+countryCode,phone);
        countTimerView = new CountTimerView(mBtnResend, cn.smssdk.gui.R.string.smssdk_resend_identify_code);
        countTimerView.start();

    }

    public void doReg(){
        Map<String,Object> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Constants.DES_KEY,pwd));

        okHttpHelper.post(Constants.API.REG, params, new BaseCallback<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                if (userLoginRespMsg.getStatus() == LoginRespMsg.STATUS_ERROR){
                    ToastUtils.show(RegSecondActivity.this,"注册失败："+userLoginRespMsg.getMessage());
                    return;
                }
                CniaoApplication application = CniaoApplication.getmInstance();
                application.putUser(userLoginRespMsg.getData(),userLoginRespMsg.getToken());

                startActivity(new Intent(RegSecondActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);
            }
            @Override
            public void onRequestBefore(Request request) {
            }
            @Override
            public void onFailure(Request request, IOException e) {
                if (dialog!=null && dialog.isShowing())
                    dialog.dismiss();
            }
            @Override
            public void onResponse(Response response) {
                if (dialog!=null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }

    class SMSEventHandler extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");

                            ToastUtils.show(RegSecondActivity.this,"验证成功："+phone+",country:"+country);
                            doReg();
                        } else {
                            try {
                                ((Throwable) data).printStackTrace();
                                Throwable throwable = (Throwable) data;

                                JSONObject object = new JSONObject(
                                        throwable.getMessage());
                                String des = object.optString("detail");
                                if (!TextUtils.isEmpty(des)) {
                                    // ToastUtils.show(RegSecondActivity.this, des);
                                    return;
                                }
                            } catch (Exception e) {
                                SMSLog.getInstance().w(e);
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
