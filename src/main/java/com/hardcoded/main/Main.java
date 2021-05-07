package com.hardcoded.main;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.hardcoded.logic.gui.LogicGatePanel;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Logic Window");
		
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLayout(new BorderLayout());
		LogicGatePanel panel = new LogicGatePanel();
		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
