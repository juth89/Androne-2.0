package de.dhbw.androne.model;

import android.graphics.Point;

public class Vector2 {

	public float x;
	public float y;

	
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	
	public float absolute() {
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	
	public static Vector2 pointsToVector(Point point1, Point point2) {
		float x = point2.x - point1.x;
		float y = point2.y - point1.y;
		return new Vector2(x, y);
	}
}
