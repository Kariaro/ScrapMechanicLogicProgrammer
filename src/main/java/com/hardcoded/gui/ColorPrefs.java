package com.hardcoded.gui;

import java.awt.Color;

public enum ColorPrefs implements IColor {
	LOGIC_PANEL_BG("logic-panel-bg", Color.white, Color.lightGray),
	LOGIC_PANEL_DOTS("logic-panel-dots", Color.green, Color.green),
	;
	
	private final String key;
	private final Color light;
	private final Color dark;
	private ColorPrefs(String key, Color light, Color dark) {
		this.key = key;
		this.light = light;
		this.dark = dark;
	}
	
	public String getKey() {
		return key;
	}
	
	public Color getLight() {
		return light;
	}
	
	public Color getDark() {
		return dark;
	}
}
