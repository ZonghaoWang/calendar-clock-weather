package utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.jierui.canvas_path.R;

/**
 * Created by jierui on 2017/1/8.
 */

public class SandyClock extends View {
    public SandyClock(Context context) {
        this(context, null);
    }

    public SandyClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }



    public SandyClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 紧急的程度
     */
    private final int DEFAULT_LEVEL = 0;
    private int level = 0;
    private int totalDegree = 10;

    /**
     * 沙漏是否可点击，true为可点击
     */
    private boolean CLICKED = false;
    private boolean clickable = false;
    /**
     *  是否用动画模式
     */
    private boolean withAnmi = false;

    /**
     * lineColor waterColor dropColor
     */
    private final int DEFAULT_LINE_COLOR = Color.argb(0xff, 0x00, 0x00, 0x00);
    private final int DEFAULT_WATER_COLOR = Color.argb(0xff, 0x00, 0x00, 0x00);
    private final int DEFAULT_DROP_COLOR = Color.argb(0xff, 0x00, 0x00, 0x00);

    private int lineColor, waterColor, dropColor;

    /**
     * 沙漏线宽
     */
    private final int DEFAULT_SANDY_LINE_WIDTH = 4;
    private int sandyLineWidth;


    /**
     *
     */
    private Context context;
    private Canvas mCanvas;

    /**
     * yoffset 沙漏的高度margin
     */
    private final int OFFSETY = 1;
    private int offsetY;

    private final int WIDTHX = 100;
    private int widthX;
    private float percent = 0.5f;
    private float percentStep = 0.1f;

    public void setPercent(float percent){
        this.percent = percent / 100;
        invalidate();
    }
    public int getPercent(){
        return (int) (percent * 100);
    }

    private float clickX, clickY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickX = event.getX();
                clickY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (clickY > centerY) {
                    percent += percentStep;
                } else {
                    percent -= percentStep;
                }
                percent = percent > 1 ? 1 : percent;
                percent = percent < 0 ? 0 : percent;
                invalidate();
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        this.setFocusable(true);
        setClickable(CLICKED);
        if (attrs != null) {
            /**
             * TypedArray得到attrs下的某个view的所有属性
             * attr 从父view传过来的属性值，switch一下获得属性的px值，没有的选择默认值
             */

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SandyClock);
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++){
                int attr = a.getIndex(i);
                switch (attr){
                    case R.styleable.SandyClock_level:
                        level = a.getInteger(R.styleable.SandyClock_level, DEFAULT_LEVEL);
                        break;
                    case R.styleable.SandyClock_click:
                        clickable = a.getBoolean(R.styleable.SandyClock_click, CLICKED);
                        break;
                    case R.styleable.SandyClock_offsetY:
                        offsetY = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, OFFSETY, getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.SandyClock_widthX:
                        widthX = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTHX, getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.SandyClock_lineColor:
                        lineColor = a.getColor(attr, DEFAULT_LINE_COLOR);
                        break;
                    case R.styleable.SandyClock_waterColor:
                        waterColor = a.getColor(attr, DEFAULT_WATER_COLOR);
                        break;
                    case R.styleable.SandyClock_dropColor:
                        dropColor = a.getColor(attr, DEFAULT_DROP_COLOR);
                        break;
                    case R.styleable.SandyClock_sandyLineWidth:
                        sandyLineWidth = a.getDimensionPixelSize(attr, DEFAULT_SANDY_LINE_WIDTH);
                        break;
                    case R.styleable.SandyClock_distanceIO:
                        distanceIO = a.getDimensionPixelSize(attr, DEFAULT_DISTANCE_IO);
                        break;
                    case R.styleable.SandyClock_centerWidthX:
                         centerWidthX = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CENTER_WIDTHX, getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.SandyClock_centerHeightY:
                        centerHeightY = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CENTER_HEIGHTY, getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.SandyClock_holeWidth:
                        holeWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HOLE_WIDTH, getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.SandyClock_withAnmi:
                        withAnmi = a.getBoolean(attr, false);
                        break;
                    case R.styleable.SandyClock_startPercent:
                        percent = a.getFloat(attr, percent);
                        break;
                    case R.styleable.SandyClock_percentStep:
                        percentStep = a.getFloat(attr, percentStep);
                        break;
                    default:
                        break;
                }
            }
            a.recycle();
        }
    }
    /**
     * xml 文件传来的是否可点击事件
     */
    public void setClickable(boolean clickable) {
        this.CLICKED = clickable;
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
        }else {
            width = specModeWidth;
        }

        if (MeasureSpec.EXACTLY == specModeHeight){
            height = specSizeHeight;
        }else {
            height = specModeHeight;
        }
        setMeasuredDimension(width, height);

    }



    private float centerX, centerY, tlCenterX, trCenterX, blCenterX, brCenterX, tCenterY, bCenterY;
    private float rectH, rectW; // 矩形的宽和高
    private final float DEFAULT_HOLE_WIDTH = 0.5f;
    private float holeWidth = DEFAULT_HOLE_WIDTH;
    private RectF tlRectf, trRectf, blRectf, brRectf;
    private final float DEFAULT_CENTER_HEIGHTY = 2;
    private final float DEFAULT_CENTER_WIDTHX = 2;
    private float centerHeightY = DEFAULT_CENTER_HEIGHTY, centerWidthX = DEFAULT_CENTER_WIDTHX;
    private Paint linePaint, waterPaint, dropPaint;
    private Path linePath;


    // 圆形参量
    private float lcenterX, rcenterX, radius, alpha;
    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        centerX = mWidth / 2;
        centerY = mHeight / 2;
        float xian = (float) Math.hypot(centerY - centerHeightY - offsetY, widthX / 2 - centerWidthX);
        radius = (float) (xian * xian / Math.sqrt(xian * xian - (centerY - centerHeightY - offsetY) * (centerY - centerHeightY - offsetY))) / 2;
        alpha = (float) Math.asin(xian / 2 / radius) * 2;
        alpha = (float) (alpha * 180 / Math.PI);
        lcenterX = centerX - widthX / 2 + radius;
        rcenterX = centerX + widthX / 2 - radius;
        tCenterY = offsetY;
        bCenterY = mHeight - offsetY;
        tlRectf = new RectF(lcenterX - radius, tCenterY - radius, lcenterX + radius, tCenterY + radius);
        trRectf = new RectF(rcenterX - radius, tCenterY - radius, rcenterX + radius, tCenterY + radius);
        blRectf = new RectF(lcenterX - radius, bCenterY - radius, lcenterX + radius, bCenterY + radius);
        brRectf = new RectF(rcenterX - radius, bCenterY - radius, rcenterX + radius, bCenterY + radius);

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(sandyLineWidth);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePath = new Path();
        linePath.moveTo(centerX - widthX/2, tCenterY);
        linePath.arcTo(tlRectf, 180, -alpha);
        linePath.quadTo(centerX - holeWidth, centerY, centerX - centerWidthX, centerY + centerHeightY);
