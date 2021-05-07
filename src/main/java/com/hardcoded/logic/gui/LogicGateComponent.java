package com.hardcoded.logic.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.hardcoded.logic.*;

public class LogicGateComponent extends LogicComponent {
	private static final BufferedImage[] GATES;
	
	static {
		GATES = new BufferedImage[6];
		try {
			GATES[0] = ImageIO.read(LogicGateComponent.class.getResourceAsStream("/images/or.png"));
			GATES[1] = ImageIO.read(LogicGateComponent.class.getResourceAsStream("/images/xor.png"));
			GATES[2] = ImageIO.read(LogicGateComponent.class.getResourceAsStream("/images/and.png"));
			GATES[3] = ImageIO.read(LogicGateComponent.class.getResourceAsStream("/images/nor.png"));
			GATES[4] = ImageIO.read(LogicGateComponent.class.getResourceAsStream("/images/xnor.png"));
			GATES[5] = ImageIO.read(LogicGateComponent.class.getResourceAsStream("/images/nand.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private LogicGate gate;
	private long last_index;
	
	public LogicGateComponent(LogicGate gate) {
		this.gate = Objects.requireNonNull(gate, "LogicGateComponent gate must not be null");
		setSize(60, 60);
		
		// Update the location
		prePaintComponent();
	}
	
	@Override
	public LogicGate getLogicObject() {
		return gate;
	}
	
	protected boolean paintPoints(Graphics2D g, int x, int y, int count, Color color) {
		return paintPoints(g, x, y, count, color, color);
	}
	
	protected boolean paintPoints(Graphics2D g, int x, int y, int count, Color border, Color center) {
		boolean gap = false;
		if((count & 1) == 0) gap = true;
		
		int h = GridPanel.DOTS_SPACING * (count - 1 + (gap ? 1:0));
		int r = GridPanel.WIRE_RADIUS;
		int d = r * 2;
		x -= r;
		y -= r;
		
		Color old_color = g.getColor();
		g.setColor(border);
		for(int i = 0; i < count; i++) {
			int add = gap ? (i >= count / 2 ? 1:0):0;
			g.fillOval(x, y - h / 2 + GridPanel.DOTS_SPACING * (i + add), d, d);
		}
		
		if(border != center) {
			g.setColor(center);
			for(int i = 0; i < count; i++) {
				int add = gap ? (i >= count / 2 ? 1:0):0;
				g.fillOval(x + 2, y - h / 2 + GridPanel.DOTS_SPACING * (i + add) + 2, 8, 8);
			}
		}
		
		g.setColor(old_color);
		
		return false;
	}
	
	@Override
	protected void prePaintComponent() {
		if(gate == null) return;
		
		if(last_index != gate.getIndex()) {
			setLocation(
				gate.getX() * GridPanel.DOTS_SPACING,
				gate.getY() * GridPanel.DOTS_SPACING
			);
		}
	}
	
	@Override
	public void setLocation(double x, double y) {
		// Try set the location of the gate
		gate.setLocation(
			(int)(x / (GridPanel.DOTS_SPACING + 0.0)),
			(int)(y / (GridPanel.DOTS_SPACING + 0.0))
		);
		
		super.setLocation(
			gate.getX() * GridPanel.DOTS_SPACING,
			gate.getY() * GridPanel.DOTS_SPACING
		);
	}
	
	@Override
	public void paint(Graphics2D g) {
		if(gate == null) return;
		
		LogicGateType type = gate.getGateType();
		gate.setInputs(4);
		
		if(hasFocus()) {
			g.setColor(new Color(0, 0, 0, 0.5f));
			g.fillRect(5, 5, 50, 50);
		}
		
		Color col = new Color(0x88ff88);
		paintPoints(g, 0, 30, gate.getInputs(), Color.green);
		paintPoints(g, 60, 30, gate.getOutputs(), col);
		
		{
			LogicSystem system = gate.getSystem();
			
			g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.setColor(new Color(0x11bb77));
			LogicGateWire w1 = gate.getGateWire();
			
			for(long index : w1.getInput()) {
				LogicWire w2 = system.getWire(index);
				if(w2 == null) continue;
				
				g.drawLine(
					w1.getX() * GridPanel.DOTS_SPACING,
					w2.getY() * GridPanel.DOTS_SPACING,
					w1.getX() * GridPanel.DOTS_SPACING,
					w2.getY() * GridPanel.DOTS_SPACING
				);
			}
		}
		
		if(!type.isGate()) {
			g.setColor(new Color(0, 0, 0, 0.5f));
			g.fillRect(0, 0, 60, 60);
			
			return;
		}
		
		BufferedImage bi = GATES[type.ordinal()];
		g.drawImage(bi, 0, 10, 60, 40, null);
	}
	
	public boolean containsPoint(double pwx, double pwy) {
		return !(pwx < x - width / 2.0 + 5 || pwx > x + width / 2.0 - 5 || pwy < y - height / 2.0 + 5 || pwy > y + height / 2.0 - 5);
	}
}
