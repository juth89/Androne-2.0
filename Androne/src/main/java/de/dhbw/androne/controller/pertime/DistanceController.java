package de.dhbw.androne.controller.pertime;

import java.io.IOException;

import android.util.Log;

import com.codeminders.ardrone.ARDrone;

import de.dhbw.androne.controller.DroneController;

public class DistanceController extends ValueController {

	private static final String TAG = DistanceController.class.getName();

	
	public DistanceController(ControllerLock controllerLock, ARDrone drone) {
		super(controllerLock, drone);
	}
	
	
	public void flyForward(float distance) {
		if(distance == 0.0f) {
			return;
		}
		controllerLock.lock();
		value = distance;
	}
	
	
	@Override
	protected void flyImpl() throws IOException, InterruptedException {
		long flyTime = (long) ((value / VELOCITY) * 1000);
		long endTime = System.currentTimeMillis() + flyTime;
		
		Log.e(TAG, value + " " + (endTime - System.currentTimeMillis()));
		while(running && System.currentTimeMillis() < endTime) {
			drone.move(0.0f, FRONT_BACK_TILT, 0.0f, 0.0f);
			Thread.sleep(DroneController.SLEEP_TIME);
		}
	}

	
}
