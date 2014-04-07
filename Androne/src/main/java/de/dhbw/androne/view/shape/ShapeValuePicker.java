package de.dhbw.androne.view.shape;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import de.dhbw.androne.R;

public class ShapeValuePicker extends DialogFragment {

	private static final String POSITIVE_TEXT = "Set";
	private static final String NEGATIVE_TEXT = "Cancel";
	
	private static final int MIN_WIDTH = 2;
	private static final int MIN_HEIGHT = 2;
	
	private int width, tmpWidth, height, tmpHeight;
	
	private ShapeFragment shapeFragment;
	private ShapeView shapeView;
	
	
	public void setShapeFragment(ShapeFragment shapeFragment) {
		this.shapeFragment = shapeFragment;
	}
	
	public void setShapeView(ShapeView shapeView) {
		this.shapeView = shapeView;
		width = shapeView.getShapeWidth();
		height = shapeView.getShapeHeight();
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		tmpWidth = width;
		tmpHeight = height;
		
		Builder builder = new Builder(getActivity());
		
		builder.setPositiveButton(POSITIVE_TEXT, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				shapeView.setShapeWidthHeight(width, height);
				shapeFragment.setWidth(width);
				shapeFragment.setHeight(height);
			}
		});

		builder.setNegativeButton(NEGATIVE_TEXT, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				width = tmpWidth;
				height = tmpHeight;
			}
		});
		
		builder.setView(createView());
		
		return builder.create();
	}
	
	
	private View createView() {
		View view = getActivity().getLayoutInflater().inflate(R.layout.shape_picker, null);

		NumberPicker widthPicker = (NumberPicker)view.findViewById(R.id.shape_picker_width);
		widthPicker.setMinValue(MIN_WIDTH);
		widthPicker.setMaxValue(shapeView.getVerticalLineCount() - 2);
		widthPicker.setWrapSelectorWheel(false);
		widthPicker.setLongClickable(false);
		widthPicker.setValue(shapeView.getShapeWidth());
		widthPicker.setOnValueChangedListener(new OnValueChangeListener() {
			public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
				width = arg2;
			}
		});
		
		NumberPicker heightPicker = (NumberPicker)view.findViewById(R.id.shape_picker_height);
		heightPicker.setMinValue(MIN_HEIGHT);
		heightPicker.setMaxValue(shapeView.getHorizontalLineCount() - 2);
		heightPicker.setWrapSelectorWheel(false);
		heightPicker.setLongClickable(false);
		heightPicker.setValue(shapeView.getShapeHeight());
		heightPicker.setOnValueChangedListener(new OnValueChangeListener() {
			public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
				height = arg2;
			}
		});
		
		return view;
	}
}
