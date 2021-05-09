package com.hardcoded.logic.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import com.hardcoded.gui.ColorMap;
import com.hardcoded.gui.ColorPrefs;
import com.hardcoded.gui.Gui;
import com.hardcoded.logic.*;

/**
 * A logic panel
 * 
 * @author HardCoded
 * @since v0.1
 */
public class GridPanel extends JPanel {
	private static final long serialVersionUID = -4477535226248145471L;
	
	private static final ColorMap colors = Gui.getColors();
	private static final TexturePaint DOTS_TEXTURE;
	public static final int DOTS_SPACING = 15;
	public static final int DOTS_RADIUS = 1;
	public static final int WIRE_RADIUS = 4;
	
	static {
		int size = 4;
		int is = 15 * size;
		BufferedImage dots = new BufferedImage(is, is, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = dots.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		g.scale(size, size);
		//int yt = (int)((is / size) / DOTS_SPACING) + 1;
		//int xt = (int)((is / size) / DOTS_SPACING) + 1;
		
		//int dd = DOTS_RADIUS * 2;
		g.setColor(new Color(0xeeeeee));
		g.drawRect(0, 0, is, is);
//		g.setColor(Gui.getColors().get(ColorPrefs.LOGIC_PANEL_DOTS));
//		for(int y = 0; y < yt; y++) {
//			for(int x = 0; x < xt; x++) {
//				//g.fillOval(x * DOTS_SPACING - 1, y * DOTS_SPACING - 1, dd, dd);
//			}
//		}
		g.dispose();
		
		DOTS_TEXTURE = new TexturePaint(dots, new Rectangle(dots.getWidth() / size, dots.getHeight() / size));
	}
	
	protected java.util.List<LogicComponent> list;
	protected LogicSystem system;
	private GridPanelListener listener;
	
	public GridPanel() {
		setOpaque(true);
		setDoubleBuffered(true);
		setIgnoreRepaint(true);
		setLayout(null);
		setBorder(null);
		setFocusable(true);
		
		list = new ArrayList<>();
		system = new LogicSystem();
		
		listener = new GridPanelListener(this);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		addMouseWheelListener(listener);
		addKeyListener(listener);
		
		int idx = 0;
		for(LogicObjectType type : LogicObjectType.values()) {
			LogicGate obj = system.addGate(0, (idx++) * 6, type);
			LogicGateComponent comp = new LogicGateComponent(obj);
			add(comp);
		}
		
		{
			int rx = 0;
			int ry = 0;
			
			java.util.Random random = new java.util.Random();
			random.nextInt();
			
			LogicWireComponent last = addWire(rx, ry);
			
//			for(int i = 0; i < 1000; i++) {
//				int xp = rx + random.nextInt(10) - 5;
//				int yp = ry + random.nextInt(10) - 5;
//				
//				LogicWireComponent wire = addWire(xp, yp);
//				
//				if(wire == null) {
//					i--;
//					continue;
//				}
//				
//				if(last == null) {
//					last = wire;
//					i--;
//					continue;
//				}
//				
//				if(!system.connectWire(last.getLogicObject(), wire.getLogicObject())) {
//					System.err.println("Failed to connect wires!");
//				}
//				
//				last = wire;
//				
//				rx = xp;
//				ry = yp;
//			}
			
			for(int i = 0; i < 100; i++) {
				int xp = rx++ + 1;
				int yp = ry + 0;
				
				LogicWireComponent wire = addWire(xp, yp);
				
				if(wire == null) {
					i--;
					continue;
				}
				
				if(last == null) {
					last = wire;
					i--;
					continue;
				}
				
				if(!system.connectWire(last.getLogicObject(), wire.getLogicObject())) {
					System.err.println("Failed to connect wires!");
				}
				
				last = wire;
				
				rx = xp;
				ry = yp;
			}
		}
		
		for(LogicWire wire : system.getWires()) {
			LogicWireComponent comp = new LogicWireComponent(wire);
			add(comp);
		}
	}
	
	private double zoom = 1;
	private double x;
	private double y;
	public void setWorldPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setWorldZoom(double zoom) {
		this.zoom = zoom;
	}
	
	public LogicSystem getSystem() {
		return system;
	}
	
	public LogicComponent getLogicComponent(double pwx, double pwy) {
		for(LogicComponent c : list) {
			if(c.containsPoint(pwx, pwy)) return c;
		}
		
		return null;
	}
	
	public List<LogicComponent> getLogicComponents() {
		return Collections.unmodifiableList(list);
	}
	
	public List<LogicComponent> getLogicComponents(Selection s) {
		List<LogicComponent> result = null;
		double x1 = s.getX();
		double y1 = s.getY();
		double x2 = x1 + s.getWidth();
		double y2 = y1 + s.getHeight();
		
		for(LogicComponent c : list) {
			int x = c.getX();
			int y = c.getY();
			
			if(!(x < x1 || x > x2 || y < y1 || y > y2)) {
				if(result == null) {
					result = new ArrayList<>();
				}
				
				result.add(c);
			}
		}
		
		return result == null ? Collections.emptyList():result;
	}
	
	public void setMouseCreate(LogicObjectType type) {
		if(type.isGate()) {
			listener.removeSelection();
			listener.setState(GridPanelListener.State.CREATE_GATE, type);
		}
		
		if(type == LogicObjectType.WIRE) {
			listener.removeSelection();
			listener.setState(GridPanelListener.State.CREATE_WIRE);
		}
	}
	
	protected void add(LogicComponent comp) {
		if(comp == null) return;
		list.add(comp);
	}
	
	private Selection selection;
	public void setSelection(Selection selection) {
		this.selection = selection;
	}
	
	@Override
	protected void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D)gr.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		g.setColor(colors.get(ColorPrefs.LOGIC_PANEL_BG));
		g.fillRect(0, 0, getWidth(), getHeight());
		
//		System.out.println("Comps: " + list.size());
//		System.out.printf("Grid: %s, %s, State: %s\n", system.hasObject(0), system.getLogicObject(0, -1), listener.getState());
		{
			g.setColor(Color.lightGray);
			int xp = (int)(x * zoom);
			int yp = (int)(y * zoom);
			g.drawLine((xp), 0, (xp), getHeight());
			g.drawLine(0, (yp), getWidth(), (yp));
		}
		
