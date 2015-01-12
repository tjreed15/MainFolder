import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.*;

public class MineSweeper extends GraphicsProgram {
	
	private static final int NBOMBS = 10;
	private static final int BOMB_RADIUS = 5;
	
	private static final int COLUMN_WIDTH = (BOMB_RADIUS * 2) + 20;
	private static final int ROW_HEIGHT = COLUMN_WIDTH;
	
/** Max Columns: 40	Max Rows: 22	*/	
	private static final int NCOLUMNS = 10;
	private static final int NROWS = 10;
	
	private static final int BOARD_WIDTH =  (NCOLUMNS * COLUMN_WIDTH);
	private static final int BOARD_HEIGHT = (NROWS * ROW_HEIGHT);
	
	private static final Color BOMB1_COLOR = Color.BLUE;
	private static final Color BOMB2_COLOR = Color.RED;
	private static final Color BOMB3_COLOR = Color.BLACK;
	private static final Color BOMB4_COLOR = Color.MAGENTA;
	private static final Color BOMB5_COLOR = Color.GRAY;
	private static final Color BOMB6_COLOR = Color.CYAN;
	private static final Color BOMB7_COLOR = Color.ORANGE;
	private static final Color BOMB8_COLOR = Color.GREEN;
	
	
	
	
	public void run(){
		for (int i = 0; i < 1; i ++){
			gameOver = false;
			loss = false;
			while (getWidth() < BOARD_WIDTH || getHeight() < BOARD_HEIGHT){
				err = new GLabel("Error: Screen Too Small");
				double a = (getWidth() - err.getWidth()) / 2;
				double b = (getHeight() - err.getHeight()) / 2;
				add(err, a, b);
				waitForClick();
				remove(err);
			}
			setUpBoard();
			addMouseListeners();
			addKeyListeners();
			addTimer();
			while (restartBoolean == false){
				if (restartBoolean == true){
					i--;
					restartGame();
					break;
				}
			}
			i--;
			restartGame();
		}	
	}
	
	private void setUpBoard(){
		setUpScreen();
		addRows();
		addCollumns();
		addBombs();
		generateLabels();
		addCoverColumns();
	}
	
	private void setUpScreen(){
		col = new GLabel ("C: ");
		add(col, 10, 10);
		row = new GLabel ("R: ");
		add(row, 10, 50);
		double x = (getWidth() - BOARD_WIDTH) / 2;
		double y = (getHeight() - BOARD_HEIGHT) / 2;
		topLeft = new GPoint(x, y);
		restartBoolean = false;
	}

	private void addRows(){
		for (int i = 0; i < (NCOLUMNS + 1); i++){
			double x = (topLeft.getX() + (i * COLUMN_WIDTH));
			double y = (topLeft.getY());
			GLine col = new GLine (x, y, x, (y + BOARD_HEIGHT));
			add(col);
		}
	}
	
	private void addCollumns(){
		for (int i = 0; i < (NROWS + 1); i++){
			double x = (topLeft.getX());
			double y = (topLeft.getY() + (i * ROW_HEIGHT));
			GLine row = new GLine (x, y, (x + BOARD_WIDTH), y);
			add(row);
		}
	}
	
	private void addCoverColumns(){
		for (int i = 0; i < NROWS; i++){
			double y = topLeft.getY() + (i * ROW_HEIGHT);			
			addCoverRow(y);
		}		
	}
	
	private void addCoverRow(double y){
		for (int i = 0; i < NCOLUMNS; i++){
			double x = (topLeft.getX() + (i * COLUMN_WIDTH));
			cover = new GRect(x, y, COLUMN_WIDTH, ROW_HEIGHT);
			cover.setFilled(true);
			cover.setFillColor(Color.GRAY);
			add(cover);
		}
	}
	
