package de.dhbw.androne.view.tabs;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import de.dhbw.androne.view.direct.DirectFragment;
import de.dhbw.androne.view.polygon.PolygonFragment;
import de.dhbw.androne.view.shape.ShapeFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private int currentIndex;

	
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		fragments.add(new DirectFragment());
		fragments.add(new ShapeFragment());
		fragments.add(new PolygonFragment());
	}

	@Override
	public Fragment getItem(int index) {
		return fragments.get(index);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	
	public void setCurrentIndex(int index) {
		currentIndex = index;
	}
	
	
	public Fragment getCurrentFragment() {
		return fragments.get(currentIndex);
	}
}
