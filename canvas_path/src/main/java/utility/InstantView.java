package utility;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by jierui on 2017/1/8.
 */

public class InstantView extends Drawable {
    private int level, width, height;
    private Paint mPaint;


    public InstantView(int level, int width, int height){

    }
    @Override
    public void draw(Canvas canvas) {

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
