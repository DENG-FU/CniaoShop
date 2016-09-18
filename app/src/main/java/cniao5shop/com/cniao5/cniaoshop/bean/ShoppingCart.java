package cniao5shop.com.cniao5.cniaoshop.bean;

import java.io.Serializable;

/**
 * Created by DENGFU on 2016/6/14.
 */
public class ShoppingCart extends Wares implements Serializable {

    private int count;
    private boolean isChecked = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
