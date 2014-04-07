package de.dhbw.androne.controller;

import java.io.IOException;

import android.util.Log;

import com.codeminders.ardrone.ARDrone;

import de.dhbw.androne.view.direct.DirectFragment;

public class DirectController {

	private static final String TAG = "DirectController";
	
	private static final float SPEED = 1;
	
	private ARDrone drone;
	private DirectFragment directFragment;

	
	public DirectController(ARDrone drone, DirectFragment directFragment) {
		this.drone = drone;
		this.directFragment = directFragment;
	}

	
	public void updateLoop() {
		float leftRightTilt = 0;
		float frontBackTilt = 0;
		float verticalSpeed = 0;
		float angularSpeed = 0;
		
		if(directFragment.isForward()) {
			frontBackTilt -= SPEED;
		}
		if(directFragment.isBackward()) {
			frontBackTilt += SPEED;
		}
		if(directFragment.isLeft()) {
			leftRightTilt -= SPEED;
		}
		if(directFragment.isRight()) {
			leftRightTilt += SPEED;
		}
		if(directFragment.isUp()) {
			verticalSpeed += SPEED;
		}
		if(directFragment.isDown()) {
			verticalSpeed -= SPEED;
		}
		if(directFragment.isRotateLeft()) {
			angularSpeed -= SPEED;
		}
		if(directFragment.isRotateRight()) {
			angularSpeed += SPEED;
		}
		
		try {
			if(leftRightTilt == 0 && frontBackTilt == 0 && verticalSpeed == 0 && angularSpeed == 0) {
				drone.hover();
			} else {
				drone.move(leftRightTilt, frontBackTilt, verticalSpeed, angularSpeed);
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}
}
