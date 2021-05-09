package com.hardcoded.logic;

import java.util.*;

/**
 * A logic node that can be connected to
 * 
 * @author HardCoded
 * @since v0.1
 */
public class LogicWire implements LogicObject {
	protected final LogicSystem system;
	protected final Set<Long> connections;
	protected transient boolean removed;
	protected LogicWireGroup group;
	protected long index;
	
	public LogicWire(LogicSystem system, long index) {
		this.system = Objects.requireNonNull(system);
		this.index = index;
		this.connections = new HashSet<>();
	}
	
	@Override
	public LogicSystem getSystem() {
		return system;
	}
	
	@Override
	public long getIndex() {
		return index;
	}
	
	@Override
	public int getX() {
		return LogicSystem.get_x(index);
	}
	
	@Override
	public int getY() {
		return LogicSystem.get_y(index);
	}
	
	public Set<Long> getConnections() {
		return connections;
	}

	protected LogicWireGroup getGroup() {
		return group;
	}
	
	@Override
	public boolean setLocation(int x, int y) {
		if(removed) return false;
		
		if(system.hasObject(x, y)) {
			// We cannot move the wire to this position because there
			// exists another wire
			return false;
		}
		
		long new_index = LogicSystem.get_index(x, y);
		if(new_index == index) return true;
		
		{ // Update the connections to allow the new position
			Iterator<Long> iter = connections.iterator();
			while(iter.hasNext()) {
				long idx = iter.next();
				LogicWire wire = system.getWire(idx);
				assert wire == null : "LogicWire contained a null reference!";
				
				wire.remove(index);
				wire.add(new_index);
			}
		}
		
		system.wires.remove(index);
		system.wires.put(new_index, this);
		
		if(group != null) {
			group.wires.remove(index);
			group.wires.add(new_index);
		}
		
		index = new_index;
		
		return true;
	}
	
	protected void add(long id) {
		if(removed) return;
		
		// Make sure we do not connect to ourselves
		if(id == index) return;
		connections.add(id);
	}
	
	protected void remove(long id) {
		connections.remove(id);
	}
	
	@Override
	public boolean isRemoved() {
		return removed;
	}
	
	public boolean isConnected(LogicWire wire) {
		return connections.contains(wire.getIndex());
	}
	
	@Override
	public void remove() {
		if(removed) return;
		removed = true;
		
		// Remove this wire from the system
		system.wires.remove(index);
		
		Iterator<Long> iter = connections.iterator();
		while(iter.hasNext()) {
			long id = iter.next();
			LogicWire node = system.getWire(id);
			assert node == null : "LogicWire contained a null reference!";
			node.remove(index);
		}
		
		if(group != null) {
			group.input.remove(index);
			group.output.remove(index);
			group.wires.remove(index);
			group = null;
		}
		
		connections.clear();
	}
}
