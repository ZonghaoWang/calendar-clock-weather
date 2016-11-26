package com.example.jierui.canvas_path;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by jierui on 2016/11/18.
 */

public class DrawCenterPath extends View {
    // 汉字和主要字体大小
    private float mainSize;
    final int DEFAULT_MAIN_SIZE = 20;
    // 数字的大小
    private float numSize;
    final int DEFAULT_NUM_SIZE = 20;
    // 天气数据的大小
    private float aqiSize;
    final int DEFAULT_AQI_SIZE = 20;
    // 温度的大小
    private float tempSize;
    final int DEFAULT_TEMP_SIZE = 20;
    // 最大园半径
    private float maxRadius;
    final int DEFAULT_MAX_RADIUS = 20;
    // 中心园半径
    private float centerRadius;
    final int DEFAULT_CENTER_RADIUS = 20;
    // 切换按钮大小
    private float nextSize;
    final int DEFAULT_NEXT_SIZE = 20;
    // 上下文
    private Context context;
    // 计算出的大圆参数 圆心坐标和半径
    private float centerX;
    private float centerY;
    private float phyMaxRadius;
    // nextButton 参数，坐标
    private float centerNextButtonX;
    private float centerNextButtonY;



    //
    public enum PageSelect{now, hourly, daily, suggestion, city};
    private PageSelect currentPage = PageSelect.now;
    // 每个页面的扫过的角度
    private float nowSweepAngle;
    private float hourlySweepAngle;
    private float dailySweepAngle;
    private float suggestionSweepAngle;
    private float citySweepAngle;

    // 每一个页面中各个元素的位置

    private float[] nowAngleSet;
    private float[] hourlyAngleSet;
    private float[] dailyAngleSet;
    private float[] suggestionAngleSet;
    private float[] cityAngleSet;

    private float[] nowRadius;
    private float[] hourlyRadius;
    private float[] dailyRadius;
    private float[] suggestionRadius;
    private float[] cityRadius;







