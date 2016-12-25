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
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import toolbox.DateTimeCalender;
import weatherHelper.Aqi;
import weatherHelper.DailyForecast;
import weatherHelper.Heweather5;
import weatherHelper.HourForeCast;
import weatherHelper.Suggestion;

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

    // 当前日期
    private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
    private String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis()));



    // city (time now hours suggestion and aqi)
    private List<String> cityName = new ArrayList<String>();
    private List<Heweather5> weatherInfoList = new ArrayList<Heweather5>();
    private String currentCity;
    private Heweather5 currentHeweather5;

    // 定义接口
    public interface OnItemClickListener{
        void onClick(Heweather5 heweather5);
    }
    private OnItemClickListener mItemClickListener;
    /**
     * 子函数回调set
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mItemClickListener = listener;
    }










    //
    public enum PageSelect{now, hourly, daily, suggestion, city, aqi};
    private PageSelect currentPage = PageSelect.city;
    // 每个页面的扫过的角度
    private float nowSweepAngle;
    private float hourlySweepAngle;
    private float dailySweepAngle;
    private float suggestionSweepAngle;
    private float citySweepAngle;
    private float aqiSweepAngle;

    // 每一个页面中各个元素的位置
    // 每个页面各个元素的角度边界
    private class Limit{
        private float boud;
        Limit(float lim){
            boud = lim;
        }

        public float getBoud() {
            return boud;
        }

        public void setBoud(float boud) {
            this.boud = boud;
        }
    }
    private List<Limit> nowAngleSet = new ArrayList<Limit>();
    private List<Limit> hourlyAngleSet = new ArrayList<Limit>();
    private List<Limit> dailyAngleSet = new ArrayList<Limit>();
    private List<Limit> suggestionAngleSet = new ArrayList<Limit>();
    private List<Limit> cityAngleSet = new ArrayList<Limit>();
    private List<Limit> aqiAngleSet = new ArrayList<Limit>();



    private float[] nowRadius;
    private float[] hourlyRadius;
    private float[] dailyRadius;
    private float[] suggestionRadius;
    private float[] cityRadius;
    private float[] aqiRadius;







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
            /**
             * TypedArray得到attrs下的某个view的所有属性
             * attr 从父view传过来的属性值，switch一下获得属性的px值，没有的选择默认值
             */

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


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //完成主界面更新,拿到数据
                    Heweather5  heweather5 = (Heweather5) msg.obj;
                    weatherInfoList.add(heweather5);
                    if (heweather5.getCityName().equals(currentCity)){
                        currentHeweather5 = heweather5;
                    }
                    requestLayout();
                    Log.d("tag", heweather5.getCityName());
                    break;
                default:
                    break;
            }

        }

    };


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
                drawNowPage(canvas);
                break;
            case hourly:
                drawHourlyPage(canvas);
                break;
            case daily:
                drawDailyPage(canvas);
                break;
            case suggestion:
                drawSuggestionPage(canvas);
                break;
            case city:
                drawCityPage(canvas);
                break;
            case aqi:
                drawAqiPage(canvas);

        }

        canvas.drawBitmap(mBgBitmap, null, new Rect((int)(centerNextButtonX - nextSize / 2), (int)(centerNextButtonY - nextSize / 2), (int)(centerNextButtonX + nextSize / 2), (int)(centerNextButtonY + nextSize / 2)), null);

        super.onDraw(canvas);
    }


    private void drawAqiPage(Canvas canvas) {


        Aqi aqi = currentHeweather5.getAqi();
        if (aqi == null){
            nextState();
            requestLayout();
            return;
        }
        List<String> indicators = new ArrayList<String>();
        List<String> degrees = new ArrayList<String>();
        if (aqi.getAqi()!=null){
            indicators.add(getResources().getString(R.string.aqi));
            degrees.add(aqi.getAqi());
        }
        if (aqi.getCo()!=null){
            indicators.add(getResources().getString(R.string.co));
            degrees.add(aqi.getCo());
        }
        if (aqi.getNo2()!=null){
            indicators.add(getResources().getString(R.string.no2));
            degrees.add(aqi.getNo2());
        }
        if (aqi.getO3()!=null){
            indicators.add(getResources().getString(R.string.o3));
            degrees.add(aqi.getO3());
        }
        if (aqi.getPm10()!=null){
            indicators.add(getResources().getString(R.string.pm10));
            degrees.add(aqi.getPm10());
        }
        if (aqi.getPm25()!=null){
            indicators.add(getResources().getString(R.string.pm25));
            degrees.add(aqi.getPm25());
        }
        if (aqi.getSo2()!=null){
            indicators.add(getResources().getString(R.string.so2));
            degrees.add(aqi.getSo2());
        }

        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(4);
        Paint mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(numSize);
        Paint nTextPaint = new Paint();
        nTextPaint.setColor(Color.BLACK);
        nTextPaint.setTextSize(numSize);




        float circleRadius1 = phyMaxRadius - numSize - mTextPaint.getFontMetrics().descent;
        float circleRadius2 = phyMaxRadius - (numSize + mTextPaint.getFontMetrics().descent) * 2;
//        float circleRadius3 = phyMaxRadius - (numSize + mTextPaint.getFontMetrics().descent) * 3;

        float textRadius1 = phyMaxRadius - numSize;
        float textRadius2 = phyMaxRadius - 2 * numSize - mTextPaint.getFontMetrics().descent;
//        float textRadius3 = phyMaxRadius - 3 * numSize - mTextPaint.getFontMetrics().descent * 2;

        RectF rectf1 = new RectF(centerX - textRadius1, centerY - textRadius1, centerX + textRadius1, centerY + textRadius1);
        RectF rectf2 = new RectF(centerX - textRadius2, centerY - textRadius2, centerX + textRadius2, centerY + textRadius2);
//        RectF rectf3 = new RectF(centerX - textRadius3, centerY - textRadius3, centerX + textRadius3, centerY + textRadius3);
        List<List<String>> strings = new ArrayList<List<String>>();
        strings.add(indicators);
        strings.add(degrees);
        drawRepeatPage(canvas, aqiSweepAngle, aqiAngleSet, new float[]{phyMaxRadius, circleRadius1, circleRadius2}, new RectF[]{rectf1, rectf2}, new Paint[]{circlePaint, mTextPaint, mTextPaint}, strings, new boolean[]{false, false});

    }

    private void drawNowPage(Canvas canvas) {


    }
    private void drawHourlyPage(Canvas canvas) {
        Date time = new Date(System.currentTimeMillis());
        hourlyAngleSet.clear();
        List<HourForeCast> hourForeCasts = currentHeweather5.getHourForeCast();
        int timeIndex = 0;
        long minTime = 24 * 3600 * 1000;
        for (int i = 0; i < hourForeCasts.size(); i++) {
            try {
                Date forcastTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(hourForeCasts.get(i).getDate());
                if (Math.abs(forcastTime.getTime() - time.getTime()) <= minTime){
                    minTime = Math.abs(forcastTime.getTime() - time.getTime());
                    timeIndex = i;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int len = hourForeCasts.size();

        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(4);
        Paint mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(numSize);
        Paint nTextPaint = new Paint();
        nTextPaint.setColor(Color.BLACK);
        nTextPaint.setTextSize(numSize);

        List<String> stringHourTime = new ArrayList<String>();
        List<String> stringTmpTime = new ArrayList<String>();
        List<String> stringWindSC = new ArrayList<String>();
        for (int i = 0; i < hourForeCasts.size(); i++){
            HourForeCast hourForeCast = hourForeCasts.get(i);
            String hourTime;
            String tmpTime;
            String windSC;
            hourTime = hourForeCast.getDate().substring(11);
            tmpTime = hourForeCast.getCondTxt() + " " + hourForeCast.getTmp();
            windSC = hourForeCast.getWind().getSc();
            stringHourTime.add(hourTime);
            stringTmpTime.add(tmpTime);
            stringWindSC.add(windSC);
        }
        List<List<String>> strings = new ArrayList<List<String>>();
        strings.add(stringHourTime);
        strings.add(stringTmpTime);
        strings.add(stringWindSC);

        try {
            if (stringHourTime.size()!=0) {
                if (Math.abs(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(hourForeCasts.get(timeIndex).getDate()).getTime() - time.getTime()) <= 0.5 * 3600 * 1000) {
                    stringHourTime.set(timeIndex, "现在");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        float circleRadius1 = phyMaxRadius - numSize - mTextPaint.getFontMetrics().descent;
        float circleRadius2 = phyMaxRadius - (numSize + mTextPaint.getFontMetrics().descent) * 2;
        float circleRadius3 = phyMaxRadius - (numSize + mTextPaint.getFontMetrics().descent) * 3;

        float textRadius1 = phyMaxRadius - numSize;
        float textRadius2 = phyMaxRadius - 2 * numSize - mTextPaint.getFontMetrics().descent;
        float textRadius3 = phyMaxRadius - 3 * numSize - mTextPaint.getFontMetrics().descent * 2;

        RectF rectf1 = new RectF(centerX - textRadius1, centerY - textRadius1, centerX + textRadius1, centerY + textRadius1);
        RectF rectf2 = new RectF(centerX - textRadius2, centerY - textRadius2, centerX + textRadius2, centerY + textRadius2);
        RectF rectf3 = new RectF(centerX - textRadius3, centerY - textRadius3, centerX + textRadius3, centerY + textRadius3);

        drawRepeatPage(canvas, hourlySweepAngle, hourlyAngleSet, new float[]{phyMaxRadius, circleRadius1, circleRadius2, circleRadius3}, new RectF[]{rectf1, rectf2, rectf3}, new Paint[]{circlePaint, mTextPaint, mTextPaint, mTextPaint}, strings, new boolean[]{false, true, false});

        if (hourlyPagePressed){
            int index = calculateIndexZone(hourlySweepAngle, hourlyAngleSet);
            if (index!=-1){
                HourForeCast hourForeCast = hourForeCasts.get(index);
            }
        }
    }
    private void drawDailyPage(Canvas canvas) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        dailyAngleSet.clear();
        List<DailyForecast> dailyForecasts = currentHeweather5.getDailyForecast();
        int dateIndex = 0;
        for (int i = 0; i < dailyForecasts.size(); i++) {
            if (dailyForecasts.get(i).getDate().equals(date)) {
                dateIndex = i;
                break;
            }
        }

            int len = dailyForecasts.size();



            Paint circlePaint = new Paint();
            circlePaint.setColor(Color.RED);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeWidth(4);
            Paint mTextPaint = new Paint();
            mTextPaint.setColor(Color.BLACK);
            mTextPaint.setTextSize(numSize);
            Paint nTextPaint = new Paint();
            nTextPaint.setColor(Color.BLACK);
            nTextPaint.setTextSize(numSize);



        List<String> stringCalender = new ArrayList<String>();
        List<String> stringDayCond = new ArrayList<String>();
        List<String> stringNightCond = new ArrayList<String>();
        for (int i = 0; i < dailyForecasts.size(); i++){
            DailyForecast dailyForecast = dailyForecasts.get(i);
            String calender;
            String dayCond;
            String nightCond;
             try {
                calender = DateTimeCalender.getWeekOfDate(new SimpleDateFormat("yyyy-MM-dd").parse(dailyForecast.getDate()));
                dayCond = dailyForecast.getCond().getTxt_d() + " " + dailyForecast.getTmp().getMax();
                nightCond = dailyForecast.getCond().getTxt_n() + " " + dailyForecast.getTmp().getMin();
                stringDayCond.add(dayCond);
                stringNightCond.add(nightCond);
                stringCalender.add(calender);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<List<String>> strings = new ArrayList<List<String>>();
        strings.add(stringCalender);
        strings.add(stringDayCond);
        strings.add(stringNightCond);
        // 确定昨天今天明天和周几
        for (int i = 0; i < dailyForecasts.size(); i++){
            if (i == dateIndex){
                stringCalender.set(i, "今天");
            }
            if (i == dateIndex - 1){
                stringCalender.set(i, "昨天");
            }
            if (i == dateIndex + 1){
                stringCalender.set(i, "明天");
            }
        }
        // 内圈文字半径， mrange main文字的半径, nrange 温度天气的半径
        // 包围文字的两个圆
        float circleRadius1 = phyMaxRadius - numSize - mTextPaint.getFontMetrics().descent;
        float circleRadius2 = phyMaxRadius - (numSize + mTextPaint.getFontMetrics().descent) * 2;
        float circleRadius3 = phyMaxRadius - (numSize + mTextPaint.getFontMetrics().descent) * 3;

        float textRadius1 = phyMaxRadius - numSize;
        float textRadius2 = phyMaxRadius - 2 * numSize - mTextPaint.getFontMetrics().descent;
        float textRadius3 = phyMaxRadius - 3 * numSize - mTextPaint.getFontMetrics().descent * 2;

        RectF rectf1 = new RectF(centerX - textRadius1, centerY - textRadius1, centerX + textRadius1, centerY + textRadius1);
        RectF rectf2 = new RectF(centerX - textRadius2, centerY - textRadius2, centerX + textRadius2, centerY + textRadius2);
        RectF rectf3 = new RectF(centerX - textRadius3, centerY - textRadius3, centerX + textRadius3, centerY + textRadius3);

        drawRepeatPage(canvas, dailySweepAngle, dailyAngleSet, new float[]{phyMaxRadius, circleRadius1, circleRadius2, circleRadius3}, new RectF[]{rectf1, rectf2, rectf3}, new Paint[]{circlePaint, mTextPaint, mTextPaint, mTextPaint}, strings, new boolean[]{false, true, true});

        if (dailyPagePressed){
            int index = calculateIndexZone(dailySweepAngle, dailyAngleSet);
            if (index!=-1){
                DailyForecast dailyForecast = dailyForecasts.get(index);
            }
        }
    }




    private void drawSuggestionPage(Canvas canvas) {
        Suggestion suggestion = currentHeweather5.getSuggestion();
        if (suggestion == null){
            nextState();
            requestLayout();
            return;
        }
        List<String> indicators = new ArrayList<String>();
        List<String> brf = new ArrayList<String>();
        List<String> txt = new ArrayList<String>();
        if (suggestion.getAirBrf()!= null){
            indicators.add(getResources().getString(R.string.air_suggestion));
            brf.add(suggestion.getAirBrf());
            txt.add(suggestion.getAirTxt());
        }
        if (suggestion.getComfBrf()!= null){
            indicators.add(getResources().getString(R.string.comf_suggestion));
            brf.add(suggestion.getComfBrf());
            txt.add(suggestion.getComfTxt());
        }
        if (suggestion.getCwBrf()!= null){
            indicators.add(getResources().getString(R.string.cw_suggestion));
            brf.add(suggestion.getCwBrf());
            txt.add(suggestion.getCwTxt());
        }
        if (suggestion.getDrsgBrf()!= null){
            indicators.add(getResources().getString(R.string.drsg_suggestion));
            brf.add(suggestion.getDrsgBrf());
            txt.add(suggestion.getDrsgTxt());
        }
        if (suggestion.getFluBrf()!= null){
            indicators.add(getResources().getString(R.string.flu_suggestion));
            brf.add(suggestion.getFluBrf());
            txt.add(suggestion.getFluTxt());
        }
        if (suggestion.getSportBrf()!= null){
            indicators.add(getResources().getString(R.string.sport_suggestion));
            brf.add(suggestion.getSportBrf());
            txt.add(suggestion.getSportTxt());
        }
        if (suggestion.getTravBrf()!= null){
            indicators.add(getResources().getString(R.string.trav_suggestion));
            brf.add(suggestion.getTravBrf());
            txt.add(suggestion.getTravTxt());
        }
        if (suggestion.getUvBrf()!= null){
            indicators.add(getResources().getString(R.string.uv_suggestion));
            brf.add(suggestion.getUvBrf());
            txt.add(suggestion.getUvTxt());
        }


        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(4);
        Paint mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(numSize);
        Paint nTextPaint = new Paint();
        nTextPaint.setColor(Color.BLACK);
        nTextPaint.setTextSize(numSize);




        float circleRadius1 = phyMaxRadius - numSize - mTextPaint.getFontMetrics().descent;
        float circleRadius2 = phyMaxRadius - (numSize + mTextPaint.getFontMetrics().descent) * 2;
//        float circleRadius3 = phyMaxRadius - (numSize + mTextPaint.getFontMetrics().descent) * 3;

        float textRadius1 = phyMaxRadius - numSize;
        float textRadius2 = phyMaxRadius - 2 * numSize - mTextPaint.getFontMetrics().descent;
//        float textRadius3 = phyMaxRadius - 3 * numSize - mTextPaint.getFontMetrics().descent * 2;

        RectF rectf1 = new RectF(centerX - textRadius1, centerY - textRadius1, centerX + textRadius1, centerY + textRadius1);
        RectF rectf2 = new RectF(centerX - textRadius2, centerY - textRadius2, centerX + textRadius2, centerY + textRadius2);
//        RectF rectf3 = new RectF(centerX - textRadius3, centerY - textRadius3, centerX + textRadius3, centerY + textRadius3);
        List<List<String>> strings = new ArrayList<List<String>>();
        strings.add(indicators);
        strings.add(brf);
        drawRepeatPage(canvas, suggestionSweepAngle, suggestionAngleSet, new float[]{phyMaxRadius, circleRadius1, circleRadius2}, new RectF[]{rectf1, rectf2}, new Paint[]{circlePaint, mTextPaint, mTextPaint}, strings, new boolean[]{false, false});

        if (suggestionPagePressed){
            int index = calculateIndexZone(suggestionSweepAngle, suggestionAngleSet);
            if (index!=-1){
                String str = txt.get(index);
            }
        }
    }
    private void drawCityPage(Canvas canvas) {
        cityAngleSet.clear();
        float textRadius = phyMaxRadius - mainSize;


        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(4);
        Paint mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(mainSize);
        Paint nTextPaint = new Paint();
        nTextPaint.setColor(Color.BLACK);
        nTextPaint.setTextSize(numSize);

        // 内圈文字半径， mrange main文字的半径, nrange 温度天气的半径
        // 包围文字的两个圆

        float innerRadius = textRadius - mTextPaint.getFontMetrics().descent - numSize;
        canvas.drawCircle(centerX, centerY, textRadius + mainSize, circlePaint);
        canvas.drawCircle(centerX, centerY, textRadius - mTextPaint.getFontMetrics().descent, circlePaint);
        canvas.drawCircle(centerX, centerY, innerRadius - nTextPaint.getFontMetrics().descent, circlePaint);
        RectF mRange = new RectF(centerX - textRadius, centerY - textRadius, centerX + textRadius, centerY + textRadius);
        RectF nRange = new RectF(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius);

        float eachAngle = cityName.size() >= 5 ? 60 : 300 / cityName.size();
        for (int i = 0; i < cityName.size(); i++) {
            if (120 + citySweepAngle + eachAngle * (i + 0.5) >= VISIABLE_START_ANGLE && 120 + citySweepAngle + eachAngle * (i + 0.5) <= VISIABLE_END_ANGLE) {
                Path path = new Path();
                path.addArc(mRange, 120 + citySweepAngle + eachAngle * i, eachAngle);
                float textWidth = mTextPaint.measureText(cityName.get(i));
                float hOffset = (float) (textRadius * Math.PI / 360 * eachAngle - textWidth / 2);// 水平偏移
                canvas.drawTextOnPath(cityName.get(i), path, hOffset, 0, mTextPaint);
                for (int j = 0; j < weatherInfoList.size(); j++){
                    Heweather5 heweather5 = weatherInfoList.get(j);
                    if (heweather5.getCityName().equals(cityName.get(i))){
                        String str = heweather5.getNow().getCondTxt() + "  " + heweather5.getDailyForecast().get(0).getTmp().getMin() + "~" + heweather5.getDailyForecast().get(0).getTmp().getMax();
                        Path pathn = new Path();
                        pathn.addArc(nRange, 120 + citySweepAngle + eachAngle * i, eachAngle);
                        float textWidthn = nTextPaint.measureText(str);
                        float hOffsetn = (float) (innerRadius * Math.PI / 360 * eachAngle - textWidthn / 2);// 水平偏移
                        canvas.drawTextOnPath(str, pathn, hOffsetn, 0, nTextPaint);
                    }
                }
            }


            cityAngleSet.add(new Limit(eachAngle * (i + 1)));
            // 画开始的图像
//            if (citySweepAngle > 0){
//                float width = mainSize + mTextPaint.getFontMetrics().descent +nTextPaint.getFontMetrics().descent + numSize;
//                float radius = phyMaxRadius - width;
//                RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
//                Paint startAreaPaint = new Paint();
//                startAreaPaint.setStyle(Paint.Style.STROKE);
//                startAreaPaint.setStrokeWidth(width);
//                canvas.drawArc(rectF, 90, citySweepAngle + 120, false, startAreaPaint);
//
//            }
        }
        if (cityPagePressed){
            int index = calculateIndexZone(citySweepAngle, cityAngleSet);
            if (index!=-1){

            }
        }
    }

    /**\
     * Canvas canvas 画布
     * float sweepAngle 该page的拖动角度
     * List<Limit> angleSet 边界值
     * float[] radiuses 圆的半径
     * Paint[] paintses 画笔，第一个画笔是圆画笔，之后的画笔是字体的画笔
     * List<List<String>> strings 每一圈的文本
     */

    private void drawRepeatPage(Canvas canvas, float sweepAngle, List<Limit> angleSet, float[] radiuses, RectF[] rectFs, Paint[] paintses, List<List<String>> strings, boolean[] booleens) {
//        float innerRadius = textRadius - mTextPaint.getFontMetrics().descent - numSize;
//
//        RectF mRange = new RectF(centerX - textRadius, centerY - textRadius, centerX + textRadius, centerY + textRadius);
//        RectF nRange = new RectF(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius);

        if (paintses!=null) {
            for (int i = 0; i < radiuses.length; i++) {
                canvas.drawCircle(centerX, centerY, radiuses[i], paintses[0]);
            }
        }
        float eachAngle;
        if (strings.get(0).size()==0){
            eachAngle = 300;
        }else {
             eachAngle = strings.get(0).size() >= 5 ? 60 : 300 / strings.get(0).size();
        }
        for (int i = 0; i < strings.size(); i++){
            boolean ob = booleens[i];
            List<String> string = strings.get(i);
            Paint paint = paintses[i + 1];
            RectF rectF = rectFs[i];
            float radius = rectF.width()/2;
            for (int j = 0; j < string.size(); j++) {
                if (120 + sweepAngle + eachAngle * (j + 0.5) >= VISIABLE_START_ANGLE && 120 + sweepAngle + eachAngle * (j + 0.5) <= VISIABLE_END_ANGLE) {

                    Path path = new Path();
                    path.addArc(rectF, 120 + sweepAngle + eachAngle * j, eachAngle);
                    float textWidth = paint.measureText(string.get(j));
                    float hOffset = (float) (radius * Math.PI / 360 * eachAngle - textWidth / 2);// 水平偏移
                    canvas.drawTextOnPath(string.get(j), path, hOffset, 0, paint);
                    if (ob){
                        Paint p = new Paint();
                        p.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, getResources().getDisplayMetrics()));
                        canvas.drawTextOnPath("o", path, hOffset + textWidth, paint.getFontMetrics().ascent + p.getTextSize(), p);
                    }
                }
                if (i == 0) {
                    angleSet.add(new Limit(eachAngle * (j + 1)));
                }
            }

        }


    }
    private float mLastX;
    private float mLastY;
    private float clickedX;
    private float clickedY;
    private float clickedAngle;
    private long mDownTime;
    private float mTmpAngle;
    private boolean isFling;
    private AutoFlingRunnable mFlingRunnable;
    private final float VISIABLE_START_ANGLE = 90;
    private final float VISIABLE_END_ANGLE = 450;
    private final float STARTANGLE = 120;
    private final float ANGLE_DECRASE_RATIO = 1.0666F;
    private final int mFlingableValue = 15;
    private final float NOCLICK_VALUE = 5;

    // 是否点击了nextButton
    private boolean isNextButtonPressed = false;
    // 点击是否在小圆内
    private boolean isInCenterRadius = false;
    // 各个page页面是否被点击
    private boolean cityPagePressed = false;
    private boolean dailyPagePressed = false;
    private boolean hourlyPagePressed = false;
    private boolean suggestionPagePressed = false;
    private boolean aqiPagePressed = false;
    private boolean nowPagePressed = false;

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
                if (isFling) {
                    // 当视图正在滚动时，移除快速滚动的回调，停止旋转，保证move时ifFling为flase
                    removeCallbacks(mFlingRunnable);
                    isFling = false;
                    return true;
                }

                if (inCenterRadius(x, y)) {
                    isInCenterRadius = true;
                    isNextButtonPressed = false;
                    return true;
                } else if (inNextButton(x, y)) {
                    isNextButtonPressed = true;
                    isInCenterRadius = false;
                    return true;
                } else {
                    isNextButtonPressed = false;
                    isInCenterRadius = false;
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                // 首先判断是否点击在中心圆之内或者nextbutton之内，如果在则屏蔽move事件
                if (isInCenterRadius || isNextButtonPressed) {
                    return true;
                } else {
                    /**
                     * 获得开始的角度
                     */
                    float start = getAngle(mLastX, mLastY);
                    /**
                     * 获得当前的角度
                     */
                    float end = getAngle(x, y);
                    float addedAngle = Math.abs(end - start) >= 180 ? (end > 180 ? end - 360 - start : end + 360 - start) : end - start;

                    /**
                     * 改变page的sweep角度，需要判断当前页，可以使得每个页面度保存自己的角度值
                     */
                    switch (currentPage) {
                        case now:
                            nowSweepAngle += comAngle(nowSweepAngle, addedAngle, 30f, 270 - nowAngleSet.get(nowAngleSet.size() - 1).getBoud());
                            break;
                        case hourly:
                            if (hourlyAngleSet.size()!=0) {
                                hourlySweepAngle += comAngle(hourlySweepAngle, addedAngle, 30f, 270 - hourlyAngleSet.get(hourlyAngleSet.size() - 1).getBoud());
                            }else{
                                hourlySweepAngle += comAngle(hourlySweepAngle, addedAngle, 30f, 270 - 0);

                            }
                            break;
                        case daily:
                            dailySweepAngle += comAngle(dailySweepAngle, addedAngle, 30f, 270 - dailyAngleSet.get(dailyAngleSet.size() - 1).getBoud());
                            break;
                        case suggestion:
                            suggestionSweepAngle += comAngle(suggestionSweepAngle, addedAngle, 30f, 270 - suggestionAngleSet.get(suggestionAngleSet.size() - 1).getBoud());
                            break;
                        case city:
                            citySweepAngle += comAngle(citySweepAngle, addedAngle, 30f, 270 - cityAngleSet.get(cityAngleSet.size() - 1).getBoud());
                            break;
                        case aqi:
                            aqiSweepAngle += comAngle(aqiSweepAngle, addedAngle, 30f, 270 - aqiAngleSet.get(aqiAngleSet.size() - 1).getBoud());
                            break;
                    }

                    mTmpAngle += addedAngle;
                    requestLayout();

                    mLastX = x;
                    mLastY = y;
                    return true;
                }
            case MotionEvent.ACTION_UP:
                if (isInCenterRadius) {
                    return true;
                } else if (isNextButtonPressed) {
                    nextState();
                    requestLayout();
                    return true;
                } else {
                    // 计算，每秒移动的角度
                    float anglePerSecond = mTmpAngle * 1000
                            / (System.currentTimeMillis() - mDownTime);

                    // 如果达到该值认为是快速移动，在此返回并消费事件，开启线程自动减速旋转
                    if (Math.abs(anglePerSecond) > mFlingableValue && !isFling) {
                        // post一个任务，去自动滚动
                        post(mFlingRunnable = new AutoFlingRunnable(anglePerSecond));
                        return true;
                    }

                    // 如果没有快速转动，有可能（1，转速不够2， 正在转动），如果当前旋转角度超过NOCLICK_VALUE屏蔽点击
                    if (Math.abs(mTmpAngle) < NOCLICK_VALUE) {  // 点击事件
                        clickedX = event.getX();
                        clickedY = event.getY();
                        if (inInstristZone(clickedX, clickedY, phyMaxRadius, centerRadius)) {
                            clickedAngle = getAngle(clickedX, clickedY);
                            clickedAngle = clickedAngle <= 90 ? 360 + clickedAngle : clickedAngle;
                            switch (currentPage) {
                                case now:
                                    nowPagePressed = !nowPagePressed;
                                    break;
                                case hourly:
                                    hourlyPagePressed = !hourlyPagePressed;
                                    break;
                                case daily:
                                    dailyPagePressed = !dailyPagePressed;
                                    break;
                                case suggestion:
                                    suggestionPagePressed = !suggestionPagePressed;
                                    break;
                                case city:
                                    cityPagePressed = !cityPagePressed;

                                    float siteAngle = clickedAngle - citySweepAngle - STARTANGLE;
                                    int cityNameIndex = 0;
                                    for (int i = 0; i < cityAngleSet.size(); i++) {
                                        if (siteAngle < cityAngleSet.get(i).getBoud()) {
                                            cityNameIndex = i;
                                            currentCity = cityName.get(i);
                                            break;
                                        }
                                    }
                                    for (int i = 0; i < weatherInfoList.size(); i++) {
                                        if (weatherInfoList.get(i).getCityName().equals(cityName.get(cityNameIndex))) {
                                            currentHeweather5 = weatherInfoList.get(i);
                                            mItemClickListener.onClick(currentHeweather5);
                                            break;
                                        }
                                    }

                                    break;
                                case aqi:
                                    break;
                            }
//
                            requestLayout();
                        }
                    }
                    return true;

                }
                default:
                    return false;
        }
    }

    private int calculateIndexZone(float sweepAngle, List<Limit> limits){
        float siteAngle = clickedAngle - sweepAngle - STARTANGLE;
        int cityNameIndex = -1;
        for (int i = 0; i < limits.size(); i++) {
            if (siteAngle < limits.get(i).getBoud()) {
                cityNameIndex = i;
                return cityNameIndex;
            }
        }
        return cityNameIndex;
    }
    private boolean inInstristZone(float cliX, float cliY, float maxR, float minR) {
        return (Math.hypot(cliX - centerX, cliY - centerY) <= maxR) && (Math.hypot(cliX - centerX, cliY - centerY) >= minR);
    }

    private float comAngle(float nowAngle, float addedAngle, float maxValue, float minValue) {
        if (nowAngle + addedAngle >= maxValue){
            return (maxValue - nowAngle) / 2;
        }else if (nowAngle + addedAngle <= minValue){
            return (minValue - nowAngle) / 2;
        } else {
            return addedAngle;
        }

    }

    private void nextState() {
        switch (currentPage) {
            case now:
                currentPage = PageSelect.now;
                break;
            case hourly:
                currentPage = PageSelect.aqi;
                break;
            case daily:
                currentPage = PageSelect.hourly;
                break;
            case suggestion:
                currentPage = PageSelect.city;
                break;
            case city:
                currentPage = PageSelect.daily;
                break;
            case aqi:
                currentPage = PageSelect.suggestion;
                break;
        }
    }

    private boolean inNextButton(float x, float y) {
        return Math.hypot(x - centerNextButtonX, y - centerNextButtonY) < nextSize / 2;
    }

    private boolean inCenterRadius(float x, float y) {
        return Math.hypot(x - centerX, y - centerY) < centerRadius;
    }


    private void drawRotateText(Canvas canvas, float cx, float cy, float r, float degree, String str, int ts) {
        RectF mRange = new RectF(cx - r, cy - r, cx + r, r + cy);
        Path path = new Path();
        Paint mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
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
            switch (currentPage){
                case now:
                    nowRun();
                    break;
                case hourly:
                    hourlyRun();
                    break;
                case daily:
                    dailyRun();
                    break;
                case suggestion:
                    suggestionRun();
                    break;
                case city:
                    cityRun();
                    break;
                case aqi:
                    aqiRun();
            }
            // 如果小于20,则停止
            if ((int) Math.abs(angelPerSecond) < 20)
            {
                isFling = false;
                return;
            }
            isFling = true;
            // 不断改变mStartAngle，让其滚动，/30为了避免滚动太快
//            mStartAngle += (angelPerSecond / 30);
            // 逐渐减小这个值
            angelPerSecond /= 1.0666F;
            postDelayed(this, 30);
            // 重新布局
            requestLayout();
            return;
        }

        private void aqiRun() {
            if (aqiSweepAngle >= 0){
                aqiSweepAngle -= Math.min(Math.abs((angelPerSecond / 30)), aqiSweepAngle);
            } else if (aqiSweepAngle <= 300 - aqiAngleSet.get(aqiAngleSet.size() - 1).getBoud()){
                aqiSweepAngle += Math.min(Math.abs(angelPerSecond / 30), 300 - aqiAngleSet.get(aqiAngleSet.size() - 1).getBoud() - aqiSweepAngle);
            }else{
                aqiSweepAngle += (angelPerSecond / 30);
            }
        }


        private void hourlyRun() {

            if (hourlySweepAngle >= 0){
                hourlySweepAngle -= Math.min(Math.abs((angelPerSecond / 30)), hourlySweepAngle);
            } else if (hourlySweepAngle <= 300 - hourlyAngleSet.get(hourlyAngleSet.size() - 1).getBoud()){
                hourlySweepAngle += Math.min(Math.abs(angelPerSecond / 30), 300 - hourlyAngleSet.get(hourlyAngleSet.size() - 1).getBoud() - hourlySweepAngle);
            }else{
                hourlySweepAngle += (angelPerSecond / 30);
            }
        }

        private void nowRun() {

            if (nowSweepAngle >= 0){
                nowSweepAngle -= Math.min(Math.abs(angelPerSecond / 30), nowSweepAngle);
            } else if (nowSweepAngle <= 300 - nowAngleSet.get(nowAngleSet.size() - 1).getBoud()){
                nowSweepAngle += Math.min(Math.abs(angelPerSecond / 30), 300 - nowAngleSet.get(nowAngleSet.size() - 1).getBoud() - nowSweepAngle);
            }else {
                nowSweepAngle += (angelPerSecond / 30);
            }
        }
        private void cityRun(){

            if (citySweepAngle >= 0){
                citySweepAngle -= Math.min(Math.abs((angelPerSecond / 30)), citySweepAngle);
            } else if (citySweepAngle <= 300 - cityAngleSet.get(cityAngleSet.size() - 1).getBoud()){
                citySweepAngle += Math.min(Math.abs(angelPerSecond / 30), 300 - cityAngleSet.get(cityAngleSet.size() - 1).getBoud() - citySweepAngle);
            }else{
                citySweepAngle += (angelPerSecond / 30);
            }
        }
        private void suggestionRun(){

            if (suggestionSweepAngle >= 0){
                suggestionSweepAngle -= Math.min(Math.abs((angelPerSecond / 30)), suggestionSweepAngle);
            } else if (suggestionSweepAngle <= 300 - suggestionAngleSet.get(suggestionAngleSet.size() - 1).getBoud()){
                suggestionSweepAngle += Math.min(Math.abs(angelPerSecond / 30), 300 - suggestionAngleSet.get(suggestionAngleSet.size() - 1).getBoud() -suggestionSweepAngle);
            }else{
                suggestionSweepAngle += (angelPerSecond / 30);
            }
        }
        private void dailyRun(){

            if (dailySweepAngle >= 0){
                dailySweepAngle -= Math.min(Math.abs((angelPerSecond / 30)), dailySweepAngle);
            } else if (dailySweepAngle <= 300 - dailyAngleSet.get(dailyAngleSet.size() - 1).getBoud()){
                dailySweepAngle += Math.min(Math.abs(angelPerSecond / 30), 300 - dailyAngleSet.get(dailyAngleSet.size() - 1).getBoud() - dailySweepAngle);
            }else{
                dailySweepAngle += (angelPerSecond / 30);
            }
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




















    // set get add
    public List<String> getCityName() {
        return cityName;
    }

    public void setCityName(List<String> cityName) {
        this.cityName = cityName;
    }

    public void addCity(String str){
        if (!cityName.contains(str)){
            cityName.add(str);
            Heweather5 heweather5 = new Heweather5(mHandler, str);
            if (cityName.size() == 1){
                currentCity = cityName.get(0);
            }
        }
    }
    private void deleteCity(String city){
        if (cityName!=null){
            cityName.remove(city);
        }
        if (weatherInfoList!=null){
            for (int i = 0; i < weatherInfoList.size(); i++){
                if (weatherInfoList.get(i).getCityName().equals(city)){
                    weatherInfoList.remove(i);
                    i--;
                }
            }
        }
    }
}