//        linePath.lineTo(centerX - holeWidth, centerY);
//        linePath.lineTo(centerX - centerWidthX, centerY + centerHeightY);
        linePath.arcTo(blRectf, 180 + alpha, -alpha);
        linePath.lineTo(centerX + widthX / 2, bCenterY);
        linePath.arcTo(brRectf, 0, -alpha);
        linePath.quadTo(centerX + holeWidth, centerY, centerX + centerWidthX, centerY - centerHeightY);
//        linePath.lineTo(centerX + holeWidth, centerY);
//        linePath.lineTo(centerX + centerWidthX, centerY - centerHeightY);
        linePath.arcTo(trRectf, alpha, -alpha);
        linePath.lineTo(centerX - widthX / 2, tCenterY);
        linePath.close();
        canvas.drawPath(linePath, linePaint);
        drawWater(canvas);
    }

    //    @Override
//    protected void onDraw(Canvas canvas) {
//        mCanvas = canvas;
//        int mWidth = getMeasuredWidth();
//        int mHeight = getMeasuredHeight();
//        centerX = mWidth / 2;
//        centerY = mHeight / 2;
//        rectW = (widthX / 2 - centerWidthX + centerY - centerHeightY - offsetY) * 2;
//        float hhh = centerY - centerHeightY - offsetY;
//        rectH = (float) (hhh / Math.sqrt(1 - hhh * hhh / rectW / rectW * 4)) * 2;
//        tlCenterX = blCenterX = centerX - widthX / 2 + rectW / 2;
//        trCenterX = brCenterX = centerX + widthX / 2 - rectW / 2;
//        tCenterY = offsetY;
//        bCenterY = mHeight - offsetY;
//
//        // 矩形
//        tlRectf = new RectF(tlCenterX - rectW / 2, tCenterY - rectH / 2, tlCenterX + rectW / 2, tCenterY + rectH / 2);
//        trRectf = new RectF(trCenterX - rectW / 2, tCenterY - rectH / 2, trCenterX + rectW / 2, tCenterY + rectH / 2);
//        blRectf = new RectF(blCenterX - rectW / 2, bCenterY - rectH / 2, blCenterX + rectW / 2, bCenterY + rectH / 2);
//        brRectf = new RectF(brCenterX - rectW / 2, bCenterY - rectH / 2, brCenterX + rectW / 2, bCenterY + rectH / 2);
//        linePaint = new Paint();
//        linePaint.setColor(lineColor);
//        linePaint.setStyle(Paint.Style.STROKE);
//        linePaint.setStrokeWidth(sandyLineWidth);
//        linePaint.setAntiAlias(true);
//        linePaint.setStrokeCap(Paint.Cap.ROUND);
//        linePath = new Path();
//        linePath.moveTo(centerX - widthX/2, tCenterY);
//        linePath.arcTo(tlRectf, 180, -45);
////        linePath.quadTo(centerX - holeWidth, centerY, centerX - centerWidthX, centerY + centerHeightY);
////        linePath.lineTo(centerX - holeWidth, centerY);
//        linePath.lineTo(centerX - centerWidthX, centerY + centerHeightY);
//        linePath.arcTo(blRectf, 225, -45);
//        linePath.lineTo(centerX + widthX / 2, bCenterY);
//        linePath.arcTo(brRectf, 0, -45);
////        linePath.quadTo(centerX + holeWidth, centerY, centerX + centerWidthX, centerY - centerHeightY);
////        linePath.lineTo(centerX + holeWidth, centerY);
//        linePath.lineTo(centerX + centerWidthX, centerY - centerHeightY);
//        linePath.arcTo(trRectf, 45, -45);
//        linePath.lineTo(centerX - widthX / 2, tCenterY);
//        linePath.close();
//
//        canvas.drawPath(linePath, linePaint);
////        maxWaterHeight = (int)(rectH * 0.618 / 2);
////        tWaterQuantity = new int[maxWaterHeight][2];
////        bWaterQuantity = new int[maxWaterHeight][2];
////        drawWater(canvas);
//    }





    //    @Override
