package cniao5shop.com.cniao5.cniaoshop.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import cniao5shop.com.cniao5.cniaoshop.AddressListActivity;
import cniao5shop.com.cniao5.cniaoshop.CniaoApplication;
import cniao5shop.com.cniao5.cniaoshop.Constants;
import cniao5shop.com.cniao5.cniaoshop.LoginActivity;
import cniao5shop.com.cniao5.cniaoshop.MyFavoriteActivity;
import cniao5shop.com.cniao5.cniaoshop.MyOrderActivity;
import cniao5shop.com.cniao5.cniaoshop.R;
import cniao5shop.com.cniao5.cniaoshop.bean.User;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author DENGFU
 *
 */
public class MineFragment extends BaseFragment {

    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;

    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;

    @ViewInject(R.id.btn_logout)
    private Button mbtnLogout;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, null);
    }

    @Override
    public void init() {
        showUser();
    }

    //从CniaoApplication中读取全局变量user信息
    private void showUser() {
        User user = CniaoApplication.getmInstance().getUser();
        if (user == null){
            mbtnLogout.setVisibility(View.GONE);
            mTxtUserName.setText("点击登录");
        } else{
            mbtnLogout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(user.getLogo_url()))
                Picasso.with(getActivity()).load(Uri.parse(user.getLogo_url())).into(mImageHead);
            mTxtUserName.setText(user.getUsername());
        }
    }
    //跳转到登陆页面
    @OnClick(value = {R.id.img_head, R.id.txt_username})
    public void toLoginActivity(View view){
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }
    //跳转到订单页面
    @OnClick(R.id.txt_my_orders)
    public void toMyOrderActivity(View view){
        startActivity(new Intent(getActivity(), MyOrderActivity.class),true);
    }
    //跳转到地址页面
    @OnClick(R.id.txt_my_address)
    public void toAddressActivity(View view){
        startActivity(new Intent(getActivity(),AddressListActivity.class),true);
    }
    //跳转到收藏页面
    @OnClick(R.id.txt_my_favorite)
    public void toFavoriteActivity(View view){
        startActivity(new Intent(getActivity(), MyFavoriteActivity.class),true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        showUser();
    }
}