	private void addBombs(){
		for(int i = 0; i < NBOMBS; i++){
			int rowNumber = rgen.nextInt(0, (NROWS - 1));
			int colNumber = rgen.nextInt(0, (NCOLUMNS - 1));
			double x = (topLeft.getX() + (COLUMN_WIDTH * colNumber)) + (COLUMN_WIDTH / 2);
			double y = (topLeft.getY() + ROW_HEIGHT * rowNumber) + (ROW_HEIGHT / 2);
			gobj = getElementAt(x, y);
			if(gobj != null)i--;
			else addBomb(x, y);
		}
	}
	
	private void addBomb(double x, double y){
		double a = x - BOMB_RADIUS;
		double b = y - BOMB_RADIUS;
		GOval bomb = new GOval(a, b, (2 * BOMB_RADIUS), (2 * BOMB_RADIUS));
		bomb.setFilled(true);
		add(bomb);
	}
	
	private void generateLabels(){
		for (int i = 0; i < NCOLUMNS; i++){
			generateColumnLabels(i);
		}
	}
	
	private void generateColumnLabels(int j){
		for (int i = 0; i < NROWS; i++){
			double centerX = topLeft.getX() + (COLUMN_WIDTH / 2);
			double centerY = topLeft.getY() + (ROW_HEIGHT / 2);
			double checkX = centerX + (j * COLUMN_WIDTH);
			double checkY = centerY + (i * ROW_HEIGHT);
			checkSurroundingSquares(checkX, checkY);
		}
	}
	
	private void checkSurroundingSquares(double x, double y){
		nSurroundingBombs = 0;
		checkUp(x, y);
		checkDown(x, y);
		checkLeft(x, y);
		checkRight(x, y);
		checkUpLeft(x, y);
		checkUpRight(x, y);
		checkDownLeft(x, y);
		checkDownRight(x, y);
		printNumberOfBombs(x, y);
	}
	
	private void checkUp(double x, double y){
		gobj3 = getElementAt(x + BOMB_RADIUS - 1, (y - ROW_HEIGHT));
		if (gobj3 != null){
			nSurroundingBombs++;
		}
	}
	
	private void checkDown(double x, double y){
		gobj3 = getElementAt(x + BOMB_RADIUS - 1, (y + ROW_HEIGHT));
		if (gobj3 != null){
			nSurroundingBombs++;
		}
	}
	
	private void checkRight(double x, double y){
		gobj3 = getElementAt((x + COLUMN_WIDTH + BOMB_RADIUS - 1), y);
		if (gobj3 != null){
			nSurroundingBombs++;
		}
	}
	
	private void checkLeft(double x, double y){
		gobj3 = getElementAt((x - COLUMN_WIDTH + BOMB_RADIUS - 1), y);
		if (gobj3 != null){
			nSurroundingBombs++;
		}
	}
	
	private void checkUpLeft(double x, double y){
		gobj3 = getElementAt((x - COLUMN_WIDTH + BOMB_RADIUS - 1), (y - ROW_HEIGHT));
		if (gobj3 != null){
			nSurroundingBombs++;
		}
	}
	
	private void checkUpRight(double x, double y){
		gobj3 = getElementAt((x + COLUMN_WIDTH + BOMB_RADIUS - 1), (y - ROW_HEIGHT));
		if (gobj3 != null){
			nSurroundingBombs++;
		}
	}
	
	private void checkDownLeft(double x, double y){
		gobj3 = getElementAt((x - COLUMN_WIDTH + BOMB_RADIUS - 1), (y + ROW_HEIGHT));
		if (gobj3 != null){
			nSurroundingBombs++;
		}
	}
	
	private void checkDownRight(double x, double y){
		gobj3 = getElementAt((x + COLUMN_WIDTH + BOMB_RADIUS - 1), (y + ROW_HEIGHT));
		if (gobj3 != null){
			nSurroundingBombs++;
		}
	}
	
