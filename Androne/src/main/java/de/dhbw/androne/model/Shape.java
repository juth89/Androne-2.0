package de.dhbw.androne.model;

public abstract class Shape {
	
	protected int width;
	protected int height;
	private boolean flyRight;
	
	public Shape() {
		this.width = 5;
		this.height = 5;
		this.flyRight = false;
	}
	
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
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setFlyRight(boolean flyRight) {
		this.flyRight = flyRight;
	}
}
