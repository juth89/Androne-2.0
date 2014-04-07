package de.dhbw.androne.controller;

import android.util.Log;

import com.codeminders.ardrone.ARDrone;

import de.dhbw.androne.model.Rectangle;
import de.dhbw.androne.model.Shape;
import de.dhbw.androne.model.Triangle;
import de.dhbw.androne.view.shape.ShapeFragment;

public class ShapeController {

	private static final String TAG = "ShapeController";

	private static final float FORWARD_SPEED = 2; // 2 m/s
	
	private ARDrone drone;
	private ShapeFragment shapeFragment;
	
	private boolean isFlying;
	private boolean lock;
	
	private AngularController angularController;
	
	
	public ShapeController(ARDrone drone, ShapeFragment shapeFragment) {
		this.drone = drone;
		this.shapeFragment = shapeFragment;
		angularController = new AngularController(drone);
	}

	
	public void updateLoop() {
		if(shapeFragment.flyShape()) {
			if(!isFlying) {
				isFlying = true;
				flyShape(shapeFragment.getShape());
				isFlying = false;
				shapeFragment.resetStart();
			}
		}
	}
	
	
	private void flyShape(Shape shape) {
		if(shape instanceof Rectangle) {
			Rectangle rectangle = (Rectangle)shape;
			flyRectangle(rectangle);
		} else if(shape instanceof Triangle) {
			Triangle triangle = (Triangle)shape;
			flyTriangle(triangle);
		}
	}
	
	
	private void flyRectangle(Rectangle rectangle) {
		int firstDistance, secondDistance;
		int degrees;
		
		if(rectangle.flyRight()) {
			firstDistance = rectangle.getWidth();
			secondDistance = rectangle.getHeight();
			degrees = 90;
		} else {
			firstDistance = rectangle.getHeight();
			secondDistance = rectangle.getWidth();
			degrees = -90;
		}
		
		for(int i = 0; i < 2; i++) {
			flyForward(firstDistance);
			sleepUntilLockOpen();
			rotate(degrees);
			sleepUntilLockOpen();
			flyForward(secondDistance);
			sleepUntilLockOpen();
			rotate(degrees);
			sleepUntilLockOpen();
		}
	}

	
	private void flyTriangle(Triangle triangle) {
		
	}
	
	
	private void flyForward(float distance) {
		long flyTime = (long) ((distance / FORWARD_SPEED) * 1000);
		final long stopTime = System.currentTimeMillis() + flyTime;
		
		Runnable forwardRunnable = new Runnable() {
		
			public void run() {
				while(stopTime > System.currentTimeMillis()) {
					try {
						drone.move(0, -1, 0, 0);
						Thread.sleep(DroneController.SLEEP_TIME);
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
					}
				}
				lock = false;
			}

		};

		Thread forwardThread = new Thread(forwardRunnable);
		lock = true;
		forwardThread.start();
	}

	
	private void rotate(final float degrees) {
		Runnable rotateRunnable = new Runnable() {
			public void run() {
				try {
					angularController.rotate(degrees);
					Thread.sleep(DroneController.SLEEP_TIME);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
				lock = false;
				
			}
		};
		
		Thread rotateThread = new Thread(rotateRunnable);
		lock = true;
		rotateThread.start();
	}
	
	
	private void sleepUntilLockOpen() {
		while(lock) {
			try {
				Thread.sleep(DroneController.SLEEP_TIME);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}
}
