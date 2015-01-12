import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.KeyEvent;

import acm.program.*;
import acm.util.RandomGenerator;
import acm.graphics.*;

public class JamDat extends GraphicsProgram{
	
	private Player player1, player2, user;
	private GCompound world;
	private GLabel bottomLeft, bottomRight, topLeft, topRight;
	boolean alive, labelsOn;

	public void run(){
		waitForClick();
		addKeyListeners();
		setUpBoard();
		while(true){
			world.add(user.player);
			alive = true;
			while(alive){
				play();
			}
			waitForClick();
			world.removeAll();
			switchUser();
		}
	}
	
	
	private void setUpBoard(){
		alive = true;
		world = new GCompound();
		GRect boundary = new GRect(FEILD_WIDTH, FEILD_HEIGHT);
		boundary.setFilled(true);
		boundary.setFillColor(Color.green);
		add(boundary, (getWidth()-FEILD_WIDTH)/2, (getHeight()-FEILD_HEIGHT)/2);
		add(world, (getWidth()-FEILD_WIDTH)/2, (getHeight()-FEILD_HEIGHT)/2);
		for (int i = 0; i<NROWS_PER_SCREEN; i++){
			GLine yardMarker = new GLine(boundary.getX(), boundary.getY() + i*HEIGHT,
					boundary.getX() + FEILD_WIDTH, boundary.getY() + i*HEIGHT);
			yardMarker.setColor(Color.white);
			add(yardMarker);
		}
		
		player1 = new Player(world, NCOLS, NROWS_PER_SCREEN, Color.red);
		player2 = new Player(world, NCOLS, NROWS_PER_SCREEN, Color.blue);
		world.remove(player2.player);
		user = player1;
	}
	
	private void play(){
		int curScreen;
		for(curScreen = 0; curScreen<NSCREENS && alive; curScreen++){
			addDefenders(world, curScreen);
			user.resetRow();
			addYards(curScreen);
			while (user.row > 0){
				if (alive)alive = user.moveForward();
				else break;
				pause(PAUSE_TIME - (curScreen*PAUSE_DROP));
			}
			world.removeAll();
		}
		if(alive){
			GLabel score = new GLabel("Touchdown!");
			add(score, (FEILD_WIDTH-score.getWidth())/2, (FEILD_HEIGHT-score.getWidth())/2);
		}
		else {
			int rowReached = ((curScreen-1) * NROWS_PER_SCREEN) + (NROWS_PER_SCREEN-user.row);
			Frame f = new Frame("Tackled");
			f.add(new MainCanvas());
			f.setSize(new Dimension(200,0));
			f.setResizable(true);
			f.setLocation((getWidth()-f.getWidth())/2, (getHeight()-f.getHeight())/2);
			MenuBar mb = new MenuBar();
			Menu m = new Menu();
			m.setName("MENU");
			mb.setHelpMenu(m);
			f.setMenuBar(mb);
			f.setVisible(true);
			
			GLabel tackled = new GLabel("Tackled at the " + rowReached + "th row");
			world.add(tackled, (FEILD_WIDTH-tackled.getWidth())/2, (FEILD_HEIGHT-tackled.getHeight())/2);
		}
	}
	
	private void switchUser(){
		if (user == player1) user = player2;
		else user = player1;
	}
	
	public void keyPressed(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_LEFT && alive){
			alive = user.move(-1);
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && alive){	
			alive = user.move(1);
		}
	}
	
	private void addDefenders(GCompound world, int level){
		for (int i = 1; i<NROWS_PER_SCREEN; i++){
			int col = rgen.nextInt(0, NCOLS+2);
			Player defender = new Player(world, col, i, Color.black);
		}
	}
	
	private void addYards(int screen){
		if (labelsOn){
			remove(bottomLeft);
			remove(bottomRight);
			remove(topLeft);
			remove(topRight);
		}
		else labelsOn = true;
		int lower = NYARDS_PER_SCREEN*screen;
		if(lower>50) lower = 100 - lower;
		
		bottomLeft = (lower>50)? new GLabel("" +(100-lower)):new GLabel("" + lower);
		bottomRight = (lower>50)? new GLabel("" +(100-lower)):new GLabel("" + lower);
		topLeft = (lower>50)? new GLabel("" + (lower-5)): new GLabel("" + (lower + 5));
		topRight = (lower>50)? new GLabel("" + (lower-5)): new GLabel("" + (lower + 5));
		
		add(bottomLeft, world.getX() - bottomRight.getWidth() - 5, world.getY() + FEILD_HEIGHT);
		add(bottomRight, world.getX() + FEILD_WIDTH + 5, world.getY() + FEILD_HEIGHT);
		add(topLeft, world.getX() - bottomRight.getWidth() - 5, world.getY());
		add(topRight, world.getX() + FEILD_WIDTH + 5, world.getY());
	}
	
class Player{
	GOval player;
	GCompound world;
	int col, row;
	int nCol, nRow;
	Color color;
	
	public Player(GCompound world, int col, int row, Color color){
		this.world = world;
		this.nCol = col;
		this.nRow = row;
		this.col = col/2;
		this.row = row-1;
		this.color = color;
		
		double x = (WIDTH * this.col) + ((WIDTH - PLAYER_WIDTH)/2);
		double y = (HEIGHT * this.row) + ((HEIGHT - PLAYER_HEIGHT)/2);
		player = new GOval (x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		player.setFilled(true);
		player.setFillColor(color);
		world.add(player);
	}
	
	public Player(GCompound world, int col, int row){
		this.world = world;
		this.col = col;
		this.row = row;
		this.color = Color.black;
		
		double x = (WIDTH * this.col) + ((WIDTH - PLAYER_WIDTH)/2);
		double y = (HEIGHT * this.row) + ((HEIGHT - PLAYER_HEIGHT)/2);
		player = new GOval (x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		player.setFilled(true);
		player.setFillColor(color);
		world.add(player);
	}
	
	public boolean moveForward(){
		row--;
		boolean toReturn = getOccupied();
		refactor();
		return !toReturn;
	}
	
	public void resetRow(){
		row = nRow;
		refactor();
	}
	
	public boolean move(int colChange){
		if((colChange<0 && col>0) || (colChange>0 && col<nCol-1)){
			col += colChange;
			boolean toReturn = getOccupied();
			refactor();
			return !toReturn;
		}
		else return true;
	}
	
	private void refactor(){
		world.remove(player);
		double x = (WIDTH * col) + ((WIDTH - PLAYER_WIDTH)/2);
		double y = (HEIGHT * row) + ((HEIGHT - PLAYER_HEIGHT)/2);
		player = new GOval (x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		player.setFilled(true);
		player.setFillColor(color);
		world.add(player);
	}
	
	private boolean getOccupied(){
		return (world.getElementAt((col*WIDTH) + WIDTH/2, (row*HEIGHT) + HEIGHT/2) != null);
	}
	
	private static final int PLAYER_WIDTH = WIDTH/4;
	private static final int PLAYER_HEIGHT = HEIGHT/4;
}


private static final int NROWS_PER_SCREEN = 4;
private static final int NYARDS_PER_SCREEN = 5;
private static final int NSCREENS = 25;
private static final int NCOLS = 4;
private static final int WIDTH = 50;
private static final int HEIGHT = 50;
private static final int FEILD_WIDTH = NCOLS*WIDTH;
private static final int FEILD_HEIGHT = NROWS_PER_SCREEN*HEIGHT;
private static final int PAUSE_TIME = 600;
private static final int PAUSE_DROP = 20;

	private RandomGenerator rgen = new RandomGenerator();
}
