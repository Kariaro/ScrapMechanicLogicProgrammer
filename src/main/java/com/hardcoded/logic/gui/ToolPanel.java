package com.hardcoded.logic.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.hardcoded.logic.LogicGateType;

/**
 * A toolbar panel
 * @author Admin
 * @since v0.1
 */
public class ToolPanel extends JPanel {
	private static final long serialVersionUID = 8223809619889953071L;
	
	public ToolPanel(final GridPanel grid) {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setPreferredSize(new Dimension(100, 500));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JLabel lblNewLabel = new JLabel("Logic Gates");
		add(lblNewLabel);

		JButton btn_xor = new JButton("XOR");
		btn_xor.addActionListener((e) -> grid.setMouseCreate(LogicGateType.XOR));
		btn_xor.setMaximumSize(new Dimension(100, 23));
		btn_xor.setFocusable(false);
		add(btn_xor);
		
		JButton btn_and = new JButton("AND");
		btn_and.addActionListener((e) -> grid.setMouseCreate(LogicGateType.AND));
		btn_and.setMaximumSize(new Dimension(100, 23));
		btn_and.setFocusable(false);
		add(btn_and);
		
		JButton btn_or = new JButton("OR");
		btn_or.addActionListener((e) -> grid.setMouseCreate(LogicGateType.OR));
		btn_or.setMaximumSize(new Dimension(100, 23));
		btn_or.setFocusable(false);
		add(btn_or);
		
		JButton btn_xnor = new JButton("XNOR");
		btn_xnor.addActionListener((e) -> grid.setMouseCreate(LogicGateType.XNOR));
		btn_xnor.setMaximumSize(new Dimension(100, 23));
		btn_xnor.setFocusable(false);
		add(btn_xnor);
		
		JButton btn_nand = new JButton("NAND");
		btn_nand.addActionListener((e) -> grid.setMouseCreate(LogicGateType.NAND));
		btn_nand.setMaximumSize(new Dimension(100, 23));
		btn_nand.setFocusable(false);
		add(btn_nand);
		
		JButton btn_nor = new JButton("NOR");
		btn_nor.addActionListener((e) -> grid.setMouseCreate(LogicGateType.NOR));
		btn_nor.setMaximumSize(new Dimension(100, 23));
		btn_nor.setFocusable(false);
		add(btn_nor);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut);
		
		JLabel lblNewLabel_1 = new JLabel("Components");
		add(lblNewLabel_1);
		
		JButton btn_switch = new JButton("Switch");
		btn_switch.setMaximumSize(new Dimension(100, 23));
		btn_switch.setFocusable(false);
		add(btn_switch);
		
		JButton btn_wire = new JButton("Wire");
		btn_wire.setMaximumSize(new Dimension(100, 23));
		btn_wire.setFocusable(false);
		add(btn_wire);
	}
}
