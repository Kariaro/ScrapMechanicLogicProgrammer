package com.hardcoded.logic.gui;

/**
 * A rotation class containing some rotation methods.
 * 
 * <pre>
 * NORTH,
 * EAST,
 * SOUTH,
 * WEST
 * </pre>
 * 
 * @author HardCoded
 * @since v0.1
 */
public enum LogicRotation {
	NORTH(0),
	EAST(1),
	SOUTH(2),
	WEST(3),
	;
	
	private static final LogicRotation[] CACHE = { NORTH, EAST, SOUTH, WEST };
	
	private final int id;
	private LogicRotation(int id) {
		this.id = id;
	}
	
	/**
	 * Returns the index of this rotation.
	 * <pre>
	 * north: 0
	 * east : 1
	 * south: 2
	 * west : 3
	 * </pre>
	 * 
	 * @return the index of this rotation
	 */
	public int getIndex() {
		return id;
	}
	
	public LogicRotation moveClockwise(int steps) {
		return CACHE[(id + steps) & 3];
	}
	
	public LogicRotation moveCounterclockwise(int steps) {
		return CACHE[(id - steps) & 3];
	}
}