	private void printNumberOfBombs (double x, double y){
		gobjCheck = getElementAt(x, y);
		if (nSurroundingBombs == 0);
		else if (gobjCheck == null){
			GLabel surround = new GLabel("" + nSurroundingBombs);
			double a = x - (surround.getWidth() / 2);
			double b = y + (surround.getHeight() / 2);
			switch (nSurroundingBombs){
				case 1: surround.setColor(BOMB1_COLOR); break;
				case 2: surround.setColor(BOMB2_COLOR); break;
				case 3: surround.setColor(BOMB3_COLOR); break;
				case 4: surround.setColor(BOMB4_COLOR); break;
				case 5: surround.setColor(BOMB5_COLOR); break;
				case 6: surround.setColor(BOMB6_COLOR); break;
				case 7: surround.setColor(BOMB7_COLOR); break;
				case 8: surround.setColor(BOMB8_COLOR); break;
			}
			add(surround, a, b);
		}
	}
	
	public void mouseClicked(MouseEvent e){
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && gameOver == false){
			getColumnClicked(e.getX(), e.getY());
			getRowClicked(e.getX(), e.getY());
			removeNCovers(colNumber, rowNumber);
		}
		else if (e.getButton() == MouseEvent.BUTTON1 && gameOver == false){
			if (e.getX() > topLeft.getX() && e.getX() < (topLeft.getX() + BOARD_WIDTH)){
				gobj1 = getElementAt(e.getX(), e.getY());
				if (gobj1 != null && gobj1.getColor() != Color.YELLOW && gobj1.getWidth() == COLUMN_WIDTH && gobj1.getHeight() == ROW_HEIGHT){
					remove(gobj1);
					getColumnClicked(e.getX(), e.getY());
					getRowClicked(e.getX(), e.getY());
					checkSquareClicked(colNumber, rowNumber);
				}	
			}
		}
		else if(e.getButton() == MouseEvent.BUTTON3 && gameOver == false){
			if (e.getX() > topLeft.getX() && e.getX() < (topLeft.getX() + BOARD_WIDTH)){
				gobj1 = getElementAt(e.getX(), e.getY());
				if (gobj1.getWidth() == COLUMN_WIDTH && gobj1.getHeight() == ROW_HEIGHT){
					if(gobj1.getColor() == Color.YELLOW){
						remove(gobj1);
						cover = new GRect (gobj1.getX(), gobj1.getY(), COLUMN_WIDTH, ROW_HEIGHT);
						cover.setFilled(true);
						cover.setFillColor(Color.GRAY);
						add(cover);
					}
					else{
						remove(gobj1);
						cover = new GRect (gobj1.getX(), gobj1.getY(), COLUMN_WIDTH, ROW_HEIGHT);
						cover.setFilled(true);
						cover.setColor(Color.YELLOW);
						add(cover);
					}
				}
			}
		}
		checkForWin();
	}
	
	private void getColumnClicked(double x, double y){
		double margin = topLeft.getX();
		double dif = (x - margin) % COLUMN_WIDTH;
		double dist = x - dif;
		colNumber = ((dist - topLeft.getX()) / COLUMN_WIDTH);
		remove(col);
		col = new GLabel ("C: " + colNumber, 10, 10);
		add(col);
	}
	
	private void getRowClicked(double x, double y){
		double margin = topLeft.getY();
		double dif = (y - margin) % ROW_HEIGHT;
		double dist = y - dif;
		rowNumber = ((dist - topLeft.getY()) / ROW_HEIGHT);
		remove(row);
		row = new GLabel ("R: " + rowNumber, 10, 50);
		add(row);
	}
	
	private void checkSquareClicked(double column, double row){
		double centerX = topLeft.getX() + (column * COLUMN_WIDTH) + (COLUMN_WIDTH / 2);
		double centerY = topLeft.getY() + (row * ROW_HEIGHT) + (ROW_HEIGHT / 2);
		gobj1 = getElementAt(centerX, centerY);
		if(gobj1 == null)clearSpace(centerX, centerY);
		else if (gobj1.getWidth() == (2 * BOMB_RADIUS)){
			gobj1.setColor(Color.RED);
			remove(gobj1);
			add(gobj1);
			gameOver = true;
			showAllBombs();
			youLose();
		}
		
	}
	
	private void removeNCovers(double column, double row){
		double centerX = topLeft.getX() + (column * COLUMN_WIDTH) + (COLUMN_WIDTH / 2);
		double centerY = topLeft.getY() + (row * ROW_HEIGHT) + (ROW_HEIGHT / 2);
		gobj1 = getElementAt(centerX, centerY);
		if(gobj1.getClass() == GLabel.class){
			int nBombs = getNSurroundingBombs();
			getNSurroundingMarks(centerX, centerY);
			if (nBombs == nSurroundingMarks){
				clearSpace(centerX, centerY);
			}
		}
	}
	
	private int getNSurroundingBombs(){
		if (gobj1.getColor() == BOMB1_COLOR) return 1;
		if (gobj1.getColor() == BOMB2_COLOR) return 2;
		if (gobj1.getColor() == BOMB3_COLOR) return 3;
		if (gobj1.getColor() == BOMB4_COLOR) return 4;
		if (gobj1.getColor() == BOMB5_COLOR) return 5;
		if (gobj1.getColor() == BOMB6_COLOR) return 6;
		if (gobj1.getColor() == BOMB7_COLOR) return 7;
		if (gobj1.getColor() == BOMB8_COLOR) return 8;
		return 0;
	}
	
	private void getNSurroundingMarks(double x, double y){
		nSurroundingMarks = 0;
		checkMarkUp(x, y);
		checkMarkDown(x, y);
		checkMarkLeft(x, y);
		checkMarkRight(x, y);
		checkMarkUpLeft(x, y);
		checkMarkUpRight(x, y);
		checkMarkDownLeft(x, y);
		checkMarkDownRight(x, y);
	}
	
	private void checkMarkUp(double x, double y){
		gobj3 = getElementAt(x + BOMB_RADIUS - 1, (y - ROW_HEIGHT));
		if (gobj3 != null && gobj3.getColor() == Color.YELLOW && gobj3.getWidth() == COLUMN_WIDTH && gobj3.getHeight() == ROW_HEIGHT){
			nSurroundingMarks++;
		}
	}
	
	private void checkMarkDown(double x, double y){
		gobj3 = getElementAt(x + BOMB_RADIUS - 1, (y + ROW_HEIGHT));
		if (gobj3 != null && gobj3.getColor() == Color.YELLOW && gobj3.getWidth() == COLUMN_WIDTH && gobj3.getHeight() == ROW_HEIGHT){
			nSurroundingMarks++;
		}
	}
	
	private void checkMarkRight(double x, double y){
		gobj3 = getElementAt((x + COLUMN_WIDTH + BOMB_RADIUS - 1), y);
		if (gobj3 != null && gobj3.getColor() == Color.YELLOW && gobj3.getWidth() == COLUMN_WIDTH && gobj3.getHeight() == ROW_HEIGHT){
			nSurroundingMarks++;
		}
	}
	
	private void checkMarkLeft(double x, double y){
		gobj3 = getElementAt((x - COLUMN_WIDTH + BOMB_RADIUS - 1), y);
		if (gobj3 != null && gobj3.getColor() == Color.YELLOW && gobj3.getWidth() == COLUMN_WIDTH && gobj3.getHeight() == ROW_HEIGHT){
			nSurroundingMarks++;
		}
	}
	
	private void checkMarkUpLeft(double x, double y){
		gobj3 = getElementAt((x - COLUMN_WIDTH + BOMB_RADIUS - 1), (y - ROW_HEIGHT));
		if (gobj3 != null && gobj3.getColor() == Color.YELLOW && gobj3.getWidth() == COLUMN_WIDTH && gobj3.getHeight() == ROW_HEIGHT){
			nSurroundingMarks++;
		}
	}
	
	private void checkMarkUpRight(double x, double y){
		gobj3 = getElementAt((x + COLUMN_WIDTH + BOMB_RADIUS - 1), (y - ROW_HEIGHT));
		if (gobj3 != null && gobj3.getColor() == Color.YELLOW && gobj3.getWidth() == COLUMN_WIDTH && gobj3.getHeight() == ROW_HEIGHT){
			nSurroundingMarks++;
		}
	}
	
	private void checkMarkDownLeft(double x, double y){
		gobj3 = getElementAt((x - COLUMN_WIDTH + BOMB_RADIUS - 1), (y + ROW_HEIGHT));
		if (gobj3 != null && gobj3.getColor() == Color.YELLOW && gobj3.getWidth() == COLUMN_WIDTH && gobj3.getHeight() == ROW_HEIGHT){
			nSurroundingMarks++;
		}
	}
	
	private void checkMarkDownRight(double x, double y){
		gobj3 = getElementAt((x + COLUMN_WIDTH + BOMB_RADIUS - 1), (y + ROW_HEIGHT));
		if (gobj3 != null && gobj3.getColor() == Color.YELLOW && gobj3.getWidth() == COLUMN_WIDTH && gobj3.getHeight() == ROW_HEIGHT){
			nSurroundingMarks++;
		}
	}
	
	
	private void clearSpace(double x, double y){
		clearUp(x, y);
		clearDown(x, y);
		clearLeft(x, y);
		clearRight(x, y);
		clearUpLeft(x, y);
		clearDownLeft(x, y);
		clearDownRight(x, y);
		clearUpRight(x, y);
		
	}
	
	private void clearUp(double x, double y){
		gobjUp = getElementAt((x + BOMB_RADIUS + 1), (y - ROW_HEIGHT));
		if (gobjUp != null && gobjUp.getColor() != Color.YELLOW){
			remove(gobjUp);
			gobj2 = getElementAt(x, y - ROW_HEIGHT);
			if (gobj2 == null) clearSpace(x, y - ROW_HEIGHT);
			else if (gobj2 != null && gobj2.getClass() == GOval.class){
				gobj2.setColor(Color.RED);
				remove(gobj2);
				add(gobj2);
				gameOver = true;
				showAllBombs();
				youLose();
			}
		}
	}
	
	private void clearDown(double x, double y){
		gobjDown = getElementAt((x + BOMB_RADIUS + 1), (y + ROW_HEIGHT));
		if(gobjDown != null && gobjDown.getColor() != Color.YELLOW){
			remove(gobjDown);
			gobj2 = getElementAt(x, y + ROW_HEIGHT);
			if (gobj2 == null)clearSpace(x, y + ROW_HEIGHT);
			else if (gobj2 != null && gobj2.getClass() == GOval.class){
				gobj2.setColor(Color.RED);
				remove(gobj2);
				add(gobj2);
				gameOver = true;
				showAllBombs();
				youLose();
			}
		}
	}
	
	private void clearLeft(double x, double y){
		gobjLeft = getElementAt((x - COLUMN_WIDTH + BOMB_RADIUS + 1), y);
		if (gobjLeft != null && gobjLeft.getColor() != Color.YELLOW){
			remove(gobjLeft);
			gobj2 = getElementAt(x - COLUMN_WIDTH, y);
			if (gobj2 == null)clearSpace(x - COLUMN_WIDTH, y);
			else if (gobj2 != null && gobj2.getClass() == GOval.class){
				gobj2.setColor(Color.RED);
				remove(gobj2);
				add(gobj2);
				gameOver = true;
				showAllBombs();
				youLose();
			}
		}
	}
	
	private void clearRight(double x, double y){
		gobjRight = getElementAt((x + COLUMN_WIDTH + BOMB_RADIUS + 1), y);
		if (gobjRight != null && gobjRight.getColor() != Color.YELLOW) {
			remove(gobjRight);
			gobj2 = getElementAt(x + COLUMN_WIDTH, y);
			if (gobj2 == null) clearSpace(x + COLUMN_WIDTH, y);
			else if (gobj2 != null && gobj2.getClass() == GOval.class){
				gobj2.setColor(Color.RED);
				remove(gobj2);
				add(gobj2);
				gameOver = true;
				showAllBombs();
				youLose();
			}
		}	
	}
	
	private void clearUpLeft(double x, double y){
		gobjUpLeft = getElementAt((x - COLUMN_WIDTH + BOMB_RADIUS + 1), (y - ROW_HEIGHT));
		if (gobjUpLeft != null && gobjUpLeft.getColor() != Color.YELLOW){
			remove(gobjUpLeft);
			gobj2 = getElementAt(x - COLUMN_WIDTH, y - ROW_HEIGHT);
			if (gobj2 == null)clearSpace(x - COLUMN_WIDTH, y - ROW_HEIGHT);
			else if (gobj2 != null && gobj2.getClass() == GOval.class){
				gobj2.setColor(Color.RED);
				remove(gobj2);
				add(gobj2);
				gameOver = true;
				showAllBombs();
				youLose();
			}
		}
	}
	
	private void clearUpRight(double x, double y){
		gobjUpRight = getElementAt((x + COLUMN_WIDTH + BOMB_RADIUS + 1), (y - ROW_HEIGHT));
		if (gobjUpRight != null && gobjUpRight.getColor() != Color.YELLOW) {
			remove(gobjUpRight);
			gobj2 = getElementAt(x + COLUMN_WIDTH, y - ROW_HEIGHT);
			if (gobj2 == null)clearSpace(x + COLUMN_WIDTH, y - ROW_HEIGHT);
			else if (gobj2 != null && gobj2.getClass() == GOval.class){
				gobj2.setColor(Color.RED);
				remove(gobj2);
				add(gobj2);
				gameOver = true;
				showAllBombs();
				youLose();
			}
		}
	}
	
	private void clearDownRight(double x, double y){
		gobjDownRight = getElementAt((x + COLUMN_WIDTH + BOMB_RADIUS + 1), (y + ROW_HEIGHT));
		if (gobjDownRight != null && gobjDownRight.getColor() != Color.YELLOW) {
			remove(gobjDownRight);
			gobj2 = getElementAt(x + COLUMN_WIDTH, y + ROW_HEIGHT);
			if (gobj2 == null)clearSpace(x + COLUMN_WIDTH, y + ROW_HEIGHT);
			else if (gobj2 != null && gobj2.getClass() == GOval.class){
				gobj2.setColor(Color.RED);
				remove(gobj2);
				add(gobj2);
				gameOver = true;
				showAllBombs();
				youLose();
			}
		}
	}
	
	private void clearDownLeft(double x, double y){
		gobjDownLeft = getElementAt((x - COLUMN_WIDTH + BOMB_RADIUS + 1), (y + ROW_HEIGHT));
		if (gobjDownLeft != null && gobjDownLeft.getColor() != Color.YELLOW) {
			remove(gobjDownLeft);
			gobj2 = getElementAt(x - COLUMN_WIDTH, y + ROW_HEIGHT);
			if(gobj2 == null)clearSpace(x - COLUMN_WIDTH, y + ROW_HEIGHT);
			else if (gobj2 != null && gobj2.getClass() == GOval.class){
				gobj2.setColor(Color.RED);
				remove(gobj2);
				add(gobj2);
				gameOver = true;
				showAllBombs();
				youLose();
			}
		}
	}
	
	private void checkForWin(){
		nCovers = 0;
		for (int i = 0; i < NCOLUMNS; i++){
			checkColumnsForWin(i);
		}
		if (nCovers == NBOMBS){
			youWin();
			gameOver = true;
		}
	}
	
	private void checkColumnsForWin(int j){
		for (int i = 0; i < NROWS; i++){
			double centerX = topLeft.getX() + (COLUMN_WIDTH / 2);
			double centerY = topLeft.getY() + (ROW_HEIGHT / 2);
			double checkX = centerX + (j * COLUMN_WIDTH);
			double checkY = centerY + (i * ROW_HEIGHT);
			gobj2 = getElementAt(checkX, checkY);
			if (gobj2 != null && gobj2.getWidth() == COLUMN_WIDTH && gobj2.getHeight() == ROW_HEIGHT){
				nCovers++;
			}
		}
	}
	
	private void youWin(){
		if (loss == false){
			GLabel winner = new GLabel ("YOU WIN!");
			double x = (getWidth() - winner.getWidth()) / 2;
			double y = (topLeft.getY() + winner.getHeight()) / 2;
			GRect done = new GRect ((x - 2), ((y - winner.getHeight())), (winner.getWidth() + 4), (winner.getHeight() + 4));
			done.setFilled(true);
			done.setFillColor(Color.WHITE);
			add(done);
			add(winner, x, y);
		}
	}
	
	private void youLose(){
		loss = true;
		GLabel loser = new GLabel ("YOU LOSE!");
		double x = (getWidth() - loser.getWidth()) / 2;
		double y = (topLeft.getY() + loser.getHeight()) / 2;
		GRect done = new GRect ((x - 2), ((y - loser.getHeight())), (loser.getWidth() + 4), (loser.getHeight() + 4));
		done.setFilled(true);
		done.setFillColor(Color.WHITE);
		add(done);
		add(loser, x, y);
	}
	
	
	private void showAllBombs(){
		for (int i = 0; i < NCOLUMNS; i++){
			checkColumnsForBombs(i);
		}
	}
	
	private void checkColumnsForBombs(int j){
		for (int i = 0; i < NROWS; i++){
			double centerX = topLeft.getX() + (COLUMN_WIDTH / 2);
			double centerY = topLeft.getY() + (ROW_HEIGHT / 2);
			double checkX = centerX + (j * COLUMN_WIDTH);
			double checkY = centerY + (i * ROW_HEIGHT);
			gobj = getElementAt(checkX, checkY);
			if (gobj != null && gobj.getWidth() == COLUMN_WIDTH && gobj.getHeight() == ROW_HEIGHT){
				remove(gobj);
				gobj2 = getElementAt(checkX, checkY);
				if(gobj2 == null)add(gobj);
				if (gobj2 != null && gobj2.getWidth() != (2 * BOMB_RADIUS)) add(gobj);
			}
		}
	}
	
	private void addTimer(){
		timeCount = 0;
		while(gameOver == false){
			count = new GLabel("time: " + timeCount);
			add(count, topLeft.getX() - count.getWidth() - 5, 300);
			if (restartBoolean == true)break;
			pause(1000);
			if (restartBoolean == true)break;
			remove(count);
			timeCount++;
			
		}
		add(count);
	}


	
	public void keyTyped (KeyEvent e){
		if (e.getKeyChar() == KeyEvent.VK_SPACE){
			restartBoolean = true;
		}
		
	}
	
	private void restartGame(){
		removeAll();
	}	
		
	
	private GPoint topLeft;
	private GObject gobj, gobj1, gobj2, gobj3, gobjUp, gobjDown, gobjLeft, gobjRight;
	private GObject gobjUpLeft, gobjUpRight, gobjDownLeft, gobjDownRight, gobjCheck;
	private GLabel col, row, err;
	private GRect cover;
	private double colNumber, rowNumber;
	private int nSurroundingBombs, nCovers, nSurroundingMarks;
	private boolean gameOver, restartBoolean, loss;
	private GLabel count;
	private int timeCount;
	
	
	/**Private instance variable for random number generator */
	private RandomGenerator rgen = RandomGenerator.getInstance();
}
