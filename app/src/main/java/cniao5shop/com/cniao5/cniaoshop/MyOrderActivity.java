package cniao5shop.com.cniao5.cniaoshop;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by DENGFU on 2016/6/21.
 */
public class MyOrderActivity extends  BaseActivity{

    public static final int STATUS_ALL = 1000;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_PAY_FAIL = 2;
    public static final int STATUS_PAY_WAIT = 0;

    private int status = STATUS_ALL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
    }
}
