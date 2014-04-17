package de.dhbw.androne.view.polygon;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import de.dhbw.androne.R;
import de.dhbw.androne.model.Polygon;
import de.dhbw.androne.model.Vector2;
import de.dhbw.androne.view.PaintUtils;

public class PolygonView extends View implements OnTouchListener {
	
	private static List<Point> points = new ArrayList<Point>(); // static to hold the points after recreate view
	
	protected Canvas canvas;
	private Paint pGrayLine, pCyanLine, pDottedLine, pDot, pFont;
	
	protected int width, height, lineDistance, midX, midY, verticalLineCount;
	protected int horizontalLineCount = 8;
	
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
		
		getPaints();

		drawGrid();
		drawPoints();
		drawLines();
		drawScale();
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
	
	
	private void getPaints() {
		pGrayLine = PaintUtils.grayLine(lineDistance * 2 / 3);
		pCyanLine = PaintUtils.cyanLine(lineDistance * 2 / 3);
		pDottedLine = PaintUtils.dottedLine(lineDistance * 2 / 3);
		pDot = PaintUtils.dot(lineDistance * 2 / 3);
		pFont = PaintUtils.font(lineDistance * 2 / 3);
	}
	
	
	public int getHorizontalLineCount() {
		return horizontalLineCount;
	}
	
	
	public int getVerticalLineCount() {
		return verticalLineCount;
	}
	

	private void addPoint(int x, int y) {
		Point point = new Point(x, y);
		points.add(point);
		updateButtons();
	}
	
	
	private void drawGrid() {
		for(int x = 0; x < width; x += lineDistance) {
			canvas.drawLine(x, 0, x, (lineDistance * horizontalLineCount), pGrayLine);
		}
		
		for(int y = 0; y < height; y += lineDistance) {
			canvas.drawLine(0, y, width, y, pGrayLine);
		}
	}
	
	
	private void drawPoints() {
		for(int i = 0; i < points.size(); i++) {
			canvas.drawPoint(points.get(i).x, points.get(i).y, pDot);
		}
	}

	
	private void drawLines() {
		if(points.size() < 2) {
			return;
		}
		Point p0 = points.get(0);
		
		Path path = new Path();
		path.moveTo(p0.x, p0.y);
		for(int i = 1; i < points.size(); i++) {
			Point p = points.get(i);
			path.lineTo(p.x, p.y);
		}

		canvas.drawPath(path, pCyanLine);
	}
	
	
	private void drawScale() {
		int startX = lineDistance;
		int startY = lineDistance;
		int stopX = lineDistance * 2;
		int stopY = lineDistance;
		
		Path path = new Path();
		path.moveTo(startX, startY);
		path.lineTo(stopX, stopY);
		canvas.drawPath(path, pDottedLine);
	
		startY = lineDistance - lineDistance / 4;
		stopY = lineDistance + lineDistance / 4;
		
		canvas.drawLine(startX, startY, startX, stopY, pCyanLine);
		canvas.drawLine(stopX, startY, stopX, stopY, pCyanLine);
		
		startX = lineDistance + lineDistance / 2;
		startY = lineDistance - lineDistance / 4;
		
		canvas.drawText("1 m", startX, startY, pFont);
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
		
		canvas.drawBitmap(droneBitmap, x, y, null);
	}
	
	
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if(arg1.getAction() == MotionEvent.ACTION_DOWN) {
			float touchX = (int) arg1.getX();
			float touchY = (int) arg1.getY();

			// if touch is near to the scale area then return
			if(touchX < (lineDistance * 3) && touchY < (lineDistance * 2) || touchX < lineDistance || touchY < lineDistance || touchX > (lineDistance * verticalLineCount) || touchY > (lineDistance * (horizontalLineCount - 1))) {
				return true;
			}
			
			int x = Math.round(touchX / lineDistance) * lineDistance;
			int y = Math.round(touchY / lineDistance) * lineDistance;

			// if touch is to near to last point then return
			if(points.size() > 0) {
				Point lastPoint = points.get(points.size() - 1);
				int lastX = lastPoint.x;
				int lastY = lastPoint.y;

				if((lastX - lineDistance) <= x && x <= (lastX + lineDistance)) {
					if((lastY - lineDistance) <= y && y <= (lastY + lineDistance)) {
						return true;
					}
				}
			}
			
			addPoint(x, y);
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

	
	public Polygon getPolygon() {
		Polygon polygon = new Polygon();
		for(int i = 0; i < points.size() - 1; i++) {
			Point p1 = new Point();
			p1.x = (points.get(i).x / lineDistance);
			p1.y = (points.get(i).y / lineDistance);
			
			Point p2 = new Point();
			p2.x = (points.get(i + 1).x / lineDistance);
			p2.y = (points.get(i + 1).y / lineDistance);
			
			Vector2 vector = Vector2.pointsToVector(p1, p2);
			polygon.addVector(vector);
		}
		return polygon;
	}
}
