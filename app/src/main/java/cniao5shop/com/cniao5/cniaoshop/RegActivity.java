package cniao5shop.com.cniao5.cniaoshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import cniao5shop.com.cniao5.cniaoshop.utils.ManifestUtil;
import cniao5shop.com.cniao5.cniaoshop.utils.ToastUtils;
import cniao5shop.com.cniao5.cniaoshop.widget.CNiaoToolBar;
import cniao5shop.com.cniao5.cniaoshop.widget.ClearEditText;
import dmax.dialog.SpotsDialog;

/**
 * Created by DENGFU on 2016/7/22.
 */
public class RegActivity extends BaseActivity {

    private static final String TAG = "RegActivity";

    //default using the china zone description
    private static final String DEFAULT_COUNTRY_ID = "42";

    @ViewInject(R.id.toolbar)
    private CNiaoToolBar mToolBar;
    @ViewInject(R.id.txtCountry)
    private TextView mTxtCountry;
    @ViewInject(R.id.txtCountryCode)
    private TextView mTxtCountryCode;
    @ViewInject(R.id.edittxt_phone)
    private ClearEditText mEtxtPhone;
    @ViewInject(R.id.edittxt_pwd)
    private ClearEditText mEtxtPwd;

    private SMSEvenHanlder eventHandler;
    private SpotsDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        ViewUtils.inject(this);

        initToolBar();
        dialog = new SpotsDialog(this,"请求验证码...");

        SMSSDK.initSDK(this, ManifestUtil.getMetaDataValue(this,"mob_sms_appKey"),
                ManifestUtil.getMetaDataValue(this,"mob_sms_appSecrect"));
        eventHandler = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(eventHandler);

        String[] country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        if (country != null){
            mTxtCountryCode.setText("+"+country[1]);
            mTxtCountry.setText(country[0]);
        }
    }

    private void initToolBar() {
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });
        mToolBar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RegActivity.this.finish();
            }
        });
    }

    private void getCode() {
        String phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*","");
        String code = mTxtCountryCode.getText().toString().trim();

        checkPhoneNum(phone,code);
        dialog.show();
        //not 86    +86
        SMSSDK.getVerificationCode(code,phone);
    }
    //check the zone description and the phone number
    private void checkPhoneNum(String phone, String code) {
        if (code.startsWith("+"))
            code = code.substring(1);

        if (TextUtils.isEmpty(phone)){
            ToastUtils.show(this,"please input the phone number");
            return ;
        }
        if ("86".equals(code)){
            if (phone.length() != 11){
                ToastUtils.show(this,"the phone number length is incorrect");
                return ;
            }
        }
        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);
        if (!m.matches()){
            ToastUtils.show(this,"the phone format is incorrect");
            return ;
        }

    }

    class SMSEvenHanlder extends EventHandler{
        @Override
        public void afterEvent(final int event, final int result, final Object data) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog!=null && dialog.isShowing())
                        dialog.dismiss();
                    if (result == SMSSDK.RESULT_COMPLETE){
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                            onCountryListGot((ArrayList<HashMap<String,Object>>) data);
                        }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                            afterVerificationCodeRequested((Boolean) data);
                        }else if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){

                        }
                    }else{
                        //show the error message
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)){
                                ToastUtils.show(RegActivity.this,des);
                                return;
                            }
                        }catch (Exception e){
                            SMSLog.getInstance().w(e);
                        }
                    }
                }
            });
        }
    }
    //jump to the verification code input page
    private void afterVerificationCodeRequested(Boolean data) {
        String phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*","");
        String code = mTxtCountryCode.getText().toString().trim();
        String pwd = mEtxtPwd.getText().toString().trim();

        if (code.startsWith("+")){
            code = code.substring(1);
        }

        Intent intent = new Intent(this,RegSecondActivity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("pwd",pwd);
        intent.putExtra("countryCode",code);
        startActivity(intent);
    }

    private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
        //analyse the country list
        for (HashMap<String,Object>country:countries){
            String code = (String)country.get("zone");
            String rule = (String)country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)){
                continue;
            }
            Log.d(TAG,"code="+code+"rule="+rule);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}