		g.scale(zoom, zoom);
		g.translate(x, y);
		
		int xs = (int)(-x);
		int xe = (int)(getWidth() / zoom);
		int ys = (int)(-y);
		int ye = (int)(getHeight() / zoom);
		
		Paint old_paint = g.getPaint();
		g.setPaint(DOTS_TEXTURE);
		g.fillRect(xs, ys, xe, ye);
		g.setPaint(old_paint);
		
		if(selection != null) {
			Color col = new Color(0, 0.3f, 0.7f, 0.3f);
			g.setColor(col);
			
			int x1 = (int)(selection.getX());
			int y1 = (int)(selection.getY());
			int x2 = (int)(selection.getWidth());
			int y2 = (int)(selection.getHeight());
			g.fillRect(x1, y1, x2, y2);
			g.drawRect(x1, y1, x2, y2);
		}
		
		g.dispose();
	}
	
	@Override
	protected void paintChildren(Graphics gr) {
		Graphics2D g = (Graphics2D)gr.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.scale(zoom, zoom);
		g.translate(x, y);
		
		super.paintChildren(g);
		
//		g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//		g.setColor(new Color(0x11bb77));
//		g.setColor(new Color(0x18ffaa).brighter());
//		for(LogicWire n1 : system.getWires()) {
//			if(n1 instanceof LogicGateWire) continue;
//			
//			g.fillOval(
//				n1.getX() * DOTS_SPACING - WIRE_RADIUS,
//				n1.getY() * DOTS_SPACING - WIRE_RADIUS,
//				WIRE_RADIUS * 2,
//				WIRE_RADIUS * 2
//			);
//		}
		
		Iterator<LogicComponent> iter = list.iterator();
		while(iter.hasNext()) {
			LogicComponent logic = iter.next();
			if(logic.getLogicObject().isRemoved()) {
				iter.remove();
				continue;
			}
			
			logic.paintComponent(g);
		}
		
		g.dispose();
	}

	public LogicWireComponent addWire(int plx, int ply) {
		LogicWire wire = system.addWire(plx, ply);
		if(wire == null) return null;
		
		LogicWireComponent comp = new LogicWireComponent(wire);
		add(comp);
		return comp;
	}
	
	public LogicGateComponent addGate(int plx, int ply, LogicObjectType type) {
		LogicGate gate = system.addGate(plx, ply, type);
		if(gate == null) return null;
		
		LogicGateComponent comp = new LogicGateComponent(gate);
		add(comp);
		return comp;
	}
}
