package com.hardcoded.logic;

import java.util.Objects;

/**
 * 
 * @author HardCoded
 * @since v0.1
 */
public class LogicGate implements LogicObject {
	protected final LogicSystem system;
	protected final LogicGateWire wire;
	protected transient boolean removed;
	protected LogicObjectType type;
	protected long index;
	protected int inputs;
	protected int outputs;
	
	protected LogicGate(LogicSystem system, long index, LogicObjectType type) {
		this.system = Objects.requireNonNull(system);
		this.type = Objects.requireNonNull(type);
		this.index = index;
		this.wire = new LogicGateWire(system, this, index);
		//system.wires.put(index, wire);
		
		inputs = type.getInputs();
		outputs = type.getOutputs();
	}
	
	public LogicObjectType getGateType() {
		return type;
	}
	
	public void setGateType(LogicObjectType type) {
		this.type = Objects.requireNonNull(type);
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
	
	public LogicGateWire getGateWire() {
		return wire;
	}
	
	public int getInputs() {
		return inputs;
	}
	
	public int getOutputs() {
		return outputs;
	}
	
	@Override
	public boolean isRemoved() {
		return removed;
	}
	
	@Override
	public boolean setLocation(int x, int y) {
		if(removed) return false;
		
		if(system.hasObject(x, y)) {
			// We cannot move the gate to this position because there
			// exists another gate.
			return false;
		}
		
		long new_index = LogicSystem.get_index(x, y);
		if(new_index == index) return true;
		system.gates.remove(index);
		system.gates.put(new_index, this);
		index = new_index;
		
		//return wire.setLocation(x, y);
		return true;
	}
	
	@Override
	public void remove() {
		if(removed) return;
		removed = true;
		
		wire.remove();
		system.gates.remove(index);
	}
	
	public void setInputs(int i) {
		this.inputs = i;
	}
	
	public void setOutputs(int i) {
		this.outputs = i;
	}
}
