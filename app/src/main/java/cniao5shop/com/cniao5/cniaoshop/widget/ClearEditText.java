package cniao5shop.com.cniao5.cniaoshop.widget;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cniao5shop.com.cniao5.cniaoshop.R;

/**
 * Created by DENGFU on 2016/6/21.
 */
public class ClearEditText extends AppCompatEditText implements View.OnTouchListener,View.OnFocusChangeListener,TextWatcher{

    private Drawable mClearTextIcon;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnTouchListener mOnTouchListener;

    public ClearEditText(Context context) {
        super(context);
        init(context);
    }
    public ClearEditText(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }
    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        //获取删除图标资源
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.icon_delete_32);
        //对图标进行着色
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable,getCurrentHintTextColor());
        mClearTextIcon = wrappedDrawable;
        mClearTextIcon.setBounds(0,0,mClearTextIcon.getIntrinsicHeight(),mClearTextIcon.getIntrinsicHeight());
        setClearIconVisible(false);

        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = (int) event.getX();
        if (mClearTextIcon.isVisible() && x>getWidth()-getPaddingRight()-mClearTextIcon.getIntrinsicWidth()){
            if (event.getAction() == event.ACTION_UP){
                setError(null);
                setText("");
            }
            return true;
        }
        return mOnFocusChangeListener != null && mOnTouchListener.onTouch(v,event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (mOnFocusChangeListener != null){
            mOnFocusChangeListener.onFocusChange(v,hasFocus);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

        if (isFocused()){
            setClearIconVisible(text.length() > 0);
        }
    }
    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setClearIconVisible(boolean visible) {
        mClearTextIcon.setVisible(visible,false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? mClearTextIcon : null,
                compoundDrawables[3]
        );
    }
}
