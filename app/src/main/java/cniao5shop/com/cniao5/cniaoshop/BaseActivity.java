package cniao5shop.com.cniao5.cniaoshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import cniao5shop.com.cniao5.cniaoshop.bean.User;

/**
 * Created by DENGFU on 2016/6/21.
 */
public class BaseActivity extends AppCompatActivity {

    protected static final String TAG = BaseActivity.class.getSimpleName();
    //判断开启的页面是否需要登录权限
    public void startActivity(Intent intent, boolean isNeedLogin){
        if (isNeedLogin){
            User user = CniaoApplication.getmInstance().getUser();
            if (user != null){
                super.startActivity(intent);
            }else {
                CniaoApplication.getmInstance().putIntent(intent);
                Intent loginIntent = new Intent(this,LoginActivity.class);
                super.startActivity(intent);
            }
        }else {
            super.startActivity(intent);
        }
    }
}
