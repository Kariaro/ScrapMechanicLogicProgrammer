package com.hardcoded.gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple color class.
 * 
 * @author HardCoded
 * @since v0.1 
 */
public class ColorMap implements java.io.Serializable {
	private static final long serialVersionUID = -178062824075372522L;
	private final Map<String, Color> l_map; // Light colors
	private final Map<String, Color> d_map; // Dark colors
	private final Map<String, Color> u_map; // User colors
	private boolean isDarkMode;
	
	public ColorMap() {
		l_map = new HashMap<>();
		d_map = new HashMap<>();
		u_map = new HashMap<>();
	}
	
	public void setDarkMode(boolean enable) {
		this.isDarkMode = enable;
	}
	
	public boolean getDarkMode() {
		return isDarkMode;
	}
	
	public void setDefault(IColor[] entries) {
		for(IColor color : entries) {
			String key = color.getKey();
			l_map.put(key, color.getLight());
			d_map.put(key, color.getDark());
		}
	}
	
	public void setDefault(String key, Color light, Color dark) {
		l_map.put(key, light);
		d_map.put(key, dark);
	}
	
	public Color get(IColor col) {
		return get(col.getKey());
	}
	
	public Color get(String key) {
		Color result = u_map.get(key);
		return result == null ? getDefault(key):result;
	}
	
	private Color getDefault(String key) {
		Color result = null;
		if(isDarkMode) {
			result = d_map.get(key);
			return result == null ? Color.white:result;
		} else {
			result = l_map.get(key);
			return result == null ? Color.black:result;
		}
	}
	
	public void set(String key, Color color) {
		u_map.put(key, color);
	}
	
	public void remove(String key) {
		u_map.remove(key);
	}
	
	public void clear() {
		u_map.clear();
	}
}