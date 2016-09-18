package cniao5shop.com.cniao5.cniaoshop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cniao5shop.com.cniao5.cniaoshop.R;

public class CategoryFragment extends BaseFragment {

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container,false);
    }

    @Override
    public void init() {

    }
}



