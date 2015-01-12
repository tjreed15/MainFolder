import acm.graphics.*;
import acm.program.*;
import java.awt.event.*;



public class timer extends GraphicsProgram{
	public void run(){
		addMouseListeners();
		addKeyListeners();
		clickCount = 0;
		count = new GLabel ("Clicks: ", 100, 200);
		hit = new GLabel ("Hits: " + 0, 100, 300);
		nHits = 0;
		GOval ball = new GOval (10, 10, 25, 25);
		ball.setFilled(true);
		add(ball);
		xVel = 3;
		yVel = -4;
		timeCount = 0;
		GLabel timer = new GLabel("Time: " + timeCount, 100, 100);
		add(timer);
		loopCount = 0;
		while(true){
			timeWise();
			ball.move(xVel, yVel);
			pause(20);
			if(((ball.getY() >= (getHeight() - 50)) && (yVel > 0)) || ((ball.getY() <= 0) && (yVel < 0))){
				yVel = -yVel;
			}
			if (((ball.getX() >= (getWidth() - 50)) && (xVel > 0)) || ((ball.getX() <= 0) && (xVel < 0))){
				xVel = -xVel;
			}
			if (true){
				remove(timer);
				timer = new GLabel ("Time: " + timeCount, 100, 100);
				add(timer);
			}
			loopCount++;
		}
	}
		
	private void timeWise(){
		if (loopCount % 32 == 0){
			timeCount++;
		}
		 
	}
	
	public void mousePressed(MouseEvent e){
		
	}
	
	public void mouseReleased(MouseEvent e){
		remove(count);
		clickCount++;
		count = new GLabel ("Clicks: " + clickCount, 100, 200);
			add(count);
		GObject gobj = getElementAt(e.getX(), e.getY());
		if (gobj != null){
			remove(hit);
			nHits++;
			hit = new GLabel ("Hits: " + nHits, 100, 300);
			add(hit);
			xVel = -xVel;
			yVel = -yVel;
		}
		else{
			xVel = xVel * 1.1;
		}
	}
	
	public void keyTyped(KeyEvent e){
		if (e.getKeyChar() == KeyEvent.VK_SPACE){
			restartGame();
		}
	}
	
	private void restartGame(){
		xVel = 3;
		yVel = -4;
		timeCount = 0;
		loopCount = 0;
		clickCount = 0;
		nHits = 0;
		remove(count);
		count = new GLabel ("Clicks: " + clickCount, 100, 200);
		add(count);
		remove(hit);
		hit = new GLabel ("Hits: " + nHits, 100, 300);
		add(hit);
	}
	
	
	private int timeCount, loopCount, nHits, clickCount;
	private GLabel hit, count;
		
	private double xVel, yVel;
	
}


	
	

