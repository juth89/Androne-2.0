package de.dhbw.androne.view;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codeminders.ardrone.ARDrone.State;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;
import com.codeminders.ardrone.data.navdata.FlyingState;

import de.dhbw.androne.AndroneActivity;
import de.dhbw.androne.R;
import de.dhbw.androne.controller.DroneController;
import de.dhbw.androne.view.tabs.TabsPagerAdapter;

public class UserInterfaceUpdater implements Runnable, NavDataListener {

	private static final String TAG = "UserInterfaceUpdater";
	private static final long SLEEP_TIME = 100;
	private boolean running = true;
	
	private AndroneActivity androneActivity;
	private TabsPagerAdapter pagerAdapter;
	private DroneController droneController;
	
	private NavData navData;


	public UserInterfaceUpdater(AndroneActivity androneActivity, TabsPagerAdapter pagerAdapter, DroneController droneController) {
		this.androneActivity = androneActivity;
		this.pagerAdapter = pagerAdapter;
		this.droneController = droneController;
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
		androneActivity.runOnUiThread(new Runnable() {

			public void run() {
				State state = droneController.getDroneState();
				
				ControllerFragment controllerFragment = (ControllerFragment) pagerAdapter.getCurrentFragment();
				
				controllerFragment.setState(state.toString());
				
				if(state == State.DISCONNECTED) {
					controllerFragment.disableControls();
				} else if(state == State.DEMO){
					if(navData.getFlyingState() == FlyingState.FLYING) {
						controllerFragment.enableControls();
					} else if(navData.getFlyingState() == FlyingState.LANDED) {
						controllerFragment.disableControls();
					}
				}
			}
			
		});
	}
	

	public void navDataReceived(NavData nd) {
		navData = nd;
		androneActivity.runOnUiThread(new Runnable() {

			public void run() {
				float altitude = navData.getAltitude();
				int battery = navData.getBattery();

				ControllerFragment controllerFragment = (ControllerFragment) pagerAdapter.getCurrentFragment();
				
				controllerFragment.setAltitude(altitude);
				controllerFragment.setBattery(battery);
			}
			
		});
	}


	public void updateMenu(Menu menu) {
		String txtConnect = androneActivity.getResources().getString(R.string.menu_connect);
		String txtDisconnect = androneActivity.getResources().getString(R.string.menu_disconnect);
		String txtTakeOff = androneActivity.getResources().getString(R.string.menu_take_off);
		String txtLand = androneActivity.getResources().getString(R.string.menu_land);
		
		State state = droneController.getDroneState();
		
		switch(state) {
		case CONNECTING:
			configureMenu(menu, txtDisconnect, false, txtTakeOff, false);
			break;
		case DEMO:
			if(navData == null) {
				break;
			}
			if(navData.getFlyingState() == FlyingState.FLYING) {
				configureMenu(menu, txtDisconnect, true, txtLand, true);
			} else if(navData.getFlyingState() == FlyingState.LANDED) {
				configureMenu(menu, txtDisconnect, true, txtTakeOff, true);
			}
			break;
		case DISCONNECTED:
			configureMenu(menu, txtConnect, true, txtTakeOff, false);
			break;
		case LANDING:
			configureMenu(menu, txtDisconnect, false, txtTakeOff, false);
			break;
		case TAKING_OFF:
			configureMenu(menu, txtDisconnect, false, txtLand, false);
			break;
		default:
			break;
		}
	}
	
	
	private void configureMenu(Menu menu, String txtConnect, boolean enConnect, String txtTakeOff, boolean enTakeOff) {
		MenuItem miConnect = menu.getItem(0);
		miConnect.setTitle(txtConnect);
		miConnect.setEnabled(enConnect);		
		
		MenuItem miTakeOff = menu.getItem(1);
		miTakeOff.setTitle(txtTakeOff);
		miTakeOff.setEnabled(enTakeOff);
	}

}
