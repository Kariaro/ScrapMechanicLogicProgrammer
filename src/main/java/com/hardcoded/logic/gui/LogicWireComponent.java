package com.hardcoded.logic.gui;

import java.awt.*;
import java.util.Objects;

import com.hardcoded.logic.LogicWire;

/**
 * A wire component.
 * 
 * @author HardCoded
 * @since v0.1
 */
public class LogicWireComponent extends LogicComponent {
	private final LogicWire wire;
	private long last_index;
	
	public LogicWireComponent(LogicWire wire) {
		this.wire = Objects.requireNonNull(wire, "LogicWireComponent wire must not be null");
		setSize(15, 15);
		
		// Update the location
		prePaintComponent();
	}
	
	@Override
	public void setRotation(LogicRotation rotation) {
		// Remove rotations
	}
	
	@Override
	public LogicWire getLogicObject() {
		return wire;
	}
	
	@Override
	protected void prePaintComponent() {
		if(wire == null) return;
		
		if(last_index != wire.getIndex()) {
			setLocation(
				wire.getX() * GridPanel.DOTS_SPACING,
				wire.getY() * GridPanel.DOTS_SPACING
			);
			
			last_index = wire.getIndex();
		}
	}
	
	@Override
	public void setLocation(double x, double y) {
		wire.setLocation(
			(int)(x / (GridPanel.DOTS_SPACING + 0.0)),
			(int)(y / (GridPanel.DOTS_SPACING + 0.0))
		);
		
		super.setLocation(
			wire.getX() * GridPanel.DOTS_SPACING,
			wire.getY() * GridPanel.DOTS_SPACING
		);
	}
	
	@Override
	public void setFocus(boolean b) {
		super.setFocus(b);
		
		wire_drag = null;
	}
	
	private Point wire_drag;
	public void setWireDrag(int x, int y) {
		wire_drag = new Point(x, y);
	}
	
	public Point getDragPoint() {
		return wire_drag;
	}
	
	@Override
	public void paint(Graphics2D g) {
		if(hasFocus()) {
			g.setColor(new Color(0, 0, 0, 0.5f));
			g.fillRect(-5, -5, 25, 25);
		}
		
		int oo = GridPanel.DOTS_SPACING / 2;
		g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(new Color(0x11bb77));
		for(long index : wire.getConnections()) {
			if(index < wire.getIndex()) continue;
			
			LogicWire n2 = wire.getSystem().getWire(index);
			int xp = n2.getX() - wire.getX();
			int yp = n2.getY() - wire.getY();
			g.drawLine(
				oo, oo,
				xp * GridPanel.DOTS_SPACING + oo,
				yp * GridPanel.DOTS_SPACING + oo
			);
		}
		
		if(hasFocus() && wire_drag != null) {
			g.drawLine(
				oo, oo,
				(wire_drag.x - wire.getX()) * GridPanel.DOTS_SPACING + oo,
				(wire_drag.y - wire.getY()) * GridPanel.DOTS_SPACING + oo
			);
		}
		
		g.setColor(Color.green);
		g.setColor(new Color(0x11bb77));
		g.setStroke(new BasicStroke(2));
		g.fillOval(4, 4, 7, 7);
	}
}
