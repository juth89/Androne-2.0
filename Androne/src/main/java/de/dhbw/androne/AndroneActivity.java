package de.dhbw.androne;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import de.dhbw.androne.Androne.Command;
import de.dhbw.androne.Androne.FlyingMode;
import de.dhbw.androne.controller.DroneController;
import de.dhbw.androne.view.UserInterfaceUpdater;
import de.dhbw.androne.view.tabs.NonSwipeableViewPager;
import de.dhbw.androne.view.tabs.TabsPagerAdapter;

public class AndroneActivity extends FragmentActivity implements TabListener {

	private static final String TAG = "AndroneActivity";
	
	private NonSwipeableViewPager viewPager;
	private TabsPagerAdapter pagerAdapter;
	private ActionBar actionBar;

	private static final String[] TAB_NAMES = { "Direct", "Shape", "Polygon" };
	
	private DroneController droneController;
	private UserInterfaceUpdater userInterfaceUpdater;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.androne_activity);
		
		viewPager = (NonSwipeableViewPager) findViewById(R.id.androne_activity_pager);
		pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		actionBar = getActionBar();
		
		viewPager.setAdapter(pagerAdapter);
		
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		for(String tabName : TAB_NAMES) {
			actionBar.addTab(actionBar.newTab().setText(tabName).setTabListener(this));
		}
		
		init();
	}

	
	private void init() {
		droneController = new DroneController(this);
		Thread droneControllerThread = new Thread(droneController);
		droneControllerThread.start();
		
		userInterfaceUpdater = new UserInterfaceUpdater(this, pagerAdapter, droneController);
		Thread userInterfaceUpdaterThread = new Thread(userInterfaceUpdater);
		userInterfaceUpdaterThread.start();

		droneController.addNavDataListener(userInterfaceUpdater);
		
		Log.i(TAG, "DroneController and UserInterfaceUpdater started");
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(de.dhbw.androne.R.menu.main, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_connect) {
			if(item.getTitle().equals(getResources().getString(R.string.menu_connect))) {
				droneController.setCommand(Command.CONNECT);
			} else if(item.getTitle().equals(getResources().getString(R.string.menu_disconnect))) {
				droneController.setCommand(Command.DISCONNECT);
			}
		} else if(item.getItemId() == R.id.menu_take_off) {
			if(item.getTitle().equals(getResources().getString(R.string.menu_take_off))) {
				droneController.setCommand(Command.TAKE_OFF);
			} else if(item.getTitle().equals(getResources().getString(R.string.menu_land))) {
				droneController.setCommand(Command.LAND);
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		userInterfaceUpdater.updateMenu(menu);
		return super.onPrepareOptionsMenu(menu);
	}
	
	
	@Override
	public void onDestroy() {
		droneController.stopThread();
		userInterfaceUpdater.stopThread();
		Log.i(TAG, "DroneController and UserInterfaceUpdater stopped");
		super.onDestroy();
	}
	
	
	public void onTabReselected(Tab tab, FragmentTransaction arg1) { }

	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		viewPager.setCurrentItem(tab.getPosition());
		pagerAdapter.setCurrentIndex(tab.getPosition());
		if(droneController == null) {
			return;
		}
		if(tab.getPosition() == 0) {
			droneController.setFlyingMode(FlyingMode.DIRECT);
		} else if(tab.getPosition() == 1) {
			droneController.setFlyingMode(FlyingMode.SHAPE);
		} else if(tab.getPosition() == 2) {
			droneController.setFlyingMode(FlyingMode.POLYGON);
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction arg1) { }

	
	public Fragment getFragment(int index) {
		return pagerAdapter.getItem(index);
	}
	
	
	public DroneController getDroneController() {
		return droneController;
	}

}
