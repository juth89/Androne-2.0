package de.dhbw.androne.model;

public class Triangle extends Shape {

	
	public Triangle() {
		super();
	}
	
	public Triangle(int width, int height, boolean flyRight) {
		super(width, height, flyRight);
	}
	
	
	/**
	 * length of the bottom line
	 */
	public float getA() {
		return width;
	}

	
	/**
	 * length of the flanking lines
	 */
	public float getB() {
		return (float) Math.sqrt(Math.pow(height, 2) + Math.pow((getA() / 2), 2));
	}
	
	
	/**
	 * degrees of the bottom angles
	 */
	public float getAlpha() {
		return (float) Math.toDegrees(Math.asin(height / getB()));
	}
	
	
	/**
	 * degrees of the top angle
	 */
	public float getBeta() {
		return 180 - (getAlpha() * 2);
	}
}
