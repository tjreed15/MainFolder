import acm.graphics.*;
import acm.program.*;
import java.awt.*;
import java.awt.event.*;

public class PaintProgram extends GraphicsProgram {

	private boolean drawErase;
	private boolean drawPoint;
	private boolean drawLine;
	private boolean drawTriangle;
	private boolean drawRect;
	private boolean drawPentagon;
	private boolean drawHexagon;
	private boolean drawSeptagon;
	private boolean drawOctagon;
	private boolean drawOval;
	
	private boolean shapeFilled;
	private boolean dragShape;
	private boolean shift;
	
	private int NSHAPES_REM = 100;
	private GObject[] drawn = new GObject[NSHAPES_REM];
	private int nShapesDrawn;
	
	
	public void run(){
		setShape();
		drawOval = true;
		beingDrawn = null;
		for (int i = 0; i<NSHAPES_REM; i++){
			drawn[i] = null;
		}
		nShapesDrawn = -1;
		addMouseListeners();
		addKeyListeners();
	}
	
	private void setShape(){
		drawErase = false;
		drawPoint = false;
		drawLine = false;
		drawTriangle = false;
		drawRect = false;
		drawPentagon = false;
		drawHexagon = false;
		drawSeptagon = false;
		drawOctagon = false;
		drawOval = false;
		dragShape = false;
	}
	
			public void mousePressed(MouseEvent e){
				last = new GPoint(e.getPoint());
				topLeft = new GPoint(e.getPoint());	
				if(dragShape) beingDrawn = getElementAt(e.getX(), e.getY());
			}
			
			public void mouseDragged(MouseEvent e){
				if(dragShape && beingDrawn != null) beingDrawn.move(e.getX() - last.getX(), e.getY() - last.getY());
				else drawShape();
				last = new GPoint(e.getPoint());
			}
			
			public void mouseReleased(MouseEvent e){
				nShapesDrawn++;
				drawn[nShapesDrawn] = beingDrawn;
				beingDrawn = null;
			}
			
			/**
			public void mouseMoved(MouseEvent e){
				remove(stencilType);
				if (drawPoint)stencilType = new GLabel("Draw");
				if (drawLine)stencilType = new GLabel("Line");
				if (drawTriangle)stencilType = new GLabel("Tri");
				if (drawRect)stencilType = new GLabel("Rect");
				if (drawPentagon)stencilType = new GLabel("Pent");
				if (drawHexagon)stencilType = new GLabel("Hex");
				if (drawSeptagon)stencilType = new GLabel("Sept");
				if (drawOctagon)stencilType = new GLabel("Oct");
				if (drawOval)stencilType = new GLabel("Oval");
				add(stencilType, e.getX(), e.getY());
			}
	*/
			public void keyTyped(KeyEvent e){
				setShape();
				switch (e.getKeyChar()){
					case KeyEvent.VK_0: drawErase = true; break;
					case KeyEvent.VK_1: drawPoint = true; break;
					case KeyEvent.VK_2: drawLine = true; break;
					case KeyEvent.VK_3: drawTriangle = true; break;
					case KeyEvent.VK_4: drawRect = true; break;
					case KeyEvent.VK_5: drawPentagon = true; break;
					case KeyEvent.VK_6: drawHexagon = true; break;
					case KeyEvent.VK_7: drawSeptagon = true; break;
					case KeyEvent.VK_8: drawOctagon = true; break;
					case KeyEvent.VK_9: drawOval = true; break;
					default: drawErase = true; break;
				}
				if (e.getKeyChar() == KeyEvent.VK_F){
					if(shapeFilled) shapeFilled = false;
					else shapeFilled = true;
				}
				if(e.getKeyChar() == KeyEvent.VK_Z){
					if (nShapesDrawn >= 0){
						remove(drawn[nShapesDrawn]);
						nShapesDrawn--;
					}
				}
				if(e.getKeyChar() == KeyEvent.VK_S){
					setShape();
					dragShape = true;
				}
			}
	
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_SHIFT){
					shift = true;
				}
			}
			
			public void keyReleased(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_SHIFT){
					shift = false;
				}
			}
	
	
	private void drawShape(){
		double width = last.getX() - topLeft.getX();
		double height = last.getY() - topLeft.getY();
		double x = topLeft.getX();
		double y = topLeft.getY();
		double min = (Math.abs(width) <= Math.abs(height))? Math.abs(width):Math.abs(height);
		if (width < 0) {
			width *= -1;
			x = topLeft.getX() - width;
			if (shift) width = -min;
		}
		else if (shift) width = min;
		if (height < 0) {
			height *= -1;
			y = topLeft.getY() - height;
			if(shift) height = -min;
		}
		else if (shift) height = min;
		
		if (beingDrawn != null)remove(beingDrawn);
		if (drawPoint)removeAll();
		if (drawLine)addLine();
		if (drawTriangle)addTriangle(x, y, width, height);
		if (drawRect)addRect(x, y, width, height);
		if (drawPentagon)addPent(x, y, width, height);
		if (drawHexagon)addHex(x, y, width, height);
		if (drawSeptagon)addSept(x, y, width, height);
		if (drawOctagon)addOct(x, y, width, height);
		if (drawOval)addOval(x, y, width, height);
		
	}
	
	private void addLine(){
		double x = last.getX();
		double y = last.getY();
		int negX = 1;
		int negY = 1;
		if (shift){
			if (Math.abs(Math.abs(topLeft.getX()-x)-Math.abs(topLeft.getY()-y)) < 25){
				if (x<0 && y>0) negX = -1;
				if (x>0 && y<0) negY = -1;
				x = (Math.abs(y)<Math.abs(x))? negY*y:x;
				y = (Math.abs(y)<Math.abs(x))? y:negY*x;
			}
			else{
				if (Math.abs(topLeft.getY()-y)<Math.abs(topLeft.getX()-x)) y = topLeft.getY();
				else x = topLeft.getX();
			}
		}
		beingDrawn = new GLine(topLeft.getX(), topLeft.getY(), x, y);
		add(beingDrawn);
	}
	
	private void addTriangle(double x, double y, double width, double height){
		GShape tri = new GShape(width, height);
		beingDrawn = tri.triangle;
		add(beingDrawn, x, y);
	}
	
	private void addRect(double x, double y, double width, double height){
		beingDrawn = new GRect(x, y, width, height);
		if(shapeFilled) ((GRect) beingDrawn).setFilled(true);
		add(beingDrawn);
	}
	
	private void addPent(double x, double y, double width, double height){
		GShape tri = new GShape(width, height);
		beingDrawn = tri.pentagon;
		add(beingDrawn, x, y);
	}
	
	private void addHex(double x, double y, double width, double height){
		GShape tri = new GShape(width, height);
		beingDrawn = tri.hexagon;
		add(beingDrawn, x, y);
	}
	
	private void addSept(double x, double y, double width, double height){
		GShape tri = new GShape(width, height);
		beingDrawn = tri.septagon;
		add(beingDrawn, x, y);
	}
	
	private void addOct(double x, double y, double width, double height){
		GShape tri = new GShape(width, height);
		beingDrawn = tri.octagon;
		add(beingDrawn, x, y);
	}
	
	private void addOval(double x, double y, double width, double height){
		beingDrawn = new GOval(x, y, width, height);
		if(shapeFilled) ((GOval) beingDrawn).setFilled(true);
		add(beingDrawn);
	}
	
	
	private GPoint last, topLeft;
	private GObject beingDrawn;
	
}
