package cniao5shop.com.cniao5.cniaoshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

import cniao5shop.com.cniao5.cniaoshop.R;
import cniao5shop.com.cniao5.cniaoshop.bean.ShoppingCart;
import cniao5shop.com.cniao5.cniaoshop.utils.CartProvider;
import cniao5shop.com.cniao5.cniaoshop.widget.NumberAddSubView;

public class CartAdapter extends BaseAdapter<ShoppingCart,BaseViewHolder> implements BaseAdapter.OnItemClickListener {

    public static final String TAG = "CartAdapter";

    private CheckBox checkBox;
    private TextView textView;
    private CartProvider cartProvider;

    //构造函数传入数据、全选CheckBox、合计金额TextView
    public CartAdapter(Context context, List<ShoppingCart> datas, final CheckBox checkBox, TextView tv) {
        super(context, R.layout.template_cart, datas);

        //添加监听事件
        setCheckBox(checkBox);
        setTextView(tv);

        cartProvider = new CartProvider(context);
        setOnItemClickListener(this);
        showTotalPrice();
    }
    //绑定RecyclerView中item的数据
    @Override
    protected void convert(BaseViewHolder viewHoder, final ShoppingCart item) {

        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥" + item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        CheckBox checkBox = (CheckBox) viewHoder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());

        NumberAddSubView numberAddSubView = (NumberAddSubView) viewHoder.getView(R.id.num_control);
        numberAddSubView.setValue(item.getCount());

        //数据加减按钮传递接口函数过去
        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }
            @Override
            public void onButtonSubClick(View view, int value) {
                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }
        });
    }
    //获取总金额
    private float getTotalPrice() {
        float sum = 0;
        if (!isNull())
            return sum;
        for (ShoppingCart cart :
                datas) {
            if (cart.isChecked())
                sum += cart.getCount() * cart.getPrice();
        }
        return sum;
    }
    //显示总金额
    public void showTotalPrice() {
        float total = getTotalPrice();
        textView.setText(Html.fromHtml("合计￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }
    //判断数据是否为空
    private boolean isNull() {
        return (datas != null && datas.size() > 0);
    }
    //Item的点击事件，主要改变商品选中状态和总金额
    @Override
    public void onItemClick(View view, int position) {
        ShoppingCart cart = getItem(position);
        cart.setChecked(!cart.isChecked());
        notifyItemChanged(position);

        checkListen();
        showTotalPrice();
    }
    //判断商品是否全选，是则钩上全选按钮
    private void checkListen() {
        int count = 0;
        int checkNum = 0;
        if (datas != null) {
            count = datas.size();
            for (ShoppingCart cart : datas) {
                if (!cart.isChecked()) {
                    checkBox.setChecked(false);
                    break;
                } else {
                    checkNum = checkNum + 1;
                }
            }
            if (count == checkNum) {
                checkBox.setChecked(true);
            }
        }
    }
    //统一设置所有商品选择状态
    public void checkAll_None(boolean isChecked) {
        if (!isNull())
            return;
        int i = 0;
        for (ShoppingCart cart : datas) {
            cart.setChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }
    }
    //删除选中商品
    public void delCart() {
        if (!isNull())
            return;

        for (Iterator iterator = datas.iterator(); iterator.hasNext(); ) {
            ShoppingCart cart = (ShoppingCart) iterator.next();
            if (cart.isChecked()) {
                int position = datas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }
    }

    public void setTextView(TextView textview) {
        this.textView = textview;
    }
    //设置全选CheckBox点击事件
    public void setCheckBox(CheckBox ck) {
        this.checkBox = ck;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll_None(checkBox.isChecked());
                showTotalPrice();
            }
        });
    }
}
