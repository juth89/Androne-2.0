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
	
	private Canvas canvas;
	private Paint paint = new Paint();
	
	private Bitmap droneBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
	

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
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(5);
		
		drawPoints();
	}


	public void addPoint(int x, int y) {
		Point point = new Point(x, y);
		points.add(point);
	}
	
	
	private void drawPoints() {
		points.clear();
		points.add(new Point(200, 500));
		points.add(new Point(400, 100));
		
		Point p1 = points.get(0);
		Point p2 = points.get(1);
		
		double px = (double) p1.x - p2.x;
		double py = (double) p1.y - p2.y;
		
		float degrees = (float) Math.toDegrees(Math.atan(px / py));
		degrees = Math.abs(degrees);
		Log.e("PolygonView", degrees + "");
		
		canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);

		int dx = p1.x - droneBitmap.getWidth() / 2;
		int dy = p1.y - droneBitmap.getHeight() / 2;

		canvas.rotate(degrees, p1.x, p1.y);
		canvas.drawBitmap(droneBitmap, dx, dy, paint);
		
//		for(int i = 0; i < points.size(); i++) {
//			if(i != 0) {
//				Point p1 = points.get(i - 1);
//				Point p2 = points.get(i);
//				paint.setStrokeWidth(6);
//				paint.setStyle(Style.FILL_AND_STROKE);
//				paint.setColor(Color.rgb(51, 181, 229));
//				canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
//				paint.setStrokeWidth(20);
//				canvas.drawPoint(p2.x, p2.y, paint);
//			} else {
//				if(points.size() > 1) {
//					Point p1 = points.get(0);
//					Point p2 = points.get(1);
//					
//					float degrees = (float) Math.toDegrees(Math.atan(Math.abs(p1.x - p2.x) / Math.abs((p1.y - p2.y) / 2)));
//					Log.e("PolygonView", degrees + "");
//					
//					int x = p1.x;
//					int y = p1.y;
//					
//					Rect rect = new Rect();
//					rect.left = 0;
//					rect.top = 0;
//					rect.right = droneBitmap.getWidth() / 2;
//					rect.bottom = droneBitmap.getHeight() / 2;
//
//					canvas.translate(x, y);
//					canvas.drawRect(rect, paint);
//					canvas.translate(-x, -y);
//					
//					canvas.rotate(degrees, x, y);
////					canvas.translate(p1.x, p1.y);
//					canvas.drawBitmap(droneBitmap, x, y, paint);
//					canvas.rotate(-degrees, x, y);
////					canvas.rotate(-degrees);
////					canvas.translate(-p1.x, -p1.y);
//				}
//			}
//		}
	}

	public boolean onTouch(View arg0, MotionEvent arg1) {
		if(arg1.getAction() == MotionEvent.ACTION_DOWN) {
			int x = (int) arg1.getX();
			int y = (int) arg1.getY();
			Log.e("POSITION", x + " " + y);
			addPoint(x, y);
			invalidate();
			return true;
		}
		return false;
	}
}
