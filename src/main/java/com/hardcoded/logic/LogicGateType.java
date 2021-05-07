package com.hardcoded.logic;

public enum LogicGateType {
	OR,
	XOR,
	AND,
	NOR,
	XNOR,
	NAND,
	SWITCH,
	CUSTOM,
	
	;
	
	private final int inputs;
	private final int outputs;
	private LogicGateType(int inputs, int outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
	}
	private LogicGateType() {
		inputs = 2;
		outputs = 1;
	}
	
	public int getInputs() {
		return inputs;
	}
	
	public int getOutputs() {
		return outputs;
	}
	
	public boolean isGate() {
		switch(this) {
			case OR:
			case XOR:
			case AND:
			case NOR:
			case XNOR:
			case NAND:
				return true;
			default: return false;
		}
	}
}