    public DrawCenterPath(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawCenterPath(Context context) {
        this(context, null);
    }

    public DrawCenterPath(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        this.setFocusable(true);
        setClickable(true);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawCenterPath);
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++){
                int attr = a.getIndex(i);
                switch (attr){
                    case R.styleable.DrawCenterPath_main_size:
                        mainSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_MAIN_SIZE, getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.DrawCenterPath_aqi_size:
                        aqiSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_AQI_SIZE, getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.DrawCenterPath_num_size:
                        numSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_NUM_SIZE, getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.DrawCenterPath_temp_size:
                        tempSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEMP_SIZE, getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.DrawCenterPath_next_size:
                        nextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_NEXT_SIZE, getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.DrawCenterPath_center_radius:
                        centerRadius = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CENTER_RADIUS, getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.DrawCenterPath_max_radius:
                        maxRadius = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MAX_RADIUS, getResources().getDisplayMetrics()));
                        break;
                    default:
                        break;
                }
            }
            a.recycle();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specModeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int specModeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int specSizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specSizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;

        if (MeasureSpec.EXACTLY == specModeWidth){
            width = specSizeWidth;
            if (MeasureSpec.EXACTLY == specModeHeight){
                height = specSizeHeight;
            }else{
                height = (int) Math.min(specSizeHeight, specSizeWidth + nextSize / 4);
            }
        }else{
            if (MeasureSpec.EXACTLY == specModeHeight){
                height = specSizeHeight;
                width = (int) Math.min(height - nextSize / 4, specSizeWidth);
            }else {
                if (specSizeHeight >= specSizeWidth + nextSize / 4){
                    width = (int) Math.min(specSizeWidth, maxRadius);
                    height = (int) (width + nextSize / 4);
                }else{
                    height = (int) Math.min(maxRadius - nextSize / 4, specSizeHeight);
                    width = (int) (height - nextSize / 4);
                }
            }
        }
        setMeasuredDimension(width, height);

    }

    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(),
            R.drawable.next);
    @Override
    protected void onDraw(Canvas canvas) {
        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        // 计算圆心坐标和nextButton的坐标
        if (mHeight - mWidth >= nextSize / 4){
            float offset = (mHeight - mWidth - nextSize / 4) / 2;
            phyMaxRadius = mWidth / 2;
            centerX = mWidth / 2;
            centerY = offset + phyMaxRadius;
            centerNextButtonX = centerX;
            centerNextButtonY = mHeight - offset - nextSize / 2;
        } else {
            phyMaxRadius = (mHeight - nextSize / 4) / 2;
            centerX = mWidth / 2;
            centerY = phyMaxRadius;
            centerNextButtonX = centerX;
            centerNextButtonY = mHeight - nextSize / 2;
        }


        switch (currentPage){
            case now:
                drawNowPage();
                break;
            case hourly:
                drawHourlyPage();
                break;
            case daily:
                drawDailyPage();
                break;
            case suggestion:
                drawSuggestionPage();
                break;
            case city:
                drawCityPage();
                break;
        }

//        drawCity(canvas, new String[]{"北京","上海","重庆","深圳","广州","乌鲁木齐","呼和浩特","巴音郭楞"}, new int[]{1,2,3,4,5,6,7,8});


//        Paint p = new Paint();
//        p.setColor(Color.RED);// 设置红色
//        p.setAntiAlias(true);
//        p.setStyle(Paint.Style.STROKE);
//        int ss = 100;
//        p.setStrokeWidth(2 * ss);
//
//
//
//        canvas.drawCircle(centerX, centerY, phyMaxRadius - ss, p);// 小圆
//
//        drawRotateText(canvas, centerX, centerY, phyMaxRadius - mainSize, 50, "国国国", (int) mainSize);
//
//        canvas.drawBitmap(mBgBitmap, null, new Rect((int)(centerNextButtonX - nextSize / 2), (int)(centerNextButtonY - nextSize / 2), (int)(centerNextButtonX + nextSize / 2), (int)(centerNextButtonY + nextSize / 2)), null);

        super.onDraw(canvas);
    }


    private float mLastX;
    private float mLastY;
    private long mDownTime;
    private float mTmpAngle;
    private boolean isFling;
    private AutoFlingRunnable mFlingRunnable;
    private final float VISIABLE_START_ANGLE = 90;
    private final float VISIABLE_END_ANGLE = 450;
    private final float STARTANGLE = 120;
    private final int mFlingableValue = 300;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        String TAG = "TAG";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTIOM_DOWN:X=" + x + "__Y=" + y);
                mLastX = x;
                mLastY = y;
                mDownTime = System.currentTimeMillis();
                mTmpAngle = 0;

                // 如果当前已经在快速滚动
                if (isFling)
                {
                    // 当视图正在滚动时，移除快速滚动的回调
                    removeCallbacks(mFlingRunnable);
                    isFling = false;
                    return true;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                /**
                 * 获得开始的角度
                 */
                float start = getAngle(mLastX, mLastY);
                /**
                 * 获得当前的角度
                 */
                float end = getAngle(x, y);
                float addedAngle = Math.abs(end - start) >= 180? (end > 180? end - 360 - start : end + 360 - start): end - start;

                /**
                 * 改变page的sweep角度
                 */
                switch (currentPage){
                    case now:
                        nowSweepAngle += addedAngle;
                        break;
                    case hourly:
                        hourlySweepAngle += addedAngle;
                        break;
                    case daily:
                        dailySweepAngle += addedAngle;
                        break;
                    case suggestion:
                        suggestionSweepAngle += addedAngle;
                        break;
                    case city:
                        citySweepAngle += addedAngle;
                        break;
                }

                mTmpAngle += addedAngle;
                requestLayout();

                mLastX = x;
                mLastY = y;
                return true;
            case MotionEvent.ACTION_UP:
                // 计算，每秒移动的角度
                float anglePerSecond = mTmpAngle * 1000
                        / (System.currentTimeMillis() - mDownTime);

                // Log.e("TAG", anglePrMillionSecond + " , mTmpAngel = " +
                // mTmpAngle);

                // 如果达到该值认为是快速移动
                if (Math.abs(anglePerSecond) > mFlingableValue && !isFling)
                {
                    // post一个任务，去自动滚动
                    post(mFlingRunnable = new AutoFlingRunnable(anglePerSecond));

                    return true;
                }

                // 如果当前旋转角度超过NOCLICK_VALUE屏蔽点击
                if (Math.abs(mTmpAngle) > NOCLICK_VALUE)
                {
                    return true;
                }

                break;
        }


    }


    private void drawRotateText(Canvas canvas, float cx, float cy, float r, float degree, String str, int ts) {
        RectF mRange = new RectF(cx - r, cy - r, cx + r, r + cy);
        Path path = new Path();
        Paint mTextPaint = new Paint();
        mTextPaint.setColor(0xFFffffff);
        mTextPaint.setTextSize(ts);
        path.addArc(mRange, degree, 250);
        float textWidth = mTextPaint.measureText(str);
        float hOffset = (float) (r * Math.PI / 20 / 2 - textWidth / 2);// 水平偏移
        float vOffset = r / 2 / 6;// 垂直偏移
        canvas.drawTextOnPath(str, path, 0, 0, mTextPaint);
    }


    /**
     * 自动滚动线程
     */
    private class AutoFlingRunnable implements Runnable
    {

        private float angelPerSecond;

        public AutoFlingRunnable(float velocity)
        {
            this.angelPerSecond = velocity;
        }

        public void run()
        {
            // 如果小于20,则停止
            if ((int) Math.abs(angelPerSecond) < 20)
            {
                isFling = false;
                return;
            }
            isFling = true;
            // 不断改变mStartAngle，让其滚动，/30为了避免滚动太快
            mStartAngle += (angelPerSecond / 30);
            // 逐渐减小这个值
            angelPerSecond /= 1.0666F;
            postDelayed(this, 30);
            // 重新布局
            requestLayout();
        }
    }

    /**
     * 根据触摸的位置，计算角度
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private float getAngle(float xTouch, float yTouch)
    {
        double x = xTouch - centerX;
        double y = yTouch - centerY;
        float angle = (float) (Math.acos(x / Math.hypot(x, y)) * 180 / Math.PI);
        return y >= 0? angle : 360 - angle;
    }
}
