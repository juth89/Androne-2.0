package de.dhbw.androne.model;

public abstract class Shape {
	
	private int width;
	private int height;
	private boolean flyRight;
	
	
	public Shape(int width, int height, boolean flyRight) {
		this.width = width;
		this.height = height;
		this.flyRight = flyRight;
	}
	
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean flyRight() {
		return flyRight;
	}
}
