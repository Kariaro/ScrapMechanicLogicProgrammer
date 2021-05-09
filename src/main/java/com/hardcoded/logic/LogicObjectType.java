package com.hardcoded.logic;

public enum LogicObjectType {
	OR,
	XOR,
	AND,
	NOR,
	XNOR,
	NAND,
	
	WIRE,
	
	SWITCH,
	CUSTOM,
	
	;
	
	private final int inputs;
	private final int outputs;
	private LogicObjectType(int inputs, int outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
	}
	private LogicObjectType() {
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
