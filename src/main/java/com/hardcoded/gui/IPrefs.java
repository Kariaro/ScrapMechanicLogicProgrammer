package com.hardcoded.gui;

/**
 * 
 * @author HardCoded
 * @since v0.1
 */
public interface IPrefs {
	public enum Type {
		DARK_MODE
	}
	
	void updateValue(Type type, Object value);
}
