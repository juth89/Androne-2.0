package de.dhbw.androne.view.shape;

import android.content.Context;
import android.util.AttributeSet;

public class TriangleView extends ShapeView {
	
	public TriangleView(Context context) {
		super(context);
	}
	
	public TriangleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	
	@Override
	protected void drawShape() {
		int startX = midX - ((shapeWidth / 2) * lineDistance);
		int startY = midY + ((shapeHeight - (shapeHeight / 2)) * lineDistance);
		int stopX = midX + ((shapeWidth - (shapeWidth / 2)) * lineDistance);
		int stopY = startY;
		
		canvas.drawLine(startX, startY, stopX, stopY, paint);
		
		stopX = (startX + stopX) / 2;
		stopY = stopY - (shapeHeight * lineDistance);
		
		canvas.drawLine(startX, startY, stopX, stopY, paint);

		startX = midX + ((shapeWidth - (shapeWidth / 2)) * lineDistance);
		
		canvas.drawLine(startX, startY, stopX, stopY, paint);		
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
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	protected void drawHeight() {
		// TODO Auto-generated method stub
		
	}
}
