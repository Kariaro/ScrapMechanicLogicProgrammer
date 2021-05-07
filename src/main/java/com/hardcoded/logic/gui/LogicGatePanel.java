package com.hardcoded.logic.gui;

import java.awt.BorderLayout;

import com.hardcoded.gui.ColorPrefs;
import com.hardcoded.gui.Gui;

/**
 * 
 * @author HardCoded
 * @since v0.1
 */
public class LogicGatePanel extends Gui {
	private static final long serialVersionUID = 4945235543041109069L;
	private ToolPanel tool;
	private GridPanel grid;
	
	public LogicGatePanel() {
		setBackground(colors.get(ColorPrefs.LOGIC_PANEL_BG));
		setLayout(new BorderLayout());
		
		grid = new GridPanel();
		add(grid, BorderLayout.CENTER);
		
		tool = new ToolPanel(grid);
		add(tool, BorderLayout.WEST);
	}
	
	@Override
	public void updateValue(Type type, Object value) {
		super.updateValue(type, value);
	}
}
