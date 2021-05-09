package com.hardcoded.logic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author HardCoded
 * @since v0.1
 */
public class LogicSystem {
	private static final AtomicInteger ID = new AtomicInteger(1);
	
	protected static int getId() {
		return ID.getAndIncrement();
	}
	
	// TODO: Instead of using "wires" and "gates" switch over to using "objects".
	//       We can still access wires by checking if the returned type is an instanceof of that class.
	//       This would make saving the data easier because gates and wires cannot be on the same position eitherway.
	//protected Map<Long, LogicObject> objects;
	protected Map<Long, LogicWire> wires;
	protected Map<Long, LogicGate> gates;
	protected Map<Long, LogicWireGroup> groups;
	
	public LogicSystem() {
		wires = new HashMap<>();
		gates = new HashMap<>();
		groups = new HashMap<>();
		//objects = new HashMap<>();
	}
	
	public Collection<LogicWire> getWires() {
		return wires.values();
	}
	
	public boolean hasWire(int x, int y) {
		return hasWire(get_index(x, y));
	}
	
	public boolean hasWire(long index) {
		return wires.containsKey(index);
	}
	
	
	public boolean hasGate(int x, int y) {
		return hasGate(get_index(x, y));
	}
	
	public boolean hasGate(long index) {
		return gates.containsKey(index);
	}
	
	
	public boolean hasObject(int x, int y) {
		return hasObject(get_index(x, y));
	}
	
	public boolean hasObject(long index) {
		return hasWire(index) || hasGate(index);
	}
	
	
	public LogicGate getGate(int x, int y) {
		return getGate(get_index(x, y));
	}
	
	public LogicGate getGate(long index) {
		return gates.get(index);
	}
	
	public LogicWire getWire(int x, int y) {
		return getWire(get_index(x, y));
	}
	
	public LogicWire getWire(long index) {
		return wires.get(index);
	}
	
	public LogicObject getLogicObject(int x, int y) {
		return getLogicObject(get_index(x, y));
	}
	
	public LogicObject getLogicObject(long index) {
		if(hasWire(index)) return getWire(index);
		return getGate(index);
	}
	
	public LogicGate addGate(int x, int y, LogicObjectType type) {
		if(hasObject(x, y)) return null;
		long index = get_index(x, y);
		
		LogicGate object = new LogicGate(this, index, type);
		gates.put(index, object);
		return object;
	}
	
	public LogicWire addWire(int x, int y) {
		long index = get_index(x, y);
		if(hasObject(index)) return null;
		
		LogicWire wire = new LogicWire(this, index);
		wires.put(index, wire);
		return wire;
	}
	
	protected LogicWireGroup new_wire_group() {
		LogicWireGroup group = new LogicWireGroup(this, getId());
		groups.put(group.index, group);
		return group;
	}
	
	public boolean disconnectWire(LogicWire a, LogicWire b) {
		if(a == b || a == null || b == null) return false;
		
		if(a.isConnected(b)) {
			return a.getGroup().disconnectWire(a, b);
		}
		
		return false;
	}
	
	public boolean connectWire(LogicWire a, LogicWire b) {
		if(a == b || a == null || b == null) return false;
		
		LogicWireGroup group = a.getGroup();
		if(group != null) {
			return group.connectWire(a, b);
		}
		
		group = b.getGroup();
		if(group != null) {
			return group.connectWire(b, a);
		}
		
		group = new_wire_group();
		group.add(a);
		return group.connectWire(a, b);
	}
	
	static long get_index(int x, int y) {
		return ((long)(x) & 0xffffffffL) | (((long)y) << 32L);
	}
	
	static int get_x(long index) {
		return (int)(index & 0xffffffffL);
	}
	
	static int get_y(long index) {
		return (int)((index >>> 32L) & 0xffffffffL);
	}
}
