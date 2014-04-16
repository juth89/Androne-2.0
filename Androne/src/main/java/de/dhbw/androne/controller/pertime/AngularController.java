package de.dhbw.androne.controller.pertime;

import android.util.Log;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;

import de.dhbw.androne.controller.DroneController;

@Deprecated
public class AngularController implements NavDataListener {

	private static final String TAG = "AngularController";
	private ARDrone drone;
	private boolean recordNavData = false;
	private NavData lastNavData;
	
	private boolean left = false;
	private float yawSum;
	
	
	public AngularController(ARDrone drone) {
		this.drone = drone;
		this.drone.addNavDataListener(this);
	}
	
	
	public void rotate(float degrees) {
		if(degrees == 0) {
			return;
		}
		
		left = (degrees < 0);
		recordNavData = true;
		float angularSpeed = left ? -1 : 1;
		float absDegrees = Math.abs(degrees);

		while(absDegrees >= yawSum) {
			try {
				drone.move(0, 0, 0, angularSpeed);
				Thread.sleep(DroneController.SLEEP_TIME);
			} catch(Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		
		recordNavData = false;
		yawSum = 0;
	}


	public void navDataReceived(NavData navData) {
		if(!recordNavData) {
			return;
		}
		
		if(lastNavData != null) {
			float oldYaw = lastNavData.getYaw();
			float newYaw = navData.getYaw();
			
			float diff = getDiff(oldYaw, newYaw);
			
			yawSum += diff;
		}
		
		lastNavData = navData;
	}
	
	
	private float getDiff(float oldYaw, float newYaw) {
		float diff = 0;
		
		boolean obz = (oldYaw > 0);
		boolean nbz = (newYaw > 0);
		boolean obn = (oldYaw > newYaw);
		
		if((obz && obn && left) || (obz && obn && left) || (!obz && !nbz && !obn && left)) {
			diff = oldYaw - newYaw;
		} else if((obz && nbz && !obn && left) && (!obz && !nbz && obn && !left)) {
			diff = 360 - (newYaw - oldYaw);
		} else if(!obz && nbz && !obn && left) {
			diff = oldYaw + 360 - newYaw;
		} else if((!obz && !nbz && !obn) || (nbz && !obn && !left)) {
			diff = newYaw - oldYaw;
		} else if(obz && obn && !left) {
			diff = 360 - (oldYaw - newYaw);
		}
		
		return diff;
	}
}
