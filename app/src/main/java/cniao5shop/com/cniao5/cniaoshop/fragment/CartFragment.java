package cniao5shop.com.cniao5.cniaoshop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import cniao5shop.com.cniao5.cniaoshop.R;
import cniao5shop.com.cniao5.cniaoshop.adapter.CartAdapter;
import cniao5shop.com.cniao5.cniaoshop.bean.ShoppingCart;
import cniao5shop.com.cniao5.cniaoshop.utils.CartProvider;
import cniao5shop.com.cniao5.cniaoshop.widget.CNiaoToolBar;

public class CartFragment extends BaseFragment implements View.OnClickListener{

    public static final int ACTION_EDIT=1;
    public static final int ACTION_COMPLETE=2;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;
    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;
    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;
    @ViewInject(R.id.btn_del)
    private Button mBtnDel;
    @ViewInject(R.id.toolbar)
    protected CNiaoToolBar mToolbar;

    private CartAdapter mAdapter;
    private CartProvider cartProvider;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart,container,false);
    }

    @Override
    public void init() {
        cartProvider = new CartProvider(getContext());
        changeToolbar();
        showData();
    }
    //绑定数据适配器，显示数据
    private void showData(){
        List<ShoppingCart> carts = cartProvider.getAll();

        mAdapter = new CartAdapter(getContext(),carts,mCheckBox,mTextTotal);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    //更新购物车中的数据
    public void refData(){
        mAdapter.clear();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
    }
    //显示正常购物车ToolBar
    public void changeToolbar(){
        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");

        mToolbar.getRightButton().setOnClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
    }
    //显示删除控制页面
    private void showDelControl(){
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_COMPLETE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);
    }
    //隐藏删除页面
    private void  hideDelControl(){
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);

        mBtnDel.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckBox.setChecked(true);
    }
    //ToolBar右上角的按钮点击事件
    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        if(ACTION_EDIT == action){
            showDelControl();
        }
        else if(ACTION_COMPLETE == action){
            hideDelControl();
        }
    }
    //删除按钮监听事件,使用了xUtils工具类
    @OnClick(R.id.btn_del)
    public void delCart(View view){
        mAdapter.delCart();
    }
}
