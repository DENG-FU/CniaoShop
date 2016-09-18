package cniao5shop.com.cniao5.cniaoshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;

import cniao5shop.com.cniao5.cniaoshop.CniaoApplication;
import cniao5shop.com.cniao5.cniaoshop.LoginActivity;
import cniao5shop.com.cniao5.cniaoshop.bean.User;

/**
 * Created by DENGFU on 2016/6/6.
 */
public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createView(inflater, container, savedInstanceState);
        ViewUtils.inject(this, view);

        initToolBar();
        init();
        return view;
    }

    public void initToolBar() {
    }

    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();
    //判断打开一个页面是否需要登陆权限
    public void startActivity(Intent intent, boolean isNeedLogin){
        if (isNeedLogin){
            User user = CniaoApplication.getmInstance().getUser();
            if (user != null){
                super.startActivity(intent);
            }else {
                CniaoApplication.getmInstance().putIntent(intent);
                super.startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        }else {
            super.startActivity(intent);
        }
    }
}
