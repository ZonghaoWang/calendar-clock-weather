package utility;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by jiangwj on 2015/3/18.
 */
public class WaveView extends ImageView {

    private Paint mPaint;

    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private float waveY;//起始点以及重点的y
    private float ctrolX,ctrolY;//控制点的y
    private float mWidth,mHeight;

    private Path mPath;// 路径对象

    private boolean isAdd;//水平增加

    private boolean isUp;//向上增加

    private Bitmap markBitmap;

    private boolean isAni=true;

    private int distHeight;//目标高度

    private float percent;//百分比


    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DITHER_FLAG);
        mPaint.setColor(0xcce1162e);
        // 不使用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        //mPaint.setMaskFilter(new EmbossMaskFilter(new float[] { 1, 1, 1F }, 0.1F, 0.1F, 10F));
        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));
        mPath = new Path();

        mXfermode=new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    public void addAnimotion(){

        percent= (float) (1-ctrolY/mHeight);

        if(percent>=0.7f) {
            mPaint.setColor(0xcc11ff24);
            setDist(20);
        }


        if (ctrolX >= mWidth + 1 / 4F * mWidth) {
            isAdd = false;
        }else if (ctrolX <= -1 / 4F * mWidth) {
            isAdd = true;
        }
        ctrolX = isAdd ? ctrolX + mWidth/10: ctrolX - mWidth/10;
        changeHeight();
        postInvalidate();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 如果类型是圆形，则强制改变view的宽高一致
         */
        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(width, width);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
        //控制点的Y,其实为高度为0
        ctrolY = mHeight;
        //起点终点的Y
        waveY =  ctrolY+mHeight/10;
        setDist(100);

    }

    public void setPath(){
        mPath.moveTo(-1 / 4F * mWidth, waveY);//起点
        mPath.quadTo(ctrolX, ctrolY, mWidth + 1 / 4F * mWidth, waveY);//二阶贝塞尔曲线
        mPath.lineTo(mWidth + 1 / 4F * mWidth, mHeight);
        mPath.lineTo(-1 / 4F * mWidth, mHeight);
        mPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(markBitmap==null){
            markBitmap=getMarkBitmap();
        }

        setPath();//生成图案路径

        //图层
        int sc = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(mPath, mPaint);//水波
        mPaint.setXfermode(mXfermode);
        canvas.drawBitmap(markBitmap,0,0,mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

        mPath.reset();//重置图案路径
        if(isAni){//是否开启动画
            addAnimotion();
        }
    }


    //设置当前百分比
    public void setDist(int percent){
        distHeight= (int) (mHeight*(100-percent)/100);
    }

    public void changeHeight(){

        if(ctrolY<distHeight){//现在的高度大于目标高度
            ctrolY +=1;
            waveY += 1;
        }

        if(ctrolY>=distHeight){
            ctrolY -=1;
            waveY -= 1;
        }
    }




    //覆盖层的圆形
    public Bitmap getMarkBitmap()
    {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DITHER_FLAG);
//        paint.setStyle(Paint.Style.FILL_AND_STROKE);
//        paint.setStrokeWidth(20);
        paint.setColor(Color.BLACK);
        // paint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.SOLID));
        canvas.drawCircle(mWidth/2, mWidth/ 2, mWidth / 2, paint);
        return bitmap;
    }




}
