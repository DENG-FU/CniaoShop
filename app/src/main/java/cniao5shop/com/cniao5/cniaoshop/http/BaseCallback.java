package cniao5shop.com.cniao5.cniaoshop.http;

import android.content.Context;
import android.content.Intent;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cniao5shop.com.cniao5.cniaoshop.CniaoApplication;
import cniao5shop.com.cniao5.cniaoshop.LoginActivity;
import cniao5shop.com.cniao5.cniaoshop.utils.ToastUtils;

/**
 * Created by DENGFU on 2016/5/20.
 */
public abstract class BaseCallback<T> {

    public   Type mType;
    Context context;

    //将泛型转化为具体的类型,使用Gson转换json数据是需要使用
    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
    public BaseCallback(Context context)
    {
        this.context = context;
        mType = getSuperclassTypeParameter(getClass());
    }

    //联网请求前
    public abstract void onRequestBefore(Request request);
    //联网请求失败
    public abstract void onFailure(Request request, IOException e);
    //联网请求成功有回应
    public abstract  void onResponse(Response response);
    //联网有回应且操作请求成功
    public abstract void onSuccess(Response response,T t);
    //联网有回应但操作请求失败
    public abstract void onError(Response response, int code, Exception e);

    public void onTokenError(Response response, int code){
        ToastUtils.show(context,"登录失效，请重新登录");

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);

        CniaoApplication.getmInstance().clearUser();
    }

}
