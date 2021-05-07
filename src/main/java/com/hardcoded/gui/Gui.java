package com.hardcoded.gui;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * A simple GUI implementation
 * 
 * @author HardCoded
 * @since v0.1
 */
public abstract class Gui extends JPanel implements IPrefs {
	private static final long serialVersionUID = -5514662386493524531L;
	protected static final ColorMap colors;
	
	static {
		colors = new ColorMap();
		colors.setDefault(ColorPrefs.values());
	}
	
	public Gui() {
		setDoubleBuffered(true);
	}
	
	@Override
	public void paint(Graphics gr) {
		super.paint(gr);
		
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	}
	
	@Override
	public void updateValue(Type type, Object value) {
		switch(type) {
			case DARK_MODE: {
				colors.setDarkMode((boolean)value);
				break;
			}
		}
	}
	
	public static ColorMap getColors() {
		return colors;
	}
}
