package de.dhbw.androne.controller;

import com.codeminders.ardrone.ARDrone;

import de.dhbw.androne.view.polygon.PolygonFragment;

public class PolygonController {

	private ARDrone drone;
	private PolygonFragment polygonFragment;

	
	public PolygonController(ARDrone drone, PolygonFragment polygonFragment) {
		this.drone = drone;
		this.polygonFragment = polygonFragment;
	}

	
	public void updateLoop() {
		
	}
}
