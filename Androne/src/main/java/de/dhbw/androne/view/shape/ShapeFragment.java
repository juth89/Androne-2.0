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
import de.dhbw.androne.R;
import de.dhbw.androne.model.Rectangle;
import de.dhbw.androne.model.Shape;
import de.dhbw.androne.model.Triangle;
import de.dhbw.androne.view.ControllerFragment;

public class ShapeFragment extends ControllerFragment implements OnItemSelectedListener, OnCheckedChangeListener, OnClickListener {

	private Spinner shapeChooser;
	private CheckBox changeDirection;
	private Button btnSetValues, btnStart;
	private TextView tvWidth, tvHeight;
	private RectangleView rectangeView;
	private TriangleView triangleView;
	private ShapeView currentView;
	
	private ShapeValuePicker valuePicker;
	
	private boolean flyShape = false;
	private Shape currentShape;
	
	
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.shape_fragment, container, false);
		
		shapeChooser = (Spinner) view.findViewById(R.id.shape_chooser);
		changeDirection = (CheckBox) view.findViewById(R.id.shape_check_change_direction);
		btnSetValues = (Button) view.findViewById(R.id.shape_button_show_value_picker);
		btnStart = (Button) view.findViewById(R.id.shape_button_start);
		tvWidth = (TextView) view.findViewById(R.id.shape_width);
		tvHeight = (TextView) view.findViewById(R.id.shape_height);
		
		rectangeView = (RectangleView) view.findViewById(R.id.shape_rectangle_view);
		triangleView = (TriangleView) view.findViewById(R.id.shape_triangle_view);

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
		rectangeView.setVisibility(View.INVISIBLE);
		triangleView.setVisibility(View.INVISIBLE);
		
		Object item = parent.getItemAtPosition(pos);
		
		String[] shapes = getResources().getStringArray(R.array.shape_chooser_entries);
		
		if(item.equals(shapes[0])) {
			currentView = rectangeView;
		} else if(item.equals(shapes[1])) {
			currentView = triangleView;
		}
		
		currentView.setVisibility(View.VISIBLE);
		currentView.invalidate();
		valuePicker.setShapeView(currentView);
	
		setWidth(currentView.getShapeWidth());
		setHeight(currentView.getShapeHeight());
	}

	
	public void onNothingSelected(AdapterView<?> arg0) { }

	
	/**
	 * Checkbox Listener
	 */
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
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
				flyShape = true;
				
				if(currentView instanceof RectangleView) {
					currentShape = new Rectangle(currentView.getShapeWidth(), currentView.getShapeHeight(), changeDirection.isChecked());
				} else if(currentView instanceof TriangleView) {
					currentShape = new Triangle(currentView.getShapeWidth(), currentView.getShapeHeight(), changeDirection.isChecked());
					
				}
				
				btn.setText(getResources().getString(R.string.shape_fly_stop));
			} else if(btn.getText().equals(getResources().getString(R.string.shape_fly_stop))) {
				resetStart();
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
	
	
	public boolean flyShape() {
		return flyShape;
	}
	
	public Shape getShape() {
		return currentShape;
	}
	
	public void resetStart() {
		flyShape = false;
		currentShape = null;
		btnStart.setText(getResources().getString(R.string.shape_fly_start));
	}
}
