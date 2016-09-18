package cniao5shop.com.cniao5.cniaoshop.http;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import cniao5shop.com.cniao5.cniaoshop.CniaoApplication;
import cniao5shop.com.cniao5.cniaoshop.LoginActivity;
import cniao5shop.com.cniao5.cniaoshop.utils.ToastUtils;
import dmax.dialog.SpotsDialog;

/**
 * Created by DENGFU on 2016/5/20.
 *
 * 使用开源库SpotsDialog，显示加载资源时的加载进度
 */
public abstract class SpotsCallback<T> extends BaseCallback<T>{

    private SpotsDialog dialog;
    private Context context;

    public SpotsCallback(Context context){
        super(context);
        this.context = context;
        dialog = new SpotsDialog(context,"资源加载中…");
    }

    public void showDialog(){
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        dismissDialog();
        Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }



}
