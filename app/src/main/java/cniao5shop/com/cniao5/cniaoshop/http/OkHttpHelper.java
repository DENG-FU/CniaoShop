package cniao5shop.com.cniao5.cniaoshop.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by DENGFU on 2016/5/20.
 */
public class OkHttpHelper {
    private static OkHttpClient okHttpClient;
    private Gson gson;
    private Handler handler;

    final static String TAG = "OkHttpHelper";

    enum HttpMethodType{
        GET,
        POST
    }

    //单例
    private OkHttpHelper(){
        okHttpClient = new OkHttpClient();
        //基本联网属性设置
        okHttpClient.setConnectTimeout(100, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(100, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(100, TimeUnit.SECONDS);

        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    };

    public static OkHttpHelper getInstance(){
        return new OkHttpHelper();
    }

    //GET请求
    public void get(String url, BaseCallback callback){
        Request request = buildRequest(url,null,HttpMethodType.GET);
        doRequest(request,callback);
    }
    //POST请求
    public void post(String url, Map<String,Object> params, BaseCallback callback){
        Request request = buildRequest(url,params,HttpMethodType.POST);
        doRequest(request,callback);
    }
    //判断请求类型，POST请求可携带参数
    private Request buildRequest(String url,Map<String,Object> params,HttpMethodType methodType){
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (methodType == HttpMethodType.GET){
            builder.get();
        }else if (methodType == HttpMethodType.POST){
            RequestBody body = buildFormData(params);
            builder.post(body);
        }
        return builder.build();
    }
    //以提供的参数构建请求体
    private RequestBody buildFormData(Map<String,Object> params){
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (params != null){
            //循环读取Map中数据
            for (Map.Entry<String,Object> entry : params.entrySet()){
                builder.add(entry.getKey(),entry.getValue().toString());
            }
        }
        return builder.build();
    }
    //执行亲求，并调用自定义的回掉函数
    public void doRequest(final Request request, final BaseCallback callback){
        //执行请求前
        callback.onRequestBefore(request);
        //执行异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //连接请求失败
                callbackFailure(callback,request,e);
            }
            @Override
            public void onResponse(final Response response) throws IOException {
                //有响应
                callbackResponse(callback,response);
                if (response.isSuccessful()){
                    //操作执行成功
                    final String resultStr = response.body().string();
                    Log.e(TAG,"result"+resultStr);
                    if (callback.mType == String.class){
                        //如果返回的是String类型，则可不用进行转换
                        callbackSuccess(callback,response,resultStr);
                    }else {
                        try {
                            //通过Gson转换为制定类型
                            Object object = gson.fromJson(resultStr,callback.mType);
                            callbackSuccess(callback,response,object);
                        }catch (JsonParseException e){
                            //转换失败
                            callbackError(callback,response,e);
                        }
                    }
                }else{
                    //操作执行失败
                    callbackError(callback,response,null);
                }
            }
        });
    }

    //把四个回掉函数都丢到主线程去执行
    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }
    private void callbackResponse(final BaseCallback callback, final Response response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }
    private void callbackSuccess(final BaseCallback callback, final Response response, final Object obj) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });
    }
    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }


}
