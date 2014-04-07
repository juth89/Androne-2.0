package de.dhbw.androne.view.polygon;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import de.dhbw.androne.R;

public class PolygonView extends View implements OnTouchListener {
	
	private List<Point> points = new ArrayList<Point>();
	
	protected Canvas canvas;
	protected Paint paint = new Paint();
	
	protected int width, height, lineDistance, midX, midY, verticalLineCount;
	protected int horizontalLineCount = 12;
	
	private Bitmap droneBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

	private PolygonFragment polygonFragment;
	

	public PolygonView(Context context) {
		super(context);
		init();
	}
	
	public PolygonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public PolygonView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	
	private void init() {
		setOnTouchListener(this);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		this.canvas = canvas;
		
		calculateDefaults();
		
		paint.setColor(Color.LTGRAY);
		paint.setStrokeWidth(3);

		drawGrid();
		
		paint.setColor(Color.rgb(51, 181, 229));
		paint.setStrokeWidth(20);
		
		drawPoints();

		paint.setStrokeWidth(6);
		
		drawLines();
		drawDrone();
	}

	
	private void calculateDefaults() {
		width = canvas.getWidth();
		height = canvas.getHeight();
		lineDistance = height / horizontalLineCount;
		verticalLineCount = width / lineDistance;
		midX = (verticalLineCount / 2) * lineDistance;
		midY = (horizontalLineCount / 2) * lineDistance;
	}
	
	
	private void drawGrid() {
		for(int x = 0; x < width; x += lineDistance) {
			canvas.drawLine(x, 0, x, (lineDistance * horizontalLineCount), paint);
		}
		
		for(int y = 0; y < height; y += lineDistance) {
			canvas.drawLine(0, y, width, y, paint);
		}
	}
	
	
	public int getHorizontalLineCount() {
		return horizontalLineCount;
	}
	
	
	public int getVerticalLineCount() {
		return verticalLineCount;
	}
	

	public void addPoint(int x, int y) {
		Point point = new Point(x, y);
		points.add(point);
		updateButtons();
	}
	
	
	private void drawPoints() {
		for(int i = 0; i < points.size(); i++) {
			canvas.drawPoint(points.get(i).x, points.get(i).y, paint);
		}
	}

	
	private void drawLines() {
		if(points.size() < 2) {
			return;
		}
		
		for(int i = 1; i < points.size(); i++) {
			Point p1 = points.get(i - 1);
			Point p2 = points.get(i);
			
			canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
		}
	}
	
	
	private void drawDrone() {
		Point p1, p2 = null;
		int x, y;
		if(points.size() == 0) {
			return;
		} else if(points.size() == 1) {
			p1 = points.get(0);
		} else {
			p1 = points.get(0);
			p2 = points.get(1);
		}
		
		x = - droneBitmap.getWidth() / 2;
		y = - droneBitmap.getHeight() / 2;
		
		
		canvas.translate(p1.x, p1.y);
		
		if(points.size() > 1) {
			double a = p2.y - p1.y;
			double b = p2.x - p1.x;

			double degrees = Math.toDegrees(Math.atan(a / b));
			canvas.rotate((float) degrees);
		}
		
		canvas.drawBitmap(droneBitmap, x, y, paint);
	}
	
	
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if(arg1.getAction() == MotionEvent.ACTION_DOWN) {
			int x = (int) arg1.getX();
			int y = (int) arg1.getY();

			int fx = ((int)(x / lineDistance)) * lineDistance;
			int fy = ((int)(y / lineDistance)) * lineDistance;

			addPoint(fx, fy);
			invalidate();
			return true;
		}
		return false;
	}
	
	
	public void clearAll() {
		points.clear();
		invalidate();
		updateButtons();
	}
	
	
	public void clearLast() {
		points.remove(points.size() - 1);
		invalidate();
		updateButtons();
	}
	
	
	public List<Point> getPoints() {
		return points;
	}
	
	
	public void setFragment(PolygonFragment polygonFragment) {
		this.polygonFragment = polygonFragment;
		updateButtons();
	}
	
	
	private void updateButtons() {
		if(points.size() == 0) {
			polygonFragment.setButtons(false);
		} else {
			polygonFragment.setButtons(true);
		} 
	}
}
