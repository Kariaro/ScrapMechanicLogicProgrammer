package com.hardcoded.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Input wire for logic gates.
 * 
 * This is only used interally
 * 
 * @author HardCoded
 * @since v0.1
 */
public class LogicGateWire extends LogicWire {
	protected final List<Long> input;
	protected final List<Long> output;
	protected final LogicGate gate;
	
	protected LogicGateWire(LogicSystem system, LogicGate gate, long index) {
		super(system, index);
		this.input = new ArrayList<>();
		this.output = new ArrayList<>();
		this.gate = gate;
	}
	
	/**
	 * Returns a list of wire inputs.
	 * @return a list of wire inputs
	 */
	public List<Long> getInput() {
		return input;
	}
	
	/**
	 * Returns a list of wire outputs.
	 * @return a list of wire outputs
	 */
	public List<Long> getOutput() {
		return output;
	}
	
	@Override
	protected void add(long id) {
		super.add(id);
		input.add(id);
	}
}
