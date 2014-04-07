package de.dhbw.androne.view.direct;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import de.dhbw.androne.R;
import de.dhbw.androne.view.ControllerFragment;

public class DirectFragment extends ControllerFragment implements OnTouchListener {

	private List<ImageButton> buttons = new ArrayList<ImageButton>();
	private boolean forward, backward, left, right, up, down, rotateLeft, rotateRight;
	
	
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.direct_fragment, container, false);
		
		buttons.add((ImageButton)view.findViewById(R.id.direct_button_forward));
		buttons.add((ImageButton)view.findViewById(R.id.direct_button_backward));
		buttons.add((ImageButton)view.findViewById(R.id.direct_button_left));
		buttons.add((ImageButton)view.findViewById(R.id.direct_button_right));
		buttons.add((ImageButton)view.findViewById(R.id.direct_button_up));
		buttons.add((ImageButton)view.findViewById(R.id.direct_button_down));
		buttons.add((ImageButton)view.findViewById(R.id.direct_button_rotate_left));
		buttons.add((ImageButton)view.findViewById(R.id.direct_button_rotate_right));
		
		for(ImageButton button : buttons) {
			button.setOnTouchListener(this);
//			button.set
		}
		
		return view;
	}

	
	public boolean onTouch(View v, MotionEvent event) {
		switch(v.getId()) {
		case R.id.direct_button_forward:
			forward = evalEvent(event);
			break;
		case R.id.direct_button_backward:
			backward = evalEvent(event);
			break;
		case R.id.direct_button_left:
			left = evalEvent(event);
			break;
		case R.id.direct_button_right:
			right = evalEvent(event);
			break;
		case R.id.direct_button_up:
			up = evalEvent(event);
			break;
		case R.id.direct_button_down:
			down = evalEvent(event);
			break;
		case R.id.direct_button_rotate_left:
			rotateLeft = evalEvent(event);
			break;
		case R.id.direct_button_rotate_right:
			rotateRight = evalEvent(event);
			break;
		}
		return false;
	}

	
	private boolean evalEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			return false;
		} else {
			return true;
		}
	}
	
	
	public boolean isForward() {
		return forward;
	}


	public boolean isBackward() {
		return backward;
	}


	public boolean isLeft() {
		return left;
	}


	public boolean isRight() {
		return right;
	}


	public boolean isUp() {
		return up;
	}


	public boolean isDown() {
		return down;
	}


	public boolean isRotateLeft() {
		return rotateLeft;
	}


	public boolean isRotateRight() {
		return rotateRight;
	}


	@Override
	protected void enableControls() {
		for(ImageButton button : buttons) {
			button.setEnabled(true);
		}
	}


	@Override
	protected void disableControls() {
		for(ImageButton button : buttons) {
			button.setEnabled(false);
		}
	}
}
