package de.dhbw.androne.controller.pertime;

import java.io.IOException;

import android.util.Log;

import com.codeminders.ardrone.ARDrone;

import de.dhbw.androne.controller.DroneController;

public class RotationController extends ValueController {

	private static final String TAG = RotationController.class.getName();
	
	
	public RotationController(ControllerLock controllerLock, ARDrone drone) {
		super(controllerLock, drone);
	}


	public void rotate(float degrees) {
		if(degrees == 0.0f) {
			return;
		}
		controllerLock.lock();
		value = degrees;
	}


	@Override
	protected void flyImpl() throws IOException, InterruptedException {
		long flyTime = (long) ((Math.abs(value) / ROTATION) * 1000);
		long endTime = System.currentTimeMillis() + flyTime;
		
		while(running && System.currentTimeMillis() < endTime) {
			Log.e(TAG, value + " " + (endTime - System.currentTimeMillis()));
			if(value < 0) {
				drone.move(0.0f, 0.0f, 0.0f, -ANGULAR_SPEED);
			} else if(value > 0) {
				drone.move(0.0f, 0.0f, 0.0f, ANGULAR_SPEED);
			}
			Thread.sleep(DroneController.SLEEP_TIME);
		}
	}
}
