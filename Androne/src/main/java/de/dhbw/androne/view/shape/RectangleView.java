package de.dhbw.androne.view.shape;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;

public class RectangleView extends ShapeView {
	
	public RectangleView(Context context) {
		super(context);
	}
	
	public RectangleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RectangleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	

	@Override
	protected void drawShape() {
		int startX = midX - ((shapeWidth / 2) * lineDistance);
		int stopX = midX + ((shapeWidth - (shapeWidth / 2)) * lineDistance);
		int startY = midY - ((shapeHeight / 2) * lineDistance);
		int stopY = midY + ((shapeHeight - (shapeHeight / 2)) * lineDistance);
		
		canvas.drawRect(new Rect(startX, startY, stopX, stopY), paint);
	}

	@Override
	protected void drawDrone() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawArrow() {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	protected void drawWidth() {
		int startX = midX;
		
		if(shapeWidth % 2 == 0) {
			startX -= lineDistance / 2;
		}

		int stopX = startX + lineDistance;
		int startY = midY - (lineDistance * (shapeHeight / 2));
		
		paint.setColor(Color.WHITE);
		canvas.drawLine(startX, startY, stopX, startY, paint);
		
		paint.setStrokeWidth(3);
		paint.setColor(Color.LTGRAY);
		canvas.drawLine(startX, startY, stopX, startY, paint);

		paint.setColor(Color.rgb(51, 181, 229));
		paint.setTextAlign(Align.CENTER);
		paint.setStyle(Style.FILL_AND_STROKE);
		canvas.drawText("w", (startX + stopX) / 2, startY + (paint.getTextSize() / 3), paint);
	}
	
	
	@Override
	protected void drawHeight() {
		int startX = midX - (lineDistance * (shapeWidth / 2));

		int stopX = startX;
		int startY = midY - (lineDistance / 2);
		int stopY = midY + (lineDistance / 2);
		
		if(shapeHeight % 2 != 0) {
			startY += (lineDistance / 2);
			stopY += (lineDistance / 2);
		}
		
		paint.setStrokeWidth(6);
		paint.setColor(Color.WHITE);
		canvas.drawLine(startX, startY, stopX, stopY, paint);
		
		paint.setStrokeWidth(3);
		paint.setColor(Color.LTGRAY);
		canvas.drawLine(startX, startY, stopX, stopY, paint);

		paint.setColor(Color.rgb(51, 181, 229));
		paint.setTextAlign(Align.CENTER);
		canvas.drawText("h", (startX + stopX) / 2, startY + (paint.getTextSize() / 3) + (lineDistance / 2), paint);		
	}
}
