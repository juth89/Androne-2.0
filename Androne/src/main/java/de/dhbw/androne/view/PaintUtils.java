package de.dhbw.androne.view;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

public class PaintUtils {

	private static final int CYAN = Color.rgb(51, 181, 229);
	
	public static Paint font(int lineDistance) {
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(lineDistance / 10);
		paint.setColor(CYAN);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(lineDistance * 3 / 4);
		return paint;
	}

	
	public static Paint cyanLine(int lineDistance) {
		return line(lineDistance / 10, CYAN);
	}
	
	public static Paint grayLine(int lineDistance) {
		return line(lineDistance / 20, Color.LTGRAY);
	}

	public static Paint dottedLine(int lineDistance) {
		Paint paint = cyanLine(lineDistance);
		float[] intervals = new float[] {
				lineDistance / 5,
				lineDistance / 5
		};
		float phase = lineDistance / 10;
		paint.setPathEffect(new DashPathEffect(intervals, phase));
		return paint;
	}
	
	private static Paint line(float strokeWidth, int color) {
		Paint paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
		paint.setColor(color);
		return paint;
	}
	
	public static Paint dot(int lineDistance) {
		return line(lineDistance / 4, CYAN);
	}
	
}
