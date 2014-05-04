package de.dhbw.androne.controller;

import java.util.List;

import android.util.Log;

import com.codeminders.ardrone.ARDrone;

import de.dhbw.androne.Androne.Command;
import de.dhbw.androne.controller.pertime.ControllerLock;
import de.dhbw.androne.controller.pertime.DistanceController;
import de.dhbw.androne.controller.pertime.RotationController;
import de.dhbw.androne.model.Polygon;
import de.dhbw.androne.model.Vector2;
import de.dhbw.androne.model.Vector2Utils;
import de.dhbw.androne.view.polygon.PolygonFragment;

public class PolygonController implements ControllerLock {

	private static final String TAG = "PolygonController";
	
	private static final long SLEEP_TIME = 5;
	
	private DroneController droneController;
	private PolygonFragment polygonFragment;

	private boolean lock, isFlyingPolygon;
	
	private DistanceController distanceController;
	private RotationController rotationController;
	
	
	public PolygonController(ARDrone drone, DroneController droneController, PolygonFragment polygonFragment) {
		this.droneController = droneController;
		this.polygonFragment = polygonFragment;
		distanceController = new DistanceController(this, drone);
		rotationController = new RotationController(this, drone);
	}

	
	public void updateLoop(Command command) {
		if(Command.START_FLY_POLYGON == command) {
			startFlyPolygon();
		} else if(Command.STOP_FLY_POLYGON == command) {
			stopFlyPolygon();
		} else {
			if(!isFlyingPolygon) {
				droneController.hover();
			}
		}
	}


	private void startFlyPolygon() {
		distanceController.start();
		rotationController.start();
		
		isFlyingPolygon = true;
		flyPolygon();
		
		droneController.setCommand(null);
	}

	
	private void stopFlyPolygon() {
		distanceController.stop();
		rotationController.stop();
		
		isFlyingPolygon = false;
		unlock();
		
		droneController.setCommand(null);
	}
	
	
	public void stopThreads() {
		distanceController.stop();
		rotationController.stop();
	}
	
	
	private void flyPolygon() {
		final Polygon polygon = polygonFragment.getPolygon();
		
		Runnable runnable = new Runnable() {
			public void run() {
				List<Vector2> vectors = polygon.getVectors();
				for(int i = 0; i < vectors.size(); i++) {
					Vector2 v1 = vectors.get(i);
					flyForward(v1.absolute());

					if(i == vectors.size() - 1) {
						return;
					}
					
					Vector2 v2 = vectors.get(i + 1);
					float degrees = Vector2Utils.angleBetween(v1, v2);
					rotate(degrees);
				}
				isFlyingPolygon = false;
			}
		};
		
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	
	private void flyForward(float distance) {
		if(!isFlyingPolygon) {
			return;
		}
		distanceController.flyForward(distance);
		sleepUntilUnlock();
	}
	
	
	private void rotate(float degrees) {
		if(!isFlyingPolygon) {
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
