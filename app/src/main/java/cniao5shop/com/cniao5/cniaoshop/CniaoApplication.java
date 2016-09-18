package cniao5shop.com.cniao5.cniaoshop;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;

import cniao5shop.com.cniao5.cniaoshop.bean.User;
import cniao5shop.com.cniao5.cniaoshop.utils.UserLocalData;


public class CniaoApplication extends Application {

    private User user;
    private static CniaoApplication mInstance;

    public static CniaoApplication getmInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        initUser();
        Fresco.initialize(this);
    }

    //初始化全局变量user，即从本地读取user信息
    private void initUser() {
        this.user = UserLocalData.getUser(this);
    }

    public User getUser(){
        return user;
    }
    //设置user并将其存储到本地
    public void putUser(User user, String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }
    //清除user
    public void clearUser(){
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    private Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }
    public Intent getIntent(){
        return this.intent;
    }
    public void jumpToTargetActivity(Context context){
        context.startActivity(intent);
        this.intent = null;
    }

}
