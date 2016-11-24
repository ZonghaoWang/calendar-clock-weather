package com.example.jierui.view;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.example.jierui.satilate_menu.R;

/**
 * Created by jierui on 2016/11/15.
 */

public class ArcMenu extends ViewGroup implements View.OnClickListener {
    /**
    * 菜单位置枚举类
    */
    private Position mPosition = Position.RIGHT_BOTTOM;

    private final int POSITION_LEFT_TOP = 0;
    private final int POSITION_LEFT_BOTTOM = 1;
    private final int POSITION_RIGHT_TOP = 2;
    private final int POSITION_RIGHT_BOTTOM = 3;



    public enum Position{
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    /**
     * 菜单的半径
    * */
    private int mRadius;


    /**
     * 菜单的状态，打开或者关闭
     * */
    private Status mCurrentStatus = Status.CLOSE;
    public enum Status{
        OPEN, CLOSE
    }

    /**
     * 菜单的主按钮
     * */
    private View mCButton;


    private OnMenuItemClickListener mMenuItemClickListener;

    /**
     * 点击子菜单的回调接口
     * */
    public interface OnMenuItemClickListener{
        void onClick(View view, int pos);
    }
    /**
     * 子函数回调set
     */
    public void setOnMenuItemClickListener(OnMenuItemClickListener listener){
        this.mMenuItemClickListener = listener;

    }







    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 单位转换,dp转px
         */
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        // 获取自定义属性值
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyleAttr, 0);
        int pos = a.getInt(R.styleable.ArcMenu_position, POSITION_RIGHT_BOTTOM);
        switch (pos){
            case POSITION_LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;
            case POSITION_LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case POSITION_RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;
            case POSITION_RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }
        mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        Log.d("TAG", "Position=" + mPosition + ",mRadius=" + mRadius);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i111, int i2, int i3) {
        if (b){
            layoutCButton();
            int count = getChildCount();

            for (int j = 0; j < count - 1; j++){
                View child = getChildAt(j + 1);
                int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * j));
                int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * j));



                int cWidth = child.getMeasuredWidth();
                int cHeight = child.getMeasuredHeight();
                int total_h = getMeasuredHeight();
                int total_w = getMeasuredWidth();
                if (mPosition == Position.RIGHT_BOTTOM || mPosition == Position.LEFT_BOTTOM){
                    ct = getMeasuredHeight() - cHeight - ct;
                }
                if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
                    cl = getMeasuredWidth() - cWidth - cl;
                }
                child.setVisibility(View.GONE);
                child.layout(cl, ct, cl + cWidth, ct + cHeight);

            }


        }

    }

    /**
     * 定位主菜单按钮
     */

    private void layoutCButton() {
        mCButton = getChildAt(0);
        mCButton.setOnClickListener(this);

        int l = 0;
        int t = 0;
        int width = mCButton.getMeasuredWidth();
        int height = mCButton.getMeasuredHeight();
        switch (mPosition){
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }
        mCButton.layout(l, t, l + width, t + height);

    }

    @Override
    public void onClick(View view) {
//        mCButton = findViewById(R.id.id_button);
//        if (mCButton == null){
//            mCButton = getChildAt(0);
//        }
        rotateCButton(view, 0f, 360f, 300);
        toggleMenu(300);


    }

    /**
     * 切换菜单
     */

    private void toggleMenu(int duration) {
        //为menuitem添加平移和旋转动画
        int count = getChildCount();
        for(int i = 0; i < count - 1; i++){
            final View childView = getChildAt(i + 1);
            childView.setVisibility(View.VISIBLE);
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));
            int xflag = 1;
            int yflag = 1;
            if(mPosition == Position.LEFT_BOTTOM||mPosition==Position.LEFT_TOP){
                xflag = -1;
            }
            if (mPosition == Position.LEFT_TOP||mPosition==Position.RIGHT_TOP){
                yflag = -1;
            }
            AnimationSet animset = new AnimationSet(true);
            Animation tranAnim = null;
            if (mCurrentStatus == Status.CLOSE){ // To open
                tranAnim = new TranslateAnimation(xflag * cl, 0, yflag * ct, 0);
                childView.setClickable(true);
                childView.setFocusable(true);

            }else { // To close
                tranAnim = new TranslateAnimation(0, xflag * cl, 0, yflag * ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            tranAnim.setFillAfter(true);
            tranAnim.setDuration(duration);
            tranAnim.setStartOffset(20*i);

            final int pos = i + 1;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (mMenuItemClickListener!=null)
//                        mMenuItemClickListener.onClick(childView, pos);
                    menuItemAnim(pos - 1);
                    changeStatus();


                }
            });



            tranAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    if (mCurrentStatus == Status.CLOSE){
                        childView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            // 旋转动画
            RotateAnimation rotateAnim = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setDuration(duration);
            rotateAnim.setFillAfter(true);

            animset.addAnimation(rotateAnim);
            animset.addAnimation(tranAnim);

            childView.startAnimation(animset);
            /**
             * 切换菜单状态
            * */

        
        }
        changeStatus();

    }

    private void menuItemAnim(int pos) {
        for (int i = 0; i < getChildCount() - 1; i++){
            View childView = getChildAt(i + 1);
            if (i == pos){
                childView.startAnimation(scaleBigAnim(300));
            }else{
                childView.startAnimation(scaleSmallAnim(300));
            }
            childView.setClickable(false);
            childView.setFocusable(false);
        }

    }

    private Animation scaleSmallAnim(int duartion) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnima = new AlphaAnimation(1f, 0f);
        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnima);
        animationSet.setDuration(duartion);
        animationSet.setFillAfter(true);
        return animationSet;
    }

    /**
     * 放大 透明度降低
    * */
    private Animation scaleBigAnim(int duartion) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnima = new AlphaAnimation(1f, 0f);
        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnima);
        animationSet.setDuration(duartion);
        animationSet.setFillAfter(true);
        return animationSet;
    }

    private void changeStatus() {
        mCurrentStatus = (mCurrentStatus == Status.CLOSE? Status.OPEN:Status.CLOSE);
    }

    private void rotateCButton(View v, float start, float end, int duration){
        RotateAnimation anim = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }
}