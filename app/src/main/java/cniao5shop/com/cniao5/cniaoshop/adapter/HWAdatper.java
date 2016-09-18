package cniao5shop.com.cniao5.cniaoshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

import cniao5shop.com.cniao5.cniaoshop.R;
import cniao5shop.com.cniao5.cniaoshop.bean.Wares;
import cniao5shop.com.cniao5.cniaoshop.utils.CartProvider;
import cniao5shop.com.cniao5.cniaoshop.utils.ToastUtils;

public class HWAdatper extends BaseAdapter<Wares,BaseViewHolder> {
    CartProvider provider ;

    public HWAdatper(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);

        provider = new CartProvider(context);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, final Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥"+wares.getPrice());

        Button button =viewHolder.getButton(R.id.btn_add);
        if(button !=null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    provider.put(wares);
                    ToastUtils.show(context, "已添加到购物车");
                }
            });
        }
    }

    public void  resetLayout(int layoutId){
        this.layoutResId  = layoutId;
        notifyItemRangeChanged(0,getDatas().size());
    }

}