//    protected void onDraw(Canvas canvas) {
//        mCanvas = canvas;
//        int mWidth = getMeasuredWidth();
//        int mHeight = getMeasuredHeight();
//        centerX = mWidth / 2;
//        centerY = mHeight / 2;
//        tlCenterX = blCenterX = centerX - centerWidthX;
//        trCenterX = brCenterX = centerX + centerWidthX;
//        tCenterY = offsetY;
//        bCenterY = mHeight - offsetY;
//
//        // 矩形宽高
//        rectH = (centerY - tCenterY - centerHeightY) * 2;
//        rectW = (widthX / 2 - centerWidthX) * 2;
//        // 四个矩形
//        tlRectf = new RectF(centerX - widthX / 2, tCenterY - rectH / 2, tlCenterX + rectW / 2, centerY - centerHeightY);
//        trRectf = new RectF(trCenterX - rectW / 2, tCenterY - rectH / 2, centerX + widthX/2, centerY - centerHeightY);
//        blRectf = new RectF(centerX - widthX/2, centerY + centerHeightY, blCenterX + rectW / 2, bCenterY + rectH / 2);
//        brRectf = new RectF(brCenterX - rectW / 2, centerY + centerHeightY, centerX + widthX/2, bCenterY + rectH / 2);
//
//
//        linePaint = new Paint();
//        linePaint.setColor(lineColor);
//        linePaint.setStyle(Paint.Style.STROKE);
//        linePaint.setStrokeWidth(sandyLineWidth);
//        linePaint.setAntiAlias(true);
//        linePaint.setStrokeCap(Paint.Cap.ROUND);
//        linePath = new Path();
//        linePath.moveTo(centerX - widthX/2, tCenterY);
//        linePath.arcTo(tlRectf, 180, -90);
//        linePath.lineTo(centerX - holeWidth, centerY);
//        linePath.lineTo(centerX - centerWidthX, centerY + centerHeightY);
//        linePath.arcTo(blRectf, 270, -90);
//        linePath.lineTo(centerX + widthX / 2, bCenterY);
//        linePath.arcTo(brRectf, 0, -90);
//        linePath.lineTo(centerX + holeWidth, centerY);
//        linePath.lineTo(centerX + centerWidthX, centerY - centerHeightY);
//        linePath.arcTo(trRectf, 90, -90);
//        linePath.lineTo(centerX - widthX / 2, tCenterY);
//        linePath.close();
//        canvas.drawPath(linePath, linePaint);
//        maxWaterHeight = (int)(rectH * 0.618 / 2);
//        tWaterQuantity = new int[maxWaterHeight][2];
//        bWaterQuantity = new int[maxWaterHeight][2];
//        drawWater(canvas);
//    }

    private int maxWaterHeight;
    private int[][] tWaterQuantity, bWaterQuantity;

    private void drawWater(Canvas canvas) {
        initPercent(percent);
        RectF rf = new RectF();
        linePath.computeBounds(rf,true);
        Region region = new Region();
        region.setPath(linePath,new Region((int)rf.left,(int)rf.top,(int)rf.right,(int)rf.bottom));
        int tStartH = (int)(centerY - centerHeightY - sandyLineWidth / 2);
        int tEndH = tStartH - maxWaterHeight;
        waterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        waterPaint.setColor(Color.RED);
        waterPaint.setStyle(Paint.Style.FILL);
        waterPaint.setStrokeWidth(10);


        setClipPath();
        markBitmap=getMarkBitmap();
        //图层
        int sc = canvas.saveLayer(0, 0, centerX * 2, centerY * 2, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(clipPath, waterPaint);
        waterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(markBitmap,0,0,waterPaint);
        waterPaint.setXfermode(null);
        canvas.restoreToCount(sc);

        clipPath.reset();//重置图案路径
        if (withAnmi) {
//            drawDrop(canvas);
            changeBeginAlpha();
            postInvalidate();
        }

    }

    private Path dropPath;
    private void drawDrop(Canvas canvas) {
        dropPath = new Path();
        float bHalfWide = (float) (Math.sqrt(Math.pow(radius, 2) - Math.pow(centerY * 2 - waveY2, 2)) + centerX - rcenterX);
        dropPath.moveTo(centerX - centerWidthX, centerY + centerHeightY);
        dropPath.arcTo(blRectf, 180 + alpha, -alpha);
        dropPath.lineTo(waveX, waveY2);
        for(int i = (int) waveX; i < centerX * 2; i++){
            dropPath.lineTo(i, (float) (waveY2 + centerY / 60 * Math.sin(i / (bHalfWide + centerX / 300) * Math.PI + beginAlpha + 1)));
        }
        dropPath.lineTo(centerX + widthX / 2, bCenterY);
        dropPath.arcTo(brRectf, 0, -alpha);
        dropPath.close();
        int sc = canvas.saveLayer(0, 0, centerX * 2, centerY * 2, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(dropPath, waterPaint);
        waterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(getDropBitmap(),0,0,waterPaint);
        waterPaint.setXfermode(null);
        canvas.restoreToCount(sc);

    }
    private float dropRadius = 20;
    private float dropCenterX = centerX, dropCenterY = centerY;
    private Bitmap getDropBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        innerRadius = (float) Math.sqrt(Math.pow(radius, 2) + Math.pow(distanceIO, 2) - distanceIO * (centerY - centerHeightY - offsetY) * 2);
        innerDeltaAlpha = (float) ((Math.asin((centerY - centerHeightY - offsetY) / radius) - Math.asin((centerY - centerHeightY - offsetY - distanceIO) / innerRadius)) * 180 / Math.PI);
        outerDeltaAlpha = (float) (Math.asin(distanceIO / innerRadius) * 180 / Math.PI);
        float innerAlpha = alpha - innerDeltaAlpha - outerDeltaAlpha;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DITHER_FLAG);

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(20);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(dropCenterX, dropCenterY, dropRadius, paint);
        // paint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.SOLID));
//        canvas.drawCircle(mWidth/2, mWidth/ 2, mWidth / 2, paint);
        return bitmap;
    }


    // 通过百分数计算高度
    private float usePercent = 0.8f;

    private void initPercent(float percent){
        percent = 1 - percent;
        float percent0 = percent * usePercent;
        waveY1 = (float) (centerY - Math.sqrt(percent0 * Math.pow(centerY - offsetY, 2)));
        waveY2 = (float) (centerY + Math.sqrt((1 - usePercent + percent0) * Math.pow(centerY - offsetY, 2)));
    }


    private Bitmap markBitmap;
    private float waveX, waveY1, waveY2;
    private float beginAlpha = 0;
    private Path clipPath;
    public void setClipPath(){
        float tHalfWide = (float) (Math.sqrt(Math.pow(radius, 2) - Math.pow(waveY1, 2)) + centerX - rcenterX);
        float bHalfWide = (float) (Math.sqrt(Math.pow(radius, 2) - Math.pow(centerY * 2 - waveY2, 2)) + centerX - rcenterX);

        waveX = 0;
        clipPath = new Path();
        clipPath.moveTo(waveX, waveY1);
        for(int i = (int) waveX; i < centerX * 2; i++){
            clipPath.lineTo(i, (float) (waveY1 + centerY / 60 * Math.sin(i / (tHalfWide + centerX / 300)  * Math.PI + beginAlpha)));
        }
        clipPath.lineTo(centerX * 2 - waveX, waveY1);//
        clipPath.lineTo(centerX * 2 - waveX, centerY + 0);
        clipPath.lineTo(waveX, centerY + 0);
        clipPath.lineTo(waveX, waveY2);
        for(int i = (int) waveX; i < centerX * 2; i++){
            clipPath.lineTo(i, (float) (waveY2 + centerY / 60 * Math.sin(i / (bHalfWide + centerX / 300) * Math.PI + beginAlpha + 1)));
        }
        clipPath.lineTo(centerX * 2 - waveX, waveY2);//
        clipPath.lineTo(centerX * 2 - waveX, centerY * 2);
        clipPath.lineTo(waveX, centerY * 2);
        clipPath.close();
    }

    private void changeBeginAlpha(){
        if (beginAlpha < Math.PI * 2){
            beginAlpha = beginAlpha + 0.05f;
        }else {
            beginAlpha = 0;
        }
        dropCenterY += centerY / 30;
    }
    //覆盖层的圆形
    private final int DEFAULT_DISTANCE_IO = 4;
    private int distanceIO = DEFAULT_DISTANCE_IO;
    private float innerRadius;
    private float innerDeltaAlpha, outerDeltaAlpha;
    private Path blinePath;
    public Bitmap getMarkBitmap()
    {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        innerRadius = (float) Math.sqrt(Math.pow(radius, 2) + Math.pow(distanceIO, 2) - distanceIO * (centerY - centerHeightY - offsetY) * 2);
        innerDeltaAlpha = (float) ((Math.asin((centerY - centerHeightY - offsetY) / radius) - Math.asin((centerY - centerHeightY - offsetY - distanceIO) / innerRadius)) * 180 / Math.PI);
        outerDeltaAlpha = (float) (Math.asin(distanceIO / innerRadius) * 180 / Math.PI);
        float innerAlpha = alpha - innerDeltaAlpha - outerDeltaAlpha;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DITHER_FLAG);

        // 四个矩形
        RectF btlRectf = new RectF(lcenterX - innerRadius, tCenterY - innerRadius, lcenterX + innerRadius, tCenterY + innerRadius);
        RectF btrRectf = new RectF(rcenterX - innerRadius, tCenterY - innerRadius, rcenterX + innerRadius, tCenterY + innerRadius);
        RectF bblRectf = new RectF(lcenterX - innerRadius, bCenterY - innerRadius, lcenterX + innerRadius, bCenterY + innerRadius);
        RectF bbrRectf = new RectF(rcenterX - innerRadius, bCenterY - innerRadius, rcenterX + innerRadius, bCenterY + innerRadius);

        float xDistance = (float) (radius - Math.sqrt(Math.pow(innerRadius, 2) - Math.pow(distanceIO, 2)));
        blinePath = new Path();
        blinePath.moveTo(centerX - widthX/2 + xDistance, tCenterY + distanceIO);
        blinePath.arcTo(btlRectf, 180 - outerDeltaAlpha, -innerAlpha);
        blinePath.lineTo(centerX - holeWidth, centerY);
        blinePath.lineTo(centerX - centerWidthX, centerY + centerHeightY + distanceIO);
        blinePath.arcTo(bblRectf, 180 + outerDeltaAlpha + innerAlpha, -innerAlpha);
        blinePath.lineTo(centerX + widthX / 2 - xDistance, bCenterY - distanceIO);
        blinePath.arcTo(bbrRectf, -outerDeltaAlpha, -innerAlpha);
        blinePath.lineTo(centerX + holeWidth, centerY);
        blinePath.lineTo(centerX + centerWidthX, centerY - centerHeightY - distanceIO);
        blinePath.arcTo(btrRectf, innerAlpha + outerDeltaAlpha, -innerAlpha);
        blinePath.lineTo(centerX - widthX / 2 + xDistance, tCenterY + distanceIO);
        blinePath.close();
//        paint.setStyle(Paint.Style.FILL_AND_STROKE);
//        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(20);
        paint.setColor(Color.BLACK);
        canvas.drawPath(blinePath, paint);
        // paint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.SOLID));
//        canvas.drawCircle(mWidth/2, mWidth/ 2, mWidth / 2, paint);
        return bitmap;
    }
}
