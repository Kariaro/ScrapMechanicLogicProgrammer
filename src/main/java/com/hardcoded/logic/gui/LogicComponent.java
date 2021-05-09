package com.hardcoded.logic.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import com.hardcoded.logic.LogicObject;


/**
 * An abstract implementation of a logic screen object.
 * 
 * @author HardCoded
 * @since v0.1
 */
public abstract class LogicComponent {
	protected int height;
	protected int width;
	protected int id;
	protected int x;
	protected int y;
	private boolean hasFocus;
	private LogicRotation rotation = LogicRotation.EAST;
	
	public LogicComponent() {
		
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean hasFocus() {
		return hasFocus;
	}
	
	public void setFocus(boolean b) {
		this.hasFocus = b;
	}
	
	/**
	 * Set the rotation of this component. If {@code rotation} is
	 * {@code null} no exception is thrown and no action will be performed.
	 * 
	 * @param rotation the new rotation
	 */
	public void setRotation(LogicRotation rotation) {
		if(rotation == null) return;
		this.rotation = rotation;
	}
	
	public LogicRotation getRotation() {
		return rotation;
	}
	
	public int getId() {
		return id;
	}
	
	public void setLocation(double x, double y) {
		this.x = (int)x;
		this.y = (int)y;
	}
	
	public Point getLocation() {
		return new Point(getX(), getY());
	}
	
	public Dimension getSize() {
		return new Dimension(getWidth(), getHeight());
	}
	
	private static final double ROT = Math.PI / 2.0;
	
	/**
	 * Called before the component is painted
	 */
	protected void prePaintComponent() {
		
	}
	
	/**
	 * When painting a logic component call this method.
	 * @param g the graphics context
	 */
	public final void paintComponent(Graphics2D g) {
		prePaintComponent();
		
		Graphics2D gr = (Graphics2D)g.create();
		gr.rotate(ROT * (rotation.getIndex() - 1), x, y);
		gr.translate(x - width / 2.0, y - height / 2.0);
		paint(gr);
		gr.dispose();
	}
	
	public abstract void paint(Graphics2D g);
	public abstract LogicObject getLogicObject();
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public boolean containsPoint(double xp, double yp) {
		return !(xp < x - width / 2.0 || xp > x + width / 2.0 || yp < y - height / 2.0 || yp > y + height / 2.0);
	}
}
