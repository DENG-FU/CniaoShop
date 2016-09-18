package cniao5shop.com.cniao5.cniaoshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.w3c.dom.Text;

import cniao5shop.com.cniao5.cniaoshop.widget.CNiaoToolBar;
import cniao5shop.com.cniao5.cniaoshop.widget.ClearEditText;


public class LoginActivity extends AppCompatActivity {

    @ViewInject(R.id.toolbar)
    private CNiaoToolBar mToolBar;
    @ViewInject(R.id.etxt_phone)
    private ClearEditText editPhone;
    @ViewInject(R.id.etxt_pwd)
    private ClearEditText editPwd;
    @ViewInject(R.id.btn_login)
    private Button loginButton;
    @ViewInject(R.id.txt_toReg)
    private TextView regText;
    @ViewInject(R.id.txt_toFind)
    private TextView findText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        regText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegActivity.class);
                startActivity(intent);
            }
        });

        initToolBar();
    }

    private void initToolBar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }


}
