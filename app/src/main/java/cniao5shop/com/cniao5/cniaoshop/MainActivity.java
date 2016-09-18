package cniao5shop.com.cniao5.cniaoshop;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cniao5shop.com.cniao5.cniaoshop.bean.Tab;
import cniao5shop.com.cniao5.cniaoshop.fragment.CartFragment;
import cniao5shop.com.cniao5.cniaoshop.fragment.CategoryFragment;
import cniao5shop.com.cniao5.cniaoshop.fragment.HomeFragment;
import cniao5shop.com.cniao5.cniaoshop.fragment.HotFragment;
import cniao5shop.com.cniao5.cniaoshop.fragment.MineFragment;
import cniao5shop.com.cniao5.cniaoshop.widget.FragmentTabHost;


public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private LayoutInflater mInflactor;
    private CartFragment cartFragment;
    private List<Tab> mTabs = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化底部菜单栏
        initTab();
    }

    //初始化底部菜单栏，为FragmentTabHost添加TabSpec
    private void initTab() {
        //获取FragmentTabHost控件
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        mInflactor = LayoutInflater.from(this);

        //获取TextView、ImageView、Fragment资源
        Tab tab_home = new Tab(R.string.home,R.drawable.selector_icon_home, HomeFragment.class);
        Tab tab_hot = new Tab(R.string.hot,R.drawable.selector_icon_hot,HotFragment.class);
        Tab tab_category = new Tab(R.string.catagory,R.drawable.selector_icon_category,CategoryFragment.class);
        Tab tab_cart = new Tab(R.string.cart,R.drawable.selector_icon_cart,CartFragment.class);
        Tab tab_mine = new Tab(R.string.mine,R.drawable.selector_icon_mine,MineFragment.class);

        //将资源统一添加到ArrayList中，方便操作
        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        for (Tab tab : mTabs){
            //新建一个TabSpec
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));

            //ImageView和TextView添加资源
            View view = mInflactor.inflate(R.layout.tab_indicator,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.icon_tab);
            TextView textView = (TextView) view.findViewById(R.id.txt_indicator);
            imageView.setBackgroundResource(tab.getIcon());
            textView.setText(tab.getTitle());

            //设置Indicator
            tabSpec.setIndicator(view);
            //添加TabSpec和相应的Fragment
            mTabHost.addTab(tabSpec, tab.getFragment(),null);
        }
        //切换到购物车页面时，需要更新购物车
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId==getString(R.string.cart)){
                    refData();
                }
            }
        });
        //设置第一个页面为当前页面
        mTabHost.setCurrentTab(0);
    }

    //更新购物车中的数据
    private void refData(){
        if(cartFragment == null){
            Fragment fragment =  getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if(fragment !=null){
                cartFragment= (CartFragment) fragment;
                cartFragment.refData();
            }
        }
        else{
            cartFragment.refData();
        }
    }

}
