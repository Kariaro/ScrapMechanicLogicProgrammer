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
	protected LogicGateType type;
	protected long index;
	protected int inputs;
	protected int outputs;
	
	protected LogicGate(LogicSystem system, long index, LogicGateType type) {
		this.system = Objects.requireNonNull(system);
		this.type = Objects.requireNonNull(type);
		this.index = index;
		this.wire = new LogicGateWire(system, this, index);
		system.wires.put(index, wire);
		
		inputs = type.getInputs();
		outputs = type.getOutputs();
	}
	
	public LogicGateType getGateType() {
		return type;
	}
	
	public void setGateType(LogicGateType type) {
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
		
		if(system.hasGate(x, y)
		|| system.hasWire(x, y)) {
			// We cannot move the gate to this position because there
			// exists another gate.
			return false;
		}
		
		long new_index = LogicSystem.get_index(x, y);
		system.gates.remove(index);
		system.gates.put(new_index, this);
		index = new_index;
		
		return wire.setLocation(x, y);
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
