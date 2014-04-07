package de.dhbw.androne.view.polygon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.dhbw.androne.R;
import de.dhbw.androne.view.ControllerFragment;

public class PolygonFragment extends ControllerFragment {

	
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.polygon_fragment, container, false);
		return view;
	}

	@Override
	protected void enableControls() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void disableControls() {
		// TODO Auto-generated method stub
		
	}

}
