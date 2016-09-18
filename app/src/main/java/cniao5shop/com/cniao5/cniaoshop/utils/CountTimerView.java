package cniao5shop.com.cniao5.cniaoshop.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.sql.Time;

/**
 * Created by DENGFU on 2016/7/22.
 */
public class CountTimerView extends CountDownTimer {

    public static final int TIME_COUNT = 61000;
    private TextView btn;
    private int endStrRid;

    /**
     *
     * @param millisInFuture    total count down time(such as 60S)
     * @param countDownInterval each change interval
     * @param btn   the button, in this class is a textview ,button is the textview subclass
     * @param endStrRid when count finish will show the text on the button
     */
    public CountTimerView(long millisInFuture, long countDownInterval, TextView btn, int endStrRid){
        super(millisInFuture,countDownInterval);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    public CountTimerView(TextView btn,int endStrRid){
        super(TIME_COUNT,1000);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    public CountTimerView(TextView btn){
        super(TIME_COUNT,1000);
        this.btn = btn;
        this.endStrRid = cn.smssdk.gui.R.string.smssdk_resend_identify_code;
    }
    //execute during counting
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setEnabled(false);
        btn.setText(millisUntilFinished/1000+"秒后可重新发送");
    }
    //execute when count finish
    @Override
    public void onFinish() {
        btn.setText(endStrRid);
        btn.setEnabled(true);
    }
}
