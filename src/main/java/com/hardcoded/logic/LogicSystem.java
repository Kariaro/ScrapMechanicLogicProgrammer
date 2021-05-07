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
	
	protected Map<Long, LogicWire> wires;
	protected Map<Long, LogicGate> gates;
	protected Map<Long, LogicWireGroup> groups;
	
	public LogicSystem() {
		wires = new HashMap<>();
		gates = new HashMap<>();
		groups = new HashMap<>();
	}
	
	public Collection<LogicWire> getWires() {
		return wires.values();
	}
	
	public LogicGate getGate(long index) {
		return gates.get(index);
	}
	
	public boolean hasGate(int x, int y) {
		return gates.containsKey(get_index(x, y));
	}
	
	public LogicGate addGate(int x, int y, LogicGateType type) {
		if(hasWire(x, y) || hasGate(x, y)) return null;
		long index = get_index(x, y);
		
		LogicGate object = new LogicGate(this, index, type);
		gates.put(index, object);
		return object;
	}
	
	public LogicWire getWire(int x, int y) {
		return getWire(get_index(x, y));
	}
	
	public LogicWire getWire(long index) {
		return wires.get(index);
	}
	
	public boolean hasWire(int x, int y) {
		return hasWire(get_index(x, y));
	}
	
	public boolean hasWire(long index) {
		return wires.containsKey(index);
	}
	
	public LogicWire addWire(int x, int y) {
		long index = get_index(x, y);
		if(hasWire(index)) return null;
		
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
