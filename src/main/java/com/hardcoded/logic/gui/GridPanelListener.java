package com.hardcoded.logic.gui;

import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import com.hardcoded.logic.LogicObject;
import com.hardcoded.logic.LogicWire;

public class GridPanelListener implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
	private final GridPanel grid;
	public State state;
	
	// World position
	private double zoom = 1;
	private double world_x;
	private double world_y;
	
	// Scrolling
	private double ms;
	private double ms_c = 1;
	
	// The pointing position
	private double pwx, pwy;
	
	private boolean dragging = false;
	private boolean pressing = false;
	
	private final List<LogicComponent> selection;
	private Selection selection_area = new Selection();
	
	protected GridPanelListener(GridPanel grid) {
		this.grid = grid;
		this.selection = new ArrayList<>();
		this.state = State.NORMAL;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		pressing = true;
		
		pwx = -world_x + (e.getX() / zoom);
		pwy = -world_y + (e.getY() / zoom);
		
		LogicComponent comp = grid.getLogicComponent(pwx, pwy);
		if(comp != null) {
			setSelection(comp);
			grid.repaint();
		}
		
		if(comp == null) {
			// Selecting the background will turn the state back to normal
			state = State.NORMAL;
			if(!selection.isEmpty()) {
				for(LogicComponent c : selection) {
					c.setFocus(false);
				}
				selection.clear();
			}
			
			if(e.isControlDown()) {
				state = State.CREATE_SELECTION;
				selection_area.setPos1(pwx, pwy);
				selection_area.setPos2(pwx, pwy);
			} else {
				grid.setSelection(null);
			}
		}
		
		grid.repaint();
	}
	
	private void setSelection(LogicComponent comp) {
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
	
	private void setSelection(List<LogicComponent> list) {
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
	public void mouseReleased(MouseEvent e) {
		dragging = false;
		pressing = false;
		
		switch(state) {
			case CREATE_SELECTION: {
				selection.addAll(grid.getLogicComponents(selection_area));
				for(LogicComponent comp : selection) {
					comp.setFocus(true);
				}
				
				state = State.HAS_SELECTION;
				grid.repaint();
				break;
			}
			
			case CONNECT_WIRE: {
				LogicComponent sel_logic = selection.get(0);
				if(sel_logic instanceof LogicWireComponent) {
					LogicWireComponent comp = (LogicWireComponent)sel_logic;
					
					Point p = comp.getDragPoint();
					if(p != null) {
						LogicWire w1 = comp.getLogicObject();
						LogicWire w2 = grid.system.getWire(p.x, p.y);
						
						if(w2 != null) {
							if(w1.isConnected(w2)) {
								grid.system.disconnectWire(w1, w2);
								comp.setFocus(true);
							} else {
								grid.system.connectWire(w1, w2);
							}
							
							grid.repaint();
						}
					}
				}
			}
			
			case DISABLE_MOUSE: {
				state = State.NORMAL;
				break;
			}
			
			default:
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		switch(state) {
			case CREATE_SELECTION: {
				double wx = -world_x + (e.getX() / zoom);
				double wy = -world_y + (e.getY() / zoom);
				selection_area.setPos2(wx, wy);
				
				grid.setSelection(selection_area);
				grid.repaint();
				return;
			}
			
			case HAS_SELECTION: {
				// Dragging with a selection should move all of them. Effectively moving stuff
				LogicComponent sel_logic = selection.get(0); // FIXME
				
				dragging = true;
				double dx = -world_x + (e.getX() / zoom);
				double dy = -world_y + (e.getY() / zoom);
				double rx = (int)(dx / (GridPanel.DOTS_SPACING + 0.0)) * GridPanel.DOTS_SPACING;
				double ry = (int)(dy / (GridPanel.DOTS_SPACING + 0.0)) * GridPanel.DOTS_SPACING;
				
				sel_logic.setLocation(rx, ry);
				// System.out.printf("Logic: { %d, %d, %d, %d }\n", (int)rx, (int)ry, sel_logic.getX(), sel_logic.getY());
				grid.repaint();
				return;
			}
			
			case CREATE_CONNECTED_WIRE: {
				// Creating a wire can only be done from a single selection
				LogicWireComponent comp = (LogicWireComponent)selection.get(0);
				LogicWire wire = comp.getLogicObject();
				
				double dx = -world_x + (e.getX() / zoom);
				double dy = -world_y + (e.getY() / zoom);
				double rx = (int)(dx / (GridPanel.DOTS_SPACING + 0.0)) * GridPanel.DOTS_SPACING;
				double ry = (int)(dy / (GridPanel.DOTS_SPACING + 0.0)) * GridPanel.DOTS_SPACING;
				int plx = (int)(rx / GridPanel.DOTS_SPACING);
				int ply = (int)(ry / GridPanel.DOTS_SPACING);
				
				if(!grid.system.hasWire(plx, ply)) {
					LogicWire w2 = grid.addWire(plx, ply);
					grid.system.connectWire(w2, wire);
					
					comp.setFocus(false);
					
					// NODE: This seams like it could give errors or some rare exception at some point.
					comp = (LogicWireComponent)grid.getLogicComponent(rx, ry);
					setSelection(comp);
				} else {
					comp.setLocation(rx, ry);
				}
				
				grid.repaint();
				return;
			}
			
			case CONNECT_WIRE: {
				// Connecting a wire can only be done from a single selection
				LogicWireComponent comp = (LogicWireComponent)selection.get(0);
				
				double dx = -world_x + (e.getX() / zoom);
				double dy = -world_y + (e.getY() / zoom);
				double rx = (int)((dx) / (GridPanel.DOTS_SPACING + 0.0)) * GridPanel.DOTS_SPACING;
				double ry = (int)((dy) / (GridPanel.DOTS_SPACING + 0.0)) * GridPanel.DOTS_SPACING;
				
				int plx = (int)(rx / GridPanel.DOTS_SPACING);
				int ply = (int)(ry / GridPanel.DOTS_SPACING);
				
				comp.setWireDrag(plx, ply);
				grid.repaint();
				return;
			}
			
			case DISABLE_MOUSE: return;
			
			case NORMAL: {
				dragging = true;
				world_x = (e.getX() / zoom) - pwx;
				world_y = (e.getY() / zoom) - pwy;
				grid.setWorldPosition(world_x, world_y);
				grid.repaint();
				return;
			}
			
			default:
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DELETE) {
			for(LogicComponent comp : selection) {
				LogicObject logic = comp.getLogicObject();
				logic.remove();
			}
			if(!selection.isEmpty()) {
				selection.clear();
				grid.setSelection(null);
				grid.repaint();
				
				state = State.DISABLE_MOUSE;
			}
		}
		
		if(e.isControlDown()) {
			if(e.getKeyCode() == KeyEvent.VK_A) {
				state = State.HAS_SELECTION;
				setSelection(grid.getLogicComponents());
			}
			
			if(e.getKeyCode() == KeyEvent.VK_C) {
				// Copy
			}
			
			if(e.getKeyCode() == KeyEvent.VK_X) {
				// Cut
			}
			
			if(e.getKeyCode() == KeyEvent.VK_Z) {
				// Undo ?????
			}
			
			if(e.getKeyCode() == KeyEvent.VK_V) {
				// Paste
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(state == State.HAS_SELECTION && selection.size() == 1) {
			LogicComponent comp = selection.get(0);
			
			if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
				if(comp instanceof LogicWireComponent) {
					state = State.CREATE_CONNECTED_WIRE;
				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
				if(comp instanceof LogicWireComponent) {
					state = State.CONNECT_WIRE;
				}
			}
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		LogicComponent sel_logic = null;
		if(!selection.isEmpty()) {
			sel_logic = selection.get(0); // FIXME
		}
		
		if(sel_logic != null && pressing) {
			int rot = sel_logic.getRotation() + e.getWheelRotation();
			sel_logic.setRotation(rot);
			grid.repaint();
			return;
		}
		
		double tmp = ms + e.getWheelRotation() / 2.0;
		if(tmp < -2) tmp = -2;
		if(tmp >  4) tmp =  4;
		if(ms == tmp) return;
		
		ms = tmp;
		ms_c = Math.pow(2, -ms);
		
		if(dragging) {
			world_x = (e.getX() / ms_c) - pwx;
			world_y = (e.getY() / ms_c) - pwy;
		} else {
			double dx_0 = (e.getX()) / zoom;
			double dy_0 = (e.getY()) / zoom;
			double dx_1 = (e.getX()) / ms_c;
			double dy_1 = (e.getY()) / ms_c;
			double ddx = dx_1 - dx_0;
			double ddy = dy_1 - dy_0;
			world_x += ddx;
			world_y += ddy;
		}
		
		zoom = ms_c;
		grid.setWorldPosition(world_x, world_y);
		grid.setWorldZoom(zoom);
		grid.repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void setState(State state) {
		this.state = state;
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
		CREATE_WIRE,
		CREATE_GATE,
	}
}
