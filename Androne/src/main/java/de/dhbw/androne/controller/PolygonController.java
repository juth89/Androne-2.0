package de.dhbw.androne.controller;

import java.io.IOException;

import android.util.Log;

import com.codeminders.ardrone.ARDrone;

import de.dhbw.androne.view.polygon.PolygonFragment;

public class PolygonController {

	private static final String TAG = "PolygonController";
	private ARDrone drone;
	private PolygonFragment polygonFragment;

	
	public PolygonController(ARDrone drone, PolygonFragment polygonFragment) {
		this.drone = drone;
		this.polygonFragment = polygonFragment;
	}

	
	public void updateLoop() {
		try {
			drone.hover();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}
}
