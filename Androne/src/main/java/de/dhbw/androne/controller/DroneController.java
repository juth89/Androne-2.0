package de.dhbw.androne.controller;

import java.net.UnknownHostException;

import android.util.Log;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.ARDrone.State;
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

	private AndroneActivity androneActivity;
	
	static final long SLEEP_TIME = 5;
	
	private ARDrone drone;
	private Command command;
	private FlyingMode flyingMode;
	private FlyingState flyingState;
	
	private DirectController directController;
	private ShapeController shapeController;
	private PolygonController polygonController;
	
	
	public DroneController(AndroneActivity androneActivity) {
		this.androneActivity = androneActivity;
		
		initDrone();
		
		directController = new DirectController(drone, (DirectFragment)androneActivity.getFragment(0));
		shapeController = new ShapeController(drone, (ShapeFragment)androneActivity.getFragment(1));
		polygonController = new PolygonController(drone, (PolygonFragment)androneActivity.getFragment(2));
		
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
		running = false;
	}
	
	
	private void updateLoop() {
		if(State.DISCONNECTED == drone.getState()) {
			if(Command.CONNECT == command) {
				connect();
			}
		} else if(State.DEMO == drone.getState()) {
			if(FlyingState.LANDED == flyingState) {
				
				if(Command.TAKE_OFF == command) {
					takeOff();
				} else if(Command.DISCONNECT == command) {
					disconnect();
				}
				
			} else if(FlyingState.FLYING == flyingState) {
				
				if(Command.DISCONNECT == command) {
					disconnect();
					land();
				} else if(Command.LAND == command) {
					land();
				}
				
				if(FlyingMode.DIRECT == flyingMode) {
					directController.updateLoop();
				} else if(FlyingMode.SHAPE == flyingMode) {
					shapeController.updateLoop();
				} else if(FlyingMode.POLYGON == flyingMode) {
					polygonController.updateLoop();
				}
			}
		} else if(State.ERROR == drone.getState()) {
			
		}
	}
	
	
	private void connect() {
		
	}
	
	
	private void disconnect() {
		
	}
	
	
	private void takeOff() {
		
	}
	
	
	private void land() {
		
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
