package com.hardcoded.logic.gui;

import java.awt.Point;
import java.awt.event.*;
import java.util.*;

import com.hardcoded.logic.LogicObject;
import com.hardcoded.logic.LogicObjectType;
import com.hardcoded.logic.LogicWire;
import com.hardcoded.logic.gui.listener.AbstractGridListener;

public class GridPanelListener extends AbstractGridListener {
	protected GridPanelListener(GridPanel grid) {
		super(grid, grid.getSystem());
	}
	
	@Override
	public void onMousePressed(LogicMouseEvent e) {
		pressing = true;
		
		//pwx = -world_x + (e.getX() / zoom);
		//pwy = -world_y + (e.getY() / zoom);
		
		switch(state) {
			case CREATE_GATE:
			case CREATE_WIRE: {
				
				// Put state back to normal? Select?
				break;
			}
			default:
		}
		
		LogicComponent comp = grid.getLogicComponent(e.getWorldX(), e.getWorldY());
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
				selection_area.setPos1(e.getWorldX(), e.getWorldY());
				selection_area.setPos2(e.getWorldX(), e.getWorldY());
			} else {
				grid.setSelection(null);
			}
		}
		
		grid.repaint();
	}
	
	@Override
	public void onMouseReleased(LogicMouseEvent e) {
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
	public void onMouseDragged(LogicMouseEvent e) {
		switch(state) {
			case CREATE_SELECTION: {
				selection_area.setPos2(e.getWorldX(), e.getWorldY());
				
				grid.setSelection(selection_area);
				grid.repaint();
				return;
			}
			
			case HAS_SELECTION: {
				// Dragging with a selection should move all of them. Effectively moving stuff
				LogicComponent sel_logic = selection.get(0); // FIXME
				
				dragging = true;
				double rx = (int)(e.getWorldX() / (GridPanel.DOTS_SPACING + 0.0)) * GridPanel.DOTS_SPACING;
				double ry = (int)(e.getWorldY() / (GridPanel.DOTS_SPACING + 0.0)) * GridPanel.DOTS_SPACING;
				
				sel_logic.setLocation(rx, ry);
				// System.out.printf("Logic: { %d, %d, %d, %d }\n", (int)rx, (int)ry, sel_logic.getX(), sel_logic.getY());
				grid.repaint();
				return;
			}
			
			case CREATE_CONNECTED_WIRE: {
				// Creating a wire can only be done from a single selection
				LogicWireComponent comp = (LogicWireComponent)selection.get(0);
				LogicWire wire = comp.getLogicObject();
				
				int plx = e.getLogicX();
				int ply = e.getLogicY();
				double rx = plx * GridPanel.DOTS_SPACING;
				double ry = ply * GridPanel.DOTS_SPACING;
				
				if(!grid.system.hasWire(plx, ply)) {
					LogicWireComponent new_comp = grid.addWire(plx, ply);
					LogicWire w2 = new_comp.getLogicObject();
					grid.system.connectWire(w2, wire);
					setSelection(new_comp);
				} else {
					comp.setLocation(rx, ry);
				}
				
				grid.repaint();
				return;
			}
			
			case CONNECT_WIRE: {
				// Connecting a wire can only be done from a single selection
				LogicWireComponent comp = (LogicWireComponent)selection.get(0);
				int plx = e.getLogicX();
				int ply = e.getLogicY();
				
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
	public void onMouseMoved(LogicMouseEvent e) {
		switch(state) {
			case CREATE_WIRE: {
				int plx = e.getLogicX();
				int ply = e.getLogicY();
				double rx = plx * GridPanel.DOTS_SPACING;
				double ry = ply * GridPanel.DOTS_SPACING;
				
				if(selection.isEmpty()) {
					LogicWireComponent comp = grid.addWire(plx, ply);
					if(comp != null) {
						selection.add(comp);
						grid.repaint();
					}
				} else {
					selection.get(0).setLocation(rx, ry);
					grid.repaint();
				}
				
				break;
			}
			
			case CREATE_GATE: {
				int plx = e.getLogicX();
				int ply = e.getLogicY();
				double rx = plx * GridPanel.DOTS_SPACING;
				double ry = ply * GridPanel.DOTS_SPACING;
				
				if(selection.isEmpty()) {
					LogicGateComponent comp = grid.addGate(plx, ply, (LogicObjectType)state_data);
					if(comp != null) {
						selection.add(comp);
					}
					
					grid.repaint();
				} else {
					selection.get(0).setLocation(rx, ry);
					grid.repaint();
				}
				
				break;
			}
			
			default:
		}
	}
	
	@Override
	public void onKeyPressed(KeyEvent e) {
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
		
		if(state == State.HAS_SELECTION) {
			// Remove blue selection rectangle
			grid.setSelection(null);
			
			int ox = 0;
			int oy = 0;
			if(e.getKeyCode() == KeyEvent.VK_LEFT) ox--;
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) ox++;
			if(e.getKeyCode() == KeyEvent.VK_UP) oy--;
			if(e.getKeyCode() == KeyEvent.VK_DOWN) oy++;
			
			if(ox != 0 || oy != 0) {
				boolean can_move = true;
				Set<Long> set = new HashSet<>();
				
				for(LogicComponent comp : selection) {
					set.add(comp.getLogicObject().getIndex());
				}
				
				for(LogicComponent comp : selection) {
					LogicObject object = comp.getLogicObject();
					
					int x = object.getX() + ox;
					int y = object.getY() + oy;
					
					LogicObject collision = grid.system.getLogicObject(x, y);
					if(collision != null && !set.contains(collision.getIndex())) {
						can_move = false;
						break;
					}
				}
				
				if(can_move) {
					// GOOD: O(n)
					Set<Long> move = new HashSet<>(set);
					for(long idx : move) {
						if(!set.contains(idx)) continue;
						LogicObject object = grid.system.getLogicObject(idx);
						LinkedList<LogicObject> list_front = new LinkedList<>();
						list_front.push(object);
						
						while(!list_front.isEmpty()) {
							LogicObject obj = list_front.getLast();
							int x = obj.getX();
							int y = obj.getY();
							
							LogicObject front = grid.system.getLogicObject(x + ox, y + oy);
							if(front != null) {
								list_front.add(front);
								continue;
							}
							
							// Remove last element
							list_front.pollLast();
							
							// Remove this node from the list
							set.remove(obj.getIndex());
							if(!obj.setLocation(x + ox, y + oy)) {
								throw new RuntimeException("Failed to move logic object even though it should work!");
							}
						}
					}
					
					grid.repaint();
				}
			}
		}
	}
	
	@Override
	public void onKeyReleased(KeyEvent e) {
		if(state == State.HAS_SELECTION && selection.size() == 1) {
			LogicComponent comp = selection.get(0);
			
			if(comp instanceof LogicWireComponent) {
				if(e.isControlDown()) {
					if(e.getKeyCode() == KeyEvent.VK_W) {
						state = State.CREATE_CONNECTED_WIRE;
					}
					
					if(e.getKeyCode() == KeyEvent.VK_E) {
						state = State.CONNECT_WIRE;
					}
				}
			}
		}
	}
	
	@Override
	public void onMouseWheelMoved(LogicMouseEvent e) {
		LogicComponent sel_logic = null;
		if(!selection.isEmpty()) {
			sel_logic = selection.get(0); // FIXME
		}
		
		if(sel_logic != null && pressing) {
			sel_logic.setRotation(
				sel_logic.getRotation().moveClockwise(e.getWheelRotation())
			);
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
}
