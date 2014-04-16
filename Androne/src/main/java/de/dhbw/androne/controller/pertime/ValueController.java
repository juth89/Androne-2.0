package de.dhbw.androne.controller.pertime;

import java.io.IOException;

import android.util.Log;

import com.codeminders.ardrone.ARDrone;

public abstract class ValueController implements Runnable {

	private static final String TAG = ValueController.class.getName();
	
	private static final long SLEEP_TIME_WHILE = 100;
	private static final long SLEEP_TIME_AFTER = 1500;
	
	protected static final float VELOCITY = 2.0f; // meters per second
	protected static final float ROTATION = 116.25f; // degrees per second
	protected static final float FRONT_BACK_TILT = -1.0f;
	protected static final float ANGULAR_SPEED = 1.0f;
	
	protected ControllerLock controllerLock;
	protected ARDrone drone;

	protected boolean running;
	
	protected float value = 0.0f;
	
	
	public ValueController(ControllerLock controllerLock, ARDrone drone) {
		this.controllerLock = controllerLock;
		this.drone = drone;
	}
	
	
	public void start() {
		running = true;
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		running = false;
	}
	
	
	public void run() {
		while(running) {
			updateLoop();
		}
	}
	
	
	private void updateLoop() {
		try {
			if(value != 0.0f) {
				fly();
				value = 0.0f;
			}
			Thread.sleep(SLEEP_TIME_WHILE);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		
	}
	
	
	private void fly() throws InterruptedException, IOException {
		flyImpl();
		
		Thread.sleep(SLEEP_TIME_AFTER);
		controllerLock.unlock();
	}
	
	
	protected abstract void flyImpl() throws IOException, InterruptedException;
}
