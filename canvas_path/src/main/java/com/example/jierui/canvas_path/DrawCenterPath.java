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


//        drawCity(canvas, new String[]{"北京","上海","重庆","深圳","广州","乌鲁木齐","呼和浩特","巴音郭楞"}, new int[]{1,2,3,4,5,6,7,8});


        Paint p = new Paint();
        p.setColor(Color.RED);// 设置红色
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        int ss = 100;
        p.setStrokeWidth(2 * ss);



        canvas.drawCircle(centerX, centerY, phyMaxRadius - ss, p);// 小圆

        drawRotateText(canvas, centerX, centerY, phyMaxRadius - mainSize, 50, "国国国", (int) mainSize);

        canvas.drawBitmap(mBgBitmap, null, new Rect((int)(centerNextButtonX - nextSize / 2), (int)(centerNextButtonY - nextSize / 2), (int)(centerNextButtonX + nextSize / 2), (int)(centerNextButtonY + nextSize / 2)), null);

        super.onDraw(canvas);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        String TAG = "TAG";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTIOM_DOWN:X=" + x + "__Y=" + y);
//                mLastX = x;
//                mLastY = y;
//                mDownTime = System.currentTimeMillis();
//                mTmpAngle = 0;
//
//                // 如果当前已经在快速滚动
//                if (isFling)
//                {
//                    // 移除快速滚动的回调
//                    removeCallbacks(mFlingRunnable);
//                    isFling = false;
//                    return true;
//                }
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTIOM_MOVE:X=" + x + "__Y=" + y);
                return true;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTIOM_UP:X=" + x + "__Y=" + y);
                return true;

            default:
                return true;

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
}
