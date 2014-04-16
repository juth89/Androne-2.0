package de.dhbw.androne.view.shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import de.dhbw.androne.R;
import de.dhbw.androne.model.Rectangle;
import de.dhbw.androne.model.Shape;
import de.dhbw.androne.model.Triangle;
import de.dhbw.androne.view.PaintUtils;

public class ShapeView extends View {

	private Canvas canvas;
	private Paint pGrayLine, pCyanLine, pDottedLine, pFont;
	
	private int width, height, lineDistance, midX, midY, verticalLineCount;
	private int horizontalLineCount = 12;
	
	private int startX, startY, stopX, stopY;
	
	private Rectangle rectangle = new Rectangle();
	private Triangle triangle = new Triangle();
	private Shape currentShape;
	
	private Bitmap droneBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
	
	
	public ShapeView(Context context) {
		super(context);
	}

	public ShapeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	
	@Override
	public void onDraw(Canvas canvas) {
		this.canvas = canvas;
		
		calculateDefaults();

		getPaints();
		
		drawGrid();
		drawShape();
		drawDrone();
		drawArrow();
		drawWidth();
		drawHeight();
	}


	private void calculateDefaults() {
		width = canvas.getWidth();
		height = canvas.getHeight();
		lineDistance = height / horizontalLineCount;
		verticalLineCount = width / lineDistance;
		midX = (verticalLineCount / 2) * lineDistance;
		midY = (horizontalLineCount / 2) * lineDistance;
		
		startX = midX - ((currentShape.getWidth() / 2) * lineDistance);
		stopX = midX + ((currentShape.getWidth() - (currentShape.getWidth() / 2)) * lineDistance);
		startY = midY - ((currentShape.getHeight() / 2) * lineDistance);
		stopY = midY + ((currentShape.getHeight() - (currentShape.getHeight() / 2)) * lineDistance);
	}

	
	private void getPaints() {
		pGrayLine = PaintUtils.grayLine(lineDistance);
		pCyanLine = PaintUtils.cyanLine(lineDistance);
		pDottedLine = PaintUtils.dottedLine(lineDistance);
		pFont = PaintUtils.font(lineDistance);
	}
	
	
	private void drawGrid() {
		for(int x = 0; x < width; x += lineDistance) {
			canvas.drawLine(x, 0, x, (lineDistance * horizontalLineCount), pGrayLine);
		}
		
		for(int y = 0; y < height; y += lineDistance) {
			canvas.drawLine(0, y, width, y, pGrayLine);
		}
	}
	
	
	private void drawShape() {
		if(currentShape.equals(rectangle)) {
			drawRectangle();
		} else if(currentShape.equals(triangle)) {
			drawTriangle();
		}
	}
	
	
	private void drawRectangle() {
		canvas.drawRect(startX, startY, stopX, stopY, pCyanLine);
	}
	
	
	private void drawTriangle() {
		int middleX = (startX + stopX) / 2;
		
		Path path = new Path();
		path.moveTo(startX, stopY);
		path.lineTo(stopX, stopY);
		path.lineTo(middleX, startY);
		path.lineTo(startX, stopY);
		
		canvas.drawPath(path, pCyanLine);
	}
	
	
	private void drawDrone() {
		int xR = stopX - droneBitmap.getWidth() / 2;
		int xL = startX - droneBitmap.getWidth() / 2;
		int y = stopY - droneBitmap.getHeight() / 2;
		
		if(currentShape.flyRight()) {
			canvas.drawBitmap(droneBitmap, xR,  y, null);
		} else {
			canvas.drawBitmap(droneBitmap, xL,  y, null);
		}
	}

	
	private void drawArrow() {
		int xTb, xM;
		int yT = stopY - lineDistance / 3;
		int yB = stopY + lineDistance / 3;
		
		if(currentShape.flyRight()) {
			xTb = stopX - lineDistance * 4 / 3;
			xM = stopX - lineDistance * 5 / 3; 
		} else {
			xTb = startX + lineDistance * 4 / 3;
			xM = startX + lineDistance * 5 / 3;
		}
		
		Path path = new Path();
		path.moveTo(xTb, yT);
		path.lineTo(xM, stopY);
		path.lineTo(xTb, yB);
		
		canvas.drawPath(path, pCyanLine);
	}
	
	
	private void drawWidth() {
		int x = (startX + stopX) / 2;
		int y = startY - lineDistance;
		int yT = y - lineDistance / 4;
		int yB = y + lineDistance / 4;
		
		canvas.drawLine(startX, yT, startX, yB, pCyanLine);
		canvas.drawLine(stopX, yT, stopX, yB, pCyanLine);
		
		Path path = new Path();
		path.moveTo(startX, y);
		path.lineTo(stopX, y);
		
		canvas.drawPath(path, pDottedLine);
		
		canvas.drawText("w", x, yT, pFont);
	}

	
	private void drawHeight() {
		int y = (startY + stopY) / 2;
		int x = stopX + lineDistance;

		if(currentShape.flyRight()) {
			x = startX - lineDistance;
		}
		
		int xL = x - lineDistance / 4;
		int xR = x + lineDistance / 4;
		int xT = x + lineDistance / 2;
		int yT = y + (lineDistance * 3 / 8);
		
		if(currentShape.flyRight()) {
			xT = x - lineDistance / 2;
		}
		
		canvas.drawLine(xL, startY, xR, startY, pCyanLine);
		canvas.drawLine(xL, stopY, xR, stopY, pCyanLine);
		
		Path path = new Path();
		path.moveTo(x, startY);
		path.lineTo(x, stopY);
		
		canvas.drawPath(path, pDottedLine);
		
		canvas.drawText("h", xT, yT, pFont);
	}

	
	/*
	 * Getter and setter
	 */
	public int getHorizontalLineCount() {
		return horizontalLineCount;
	}
	
	public int getVerticalLineCount() {
		return verticalLineCount;
	}

	public void setTriangle() {
		currentShape = triangle;
		invalidate();
	}
	
	public void setRectangle() {
		currentShape = rectangle;
		invalidate();
	}
	
	public Shape getCurrentShape() {
		return currentShape;
	}
	
	public Triangle getTriangle() {
		return triangle;
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}
	
	public int getShapeWidth() {
		return currentShape.getWidth();
	}
	
	public int getShapeHeight() {
		return currentShape.getHeight();
	}
	
	public void setShapeWidthHeight(int width, int height) {
		currentShape.setWidth(width);
		currentShape.setHeight(height);
		invalidate();
	}
	
	public void setDirection(boolean flyRight) {
		currentShape.setFlyRight(flyRight);
		this.invalidate();
	}
	
	
}
