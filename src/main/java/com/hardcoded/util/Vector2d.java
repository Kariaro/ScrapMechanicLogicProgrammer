package com.hardcoded.util;

import java.util.Locale;

/**
 * A simple 2 dimensional vector implementation.
 * 
 * @author HardCoded
 * @since v0.1
 */
public class Vector2d {
	public double x;
	public double y;
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.US, "{ %.4f, %.4f }", x, y);
	}
}
