package com.hardcoded.logic.gui.listener;

import java.awt.Point;
import java.awt.event.*;
import java.util.*;

import com.hardcoded.logic.*;
import com.hardcoded.logic.gui.*;

/**
 * This abstract class is a preprocessor for the grid listeners
 * 
 * @author HardCoded
 * @since v0.1
 */
public abstract class AbstractGridListener implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
	// This fields
	protected State state = State.NORMAL;
	protected Object state_data;
	protected final GridPanel grid;
	protected final LogicSystem system;
	
	// World position
	protected double zoom = 1;
	protected double world_x;
	protected double world_y;
	
	// Scrolling
	protected double ms;
	protected double ms_c = 1;
	
	// The pointing position
	protected double pwx, pwy;
	
	protected boolean dragging = false;
	protected boolean pressing = false;
	
	protected final List<LogicComponent> selection = new ArrayList<>();
	protected Selection selection_area = new Selection();
	
	protected AbstractGridListener(GridPanel grid, LogicSystem system) {
		this.grid = Objects.requireNonNull(grid);
		this.system = Objects.requireNonNull(system);
	}
	
	/**
	 * Set the state of this listener.
	 * @param state the new state
	 */
	public void setState(State state) {
		this.state = Objects.requireNonNull(state, "AbstractGridListener state must not be null");
	}
	
	/**
	 * Returns the current state of this listener.
	 * @return the current state of this listener
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * Set the state of this listener with some extra data.
	 * @param state the new state
	 * @param data this data can be anything
	 */
	public void setState(State state, Object data) {
		this.state = state;
		this.state_data = data;
	}
	
	public void removeSelection() {
		if(!selection.isEmpty()) {
			for(LogicComponent c : selection) {
				c.setFocus(false);
			}
			selection.clear();
		}
		
		grid.repaint();
	}
	
	public void setSelection(LogicComponent comp) {
		if(!selection.isEmpty()) {
			for(LogicComponent c : selection) {
				c.setFocus(false);
			}
			selection.clear();
		}
		
		comp.setFocus(true);
		selection.add(comp);
		state = State.HAS_SELECTION;
		grid.repaint();
	}
	
	public void setSelection(List<LogicComponent> list) {
		if(!selection.isEmpty()) {
			for(LogicComponent c : selection) {
				c.setFocus(false);
			}
			selection.clear();
		}
		
		for(LogicComponent c : list) {
			c.setFocus(true);
		}
		
		selection.addAll(list);
		state = State.HAS_SELECTION;
		grid.repaint();
	}
	
	
	@Override
	public final void mousePressed(MouseEvent e) {
		pressing = true;
		
		pwx = -world_x + (e.getX() / zoom);
		pwy = -world_y + (e.getY() / zoom);
		
		onMousePressed(new LogicMouseEvent(e));
	}
	
	public abstract void onMousePressed(LogicMouseEvent e);
	public abstract void onMouseReleased(LogicMouseEvent e);
	public abstract void onMouseDragged(LogicMouseEvent e);
	public abstract void onMouseMoved(LogicMouseEvent e);
	public abstract void onKeyPressed(KeyEvent e);
	public abstract void onKeyReleased(KeyEvent e);
	public abstract void onMouseWheelMoved(LogicMouseEvent e);
	
	@Override
	public final void mouseReleased(MouseEvent e) {
		dragging = false;
		pressing = false;
		
		onMouseReleased(new LogicMouseEvent(e));
	}
	
	@Override
	public final void mouseDragged(MouseEvent e) {
		onMouseDragged(new LogicMouseEvent(e));
	}
	
	@Override
	public final void mouseMoved(MouseEvent e) {
		onMouseMoved(new LogicMouseEvent(e));
	}
	
	@Override
	public final void keyPressed(KeyEvent e) {
		onKeyPressed(e);
	}
	
	@Override
	public final void keyReleased(KeyEvent e) {
		onKeyReleased(e);
	}
	
	@Override
	public final void mouseWheelMoved(MouseWheelEvent e) {
		onMouseWheelMoved(new LogicMouseEvent(e));
	}
	
	@Override
	public final void mouseClicked(MouseEvent e) {
		
	}
	
	@Override
	public final void mouseEntered(MouseEvent e) {
		
	}
	
	@Override
	public final void mouseExited(MouseEvent e) {
		
	}
	
	@Override
	public final void keyTyped(KeyEvent e) {
		
	}
	
	public enum State {
		NORMAL,
		DISABLE_MOUSE,
		
		// Selection stuff
		CREATE_SELECTION,
		HAS_SELECTION,
		
		// Wire stuff
		CONNECT_WIRE,
		CREATE_CONNECTED_WIRE,
		
		// Spawning in new logic objects
		CREATE_GATE,
		CREATE_WIRE,
	}
	
	/**
	 * A wrapper class for MouseEvents
	 * 
	 * @author HardCoded
	 * @since v0.1
	 */
	protected class LogicMouseEvent {
		// LogicMouseEvent
		public final double owx;
		public final double owy;
		public final double wx;
		public final double wy;
		
		// MouseEvent
		public final int button;
		public final Point point;
		public final int wheel_rotation;
		public final long when;
		private final int modifiers;
		
		public LogicMouseEvent(MouseEvent e) {
			this.button = e.getButton();
			this.point = e.getPoint();
			this.when = e.getWhen();
			
			if(e instanceof MouseWheelEvent) {
				this.wheel_rotation = ((MouseWheelEvent)e).getWheelRotation();
			} else {
				this.wheel_rotation = 0;
			}
			
			this.modifiers = e.getModifiersEx();
			
			// Original world position
			this.owx = pwx;
			this.owy = pwy;
			// Current world position
			this.wx = -world_x + (e.getX() / zoom);
			this.wy = -world_y + (e.getY() / zoom);
		}
		
		// LogicMouseEvent
		public double getOriginalWorldX() {
			return this.owx;
		}
		
		public double getOriginalWorldY() {
			return this.owy;
		}
		
		public double getWorldX() {
			return this.wx;
		}
		
		public double getWorldY() {
			return this.wy;
		}
		
		public int getLogicX() {
			// TODO: Check if this gives the closest and best approximation for world to logic coordinates
			return (int)(this.wx / GridPanel.DOTS_SPACING);
		}
		
		public int getLogicY() {
			// TODO: Check if this gives the closest and best approximation for world to logic coordinates
			return (int)(this.wy / GridPanel.DOTS_SPACING);
		}
		
		// MouseEvent
		public int getX() {
			return this.point.x;
		}
		
		public int getY() {
			return this.point.y; 
		}
		
		public int getWheelRotation() {
			return this.wheel_rotation;
		}
		
		public boolean isControlDown() {
			return (this.modifiers & InputEvent.CTRL_DOWN_MASK) != 0;
		}
		
		public boolean isShiftDown() {
			return (this.modifiers & InputEvent.SHIFT_DOWN_MASK) != 0;
		}
	}
}
