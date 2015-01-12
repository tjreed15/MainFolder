import acm.graphics.*;
import acm.program.*;

import java.awt.*;
import java.awt.event.*;

public class freeLance extends GraphicsProgram{
 
	private static final int HEAD_RADIUS = 25;
	private static final int MOVE_DISTANCE = 6;
	private static final int PAUSE_TIME = 25;
	private static final int MOUTH_ANGLE1 = 15;
	private static final int MOUTH_ANGLE2 = 30;
	private static final int MOUTH_ANGLE3 = 45;
	
	public void run(){
		addPac();
		addKeyListeners();
		while(true){
			if (moveLeft == true){
				pacMoveLeft();
				
			}
			if (moveRight == true){
				pacMan.move(MOVE_DISTANCE, 0);
			}
			if (moveUp == true){
				pacMan.move(0, -MOVE_DISTANCE);
			}
			if (moveDown == true){
				pacMan.move(0, MOVE_DISTANCE);
			}
			pause(PAUSE_TIME);
		}
	}
	
	private void addPac(){
		pacMan = new GArc (HEAD_RADIUS * 2, HEAD_RADIUS * 2, 15, 330);
		pacMan.setColor(Color.YELLOW);
		pacMan.setFilled(true);
		add(pacMan, 20, 20);
		
	}
	
	public void keyPressed(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_LEFT){
			checkLeft();
			if (leftIsClear){
				moveLeft = true;
				moveRight = false;
				moveUp = false;
				moveDown = false;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT){
			checkRight();
			if (rightIsClear){
				moveLeft = false;
				moveRight = true;
				moveUp = false;
				moveDown = false;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_UP){
			checkUp();
			if (upIsClear){
				moveLeft = false;
				moveRight = false;
				moveUp = true;
				moveDown = false;			
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN){
			checkDown();
			if (downIsClear){
				moveLeft = false;
				moveRight = false;
				moveUp = false;
				moveDown = true;
			}
		}
	}
	
	private void checkLeft(){
		double x = pacMan.getX() - 1;
		double y = pacMan.getY();
		GObject moveCheck = getElementAt(x, y);
		for (int i = 0; i < MOVE_DISTANCE; i++){
			if (moveCheck == null) moveCheck = getElementAt(x - 1 - i, y);
			else if (moveCheck.getColor() != Color.BLACK)moveCheck = getElementAt(x - 1 - i, y);
		}
		for (int i = 0; i < MOVE_DISTANCE; i++){
			if (moveCheck == null) moveCheck = getElementAt(x - 1 - i, y + (2 * HEAD_RADIUS));
			else if (moveCheck.getColor() != Color.BLACK)moveCheck = getElementAt(x - 1 - i, y + (2 * HEAD_RADIUS));
		}
		if (moveCheck == null) leftIsClear = true;
		else if (moveCheck.getColor() != Color.BLACK) leftIsClear = true;
		else leftIsClear = false;
	}
	
	private void checkRight(){
		double x = pacMan.getX() + (2*HEAD_RADIUS) + 1;
		double y = pacMan.getY();
		GObject moveCheck = getElementAt(x, y);
		for (int i = 0; i < MOVE_DISTANCE; i++){
			if (moveCheck == null) moveCheck = getElementAt(x + 1 + i, y);
			else if (moveCheck.getColor() != Color.BLACK)moveCheck = getElementAt(x + 1 + i, y);
		}
		for (int i = 0; i < MOVE_DISTANCE; i++){
			if (moveCheck == null) moveCheck = getElementAt(x + 1 + i, y + (2 * HEAD_RADIUS));
			else if (moveCheck.getColor() != Color.BLACK)moveCheck = getElementAt(x + 1 + i, y + (2 * HEAD_RADIUS));
		}
		if (moveCheck == null) rightIsClear = true;
		else if (moveCheck.getColor() != Color.BLACK) rightIsClear = true;
		else rightIsClear = false;
	}
	
	private void checkUp(){
		double x = pacMan.getX();
		double y = pacMan.getY() - 1;
		GObject moveCheck = getElementAt(x, y);
		for (int i = 0; i < MOVE_DISTANCE; i++){
			if (moveCheck == null) moveCheck = getElementAt(x, y - 1 -i);
			else if (moveCheck.getColor() != Color.BLACK)moveCheck = getElementAt(x, y - 1 -i);
		}
		for (int i = 0; i < MOVE_DISTANCE; i++){
			if (moveCheck == null) moveCheck = getElementAt(x + (2 * HEAD_RADIUS), y - 1 -i);
			else if (moveCheck.getColor() != Color.BLACK)moveCheck = getElementAt(x + (2 * HEAD_RADIUS), y - 1 -i);
		}
		if (moveCheck == null) upIsClear = true;
		else if (moveCheck.getColor() != Color.BLACK) upIsClear = true;
		else upIsClear = false;
	}
	
	private void checkDown(){
		double x = pacMan.getX();
		double y = pacMan.getY() + 1 + (2*HEAD_RADIUS);
		GObject moveCheck = getElementAt(x, y);
		for (int i = 0; i < MOVE_DISTANCE; i++){
			if (moveCheck == null) moveCheck = getElementAt(x, y + 1 + i);
			else if (moveCheck.getColor() != Color.BLACK)moveCheck = getElementAt(x, y + 1 +i);
		}
		for (int i = 0; i < MOVE_DISTANCE; i++){
			if (moveCheck == null) moveCheck = getElementAt(x + (2 * HEAD_RADIUS), y + 1 + i);
			else if (moveCheck.getColor() != Color.BLACK)moveCheck = getElementAt(x + (2 * HEAD_RADIUS), y + 1 +i);
		}
		if (moveCheck == null) downIsClear = true;
		else if (moveCheck.getColor() != Color.BLACK) downIsClear = true;
		else downIsClear = false;
	}
	
	private void openMouth(int i){
		switch (i){
		case 0: pacMan.setStartAngle(MOUTH_ANGLE2); pacMan.setSweepAngle(360 - (2 * MOUTH_ANGLE2)); break;
		case 1: pacMan.setStartAngle(MOUTH_ANGLE1); pacMan.setSweepAngle(360 - (2 * MOUTH_ANGLE1)); break;
		case 2: pacMan.setStartAngle(0); pacMan.setSweepAngle(360); break;
		case 3: pacMan.setStartAngle(MOUTH_ANGLE1); pacMan.setSweepAngle(360 - (2 * MOUTH_ANGLE1)); break;
		case 4: pacMan.setStartAngle(MOUTH_ANGLE2); pacMan.setSweepAngle(360 - (2 * MOUTH_ANGLE2)); break;
		case 5: pacMan.setStartAngle(MOUTH_ANGLE3); pacMan.setSweepAngle(360 - (2 * MOUTH_ANGLE3)); break;
		default: pacMan.setStartAngle(MOUTH_ANGLE3); pacMan.setSweepAngle(360 - (2 * MOUTH_ANGLE3)); break;
		}
		
		
	}
	
	private void pacMoveLeft(){
		GLabel test = new GLabel ("test", 100, 100);
		add(test);
		checkLeft();
		if (leftIsClear){
			for(int i = 0; i < MOVE_DISTANCE; i++){
				pacMan.move(-1, 0);
				openMouth(i);
			}
		}
		
	}
	
	private GArc pacMan;
	private boolean moveLeft, leftIsClear, moveRight, rightIsClear, moveUp, upIsClear, moveDown, downIsClear;
}
