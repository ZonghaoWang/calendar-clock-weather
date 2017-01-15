package utility;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by jierui on 2017/1/7.
 */

public class ProgressView extends Drawable {
    private int width, height, lineWidth;
    private float level;
    private Paint mPaint, nPaint; // mpaint 边划线，nPaint内部划线
    private RectF rectF;
    public ProgressView(int level, int width, int height){
        this.level = level * 360 / 100;
        this.width = width;
        this.height = height;
        lineWidth = 2;
        mPaint = new Paint();
        nPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setAntiAlias(true);

        nPaint.setStyle(Paint.Style.FILL);
        nPaint.setColor(Color.RED);
        rectF = new RectF(0, 0, width, height);
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(width / 2, height / 2, Math.min(width, height) / 2 - lineWidth / 2, mPaint);
        canvas.drawArc(rectF, -90, level, true, nPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
