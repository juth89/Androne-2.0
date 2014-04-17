package de.dhbw.androne.controller;

import android.util.Log;

import com.codeminders.ardrone.ARDrone;

import de.dhbw.androne.Androne.Command;
import de.dhbw.androne.controller.pertime.ControllerLock;
import de.dhbw.androne.controller.pertime.DistanceController;
import de.dhbw.androne.controller.pertime.RotationController;
import de.dhbw.androne.model.Rectangle;
import de.dhbw.androne.model.Shape;
import de.dhbw.androne.model.Triangle;
import de.dhbw.androne.view.shape.ShapeFragment;

public class ShapeController implements ControllerLock {

	private static final String TAG = "ShapeController";

	private static final long SLEEP_TIME = 100;
	
	private DroneController droneController;
	private ShapeFragment shapeFragment;
	
	private boolean lock, fly;
	
	private DistanceController distanceController;
	private RotationController rotationController;
	
	
	public ShapeController(ARDrone drone, DroneController droneController, ShapeFragment shapeFragment) {
		this.droneController = droneController;
		this.shapeFragment = shapeFragment;
		distanceController = new DistanceController(this, drone);
		rotationController = new RotationController(this, drone);
	}

	
	public void updateLoop(Command command) {
		if(Command.START_FLY_SHAPE == command) {
			startFlyShape();
		} else if(Command.STOP_FLY_SHAPE == command) {
			stopFlyShape();
		} else {
			droneController.hover();
		}
	}
	
	
	private void startFlyShape() {
		Shape shape = shapeFragment.getShape();
		
		distanceController.start();
		rotationController.start();
		
		fly = true;
		flyShape(shape);
		
		droneController.setCommand(null);
	}
	
	
	private void stopFlyShape() {
		distanceController.stop();
		rotationController.stop();
		
		fly = false;
		unlock();
		droneController.setCommand(null);
	}
	
	
	public void stopThreads() {
		distanceController.stop();
		rotationController.stop();
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
		final float width = rectangle.getWidth();
		final float height = rectangle.getHeight();
		final float degrees;
		
		if(rectangle.flyRight()) {
			degrees = 90;
		} else {
			degrees = -90;
		}
		
		Runnable runnable = new Runnable() {
			public void run() {
				for(int i = 0; i < 2; i++) {
					flyForward(width);
					rotate(degrees);
					flyForward(height);
					rotate(degrees);
				}
				shapeFragment.resetStartButton();
			}
		};
		
		Thread thread = new Thread(runnable);
		thread.start();
	}

	
	private void flyTriangle(Triangle triangle) {
		final float a = triangle.getA();
		final float b = triangle.getB();
		final float alpha, beta;
		
		if(triangle.flyRight()) {
			alpha = 180 - triangle.getAlpha();
			beta = 180 - triangle.getBeta();
		} else {
			alpha = -180 + triangle.getAlpha();
			beta = -180 + triangle.getBeta();
		}
		
		Runnable runnable = new Runnable() {
			public void run() {
				flyForward(a);
				rotate(alpha);
				flyForward(b);
				rotate(beta);
				flyForward(b);
				rotate(alpha);
				shapeFragment.resetStartButton();
			}
		};
		
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	
	private void flyForward(float distance) {
		if(!fly) {
			return;
		}
		distanceController.flyForward(distance);
		sleepUntilUnlock();
	}
	
	
	private void rotate(float degrees) {
		if(!fly) {
			return;
		}
		rotationController.rotate(degrees);
		sleepUntilUnlock();
	}
	
	
	private void sleepUntilUnlock() {
		while(lock) {
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}

	
	public void lock() {
		lock = true;
	}
	
	
	public void unlock() {
		lock = false;
	}
}
