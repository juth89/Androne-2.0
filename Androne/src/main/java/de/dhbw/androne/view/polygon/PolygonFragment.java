package de.dhbw.androne.view.polygon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import de.dhbw.androne.R;
import de.dhbw.androne.view.ControllerFragment;

public class PolygonFragment extends ControllerFragment implements OnClickListener {

	private Button btnDeleteLast, btnDeleteAll, btnStart;
	private PolygonView polygonView;
	
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.polygon_fragment, container, false);
		
		btnDeleteLast = (Button) view.findViewById(R.id.polygon_button_delete_last);
		btnDeleteAll = (Button) view.findViewById(R.id.polygon_button_delete_all);
		btnStart = (Button) view.findViewById(R.id.polygon_button_start);
		polygonView = (PolygonView) view.findViewById(R.id.polygon_view);
		polygonView.setFragment(this);
		
		btnDeleteLast.setOnClickListener(this);
		btnDeleteAll.setOnClickListener(this);
		btnStart.setOnClickListener(this);
		
		return view;
	}

	@Override
	protected void enableControls() {
		btnStart.setEnabled(true);
	}

	@Override
	protected void disableControls() {
		btnStart.setEnabled(false);
	}

	public void onClick(View v) {
		if(v.getId() == R.id.polygon_button_delete_last) {
			polygonView.clearLast();
		} else if(v.getId() == R.id.polygon_button_delete_all) {
			polygonView.clearAll();
		} else if(v.getId() == R.id.polygon_button_start) {
			if(btnStart.getText().equals(getResources().getString(R.string.polygon_fly_start))) {
				
			} else if(btnStart.getText().equals(getResources().getString(R.string.polygon_fly_stop))) {
				
			}
		}
	}
	
	
	public void setButtons(boolean enabled) {
		btnDeleteLast.setEnabled(enabled);
		btnDeleteAll.setEnabled(enabled);
	}

}
