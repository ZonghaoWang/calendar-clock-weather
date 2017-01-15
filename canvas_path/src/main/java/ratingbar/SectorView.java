/**
 * 
 */
package ratingbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author gl 自定义一个扇形的进度条
 * @intro 引用只需设置宽度，高度会自动和宽度相等
 */
public class SectorView extends View {

	/**
	 * 扇形默认的颜色
	 */
	private int COLOR = 0xffFF5AB0;

	private Context context;
	private Paint paint;
	private float percent = 0;// 扇形当前的百分比,0~1
	private int height;

	public SectorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public SectorView(Context context) {
		super(context);
		init(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawArc(new RectF(0, 0, height, height), -90, percent * 360, true, paint);
	}

	private void init(Context context) {
		this.context = context;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(COLOR);
		post(new Runnable() {
			public void run() {
				height = getWidth();
			}
		});

	}
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 决定了当前View的大小
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
		int childWidthSize = getMeasuredWidth();
		int childHeightSize = getMeasuredHeight();
		// 高度和宽度一样
		heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
				MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 设置扇形角度，只能在0-100的范围内
	 * 
	 * @param percent
	 */
	public void setPercent(float percent) {
		if (percent > 1 || percent < 0) {
			percent = 0;
		} else {
			this.percent = 1 - percent;
		}
		invalidate();

	}

}
