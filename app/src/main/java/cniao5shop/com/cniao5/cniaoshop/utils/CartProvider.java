package cniao5shop.com.cniao5.cniaoshop.utils;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cniao5shop.com.cniao5.cniaoshop.bean.ShoppingCart;
import cniao5shop.com.cniao5.cniaoshop.bean.Wares;

/**
 * @author DENGFU
 * 该类主要通过SharedPreferences将商品列表存储到本地实现不同Fragement之间的数据共享
 */
public class CartProvider {

    public static final String CART_JSON = "cart_json";
    //类似Map，只是键值为int变量，每件商品有个id值存储方便
    private SparseArray<ShoppingCart> datas = null;
    private Context mContext;

    //构造函数初始化
    public CartProvider(Context context) {
        mContext = context;
        datas = new SparseArray<>(10);
        listToSparse();
    }
    //添加一件商品，如果商品存在则数目加一否则添加新商品
    public void put(ShoppingCart cart) {
        ShoppingCart temp = datas.get(cart.getId().intValue());
        if (temp != null) {
            temp.setCount(temp.getCount() + 1);
        } else {
            temp = cart;
            temp.setCount(1);
        }
        datas.put(cart.getId().intValue(), temp);
        //将商品列表存储到本地
        commit();
    }
    //存储一件Wares，需将它转换为cart进行存储
    public void put(Wares wares) {
        ShoppingCart cart = convertData(wares);
        put(cart);
    }
    //更新一下某件商品
    public void update(ShoppingCart cart) {
        datas.put(cart.getId().intValue(), cart);
        commit();
    }
    //删除一件商品
    public void delete(ShoppingCart cart) {
        datas.delete(cart.getId().intValue());
        commit();
    }
    //获取全部商品
    public List<ShoppingCart> getAll() {
        return getDataFromLocal();
    }
    //把商品列表以JSON格式存储到本地文件
    public void commit() {
        List<ShoppingCart> carts = sparseToList();
        PreferencesUtils.putString(mContext, CART_JSON, JSONUtil.toJSON(carts));
        //Log.e(CART_JSON,JSONUtil.toJSON(carts));
    }
    //转化为List存储
    private List<ShoppingCart> sparseToList() {
        int size = datas.size();

        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(datas.valueAt(i));
        }
        return list;
    }
    //List转化为Sparse存储
    private void listToSparse() {
        List<ShoppingCart> carts = getDataFromLocal();
        if (carts != null && carts.size() > 0) {
            for (ShoppingCart cart : carts) {
                datas.put(cart.getId().intValue(), cart);
            }
        }
    }
    //从本地获取商品列表
    public List<ShoppingCart> getDataFromLocal() {
        String json = PreferencesUtils.getString(mContext, CART_JSON);
        List<ShoppingCart> carts = null;
        if (json != null) {
            carts = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }
        return carts;
    }
    //将Wares转化为ShoppingCart
    public ShoppingCart convertData(Wares item) {

        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }
}
