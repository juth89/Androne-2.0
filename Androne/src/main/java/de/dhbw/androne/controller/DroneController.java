package de.dhbw.androne.controller;

import java.io.IOException;
import java.net.UnknownHostException;

import android.util.Log;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.ARDrone.State;
import com.codeminders.ardrone.DroneStatusChangeListener;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;
import com.codeminders.ardrone.data.navdata.FlyingState;

import de.dhbw.androne.Androne.Command;
import de.dhbw.androne.Androne.FlyingMode;
import de.dhbw.androne.AndroneActivity;
import de.dhbw.androne.view.direct.DirectFragment;
import de.dhbw.androne.view.polygon.PolygonFragment;
import de.dhbw.androne.view.shape.ShapeFragment;

public class DroneController implements Runnable, NavDataListener {

	private static final String TAG = "DroneController";
	private boolean running = true;

	public static final long SLEEP_TIME = 5;
	
	private ARDrone drone;
	private Command command;
	private FlyingMode flyingMode;
	private FlyingState flyingState;
	
	private DirectController directController;
	private ShapeController shapeController;
	private PolygonController polygonController;
	
	
	public DroneController(AndroneActivity androneActivity) {
		initDrone();
		
		directController = new DirectController(drone, (DirectFragment)androneActivity.getFragment(0));
		shapeController = new ShapeController(drone, this, (ShapeFragment)androneActivity.getFragment(1));
		polygonController = new PolygonController(drone, this, (PolygonFragment)androneActivity.getFragment(2));
		
		setFlyingMode(FlyingMode.DIRECT);
	}

	
	private void initDrone() {
		try {
			drone = new ARDrone();
		} catch (UnknownHostException e) {
			Log.e(TAG, e.getMessage());
		}
		drone.addNavDataListener(this);
	}
	
	
	public void run() {
		while(running) {
			updateLoop();
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}
	
	
	public void stopThread() {
		polygonController.stopThreads();
		shapeController.stopThreads();
		running = false;
	}
	
	
	private void updateLoop() {
		State state = drone.getState();
		if(State.DISCONNECTED == state) {
			if(Command.CONNECT == command) {
				connect();
			}
		} else if(State.DEMO == state) {
			if(FlyingState.LANDED == flyingState) {
				
				if(Command.TAKE_OFF == command) {
					takeOff();
				} else if(Command.DISCONNECT == command) {
					disconnect();
				}
				
			} else if(FlyingState.FLYING == flyingState) {
				
				if(Command.DISCONNECT == command) {
					land();
					disconnect();
				} else if(Command.LAND == command) {
					land();
				}
				
				if(FlyingMode.DIRECT == flyingMode) {
					directController.updateLoop();
				} else if(FlyingMode.SHAPE == flyingMode) {
					shapeController.updateLoop(command);
				} else if(FlyingMode.POLYGON == flyingMode) {
					polygonController.updateLoop(command);
				}
			}
		} else if(State.ERROR == state) {
			
		}
		command = null;
	}
	
	
	private void connect() {
		Log.e(TAG, command.name());
		try {
			drone.addStatusChangeListener(new DroneStatusChangeListener() {
				
				public void ready() {
					try {
						drone.disableVideo();
						drone.setCombinedYawMode(true);
						drone.trim();
					} catch (IOException e) {
						Log.e(TAG, "CONNECT LISTENER", e);
					}
				}
			});
			
			drone.connect();
			drone.waitForReady(5000);
            drone.clearEmergencySignal();

		} catch(IOException e) {
			Log.e(TAG, e.getMessage());
			initDrone();
		}
		command = null;
	}
	
	
	private void disconnect() {
		Log.e(TAG, command.name());
		try {
			drone.disconnect();
			
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		command = null;
	}
	
	
	private void takeOff() {
		Log.e(TAG, command.name());
		try {
			drone.takeOff();
			drone.trim();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		command = null;
	}
	
	
	private void land() {
		Log.e(TAG, command.name());
		try {
			drone.land();
			Thread.sleep(5000);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} catch (InterruptedException e) {
			Log.e(TAG, e.getMessage());
		}
		command = null;
	}
	
	
	public void hover() {
		try {
			drone.hover();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	
	public void setCommand(Command command) {
		this.command = command;
	}
	
	
	public void setFlyingMode(FlyingMode flyingMode) {
		this.flyingMode = flyingMode;
	}


	public void navDataReceived(NavData nd) {
		this.flyingState = nd.getFlyingState();
	}
	
	
	public void addNavDataListener(NavDataListener listener) {
		drone.addNavDataListener(listener);
	}
	
	
	public State getDroneState() {
		return drone.getState();
	}
}
