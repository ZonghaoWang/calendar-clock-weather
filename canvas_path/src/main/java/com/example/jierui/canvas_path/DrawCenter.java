package com.example.jierui.canvas_path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jierui on 2016/11/18.
 */

public class DrawCenter extends View {
    public DrawCenter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawCenter(Context context) {
        this(context, null);
    }

    public DrawCenter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specModeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int specModeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int specSizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specSizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int minDemi = Math.min(specSizeHeight, specSizeWidth);
//        setMeasuredDimension(minDemi, minDemi);
        setMeasuredDimension(minDemi, minDemi);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        int minDemi = Math.min(mHeight, mWidth);
        Paint p = new Paint();
        p.setColor(Color.RED);// 设置红色
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(80);



        canvas.drawCircle(mWidth/2, mHeight/2, minDemi/2 - 40, p);// 小圆


        drawRotateText(canvas, mWidth/2, mHeight/2, minDemi/2 - 120, 30, "乌鲁木齐", 80);
        drawRotateText(canvas, mWidth/2, mHeight/2, minDemi/2 - 120, 90, "北京", 80);
        drawRotateText(canvas, mWidth/2, mHeight/2, minDemi/2 - 120, 170, "呼和浩特", 80);
        System.out.println(Math.sin(1.57));
        Paint tp = new Paint();
        tp.setStrokeWidth(0.25f);
        tp.setTextSize(400);
        canvas.drawCircle(mWidth/2, mHeight/2, 10, p);
        canvas.drawText("|",mWidth/2, mHeight/2, tp);
        Paint.FontMetrics fontMetrics = tp.getFontMetrics();
        float baseX = mWidth/2;
        float baseY = mHeight/2;
        float topY = baseY + fontMetrics.top;
        float ascentY = baseY + fontMetrics.ascent;
        float descentY = baseY + fontMetrics.descent;
        float bottomY = baseY + fontMetrics.bottom;
        canvas.drawText( "|", baseX, baseY , tp);
        canvas.save();
        canvas.rotate(90, mWidth/2 + 20, mHeight/2 + (ascentY - bottomY) / 2);
        canvas.drawText("|",mWidth/2 + 20, mHeight/2 + (ascentY - bottomY) / 2, tp);
        canvas.restore();

//
//        canvas.save();
//        canvas.rotate(80);
//        canvas.drawText("画", 100, 700, tp);
//
//        canvas.restore();


        super.onDraw(canvas);
    }

    private void drawRotateText(Canvas canvas, float cx, float cy, float r, float degree, String str, int ts) {
        r = r - ts * 0.3333f;
        float theta = (float) (ts / r * 180 / Math.PI);
        float start = degree + (float)str.length() / 2 * theta;
        float end = degree - (float)str.length() / 2 * theta;
        Paint tp = new Paint();
        tp.setAntiAlias(true);
        tp.setStrokeJoin(Paint.Join.ROUND);
        tp.setTextSize(ts);
        Paint.FontMetrics fontMetrics = tp.getFontMetrics();
        float deltaY = (fontMetrics.top - fontMetrics.bottom) / 2 + fontMetrics.bottom;
        for (int i = 0; i < str.length(); i++) {
            canvas.save();
            float tx = (float) (cx + r * Math.cos((start) * Math.PI / 180));
            float ty = (float) (cy - r * Math.sin((start) * Math.PI / 180));
//            float txx = tx + ts / 2;
//            float tyy = ty - deltaY;
            canvas.rotate(90 - start + theta / 2, tx, ty);
            canvas.drawText(str.substring(i, i+1), tx, ty, tp);


            canvas.restore();
            start = start - theta;
        }
        r = r - ts * 0.1667f;
        RectF ovalSamll = new RectF(cx - r, cy - r, cx + r, cy + r);
        float r1 = r + ts;
        RectF ovalLarge = new RectF(cx - r1, cy - r1, cx + r1, cy + r1);
//        canvas.drawRect(cx - r, cy - r, cx + r, cy + r, tp);
        tp.setStyle(Paint.Style.STROKE);
        tp.setStrokeWidth(10);
        tp.setColor(Color.BLUE);
        Path path = new Path();
        start = degree + (float)str.length() / 2 * theta;
        float tx0 = (float) (cx + r * Math.cos((start) * Math.PI / 180));
        float ty0 = (float) (cy - r * Math.sin((start) * Math.PI / 180));
        path.moveTo(tx0, ty0);// 此点为多边形的起点
        path.arcTo(ovalSamll, 360 - start, start - end);
        float tx1 = (float) (cx + r1 * Math.cos((end) * Math.PI / 180));
        float ty1 = (float) (cy - r1 * Math.sin((end) * Math.PI / 180));
        path.lineTo(tx1, ty1);
        path.arcTo(ovalLarge, 360 - end, end - start);
        path.lineTo(tx0, ty0);
        path.close();
        canvas.drawPath(path, tp);




    }
}
