package com.hardcoded.logic.gui;

public class Selection {
	private double x1;
	private double y1;
	private double x2;
	private double y2;
	
	public Selection() {
		
	}
	
	public double getX1() {
		return x1;
	}
	
	public double getY1() {
		return y1;
	}
	
	public double getX2() {
		return x2;
	}
	
	public double getY2() {
		return y2;
	}
	
	public double getX() {
		return Math.min(x1, x2);
	}
	
	public double getY() {
		return Math.min(y1, y2);
	}
	
	public double getWidth() {
		return Math.abs(x2 - x1);
	}
	
	public double getHeight() {
		return Math.abs(y2 - y1);
	}
	
	public void setPos1(double x1, double y1) {
		this.x1 = x1;
		this.y1 = y1;
	}
	
	public void setPos2(double x2, double y2) {
		this.x2 = x2;
		this.y2 = y2;
	}
}