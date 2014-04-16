package de.dhbw.androne.view.shape;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import de.dhbw.androne.Androne.Command;
import de.dhbw.androne.R;
import de.dhbw.androne.model.Shape;
import de.dhbw.androne.view.ControllerFragment;

public class ShapeFragment extends ControllerFragment implements OnItemSelectedListener, OnCheckedChangeListener, OnClickListener {

	private Spinner shapeChooser;
	private CheckBox changeDirection;
	private Button btnSetValues, btnStart;
	private TextView tvWidth, tvHeight;
	private ShapeView shapeView;
	
	private ShapeValuePicker valuePicker;
	
	
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.shape_fragment, container, false);
		
		shapeChooser = (Spinner) view.findViewById(R.id.shape_chooser);
		changeDirection = (CheckBox) view.findViewById(R.id.shape_check_change_direction);
		btnSetValues = (Button) view.findViewById(R.id.shape_button_show_value_picker);
		btnStart = (Button) view.findViewById(R.id.shape_button_start);
		tvWidth = (TextView) view.findViewById(R.id.shape_width);
		tvHeight = (TextView) view.findViewById(R.id.shape_height);

		shapeView = (ShapeView) view.findViewById(R.id.shape_view);

		shapeChooser.setOnItemSelectedListener(this);
		changeDirection.setOnCheckedChangeListener(this);
		btnSetValues.setOnClickListener(this);
		btnStart.setOnClickListener(this);
	
		valuePicker = new ShapeValuePicker();
		valuePicker.setShapeFragment(this);
		
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

	
	/**
	 * Spinner Listener
	 */
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		Object item = parent.getItemAtPosition(pos);
		
		String[] shapes = getResources().getStringArray(R.array.shape_chooser_entries);
		
		if(item.equals(shapes[0])) {
			shapeView.setRectangle();
		} else if(item.equals(shapes[1])) {
			shapeView.setTriangle();
		}
		
		valuePicker.setShapeView(shapeView);
	
		changeDirection.setChecked(shapeView.getCurrentShape().flyRight());
		
		setWidth(shapeView.getShapeWidth());
		setHeight(shapeView.getShapeHeight());
	}

	
	public void onNothingSelected(AdapterView<?> arg0) { }

	
	/**
	 * Checkbox Listener
	 */
	public void onCheckedChanged(CompoundButton arg0, boolean flyRight) {
		shapeView.setDirection(flyRight);
	}

	
	/**
	 * Button Listener
	 */
	public void onClick(View view) {
		if(view.getId() == R.id.shape_button_show_value_picker) {
			valuePicker.show(getFragmentManager(), null);
		} else if(view.getId() == R.id.shape_button_start) {
			Button btn = (Button) view;
			if(btn.getText().equals(getResources().getString(R.string.shape_fly_start))) {
				btn.setText(getResources().getString(R.string.shape_fly_stop));
				droneController.setCommand(Command.START_FLY_SHAPE);
			} else if(btn.getText().equals(getResources().getString(R.string.shape_fly_stop))) {
				btn.setText(getResources().getString(R.string.shape_fly_start));
				droneController.setCommand(Command.STOP_FLY_SHAPE);
			}
		}
	}

	
	public void setWidth(int width) {
		String text = getResources().getString(R.string.shape_width) + " " + width + " m";
		tvWidth.setText(text);
	}
	
	public void setHeight(int height) {
		String text = getResources().getString(R.string.shape_height) + " " + height + " m";
		tvHeight.setText(text);
	}
	
	public Shape getShape() {
		return shapeView.getCurrentShape();
	}

	public void resetStartButton() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				btnStart.setText(getResources().getString(R.string.shape_fly_start));
			}
		});
	}
}
