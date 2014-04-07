package de.dhbw.androne.view;

import de.dhbw.androne.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class ControllerFragment extends Fragment {

//	protected DroneController droneController;
	private TextView tvAltitude, tvState, tvBattery;
	private String txtAltitude, txtState, txtBattery;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
		View view = createView(inflater, container);
		
//		droneController = ((MainActivity) getActivity()).getDroneController();
		tvAltitude = (TextView) view.findViewById(R.id.text_view_altitude);
		tvState = (TextView) view.findViewById(R.id.text_view_state);
		tvBattery = (TextView) view.findViewById(R.id.text_view_battery);
		
		txtAltitude = getResources().getString(R.string.text_view_altitude);
		txtState = getResources().getString(R.string.text_view_state);
		txtBattery = getResources().getString(R.string.text_view_battery);
		
		return view;
	}
	
	
	protected abstract View createView(LayoutInflater inflater, ViewGroup container);
	
	protected abstract void enableControls();
	
	protected abstract void disableControls();

	
	public void setAltitude(float altitude) {
		if(isVisible()) {
			String txtAltitude = this.txtAltitude + " " + altitude + " m";
			tvAltitude.setText(txtAltitude);
		}
	}
	
	
	public void setState(String state) {
		if(isVisible()) {
			String txtState = this.txtState + " " + state;
			tvState.setText(txtState);
		}
	}
	
	
	public void setBattery(int battery) {
		if(isVisible()) {
			String txtBattery = this.txtBattery + " " + battery + " %";
			tvBattery.setText(txtBattery);
		}
	}
}
