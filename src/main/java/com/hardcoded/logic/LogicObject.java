package com.hardcoded.logic;

/**
 * An interface for a logic object
 * @author HardCoded
 * @since v0.1
 */
public interface LogicObject {
	/**
	 * Returns the logic system that this object belongs to.
	 * @return the logic system that this object belongs to
	 */
	LogicSystem getSystem();
	
	/**
	 * Returns the unique index of this logic object.
	 * @return the unique index of this logic object
	 */
	long getIndex();
	
	/**
	 * Returns the y coordinate of this logic object.
	 * @return the y coordinate of this logic object
	 */
	int getX();
	
	/**
	 * Returns the x coordinate of this logic object.
	 * @return the x coordinate of this logic object
	 */
	int getY();
	
	/**
	 * Change this objects location inside the logic system.
	 * 
	 * @param x the new x coordinate
	 * @param y the new y coordinate
	 * @return {@code true} if this method was sucessfull
	 */
	boolean setLocation(int x, int y);
	
	/**
	 * Remove this object from the logic system
	 */
	void remove();
	
	/**
	 * Returns {@code true} if this logic object has been removed from the logic system.
	 * @return {@code ture} if this logic object has been removed from the logic system
	 */
	boolean isRemoved();
}
