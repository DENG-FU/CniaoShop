package cniao5shop.com.cniao5.cniaoshop.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.internal.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cniao5shop.com.cniao5.cniaoshop.R;

/**
 * 自定义ToolBar控件，在原ToolBar基础上添加搜索框、文本、按钮等
 */
public class CNiaoToolBar extends Toolbar {

    private LayoutInflater mInflater;

    private View mView;
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mRightButton;

    //参数少的构造函数调用参数多的构造函数，确保代码能执行到
    public CNiaoToolBar(Context context) {
       this(context,null);
    }
    public CNiaoToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CNiaoToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //将相关控件加入到ToolBar中去
        initView();
        //设置ToolBar边距
        setContentInsetsRelative(10,10);
        //自定义属性集合不为空
        if(attrs !=null) {
            //获取自定义属性集合
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CNiaoToolBar, defStyleAttr, 0);
            //根据设置的属性设置按钮图标
            final Drawable rightIcon = a.getDrawable(R.styleable.CNiaoToolBar_rightButtonIcon);
            if (rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }
            //判断是否显示搜索框
            boolean isShowSearchView = a.getBoolean(R.styleable.CNiaoToolBar_isShowSearchView,false);
            if(isShowSearchView){
                showSearchView();
                hideTitleView();
            }
            //设置按钮文字
            CharSequence rightButtonText = a.getText(R.styleable.CNiaoToolBar_rightButtonText);
            if(rightButtonText !=null){
                setRightButtonText(rightButtonText);
            }
            //资源回收
            a.recycle();
        }
    }

    //将相关控件添加到当前ToolBar中去
    private void initView() {
        if(mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);
            //获取相关控件
            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);
            //布局参数设置
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            //以上面设置的参数lp将自定义的view添加到当前ToolBar中去
            addView(mView, lp);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void  setRightButtonIcon(Drawable icon){
        if(mRightButton !=null){
            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }
    }
    public void  setRightButtonIcon(int icon){
        setRightButtonIcon(getResources().getDrawable(icon));
    }
    public  void setRightButtonOnClickListener(OnClickListener li){
        mRightButton.setOnClickListener(li);
    }
    public void setRightButtonText(CharSequence text){
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }
    public void setRightButtonText(int id){
        setRightButtonText(getResources().getString(id));
    }
    public Button getRightButton(){
        return this.mRightButton;
    }


    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }
    @Override
    public void setTitle(CharSequence title) {
        initView();
        if(mTextTitle !=null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    public  void showSearchView(){
        if(mSearchView !=null)
            mSearchView.setVisibility(VISIBLE);
    }
    public void hideSearchView(){
        if(mSearchView !=null)
            mSearchView.setVisibility(GONE);
    }
    public void showTitleView(){
        if(mTextTitle !=null)
            mTextTitle.setVisibility(VISIBLE);
    }
    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);
    }

//
//    private void ensureRightButtonView() {
//        if (mRightImageButton == null) {
//            mRightImageButton = new ImageButton(getContext(), null,
//                    android.support.v7.appcompat.R.attr.toolbarNavigationButtonStyle);
//            final LayoutParams lp = generateDefaultLayoutParams();
//            lp.gravity = GravityCompat.START | (Gravity.VERTICAL_GRAVITY_MASK);
//            mRightImageButton.setLayoutParams(lp);
//        }
//    }

}
