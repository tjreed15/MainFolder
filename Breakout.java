/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {


/** Dimensions of game board (usually the same) */
	private static final int WIDTH = 400;
	private static final int HEIGHT = 600;
	
/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 50;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick (change +/- to put edge of bricks against or separated from wall) */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW + 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;
	
/** Starting height of the ball before being shot each turn */
	private static final int BALL_Y_OFFSET = 300;

/** Radius of the ball in pixels, and speed bounds of ball */
	private static final int BALL_RADIUS = 10;
	private static final double MIN_BALL_Y_VELOCITY = 3.0;
	private static final double MAX_BALL_Y_VELOCITY = 5.0;
	private static final double MIN_BALL_X_VELOCITY = 1.0;
	private static final double MAX_BALL_X_VELOCITY = 3.0;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;
	
/** Time to pause while moving ball and bounce increase ratio */
	private static final int PAUSE_TIME = 15;
	private static final double BOUNCE_INCREASE_RATIO = 1.001;
	private static final double SLOW_RATE = 0.8;
	private static final double SPEED_RATE = 1.2;
	private static final int PADDLE_SPEED = 5;
	
/** Placement of Labels compared to top (Y) and right (X) walls and separation between each*/
	private static final int LABELX = 50;
	private static final int LABELY = 100;
	private static final int LABEL_DIF = 50;
	
/** Order of the side Labels*/
	private static final int LABEL_ORDER_TIMER = 0;
	private static final int LABEL_ORDER_BRICKS = 1;
	private static final int LABEL_ORDER_TURNS = 2;
	private static final int LABEL_ORDER_BULLETS = 3;
	private static final int LABEL_ORDER_POWER_UP = 4;
	
/** Number of turns */
	private static final int NTURNS = 3;
	
	
/** Number of power ups in a game (max is 1 less than the total number of bricks, the last brick will not drop one because the game is over). */	
	private static final boolean PLAY_WITH_POWER_UPS = true;
	private static final boolean CRAZY_POWER_UP_BOOLEAN = false;
	private static final boolean EXPLAIN_RULES = false;
	private static final int NPOWER_UPS = 20;
	
/** Explosion specifics*/
	private static final double BREAK_X_VEL = 3;
	private static final double BREAK_Y_VEL = 3;
	
/** Size, speed, and number of power ups*/
	private static final int POWER_UP_WIDTH = 8;
	private static final int POWER_UP_HEIGHT = 12;
	private static final int POWER_UP_SPEED = 2;

/** Power up specifications*/
	private static final int NBOUNCES_PER_POWER_UP = 5;
	private static final int NBOUNCES_PER_INVINCIBLE_BALL = 1;
	private static final double WIDE_PADDLE_ADDITION = 15;
	private static final double BULLET_WIDTH = 8;
	private static final double BULLET_HEIGHT = 12;
	private static final double BULLET_VELOCITY = -8;
	private static final int NBULLETS_PER_POWER_UP = 5;
	private static final int PADDLE_2_Y_OFFSET = 250;
	private static final int MAX_PADDLE_ALTITUDE = 300;
	private static final int SHEILD_HEIGHT = 10;
	private static final int SHEILD_Y_OFFSET = -25;
	private static final int GIANT_BALL_RADIUS_INCREASE = 10;
	private static final int SMALL_BALL_RADIUS = 2;
	private static final int FAN_BALL_DISTANCE = 100;
	private static final double FAN_BALL_BOUNCE_INCREASE = 0.2;
	private static final int CUP_HEIGHT = 40;
	private static final int CUP_WIDTH = 4;
	
	
/** Runs the Breakout program. */	
	public void run() {
		initiateVariables();
  		if (explainRules){
  			ruleInt = 0;
  			explainRules();
  		}
  		int j = 0;
  		while(j < 1){	
  			initiateVariables();
	  		setUpBoard();
			addMouseListeners();
			addKeyListeners();
			waitForClick();
			for (int i = 0; i < NTURNS; i++){
				if(nBricks > 0){
					shootBall();
					playBreakout();
					if (restartBoolean == true){
						restartGame();
						i = -1;
						restartBoolean = false;
					}
				}	
			}
			if (nBricks == 0){
				youWin();
			}
			else if(nBricks > 0){
				youLose();
			}
			waitForClick();
			removeAll();
			j = -1;				
			restartBoolean = false;
	  	}
	}
	
	private void explainRules(){
		ruleBoolean = true;
		addKeyListeners();
		initiateVariables();
		setUpBoard();
		ruleExplain1();
		ruleExplain2();
		ruleExplain3();
		ruleExplain4();
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		if (ball.getY() > bottomWall)reExplain4();
		ruleExplain5();
		removeAll();
		explosionBoolean = false;
		ruleBoolean = false;
	}
	
	private void ruleExplain1(){
		while(ruleInt == 0){
			GLabel rules1 = new GLabel ("1) CLICK TO START.");
			double x = (getWidth() - rules1.getWidth()) / 2;
			double y = (getHeight() - rules1.getHeight()) / 2;
			if (ruleBoolean == false)break;
				add(rules1, x, y);
			if (ruleBoolean == false)break;
				waitForClick();
				addBall();
				xVel = 3;
				yVel = 5;
				for(int i = 0; i < 20; i++)moveBall();
				rulesContinue = new GLabel ("(Click to Continue...)", x, y + 25);
				add(rulesContinue);
				remove(rules1);
			ruleInt = 1;
		}
	}
	
	private void ruleExplain2(){
		while (ruleInt == 1){
			GLabel rules2 = new GLabel ("2) MOVE MOUSE TO MOVE PADDLE");
			if (ruleBoolean == false)break;
				double x = (getWidth() - rules2.getWidth()) / 2;
				double y = (getHeight() - rules2.getHeight()) / 2;
				add(rules2, x, y);
				if(ruleBoolean == false)break;
					waitForClick();
					for(int i = 0; i < 20; i++){
						paddle.move(xVel, 0);
						pause(PAUSE_TIME);
					}
			if(ruleBoolean == false)break;
				waitForClick();
				remove(rules2);
				remove(rulesContinue);
			ruleInt = 2;
		}	
	}
	
	private void ruleExplain3(){
		while (ruleInt == 2){
			GLabel rules3 = new GLabel ("3) KEEP BALL ALIVE AND TRY TO REMOVE ALL BRICKS");
			if(ruleBoolean == false)break;
				double x = (getWidth() - rules3.getWidth()) / 2;
				double y = (getHeight() - rules3.getHeight()) / 2;
				add(rules3, x, y);
				add(rulesContinue);
			if(ruleBoolean == false)break;
				waitForClick();
				remove(rules3);
				remove(rulesContinue);
				while(nBricks == NBRICKS_PER_ROW * NBRICK_ROWS && ruleBoolean  == true){
					moveBall();
					checkForCollisions();
			}
			ruleInt = 3;
		}
	}

	private void ruleExplain4(){
		while (ruleInt == 3){	
			GLabel rules4 = new GLabel ("4) CATCH POWER UPS TO HELP YOU WIN");
			GLabel rules41 = new GLabel ("Now you try... Move the paddle, catch the power up, and keep the ball alive.");
			if(ruleBoolean == false)break;
				double x = (getWidth() - rules4.getWidth()) / 2;
				double y = (getHeight() - rules4.getHeight()) / 2;
				add(rules4, x, y);
				double z = (getWidth() - rules41.getWidth()) / 2;
				add(rules41, z, y + 25);
			if(ruleBoolean == false)break;
				addMouseListeners();
				waitForClick();
				remove(rules4);
				remove(rules41);
				dropPowerUp(ball.getX(), ball.getY());
				explainPowerUpTypes();
			ruleInt = 4;
		}
	}
	
	private void reExplain4(){
		while (ruleInt < 5){
			GLabel miss = new GLabel ("GOOD TRY! I'M SURE YOU WON'T SUCK COMPLETELY");
			GLabel miss2 = new GLabel ("WHEN YOU PLAY THE REAL GAME.");
			if(ruleBoolean == false)break;
				double x = (getWidth() - miss.getWidth()) / 2;
				double y = (getHeight() - miss.getHeight()) / 2;
				add(miss, x, y - 25);
				double z = (getWidth() - miss2.getWidth()) / 2;
				add(miss2, z, y);
				add(rulesContinue);
			if(ruleBoolean == false) break;
				waitForClick();
				remove(miss);
				remove(miss2);
				remove(rulesContinue);
				ruleInt = 5;
		}
	}
	
	private void ruleExplain5(){
		while (ruleInt < 6){
			GLabel rules5 = new GLabel ("5) PRESS'SHIFT + R' TO RESTART THE GAME AT ANY TIME");
			GLabel luck = new GLabel ("GOOD LUCK!");
			if(ruleBoolean == false)break;
				double x = (getWidth() - rules5.getWidth()) / 2;
				double y = (getHeight() - rules5.getHeight()) / 2;
				add(rules5, x, y - 25);
				double z = (getWidth() - luck.getWidth()) / 2;
				add(luck, z, y);
				add(rulesContinue);
			if(ruleBoolean == false)break;
				waitForClick();
			ruleInt = 6;
		}
	}
	
	private void explainPowerUpTypes(){
		
	}
  	
/** Used solely in game set-up, delete once complete. */
  	private void initiateVariables(){
  		timeCount = 0;
  		loopCount = 0;
  		timer = new GLabel ("Timer: 0" , 100, 100);
  		pauseGame = false;
  		boardWidth = getWidth();
  		boardHeight = getHeight();
  		fullMotionBoolean = false;
  		dualMotionBoolean = false;
  		catchBallBoolean = false;
  		cupPaddleBoolean = false;
  		caughtBall = false;
  		invincibleBallBoolean = false;
  		fanBallBoolean = false;
  		ballSackBoolean = false;
  		ballSacked = false;
  		throwBallBoolean = false;
  		explainRules = EXPLAIN_RULES;
  		playWithPowerUps = PLAY_WITH_POWER_UPS;
  		crazyPowerUpBoolean = CRAZY_POWER_UP_BOOLEAN;
  		crazyUp = 1;
  		catchBallTally = 0;
  		widePaddleTally = 0;
  		test = new GLabel ("test", 100, 100);
  		powerLabel = new GLabel ("", 0, 0);
  		powerUp = new GRect(5, 5);
  		bullet = new GRect(5, 5);
  		paddle2 = new GRect(5, 5);
  		shield = new GRect(5, 5);
  		ball2 = new GOval(5, 5);
  		ball3 = new GOval (5, 5);
  		rightCup = new GRect (5, 5);
  		leftNut = new GOval (5, 5);
  		rightNut = new GOval (5, 5);
  		add(powerUp, -1, 10);
  		add(bullet, -1, 10);
  		add(paddle2, -1, 10);
  		add(shield, -1, 10);
  		add(ball2, -1, 10);
  		add(ball3, -1, 10);
  		add(rightCup, -1, 10);
  		add(leftNut, -1, 10);
  		add(rightNut, -1, 10);
  		add(powerLabel);
  		paddle2.setVisible(false);
  		bullet.setVisible(false);
  		powerUp.setVisible(false);
  		shield.setVisible(false);
  		ball2.setVisible(false);
  		ball3.setVisible(false);
  		rightCup.setVisible(false);
  		leftNut.setVisible(false);
  		rightNut.setVisible(false);
  		powerLabel.setVisible(false);
  	}
  	
/** Waits for user and screen to be ready (the right size), then sets up perimeter, bricks, info labels, and paddle. */
  	private void setUpBoard(){
		GLabel begin = new GLabel ("CLICK TO BEGIN");
		double x = (getWidth() - begin.getWidth()) / 2;
		double y = (getHeight() - begin.getHeight()) / 2;
		add(begin, x, y);
		waitForClick();
		remove(begin);
		while (getWidth() < WIDTH || getHeight() < HEIGHT){
			err = new GLabel("Error: Screen Too Small");
			double a = (getWidth() - err.getWidth()) / 2;
			double b = (getHeight() - err.getHeight()) / 2;
			add(err, a, b);
			waitForClick();
			remove(err);
		}
		addPerimiter();
		addBricks();
		addSideLabels();
		addPaddle();
	}
	
/** Adds info labels to right side of board. */
	private void addSideLabels(){
		nBricks = (NBRICKS_PER_ROW * NBRICK_ROWS);
		changeBrickLabel();
		nTurns = NTURNS;
		changeTurnLabel();
		nBullets = 0;
		changeBulletLabel();
	}

/** Adds GRect as the perimeter of the game board. */
	private void addPerimiter(){
		int x = (getWidth() - WIDTH) / 2;
		int y = (getHeight() - HEIGHT) / 2;
		perim = new GRect(x, y, WIDTH, HEIGHT);
		add(perim);
	}

/** Adds NBRICK_ROWS number of rows of bricks to game board. */
	private void addBricks(){
		for (int i = 0; i < NBRICK_ROWS; i++){
			int y = BRICK_Y_OFFSET + (i * BRICK_HEIGHT) + (i * BRICK_SEP);			
			addRow(y,i);
		}		
	}
	
/** Adds NBRICKS_PER_ROW number of bricks into a row, changing colors depending on which row they are in. */
	private void addRow(int y ,int c ){
		for (int i = 0; i < NBRICKS_PER_ROW; i++){
			int x = ((getWidth() - WIDTH) / 2) + ((i + 1)* BRICK_SEP) + (i * BRICK_WIDTH);
			brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
			brick.setFilled(true);
			if (c < 2){
				brick.setFillColor(Color.RED);
				brick.setColor(Color.RED);
			}
			else if (c < 4){
				brick.setFillColor(Color.ORANGE);
				brick.setColor(Color.ORANGE);

			}
			else if (c < 6){
				brick.setFillColor(Color.YELLOW);
				brick.setColor(Color.YELLOW);
			}
			else if (c < 8){
				brick.setFillColor(Color.GREEN);
				brick.setColor(Color.GREEN);
			}
			else if (c < 10){
				brick.setFillColor(Color.CYAN);
				brick.setColor(Color.CYAN);
			}
			add(brick);
		}
	}

/** Adds the paddle and initializes point "last" for mouse to move paddle. */
	private void addPaddle(){
		paddleWidth = PADDLE_WIDTH;
		double y = (((getHeight() - HEIGHT) / 2) + HEIGHT) - PADDLE_Y_OFFSET;
		double x = (getWidth() - PADDLE_WIDTH) / 2;
		paddle = new GRect(x, y, paddleWidth, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
		last = new GPoint(x,0);
	}
	
/** Moves paddle same distance as mouse moves. If the paddles position is to be moved past the wall, it is reset back to touch the wall. */
	public void mouseMoved(MouseEvent e){
		int leftWall = ((getWidth() - WIDTH) / 2);
		int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		double y = bottomWall - PADDLE_Y_OFFSET;
		if (ballSackBoolean == false && paddle.getX() > leftWall && (paddle.getX() + paddleWidth) < rightWall)assignRegularMotion(e.getX(), e.getY());
		else if (ballSackBoolean == true && paddle.getX() > leftWall && (paddle.getX() + PADDLE_HEIGHT) < rightWall)assignRegularMotion(e.getX(), e.getY());
		if (paddle.getX() <= leftWall)pastLeftWall();
		if (ballSackBoolean == false && (paddle.getX() + paddleWidth) >= rightWall) pastRightWall();
		else if (ballSackBoolean == true && (paddle.getX() + PADDLE_HEIGHT) >= rightWall) pastRightWall();
		if (paddle.getY() <= MAX_PADDLE_ALTITUDE)aboveHeightLimit();
		if ((paddle.getY() + PADDLE_HEIGHT) >= bottomWall)belowLowerWall();
		if (paddle.getY() < y && (dualMotionBoolean == false || initialDualMotionBounce <= nBounces - (NBOUNCES_PER_POWER_UP * 2)) && 
				(fullMotionBoolean == false || initialFullMotionBounce <= nBounces - NBOUNCES_PER_POWER_UP)) {
			if (ballSackBoolean == false)paddle.setLocation(paddle.getX(), y); 	
			if (ballSackBoolean == true) paddle.setLocation(paddle.getX(), y - paddleWidth + PADDLE_HEIGHT);
		}
		if (caughtBall == true){
			if (throwBallBoolean == false && catchBallTally == 2 && ballSacked == false){
				double ball1X = paddle.getX() + BALL_RADIUS;
				double ball1Y = paddle.getY() + (PADDLE_HEIGHT / 2);
				pelota1.setLocation(ball1X, ball1Y);
			}
			if (throwBallBoolean == false && catchBallTally == 4 && ballSacked == false){
				ballSack(paddle.getX(), paddle.getY());
			}
			
		}
		last = new GPoint(e.getPoint());
	}

	private void assignRegularMotion(double x, double y){
		paddle.move(x - last.getX(), 0);
		if (ballSackBoolean == true)leftNut.move(x - last.getX(), 0);
		if (ballSackBoolean == true)rightNut.move(x - last.getX(), 0);
		if (cupPaddleBoolean == true && ((initialCupPaddleBounce + (2 * NBOUNCES_PER_POWER_UP)) >= nBounces))leftCup.move(x - last.getX(), 0); 
		if (cupPaddleBoolean == true && ((initialCupPaddleBounce + (2 * NBOUNCES_PER_POWER_UP)) >= nBounces))rightCup.move(x - last.getX(), 0); 
		if (twoPaddleBoolean == true && (initialTwoPaddleBounce + NBOUNCES_PER_POWER_UP) >= nBounces)paddle2.move(x - last.getX(), 0);
		if (fullMotionBoolean == true && (initialFullMotionBounce + NBOUNCES_PER_POWER_UP) >= nBounces){
			paddle.move(0, y - last.getY());
			if (ballSackBoolean == true)leftNut.move(0, y - last.getY());
			if (ballSackBoolean == true)rightNut.move(0, y - last.getY());
			if (cupPaddleBoolean == true && ((initialCupPaddleBounce + (2 * NBOUNCES_PER_POWER_UP)) >= nBounces))leftCup.move(0, y - last.getY()); 
			if (cupPaddleBoolean == true && ((initialCupPaddleBounce + (2 * NBOUNCES_PER_POWER_UP)) >= nBounces))rightCup.move(0, y - last.getY()); 
		}
		
	}
	
	private void pastLeftWall(){
		int leftWall = ((getWidth() - WIDTH) / 2);
		paddle.setLocation(leftWall + 5, paddle.getY());
		if (ballSackBoolean == true)leftNut.setLocation(leftWall + 5 - (2 * BALL_RADIUS) + (PADDLE_HEIGHT / 2), leftNut.getY());
		if (ballSackBoolean == true)rightNut.setLocation(leftWall + 5 + (PADDLE_HEIGHT / 2), rightNut.getY());
		if (twoPaddleBoolean == true && (initialTwoPaddleBounce + NBOUNCES_PER_POWER_UP) >= nBounces)paddle2.setLocation(leftWall + 5, paddle2.getY());
		if (cupPaddleBoolean == true && ((initialCupPaddleBounce + (2 * NBOUNCES_PER_POWER_UP)) >= nBounces)){
			leftCup.setLocation(leftWall + 5, leftCup.getY());
			rightCup.setLocation((leftWall + paddleWidth - CUP_WIDTH + 5), rightCup.getY());
		}
	}
	
	private void pastRightWall(){
		int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
		if (ballSackBoolean == false)paddle.setLocation((rightWall - paddleWidth) - 5, paddle.getY());
		if (ballSackBoolean == true){
			paddle.setLocation((rightWall - PADDLE_HEIGHT) - 5, paddle.getY());
		}
		if (ballSackBoolean == true)leftNut.setLocation(rightWall - (PADDLE_HEIGHT / 2) - 5 - (2 * BALL_RADIUS), leftNut.getY());
		if (ballSackBoolean == true)rightNut.setLocation(rightWall - (PADDLE_HEIGHT / 2) - 5, rightNut.getY());
		if (twoPaddleBoolean == true && (initialTwoPaddleBounce + NBOUNCES_PER_POWER_UP) >= nBounces)paddle2.setLocation(rightWall - paddleWidth - 5, paddle2.getY());
		if (cupPaddleBoolean == true && ((initialCupPaddleBounce + ( 2 * NBOUNCES_PER_POWER_UP)) >= nBounces)){
			leftCup.setLocation((rightWall - paddleWidth - 5), (paddle.getY() - CUP_HEIGHT));
			rightCup.setLocation((rightWall - CUP_WIDTH - 5), (paddle.getY() - CUP_HEIGHT));
		}
	}
	
	private void aboveHeightLimit(){
		paddle.setLocation(paddle.getX(), MAX_PADDLE_ALTITUDE + 5);
		if (ballSackBoolean == true)leftNut.setLocation(leftNut.getX(), MAX_PADDLE_ALTITUDE + paddleWidth);
		if (ballSackBoolean == true)rightNut.setLocation(rightNut.getX(), MAX_PADDLE_ALTITUDE + paddleWidth);
		if (cupPaddleBoolean == true && initialCupPaddleBounce + (2 * NBOUNCES_PER_POWER_UP) >= nBounces){
			leftCup.setLocation(leftCup.getX(), MAX_PADDLE_ALTITUDE - CUP_HEIGHT);
			rightCup.setLocation(rightCup.getX(), MAX_PADDLE_ALTITUDE - CUP_HEIGHT);
		}
	}
	
	private void belowLowerWall(){
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		if(ballSackBoolean == false) paddle.setLocation(paddle.getX(), (bottomWall - PADDLE_HEIGHT) - 5);
		if(ballSackBoolean == true)paddle.setLocation(paddle.getX(), (bottomWall - paddleWidth) - 5);
		if (ballSackBoolean == true)leftNut.setLocation(leftNut.getX(), bottomWall - 5);
		if (ballSackBoolean == true)rightNut.setLocation(rightNut.getX(), bottomWall - 5);
		if (cupPaddleBoolean == true && initialCupPaddleBounce + (2 * NBOUNCES_PER_POWER_UP) >= nBounces){
			leftCup.setLocation(leftCup.getX(), bottomWall - CUP_HEIGHT);
			rightCup.setLocation(rightCup.getX(), bottomWall - CUP_HEIGHT);
		}
	}

/** Moves paddle same distance as mouse moves. If the paddles position is to be moved past the wall, it is reset back to touch the wall. */
	public void mouseDragged (MouseEvent e){
		int leftWall = ((getWidth() - WIDTH) / 2);
		int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
		double y = (((getHeight() - HEIGHT) / 2) + HEIGHT) - PADDLE_Y_OFFSET;
		double z = y + PADDLE_Y_OFFSET - PADDLE_2_Y_OFFSET;
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		if (fullMotionBoolean == true && (initialFullMotionBounce + NBOUNCES_PER_POWER_UP) >= nBounces){
			if (paddle.getY() < (bottomWall - PADDLE_HEIGHT) && paddle.getY() > MAX_PADDLE_ALTITUDE){
				paddle.move(e.getX() - last.getX(), e.getY() - last.getY());
				last = new GPoint (e.getPoint());
			}
			if(paddle.getY() <= MAX_PADDLE_ALTITUDE){
				paddle.setLocation(paddle.getX(), MAX_PADDLE_ALTITUDE + 5);
			}
			if (paddle.getY() >= (bottomWall - PADDLE_HEIGHT)){
				paddle.setLocation(paddle.getX(), (bottomWall - PADDLE_HEIGHT) - 5);
			}
			if(paddle.getX() <= leftWall){
				paddle.setLocation(leftWall + 5, paddle.getY());
			}
			if (paddle.getX() >= (rightWall - paddleWidth)){
				paddle.setLocation((rightWall - paddleWidth) - 5, paddle.getY());
			}
		}
		
		
		else{
			if (paddle.getY() < y && (dualMotionBoolean == false || initialDualMotionBounce <= nBounces - (NBOUNCES_PER_POWER_UP * 2))) paddle.setLocation(paddle.getX(), y); 
			if (paddle.getX() < (rightWall - paddleWidth) && paddle.getX() > leftWall){
				paddle.move(e.getX() - last.getX(), 0);
				if (cupPaddleBoolean == true){
					rightCup.move(e.getX() - last.getX(), 0);
					leftCup.move(e.getX() - last.getX(), 0);
				}
				last = new GPoint (e.getPoint());
			}
			if(paddle.getX() <= leftWall){
				paddle.setLocation(leftWall + 5, paddle.getY());
				if (cupPaddleBoolean == true){
					leftCup.setLocation(leftWall + 5, (paddle.getY() - CUP_HEIGHT));
					rightCup.setLocation((leftWall + paddleWidth - CUP_WIDTH + 5), (paddle.getY() - CUP_HEIGHT));
				}
			}
			if (paddle.getX() >= (rightWall - paddleWidth)){
				paddle.setLocation((rightWall - paddleWidth) - 5, paddle.getY());
				if (cupPaddleBoolean == true){
					leftCup.setLocation((rightWall - paddleWidth - 5), (paddle.getY() - CUP_HEIGHT));
					rightCup.setLocation((rightWall - CUP_WIDTH - 5), (paddle.getY() - CUP_HEIGHT));
				}
			}
			
			int topWall = (getHeight() - HEIGHT) / 2;
			if (paddle2.getY() > topWall){
				if (paddle2.getX() < (rightWall - PADDLE_WIDTH) && paddle2.getX() > leftWall){
					paddle2.move(e.getX() - last2.getX(), 0);
					last2 = new GPoint (e.getPoint());
				}
				else if(paddle2.getX() <= leftWall){
					paddle2.setLocation(leftWall + 5, z);
				}
				else if (paddle2.getX() >= (rightWall - PADDLE_WIDTH)){
					paddle2.setLocation((rightWall - PADDLE_WIDTH) - 5, z);
				}
			}
		}
		if (caughtBall == true){
			double ballX = paddle.getX() + ((paddleWidth - (2 * ballRadius)) / 2);
			double ballY = paddle.getY() - (2 * ballRadius);
			ball.setLocation(ballX, ballY);
		}
	}

/** Adds the ball to the screen and generates the initial "velocity" of the ball (does not start any motion). */
	private void shootBall(){
		ballRadius = BALL_RADIUS;
		addBall();
		xVel = rgen.nextDouble(MIN_BALL_X_VELOCITY, MAX_BALL_X_VELOCITY);
		if (rgen.nextBoolean(0.5)) xVel = -xVel;
		yVel = rgen.nextDouble(MIN_BALL_Y_VELOCITY, MAX_BALL_Y_VELOCITY);
	}
	
/** Puts ball on the screen. */
	private void addBall(){
		double x = (getWidth() - (2 * ballRadius)) / 2;
		double y = (((getHeight() - HEIGHT) / 2) + HEIGHT) - BALL_Y_OFFSET;
		ball = new GOval (x, y, (2 * ballRadius), (2 * ballRadius));
		ball.setFilled(true);
		add(ball);
	}
	
/** Decides if the ball is in play by checking its position and if the game has ended (bricks run out). */
	private boolean ballInPlay(){
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		if (nBricks > 0 && (ball.getY() < (bottomWall - (2 * ballRadius)))){
			return true;
		}
		else{
			return false;
		}
	}

/** Moves ball through board while checking for collisions. If any power ups have been caught, it uses them.
 * Once the ball is out of play, a turn is deducted. */
	private void playBreakout(){
		while (ballInPlay()){
			paddleLoc = paddle.getX();
			moveBall();
			checkForCollisions();
			usePowerUps();
			checkBoardSize();
			if (pauseGame == true) pauseGame();
			if (restartBoolean == true){
				break;
			}
		}
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		if (ball.getY() >= (bottomWall - (2 * ballRadius))){
			remove(ball);
			nTurns--;
			remove(turnLabel);
			changeTurnLabel();
			fullMotionBoolean = false;
			dualMotionBoolean = false;
			catchBallBoolean = false;
			caughtBall = false;
			invincibleBallBoolean = false;
			fanBallBoolean = false;
			if (ball2.getY() > topWall){
				ball2.setLocation(-5, -5);
				remove(ball2);
			}
			if(ball3.getY() > topWall){
				ball3.setLocation(-5, -5);
				remove(ball3);
			}
		}
	}
	
	private void timeWise(){
		if (loopCount % (1000 / PAUSE_TIME) == 0){
			timeCount++;
			remove(timer);
			int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
			int topWall = (getHeight() - HEIGHT) / 2;
			int x = rightWall + LABELX;
			int y = topWall + LABELY + (LABEL_ORDER_TIMER * LABEL_DIF);
			timer = new GLabel ("Time: " + timeCount, x, y);
			add(timer);
		}
		 
	}
	
	private void checkBoardSize(){
		if(getWidth() < WIDTH || getHeight() < HEIGHT){
			add(test);
			pauseGame();
		}
		else{ 
			if (getWidth() < boardWidth || getWidth() > boardWidth){
				resizeWidth();
			}
			if (getHeight() < boardHeight || getHeight() > boardHeight){
				resizeHeight();
			}
		}
	}
	
	private void resizeWidth(){
		double resize = getWidth() - boardWidth;
		GRect fake = new GRect (5, 5, resize, 10);
		fake.setVisible(false);
		add(fake);
		
	}
	
	private void resizeHeight(){
		double resize = getHeight() - boardHeight;
		GRect fake = new GRect (5, 5, resize, 10);
		fake.setVisible(false);
		add(fake);
		
	}
	
/** Moves ball (and bounces off walls) and pauses after each movement for user to be able to see. */
	private void moveBall(){
		int leftWall = ((getWidth() - WIDTH) / 2);
		int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
		int topWall = (getHeight() - HEIGHT) / 2;		
		if ((ball.getX() <= leftWall) && xVel < 0) {
			xVel = - xVel;
		}
		if (ball.getX() >= (rightWall - (2 * ballRadius)) && (xVel > 0)){
			xVel = -xVel;
		}
		if ((ball.getY() <= topWall) && (yVel < 0)){
			yVel = - yVel;
			if (topSwitch == true) xVel = - xVel;
		}
		ball.move(xVel, yVel);
		pause(PAUSE_TIME);
		loopCount++;
		timeWise();
	}
	
/** Checks one pixel outside of the top, bottom, left, and right sides of the ball for any objects. 
 * (Not 4 corners because it has worked much smoother this way after testing the game out). */
	private void checkForCollisions(){
		GPoint top = new GPoint ((ball.getX() + ballRadius), (ball.getY() - 1));
		GPoint bottom = new GPoint ((ball.getX() + ballRadius), (ball.getY() + (2 * ballRadius) + 1));
		GPoint right = new GPoint ((ball.getX() + (2 * ballRadius) + 1), (ball.getY() + ballRadius));
		GPoint left = new GPoint ((ball.getX() - 1), (ball.getY() + ballRadius));
		checkSide(top);
		checkSide(bottom);
		checkSide(right);
		checkSide(left);
	}
	
/** Checks what the ball makes contact with. If it is the paddle, the ball changes Y velocity.
 * If it is an "explosion" piece, a power up, or a wall(because it is already programmed with the "ballMove()" method) nothing happens.
 * If it is anything else (usually a brick) it removes the object and deducts a brick from the nBricks count.
 * (This is where  many glitches may occur as more objects are added to the game board. Once an instance GObject is created, it replaces its previous
 * instance; therefore, if the previous instance is somehow still on the board and the ball makes contact with it, it will be counted as a brick.)
 * THEN, if the object is determined to be a brick that has been specified as a power up brick (explained later) it will drop a power up. */
	private void checkSide(GPoint check){
		gobj = getElementAt(check);
		int topWall = (getHeight() - HEIGHT) / 2;
		if (gobj == paddle || gobj == paddle2 || gobj == leftNut || gobj == rightNut){
			if (catchBallBoolean == true){
				caughtBall = true;
				catchBallTally++;
				throwBall(ball);
			}
			else if (yVel > 0){
				nBounces++;
				yVel = (-BOUNCE_INCREASE_RATIO * yVel);
				paddleSpeed = paddle.getX() - paddleLoc;
				if(paddleSpeed > -PADDLE_SPEED && paddleSpeed < PADDLE_SPEED && xVel == 0)xVel = paddleSpeed;
				else if (paddleSpeed >= 5){
					if (xVel > 0)xVel = (xVel * SPEED_RATE);
					if (xVel < 0)xVel = (xVel * SLOW_RATE);
					if(xVel == 0){
						xVel = rgen.nextDouble(MIN_BALL_X_VELOCITY, MAX_BALL_X_VELOCITY);
						if (rgen.nextBoolean(0.5)) xVel = -xVel;
					}
				}
				else if (paddleSpeed <= -5){
					if (xVel < 0)xVel = (xVel * SPEED_RATE);
					if (xVel > 0)xVel = (xVel * SLOW_RATE);
					if(xVel == 0) {
						xVel = rgen.nextDouble(MIN_BALL_X_VELOCITY, MAX_BALL_X_VELOCITY);
						if (rgen.nextBoolean(0.5)) xVel = -xVel;
					}
				}
			}	
		}
		else if (gobj == rightCup || gobj == leftCup){
			if (catchBallBoolean == true){
				caughtBall = true;
				catchBallTally++;
				throwBall(ball);
			}
			else{
				xVel = (-BOUNCE_INCREASE_RATIO * xVel);
			}
		}
		else if (gobj == shield){
			remove(shield);
			yVel = -yVel;
		}
		else if (gobj == ball2 || gobj == ball3){
			if(check.getX() == ball.getX() + ballRadius){
				yVel = (-BOUNCE_INCREASE_RATIO * yVel);
			}
			else {
				xVel = (-BOUNCE_INCREASE_RATIO * xVel);
			}
		}
		else if (gobj == break1 || gobj == break2 || gobj == break3 || gobj == break4){}
		else if (gobj == perim){}
		else if (gobj != null && gobj != perim && gobj != powerUp && gobj != bullet){
			remove(gobj);
			if (fireBallBoolean == true){
				removeSurroundingBricks();	
			}
			nBricks--;
			remove(brickLabel);
			changeBrickLabel();
			if (invincibleBallBoolean == true && initialInvincibleBallBounce + NBOUNCES_PER_INVINCIBLE_BALL >= nBounces){}
			else if(check.getX() == ball.getX() + ballRadius){
				yVel = (-BOUNCE_INCREASE_RATIO * yVel);
			}
			else {
				xVel = (-BOUNCE_INCREASE_RATIO * xVel);
				if (xVel == 0 && check.getX() == ball.getX()) xVel = 3;
				if (xVel == 0 && check.getX() == (ball.getX() + (2 * ballRadius))) xVel = -3;
			}
			if((nBricks % ((NBRICKS_PER_ROW * NBRICK_ROWS) / (NPOWER_UPS + 1)) == 0) && playWithPowerUps == true) {
				if(powerUp.getY() > topWall && powerUp.getY() <= paddle.getY()){}
				else if (nBricks > 0){
					dropPowerUp(gobj.getX(), gobj.getY());
				}
			}
			else if(explosionBoolean == true) {
				explodeBrick(gobj);
				explosionBoolean = false;
				moveBreaks();
			}
		}
	}
	
/** Adds a win label if the game finishes and the player has eliminated all bricks. */
	private void youWin(){
		removeAll();
		GLabel winner = new GLabel("YOU WIN! DON'T FEEL TOO GOOD ABOUT YOURSELF THOUGH... ");
		GLabel winner2 = new GLabel("IT'S A PRETTY FRAKIN EASY GAME.");
		double x = (getWidth() - winner.getWidth()) / 2;
		double y = (getHeight() - winner.getHeight()) / 2;
		add(winner, x, y);
		double a = (getWidth() - winner2.getWidth()) / 2;
		double b = ((getHeight() - winner2.getHeight()) / 2) + 50;
		add(winner2, a, b);
	}
	
/** Adds a lose label if the game finishes and the player has not eliminated all bricks. */
	private void youLose(){
		removeAll();
		GLabel loser = new GLabel("YOU MUST STINK PRETTY FREAKIN BAD TO BE LOSING THIS GAME.");
		double x = (getWidth() - loser.getWidth()) / 2; 
		double y = (getHeight() - loser.getHeight()) / 2;
		add(loser, x, y);
	}
	
/** When the count of nBricks changes, the side label is changed. */
	private void changeBrickLabel(){
		brickLabel = new GLabel ("# OF BRICKS LEFT: " + nBricks);
		int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
		int topWall = (getHeight() - HEIGHT) / 2;
		int x = rightWall + LABELX;
		int y = topWall + LABELY + (LABEL_ORDER_BRICKS * LABEL_DIF);
		add(brickLabel, x, y);
	}

/** When the number of turns is changed, the turn label is changed. */
	private void changeTurnLabel(){
		turnLabel = new GLabel ("# OF TURNS LEFT: " + nTurns);
		int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
		int topWall = (getHeight() - HEIGHT) / 2;
		int x = rightWall + LABELX;
		int y = topWall + LABELY + (LABEL_ORDER_TURNS * LABEL_DIF);
		add(turnLabel, x, y);
	}

/** When nBullets changes, the side label changes. (Only if the player is playing with power ups). */
	private void changeBulletLabel(){
		if (playWithPowerUps){
			bulletLabel = new GLabel ("# OF BULLETS LEFT: " + nBullets);
			int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
			int topWall = (getHeight() - HEIGHT) / 2;
			int x = rightWall + LABELX;
			int y = topWall + LABELY + (LABEL_ORDER_BULLETS * LABEL_DIF);
			add(bulletLabel, x, y);
		}
	}
	
/** Detects if a bullet is on the screen and, if so, it shoots it.*/
	private void usePowerUps(){
		if (playWithPowerUps){
			if (bulletAlive()){
				shootBullet();
			}	
			int topWall = (getHeight() - HEIGHT) / 2;
			if(paddle2.getY() > topWall && nBounces > (initialTwoPaddleBounce + NBOUNCES_PER_POWER_UP)){
				paddle2.setLocation(-5, 5);
				remove(paddle2);
			}
			if (ball2.getY() >= topWall){
				moveBall2();
				checkBall2();
			}
			if (ball3.getY() >= topWall){
				moveBall3();
				checkBall3();
			}
			if (fanBallBoolean == true && initialFanBallBounce >= (nBounces - NBOUNCES_PER_POWER_UP)){
				useFanBall();
			}
			if (initialCupPaddleBounce == (nBounces - (2 * NBOUNCES_PER_POWER_UP)) && cupPaddleBoolean == true){
				rightCup.setLocation(-5, -5);
				remove(rightCup);
				remove(leftCup);
				cupPaddleBoolean = false;
			}
			if (initialPowerLabelBounce == nBounces - 2) {
				remove(powerLabel);
				powerLabel = new GLabel ("Power Type: ");
				add(powerLabel);
			}
		}
		
	}
	
/** Randomly chooses which power up will fall from the "power up brick." If the power up is caught,
 *  the corresponding actions will be taken and the dropped power up will be removed. If it is not caught,
 *  the dropped power up will be removed as it makes contact with the bottom wall. */
	private void dropPowerUp(double x, double y){
		rand = rgen.nextInt(1,14);
		createPowerUp(x, y, rand);
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		while (powerUp.getY() <=  bottomWall - POWER_UP_HEIGHT){
			movePowerUp();
			choosePowerType();
			if (pauseGame == true) pauseGame();
			if(restartBoolean == true)break;
			if (ball.getY() > (bottomWall - (2 * ballRadius))){
				break;
			}
		}
		if (powerUp.getY() >= bottomWall - powerUp.getHeight()){
			remove(powerUp);
		}
	}
	
/** If there are no other power ups falling,a GRect that has a specific color determined from its randomly generated number falls. */
	private void createPowerUp(double x, double y, int power){
		int topWall = (getHeight() - HEIGHT) / 2;
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		if (powerUp.getY() > topWall && powerUp.getY() < bottomWall - POWER_UP_HEIGHT){}
		else{
			powerUp = new GRect((x + (BRICK_WIDTH / 2)), (y + BRICK_HEIGHT), POWER_UP_WIDTH, POWER_UP_HEIGHT);
			powerUp.setFilled(true);
			switch (power){
				case 1: powerUp.setFillColor(Color.GREEN); break;
				case 2: powerUp.setFillColor(Color.RED); break;
				case 3: powerUp.setFillColor(Color.BLUE); break;
				case 4: powerUp.setFillColor(Color.YELLOW); break;
				case 5: powerUp.setFillColor(Color.CYAN); break;
				default : powerUp.setFillColor(Color.ORANGE); break;
			}
			add(powerUp);
		}
	}
	
/** Moves the power up down the screen (and keeps moving ball as normal as well as any bullet that may be on the screen). */
	private void movePowerUp(){
		powerUp.move(0, POWER_UP_SPEED);
		moveBall();
		checkForCollisions();
		usePowerUps();
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		if (ball.getY() > (bottomWall - (2 * ballRadius))){
			powerUp.setLocation(-5, -5);
			remove(powerUp);
		}
	}
	
/** If the power up is caught, the type of power up (based on randomly selected number in dropPowerUp method) is given to the player.
 *  Changes explosion boolean to true so the next brick will explode. */
	private void choosePowerType(){
		double paddleX = paddle.getX();
		double topY = paddle.getY() - POWER_UP_HEIGHT;
		double bottomY = paddle.getY() + PADDLE_HEIGHT;
		double leftX = paddle.getX() - POWER_UP_WIDTH;
		double rightX = paddle.getX() + paddleWidth;
		if (powerUp.getY() >= topY && powerUp.getY() <= bottomY && powerUp.getX() >= leftX && powerUp.getX() <= rightX) {			
			powerUp.setLocation(-5, -5);
			remove(powerUp);
			if (crazyPowerUpBoolean){
				switch(crazyUp){
					case 1: multipleBalls(); break;
					case 2: catchBall(); break;
					case 3: widePaddlePowerUp(paddle.getX()); break;
					case 4: widePaddlePowerUp(paddle.getX()); break;
					default: bulletPowerUp(); break;
				}
				addPowerLabels(crazyUp);
				crazyUp++;
			}
			else{
				switch(rand){
					case 1: widePaddlePowerUp(paddleX); break;
					case 2: bulletPowerUp(); break;
					case 3: twoPaddlePowerUp(); break;
					case 4: fullMotionPaddlePowerUp(); break;
					case 5: dualMotionPaddlePowerUp();  break;
					case 6: invincibleBall(); break;
					case 7: addShield(); break;
					case 8: multipleBalls(); break;
					case 9: giantBall(); break;
					case 10: fireBall(); break;
					case 11: fanBall(); break;
					case 12: smallBall(); break;
					case 13: cupPaddle(); break;
					case 14: catchBall(); break;
				}
				addPowerLabels(rand);
			}
			explosionBoolean = false;
		}
	}

	private void addPowerLabels(int power){
		int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
		int topWall = (getHeight() - HEIGHT) / 2;
		int x = rightWall + LABELX;
		int y = topWall + LABELY + (LABEL_ORDER_POWER_UP * LABEL_DIF);
		String str = "";
		switch(rand){
			case 1: str = "Wide Paddle"; break;
			case 2: str = "Bullet Shooting"; break;
			case 3: str = "Two Paddles"; break;
			case 4: str = "Full Motion"; break;
			case 5: str = "DualMotion";  break;
			case 6: str = "InvinciBall"; break;
			case 7: str = "Shield"; break;
			case 8: str = "Multiple Balls"; break;
			case 9: str = "Giant Ball"; break;
			case 10: str = "Fire Ball"; break;
			case 11: str = "Fan Ball"; break;
			case 12: str = "Small Ball"; break;
			case 13: str = "Cup Paddle"; break;
			case 14: str = "Catch Ball"; break;
		}
		remove(powerLabel);
		powerLabel = new GLabel ("Power Type: " + str);
		add(powerLabel, x, y);
		initialPowerLabelBounce = nBounces;
	}
	
	
	
/** If the wide paddle power up is caught, the size of the paddle will be enlarged by WIDE_PADDLE_WIDTH_RATIO for the rest of the game. */
	private void widePaddlePowerUp(double paddleX){
		widePaddleTally++;
		remove(paddle);
		paddleWidth = paddleWidth + WIDE_PADDLE_ADDITION;
		double paddleY = (((getHeight() - HEIGHT) / 2) + HEIGHT) - PADDLE_Y_OFFSET;
		paddle = new GRect (paddleX, paddleY, paddleWidth, PADDLE_HEIGHT);
		if (ballSacked == true){
			add(test);
			paddleY = paddleY + PADDLE_HEIGHT - paddleWidth;
			paddle = new GRect (paddleX, paddleY, PADDLE_HEIGHT, paddleWidth);
		}
		int topWall = (getHeight() - HEIGHT) / 2;
		if (cupPaddleBoolean == true && rightCup.getY() > topWall){
			rightCup.move(WIDE_PADDLE_ADDITION, 0);
		}
		paddle.setFilled(true);
		add(paddle);
	}
	
/** Adds NBULLETS_PER_POWER_UP to nBullets (if the player already has bullets from a previous power up, he gains more). */
	private void bulletPowerUp(){
		nBullets = nBullets + NBULLETS_PER_POWER_UP;
		remove(bulletLabel);
		changeBulletLabel();
	}

/** If the space bar is hit (with no bullets in play), nBullets is reduced by one and a GRect bullet is added where the paddle is. */
	public void keyTyped(KeyEvent e){
		int leftWall = ((getWidth() - WIDTH) / 2);
		int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		
		if((e.getKeyChar() == KeyEvent.VK_SPACE && (nBullets > 0) || ballSackBoolean == true)){
			if(bulletAlive()){}
			else{
				addBullet();
				if (ballSackBoolean == false) nBullets--;
				remove(bulletLabel);
				changeBulletLabel();
			}
		}
		else if(e.getKeyChar() == KeyEvent.VK_R){
			restartBoolean = true;
		}
		else if (e.getKeyChar() == KeyEvent.VK_T){
			if(topSwitch == true) topSwitch = false;
			else if (topSwitch == false) topSwitch = true;
		}
		else if(e.getKeyChar() == KeyEvent.VK_N){
			ruleBoolean = false;
		}
		else if (e.getKeyChar() == KeyEvent.VK_W && dualMotionBoolean == true && initialDualMotionBounce <= nBounces - (NBOUNCES_PER_POWER_UP * 2)){
			paddle.move(0, -5);
			if(paddle.getY() <= MAX_PADDLE_ALTITUDE){
				paddle.setLocation(paddle.getX(), MAX_PADDLE_ALTITUDE + 5);
			}
			if (paddle.getY() >= (bottomWall - PADDLE_HEIGHT)){
				paddle.setLocation(paddle.getX(), (bottomWall - PADDLE_HEIGHT) - 5);
			}
			if(paddle.getX() <= leftWall){
				paddle.setLocation(leftWall + 5, paddle.getY());
			}
			if (paddle.getX() >= (rightWall - paddleWidth)){
				paddle.setLocation((rightWall - paddleWidth) - 5, paddle.getY());
			}
		}
		else if (e.getKeyChar() == KeyEvent.VK_S && dualMotionBoolean == true && initialDualMotionBounce <= nBounces - (NBOUNCES_PER_POWER_UP * 2)){
			paddle.move(0, 5);
			if (paddle.getY() <= MAX_PADDLE_ALTITUDE){
				paddle.setLocation(paddle.getX(), MAX_PADDLE_ALTITUDE + 5);
			}
			if (paddle.getY() >= (bottomWall - PADDLE_HEIGHT)){
				paddle.setLocation(paddle.getX(), (bottomWall - PADDLE_HEIGHT) - 5);
			}
			if(paddle.getX() <= leftWall){
				paddle.setLocation(leftWall + 5, paddle.getY());
			}
			if (paddle.getX() >= (rightWall - paddleWidth)){
				paddle.setLocation((rightWall - paddleWidth) - 5, paddle.getY());
			}
		}
		else if (e.getKeyChar() == KeyEvent.VK_A){
			paddle.move(-10, 0);
			if(paddle.getY() <= MAX_PADDLE_ALTITUDE){
				paddle.setLocation(paddle.getX(), MAX_PADDLE_ALTITUDE + 5);
			}
			if (paddle.getY() >= (bottomWall - PADDLE_HEIGHT)){
				paddle.setLocation(paddle.getX(), (bottomWall - PADDLE_HEIGHT) - 5);
			}
			if(paddle.getX() <= leftWall){
				paddle.setLocation(leftWall + 5, paddle.getY());
			}
			if (paddle.getX() >= (rightWall - paddleWidth)){
				paddle.setLocation((rightWall - paddleWidth) - 5, paddle.getY());
			}
		}
		else if (e.getKeyChar() == KeyEvent.VK_D){
			paddle.move(10, 0);
			if(paddle.getY() <= MAX_PADDLE_ALTITUDE){
				paddle.setLocation(paddle.getX(), MAX_PADDLE_ALTITUDE + 5);
			}
			if (paddle.getY() >= (bottomWall - PADDLE_HEIGHT)){
				paddle.setLocation(paddle.getX(), (bottomWall - PADDLE_HEIGHT) - 5);
			}
			if(paddle.getX() <= leftWall){
				paddle.setLocation(leftWall + 5, paddle.getY());
			}
			if (paddle.getX() >= (rightWall - paddleWidth)){
				paddle.setLocation((rightWall - paddleWidth) - 5, paddle.getY());
			}
		}
		else if (e.getKeyChar() == KeyEvent.VK_P){
			pauseGame = true;
		}
	}
	
/** If there is a bullet on the game board, it will move (and keep the ball moving as normal) until it makes 
 * contact with anything. */
	private void shootBullet(){
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		while(bulletAlive()){
			bullet.move(0, BULLET_VELOCITY);
			checkBulletForHit();
			int topWall = (getHeight() - HEIGHT) / 2;
			if (powerUp.getY() > topWall) movePowerUp();
			moveBall();
			checkForCollisions();
			movePowerUp();
			choosePowerType();
			if(restartBoolean == true)break;
			if (pauseGame == true) pauseGame();
			if (ball.getY() > (bottomWall - (2 * ballRadius))){
				remove(powerUp);
				remove(bullet);
				break;
			}
		}
		bullet.setLocation(-5, -5);
		remove(bullet);
	}

/** Adds the bullet to the screen where the middle of the paddle is. */
	private void addBullet(){
		double bulletX = (paddle.getX() + (.5 * PADDLE_WIDTH));
		double bulletY = (paddle.getY());
		bullet = new GRect(bulletX, bulletY, BULLET_WIDTH, BULLET_HEIGHT);
		bullet.setFilled(true);
		bullet.setFillColor(Color.CYAN);
		add(bullet);
	}

/** Determines if the bullet is "alive" by checking if it is on the game board. */	
	private boolean bulletAlive(){
		int topWall = (getHeight() - HEIGHT) / 2;
		if(bullet.getY() >= topWall){
			return true;
		}
		else return false;
	}

	
/***/
	private void checkBulletForHit(){
		checkTopLeftForHit();
		checkTopRightForHit();
		checkBottomLeftForHit();
		checkBottomRightForHit();
	}
	
/** Checks the bullet for contact with any object on the board. Removes bullet upon contact with any other object and removes any brick it hits. */
	private void checkTopLeftForHit(){
		bulHit = getElementAt(bullet.getX() - 1, bullet.getY() - 1);
		if(bulHit == bullet || bulHit == perim){}
		else if (bulHit == ball || bulHit == powerUp){
			bullet.setLocation(-5, -5);
			remove(bullet);
		}
		else if (bulHit != null && bulHit != paddle){
			remove(bulHit);
			bullet.setLocation(-5, -5);
			remove(bullet);
			nBricks--;
			remove(brickLabel);
			changeBrickLabel();
		}
	}

	
/***/
	private void checkTopRightForHit(){
		bulHit = getElementAt(bullet.getX() + BULLET_WIDTH + 1, bullet.getY() - 1);
		if(bulHit == bullet || bulHit == perim){}
		else if (bulHit == ball || bulHit == powerUp){
			bullet.setLocation(-5, -5);
			remove(bullet);
		}
		else if (bulHit != null && bulHit != paddle){
			remove(bulHit);
			bullet.setLocation(-5, -5);
			remove(bullet);
			nBricks--;
			remove(brickLabel);
			changeBrickLabel();
		}
	}
	
/***/
	private void checkBottomLeftForHit(){
		bulHit = getElementAt(bullet.getX() - 1, bullet.getY() + BULLET_HEIGHT + 1);
		if(bulHit == bullet || bulHit == perim){}
		else if (bulHit == ball || bulHit == powerUp){
			bullet.setLocation(0,0);
			remove(bullet);
		}
		else if (bulHit != null && bulHit != paddle){
			remove(bulHit);
			bullet.setLocation(-5, -5);
			remove(bullet);
			nBricks--;
			remove(brickLabel);
			changeBrickLabel();
		}
	}
	
	
/***/
	private void checkBottomRightForHit(){
		bulHit = getElementAt(bullet.getX() + BULLET_WIDTH + 1, bullet.getY() + BULLET_HEIGHT + 1);
		if(bulHit == bullet || bulHit == perim){}
		else if (bulHit == ball || bulHit == powerUp){
			bullet.setLocation(0,0);
			remove(bullet);
		}
		else if (bulHit != null && bulHit != paddle){
			remove(bulHit);
			bullet.setLocation(-5, -5);
			remove(bullet);
			nBricks--;
			remove(brickLabel);
			changeBrickLabel();
		}
	}
	
/***/
	private void twoPaddlePowerUp(){
		int topWall = (getHeight() - HEIGHT) / 2;
		initialTwoPaddleBounce = nBounces;
		if(paddle2.getY() > topWall){}
		else{
			double y = (((getHeight() - HEIGHT) / 2) + HEIGHT) - PADDLE_2_Y_OFFSET;
			double x = (getWidth() - PADDLE_WIDTH) / 2;
			paddle2 = new GRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
			paddle2.setFilled(true);
			add(paddle2);
			last2 = new GPoint(x,0);
		}
	}
	
/***/
	private void fullMotionPaddlePowerUp(){
		fullMotionBoolean = true;
		initialFullMotionBounce = nBounces;
	}
	
/***/
	private void dualMotionPaddlePowerUp(){
		dualMotionBoolean = true;
		initialDualMotionBounce = nBounces;
	}
	
/***/	
	private void catchBall(){
		catchBallBoolean = true;
		catchBallTally++;
	}

/***/
	private void throwBall(GOval stick){
		if (ballSackBoolean == true && catchBallTally == 1)catchBall1(stick);
		if (ballSackBoolean == true && catchBallTally == 2)catchBall2(stick);
		else{
			while(caughtBall == true){
				if(stick != ball){
					moveBall();
					checkForCollisions();
				}
				if (ball2.getY() > topWall - 5 && stick != ball2){
					moveBall2();
					checkBall2();
				}
				if (ball3.getY() > topWall - 5 && stick != ball3){
					moveBall3();
					checkBall3();
				}
				paddleLoc = paddle.getX();
				usePowerUps();
				if(pauseGame == true) pauseGame();
				if (restartBoolean == true){
					break;
				}
				pause(PAUSE_TIME);
			
				if (catchBallTally == 1){
					double ball1X = paddle.getX();
					double ball1Y = paddle.getY() + PADDLE_HEIGHT;
					stick.setLocation(ball1X, ball1Y);
				}
				if(catchBallTally == 2){
					double ball2X = paddle.getX() + paddleWidth;
					double ball2Y = paddle.getY() + PADDLE_HEIGHT;
					stick.setLocation(ball2X, ball2Y);
				}
				if (throwBallBoolean == true)break;
			}
			if (stick == ball) {
					xVel = 0;
					yVel = -yVel;
				}
			if (stick == ball2){
				xVel2 = 0;
				yVel2 = -yVel2;
			}
			if (stick == ball3){
				xVel3 = 0;
				yVel3 = -yVel3;
			}
			caughtBall = false;
			throwBallBoolean = false;
			catchBallBoolean = false;
		}
	}

	public void mouseClicked (MouseEvent e){
		if(throwBallBoolean == false && caughtBall == true)throwBallBoolean = true;
	}
		
	private void catchBall1(GOval stick){	
		pelota1 = stick;
		while(caughtBall == true){
			if(pelota1 != ball && pelota2 != ball){
				moveBall();
				checkForCollisions();
			}
			if (ball2.getY() > topWall - 5 && pelota1 != ball2 && pelota2 != ball2){
				moveBall2();
				checkBall2();
			}
			if (ball3.getY() > topWall - 5 && pelota1 != ball3 && pelota2 != ball3){
				moveBall3();
				checkBall3();
			}
			paddleLoc = paddle.getX();
			usePowerUps();
			if (pauseGame == true) pauseGame();
			if (restartBoolean == true){
				break;
			}
			pause(PAUSE_TIME);
			if (throwBallBoolean == true)break;
		}
		if (catchBallTally == 1){
			double ball1X = paddle.getX();
			double ball1Y = paddle.getY() + PADDLE_HEIGHT + 1;
			pelota1.setLocation(ball1X, ball1Y);
		}
		if (pelota1 == ball) {
			xVel = 0;
			yVel = -yVel;
		}
		if (pelota1 == ball2){
			xVel2 = 0;
			yVel2 = -yVel2;
		}
		if (pelota1 == ball3){
			xVel3 = 0;
			yVel3 = -yVel3;
		}
		caughtBall = false;
		throwBallBoolean = false;
		catchBallBoolean = false;
	}
	

	private void catchBall2(GOval stick){
		pelota2 = stick;
		while(caughtBall == true){
			if(ball != pelota2  && ball != pelota1){
				moveBall();
				checkForCollisions();
			}
			if (ball2.getY() > topWall - 5 && pelota2 != ball2 && ball2 != pelota1){
				moveBall2();
				checkBall2();
			}
			if (ball3.getY() > topWall - 5 && pelota2 != ball3 && ball3 != pelota1){
				moveBall3();
				checkBall3();
			}
			paddleLoc = paddle.getX();
			usePowerUps();
			if (pauseGame == true) pauseGame();
			if (restartBoolean == true){
				break;
			}
			pause(PAUSE_TIME);
			if (throwBallBoolean == true)break;
		}
		if (catchBallTally == 1){
			double ball1X = paddle.getX();
			double ball1Y = paddle.getY() + PADDLE_HEIGHT + 1;
			pelota2.setLocation(ball1X, ball1Y);
		}
		if(catchBallTally == 2){
			double ball2X = paddle.getX() + paddleWidth;
			double ball2Y = paddle.getY() + PADDLE_HEIGHT + 1;
			pelota2.setLocation(ball2X, ball2Y);
		}
		if (pelota2 == ball) {
			xVel = 0;
			yVel = -yVel;
		}
		if (pelota2 == ball2){
			xVel2 = 0;
			yVel2 = -yVel2;
		}
		if (pelota2 == ball3){
			xVel3 = 0;
			yVel3 = -yVel3;
		}
		caughtBall = false;
		throwBallBoolean = false;
		catchBallBoolean = false;
	}
	
/***/	
	private void invincibleBall(){
		invincibleBallBoolean = true;
		initialInvincibleBallBounce = nBounces;
	}
	
/***/	
	private void addShield(){
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		double y = bottomWall + SHEILD_Y_OFFSET;
		int leftWall = ((getWidth() - WIDTH) / 2);
		int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
		double w = rightWall - leftWall;
		shield = new GRect (leftWall, y, w, SHEILD_HEIGHT);
		shield.setFilled(true);
		shield.setFillColor(Color.MAGENTA);
		add(shield);
	}
	
/***/	
	private void multipleBalls(){
		double x = (getWidth() - (2 * BALL_RADIUS)) / 2;
		double y = (((getHeight() - HEIGHT) / 2) + HEIGHT) - BALL_Y_OFFSET;
		int topWall = (getHeight() - HEIGHT) / 2;	
		if (ball2.getY() < topWall){
			ball2 = new GOval (x, y, (2 * BALL_RADIUS), (2 * BALL_RADIUS));
			ball2.setVisible(true);
			ball2.setFilled(true);
			ball2.setFillColor(Color.RED);
			add(ball2);
			xVel2 = rgen.nextInt(1,3);
			if(rgen.nextBoolean(.5)) xVel2 = -xVel2;
			yVel2 = rgen.nextInt(1,3);
			yVel2 = -yVel2;
		}
		if (ball3.getY() < topWall){
			ball3 = new GOval (x, y + (2 * BALL_RADIUS), (2 * BALL_RADIUS), (2 * BALL_RADIUS));
			ball3.setVisible(true);
			ball3.setFilled(true);
			ball3.setFillColor(Color.RED);
			add(ball3);
			xVel3 = rgen.nextInt(1,3);
			if(rgen.nextBoolean(.5)) xVel3 = -xVel3;
			yVel3 = rgen.nextInt(1,3);
		}
			
	}
	
/***/	
	private void moveBall2(){
		int leftWall = ((getWidth() - WIDTH) / 2);
		int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
		int topWall = (getHeight() - HEIGHT) / 2;	
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		if(ball2.getY() >= topWall - 5){
			ball2.move(xVel2, yVel2);
			if ((ball2.getX() <= leftWall) || (ball2.getX() >= (rightWall - (2 * BALL_RADIUS)))){
				xVel2 = -xVel2;
			}
			if ((ball2.getY() <= topWall + 1)){
				yVel2 = - yVel2;
			}
			if (ball2.getY() >= (bottomWall - (2 * BALL_RADIUS))){
				ball2.setLocation(-5, -5);
				remove(ball2);
			}
		}
	}

/***/	
	private void moveBall3(){
		int leftWall = ((getWidth() - WIDTH) / 2);
		int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
		int topWall = ((getHeight() - HEIGHT) / 2) + 1;		
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		if (ball3.getY() >= topWall - 5){
			ball3.move(xVel3, yVel3);
			if ((ball3.getX() <= leftWall) || (ball3.getX() >= (rightWall - (2 * BALL_RADIUS)))){
				xVel3 = -xVel3;
			}
			if ((ball3.getY() <= topWall + 1)){
				yVel3 = - yVel3;
			}
			if (ball3.getY() >= (bottomWall - (2 * BALL_RADIUS))){
				ball3.setLocation(-5, -5);
				remove(ball3);
			}
		}	
	}
	
/***/	
	private void checkBall2(){
		GPoint top2 = new GPoint ((ball2.getX() + BALL_RADIUS), (ball2.getY() - 1));
		GPoint bottom2 = new GPoint ((ball2.getX() + BALL_RADIUS), (ball2.getY() + (2 * BALL_RADIUS) + 1));
		GPoint right2 = new GPoint ((ball2.getX() + (2 * BALL_RADIUS) + 1), (ball2.getY() + BALL_RADIUS));
		GPoint left2 = new GPoint ((ball2.getX() - 1), (ball2.getY() + BALL_RADIUS));
		checkSides2(top2);
		checkSides2(bottom2);
		checkSides2(right2);
		checkSides2(left2);
	}

/***/	
	private void checkSides2(GPoint check2){
		gobj2 = getElementAt(check2);
		if (gobj2 == paddle || gobj2 == paddle2 || gobj2 == leftNut || gobj2 == rightNut){
			if (yVel2 > 0) yVel2 = (-BOUNCE_INCREASE_RATIO * yVel2);
			paddleSpeed = paddle.getX() - paddleLoc;
			if (catchBallBoolean == true){
				caughtBall = true;
				catchBallTally++;
				throwBall(ball2);
			}
		}
		else if (gobj2 == shield){
			remove(shield);
			yVel2 = -yVel2;
		}
		else if (gobj2 == leftCup || gobj2 == rightCup){
			xVel2 = -xVel2;
		}
		else if (gobj2 == ball || gobj2 == ball3){
			if(check2.getX() == ball2.getX() + BALL_RADIUS){
				yVel2 = (-BOUNCE_INCREASE_RATIO * yVel2);
			}
			else {
				xVel2 = (-BOUNCE_INCREASE_RATIO * xVel2);
			}
		}
		else if (gobj2 == break1 || gobj2 == break2 || gobj2 == break3 || gobj2 == break4){}
		else if (gobj2 != null && gobj2 != perim && gobj2 != powerUp && gobj2 != bullet){
			remove(gobj2);
			if (invincibleBallBoolean == true && initialInvincibleBallBounce + NBOUNCES_PER_INVINCIBLE_BALL >= nBounces){}
			else if(check2.getX() == ball2.getX() + BALL_RADIUS){
				yVel2 = (-BOUNCE_INCREASE_RATIO * yVel2);
			}
			else {
				xVel2 = (-BOUNCE_INCREASE_RATIO * xVel2);
				if (xVel2 == 0 && check2.getX() == ball2.getX()) xVel2 = 3;
				if (xVel2 == 0 && check2.getX() == (ball2.getX() + (2 * BALL_RADIUS))) xVel2 = -3;
			}
			nBricks--;
			remove(brickLabel);
			changeBrickLabel();
		}
		
	}
	
/***/	
	private void checkBall3(){
		GPoint top3 = new GPoint ((ball3.getX() + BALL_RADIUS), (ball3.getY() - 1));
		GPoint bottom3 = new GPoint ((ball3.getX() + BALL_RADIUS), (ball3.getY() + (2 * BALL_RADIUS) + 1));
		GPoint right3 = new GPoint ((ball3.getX() + (2 * BALL_RADIUS) + 1), (ball3.getY() + BALL_RADIUS));
		GPoint left3 = new GPoint ((ball3.getX() - 1), (ball3.getY() + BALL_RADIUS));
		checkSides3(top3);
		checkSides3(bottom3);
		checkSides3(right3);
		checkSides3(left3);
	}

/***/	
	private void checkSides3(GPoint check3){
		gobj3 = getElementAt(check3);
		if (gobj3 == paddle || gobj3 == paddle2 || gobj3 == leftNut || gobj3 == rightNut){
			if (yVel3 > 0) yVel3 = (-BOUNCE_INCREASE_RATIO * yVel3);
			paddleSpeed = paddle.getX() - paddleLoc;
			if (catchBallBoolean == true){
				caughtBall = true;
				catchBallTally++;
				throwBall(ball3);
			}
		}
		else if (gobj3 == shield){
			remove(shield);
			yVel3 = -yVel3;
		}
		else if (gobj3 == leftCup || gobj3 == rightCup){
			xVel3 = -xVel3;
		}
		else if (gobj3 == ball || gobj3 == ball2){
			if(check3.getX() == ball3.getX() + BALL_RADIUS){
				yVel3 = (-BOUNCE_INCREASE_RATIO * yVel3);
			}
			else {
				xVel3 = (-BOUNCE_INCREASE_RATIO * xVel3);
			}
		}
		else if (gobj3 == break1 || gobj3 == break2 || gobj3 == break3 || gobj3 == break4){}
		else if (gobj3 != null && gobj3 != perim && gobj3 != powerUp && gobj3 != bullet){
			remove(gobj3);
			if (invincibleBallBoolean == true && initialInvincibleBallBounce + NBOUNCES_PER_INVINCIBLE_BALL >= nBounces){}
			else if(check3.getX() == ball3.getX() + BALL_RADIUS){
				yVel3 = (-BOUNCE_INCREASE_RATIO * yVel3);
			}
			else {
				xVel3 = (-BOUNCE_INCREASE_RATIO * xVel3);
				if (xVel2 == 0 && check3.getX() == ball3.getX()) xVel3 = 3;
				if (xVel2 == 0 && check3.getX() == (ball3.getX() + (2 * BALL_RADIUS))) xVel3 = -3;
			}
			nBricks--;
			remove(brickLabel);
			changeBrickLabel();
		}
	}
	
/***/	
	private void giantBall(){
		ballRadius =  ballRadius + GIANT_BALL_RADIUS_INCREASE;
		remove(ball);
		double x = ball.getX();
		double y = ball.getY();
		ball = new GOval (x, y, (2 * ballRadius), (2 * ballRadius));
		ball.setFilled(true);
		add(ball);
	}
	
/***/	
	private void ballSack(double x, double y){
		ballSackBoolean = true;
		catchBallBoolean = false;
		caughtBall = false;
		catchBallTally =0;
		remove(paddle);
		remove(ball);
		if (ball2.getY() > topWall){
			ball2.setLocation(-1, 10);
			remove(ball2);
		}
		if (ball2.getY() > topWall){
			ball3.setLocation(-1, 10);
			remove(ball3);
		}
		double paddleX = x + ((PADDLE_WIDTH - PADDLE_HEIGHT) / 2);
		double paddleY = y + PADDLE_HEIGHT - paddleWidth;
		paddle = new GRect(paddleX, paddleY, PADDLE_HEIGHT, paddleWidth);
		paddle.setFilled(true);
		add(paddle);
		leftNut = new GOval (paddleX - (2 * BALL_RADIUS) + (PADDLE_HEIGHT / 2), y, (2 * BALL_RADIUS), (2 * BALL_RADIUS));
		rightNut = new GOval (paddleX + (PADDLE_HEIGHT / 2), y, (2 * BALL_RADIUS), (2 * BALL_RADIUS));
		leftNut.setFilled(true);
		rightNut.setFilled(true);
		add(leftNut);
		add(rightNut);
		shootBall();
		ballSacked = true;
	}
	
/***/	
	private void fireBall(){
		fireBallBoolean = true;
	}
	
	private void removeSurroundingBricks(){
		fireLeft = getElementAt(gobj.getX() - BRICK_SEP - 1, gobj.getY() + 1);
		if (fireLeft != null && fireLeft.getWidth() == BRICK_WIDTH && fireLeft.getHeight() == BRICK_HEIGHT){
			nBricks--;
			remove(fireLeft);
		}
		fireRight = getElementAt(gobj.getX() + BRICK_SEP + BRICK_WIDTH + 1, gobj.getY() + 1);
		if (fireRight.getWidth() == BRICK_WIDTH && fireRight.getHeight() == BRICK_HEIGHT && fireRight != null) {
			nBricks--;
			remove(fireRight);
		}
		fireUp = getElementAt(gobj.getX() + 1, gobj.getY() - BRICK_SEP - 1);
		if (fireUp.getWidth() == BRICK_WIDTH && fireUp.getHeight() == BRICK_HEIGHT && fireUp != null){
			nBricks--;
			remove(fireUp);
		}
		fireDown = getElementAt(gobj.getX() + 1, gobj.getY() + BRICK_HEIGHT + BRICK_SEP + 1);
		if (fireDown.getWidth() == BRICK_WIDTH && fireDown.getHeight() == BRICK_HEIGHT && fireDown != null){
			nBricks--;
			remove(fireDown);
		}
		fireUpLeft = getElementAt(gobj.getX() - BRICK_SEP - 1, gobj.getY() - BRICK_SEP - 1);
		if (fireUpLeft != null && fireUpLeft.getWidth() == BRICK_WIDTH && fireUpLeft.getHeight() == BRICK_HEIGHT) {
			nBricks--;
			remove(fireUpLeft);
		}
		fireUpRight = getElementAt(gobj.getX() + BRICK_SEP + BRICK_WIDTH + 1, gobj.getY() - BRICK_SEP - 1);
		if (fireUpRight.getWidth() == BRICK_WIDTH && fireUpRight.getHeight() == BRICK_HEIGHT && fireUpRight != null){
			nBricks--;
			remove(fireUpRight);
		}
		fireDownLeft = getElementAt(gobj.getX() - BRICK_SEP - 1, gobj.getY() + BRICK_HEIGHT + BRICK_SEP + 1);
		if (fireDownLeft != null && fireDownLeft.getWidth() == BRICK_WIDTH && fireDownLeft.getHeight() == BRICK_HEIGHT){
			nBricks--;
			remove(fireDownLeft);
		}
		fireDownRight = getElementAt(gobj.getX() + BRICK_WIDTH + BRICK_SEP + 1, gobj.getY() + BRICK_HEIGHT + BRICK_SEP + 1);
		if (fireDownRight.getWidth() == BRICK_WIDTH && fireDownRight.getHeight() == BRICK_HEIGHT && fireDownRight != null){
			nBricks--;
			remove(fireDownRight);
		}
		fireBallBoolean = false;
	}
	
/***/	
	private void fanBall(){
		fanBallBoolean = true;
		initialFanBallBounce = nBounces;
	}
	
/***/
	private void useFanBall(){
		double z = (2 * ballRadius);
		double topY = paddle.getY() - FAN_BALL_DISTANCE;
		double bottomY = paddle.getY() + z;
		double leftX = paddle.getX() - z;
		double rightX = paddle.getX() + z;
		if (ball.getY() >= topY && ball.getY() <= bottomY && ball.getX() >= leftX && ball.getX() <= rightX) {
			if (yVel > 0){
			yVel = -(FAN_BALL_BOUNCE_INCREASE + yVel);
			nBounces++;
			}
		}
	}
	
/***/	
	private void smallBall(){
		ballRadius =  SMALL_BALL_RADIUS;
		remove(ball);
		double x = ball.getX();
		double y = ball.getY();
		ball = new GOval (x, y, (2 * ballRadius), (2 * ballRadius));
		ball.setFilled(true);
		add(ball);
	}
	
/***/	
	private void cupPaddle(){
		int topWall = (getHeight() - HEIGHT) / 2;
		if(rightCup.getY() < topWall){
			cupPaddleBoolean = true;
			leftCup = new GRect (paddle.getX(), (paddle.getY() - CUP_HEIGHT), CUP_WIDTH, CUP_HEIGHT);
			rightCup = new GRect ((paddle.getX() + paddleWidth - CUP_WIDTH), (paddle.getY() - CUP_HEIGHT), CUP_WIDTH, CUP_HEIGHT);
			leftCup.setFilled(true);
			rightCup.setFilled(true);
			add(rightCup);
			add(leftCup);
			initialCupPaddleBounce = nBounces;
		}
	}
	
/** Moves 4 pieces of explosion through up/down methods. */
	private void moveBreaks(){
		moveBreaksUp();
		moveBreaksDown();
	}

/**If explosionBoolean = true, the next brick hit will "explode" by sending 1/4 of the brick flying in 4 directions then disappearing. */		
	private void explodeBrick(GObject expl){
		Color color = expl.getColor();
		double x = expl.getX();
		double y = expl.getY();
		double explWidth = BRICK_WIDTH / 2;
		double explHeight = BRICK_HEIGHT/ 2;
		break1 = new GRect(x, y, explWidth, explHeight);
		break2 = new GRect(x + explWidth, y, explWidth, explHeight);
		break3 = new GRect(x, y + explHeight, explWidth, explHeight);
		break4 = new GRect(x + explHeight, y + explWidth, explWidth, explHeight);
		break1.setFilled(true);
		break2.setFilled(true);
		break3.setFilled(true);
		break4.setFilled(true);
		break1.setFillColor(color);
		break2.setFillColor(color);
		break3.setFillColor(color);
		break4.setFillColor(color);
		add(break1);
		add(break2);
		add(break3);
		add(break4);
	}
	

/** Moves the 4 pieces of the explosion up. */
	private void moveBreaksUp(){
		int topWall = (getHeight() - HEIGHT) / 2;
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		for(int i = 0; i < 10; i++){
			if (powerUp.getY() >= topWall && powerUp.getY() <= bottomWall){
				movePowerUp();
				choosePowerType();
				break1.move(-BREAK_X_VEL, -BREAK_Y_VEL);
				break2.move(BREAK_X_VEL, -BREAK_Y_VEL);
				break3.move(-BREAK_X_VEL, BREAK_Y_VEL);
				break4.move(BREAK_X_VEL, BREAK_Y_VEL);
				if (ball.getY() > (bottomWall - (2 * ballRadius))){
					remove(break1);
					remove(break2);
					remove(break3);
					remove(break4);
					break;
				}
			}
			else{
				break1.move(-BREAK_X_VEL, -BREAK_Y_VEL);
				break2.move(BREAK_X_VEL, -BREAK_Y_VEL);
				break3.move(-BREAK_X_VEL, BREAK_Y_VEL);
				break4.move(BREAK_X_VEL, BREAK_Y_VEL);
				moveBall();
				usePowerUps();
				checkForCollisions();
				if (ball.getY() > (bottomWall - (2 * ballRadius))){
					remove(break1);
					remove(break2);
					remove(break3);
					remove(break4);
					break;
				}
			}
		}
	}	

/**Moves the 4 pieces of the explosion down. */	
	private void moveBreaksDown(){	
		int topWall = (getHeight() - HEIGHT) / 2;
		int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
		for (int i = 0; i < 10; i++){
			if (powerUp.getY() >= topWall && powerUp.getY() <= bottomWall){
				movePowerUp();
				choosePowerType();
				break1.move(-BREAK_X_VEL, BREAK_Y_VEL);
				break2.move(BREAK_X_VEL, BREAK_Y_VEL);
				break3.move(-BREAK_X_VEL, BREAK_Y_VEL);
				break4.move(BREAK_X_VEL, BREAK_Y_VEL);
				if (ball.getY() > (bottomWall - (2 * ballRadius))){
					remove(break1);
					remove(break2);
					remove(break3);
					remove(break4);
					break;
				}
			}
			else{
				break1.move(-BREAK_X_VEL, BREAK_Y_VEL);
				break2.move(BREAK_X_VEL, BREAK_Y_VEL);
				break3.move(-BREAK_X_VEL, BREAK_Y_VEL);
				break4.move(BREAK_X_VEL, BREAK_Y_VEL);
				moveBall();
				usePowerUps();
				checkForCollisions();
				if (ball.getY() > (bottomWall - (2 * ballRadius))){
					remove(break1);
					remove(break2);
					remove(break3);
					remove(break4);
					break;
				}
			}
		}
		remove(break1);
		remove(break2);
		remove(break3);
		remove(break4);
	}
	
/***/	
	private void restartGame(){
		removeAll();
		setUpBoard();
		initiateVariables();
		waitForClick();
	}
	
	private void pauseGame(){
		GRect white = new GRect (0, 0, getWidth(), getHeight());
		white.setColor(Color.WHITE);
		white.setFilled(true);
		add(white);
		GLabel pause = new GLabel ("GAME PAUSED");
		GLabel cont = new GLabel ("Click to Continue");
		add(pause, ((getWidth() - pause.getWidth()) / 2), (getHeight() / 2));
		add(cont, ((getWidth() - cont.getWidth()) / 2), (pause.getY() + (4 * pause.getHeight())));
		waitForClick();
		pauseGame = false;
		remove(white);
		remove(pause);
		remove(cont);
	}

	
/**Private instance variables for general game. */
	private GObject gobj;
	private GRect perim, brick, paddle;
	private GOval ball;
	private GLabel err, brickLabel, turnLabel;
	private GPoint last;
	private double xVel, yVel, paddleWidth, paddleSpeed, paddleLoc, boardWidth, boardHeight;
	private int nBricks, nTurns, nBounces, ballRadius;
	private boolean restartBoolean, topSwitch, pauseGame;
	
/**Private instance variables for power ups. */
	private GObject bulHit, gobj2, gobj3, fireLeft, fireRight, fireUp, fireDown, fireUpLeft, fireUpRight, fireDownLeft, fireDownRight;
	private GOval ball2, ball3, pelota1, pelota2;
	private GRect paddle2, powerUp, bullet, shield, leftCup, rightCup;
	private GLabel bulletLabel, powerLabel;
	private GPoint last2;
	private double xVel2, xVel3, yVel2, yVel3;
	private int nBullets, rand, initialTwoPaddleBounce, initialFullMotionBounce, initialDualMotionBounce, initialInvincibleBallBounce;
	private int initialFanBallBounce, initialCupPaddleBounce, initialPowerLabelBounce;
	private boolean fullMotionBoolean, dualMotionBoolean, catchBallBoolean, caughtBall, invincibleBallBoolean, fanBallBoolean;
	private boolean throwBallBoolean, cupPaddleBoolean, twoPaddleBoolean, fireBallBoolean;
	
/** Private instance variables for Super Power Up. */
	private GOval leftNut, rightNut;
	private int crazyUp, widePaddleTally, catchBallTally;
	private boolean crazyPowerUpBoolean, playWithPowerUps, ballSackBoolean, ballSacked;
	
/**Private instance variables for explosions. */
	private GRect break1, break2, break3, break4;
	private boolean explosionBoolean;
	
/** Private instance variables for rules. */	
	private GLabel rulesContinue;
	private int ruleInt;
	private boolean ruleBoolean, explainRules;
	
	private GLabel test;
	private int timeCount;
	private int loopCount;
	private GLabel timer;
	
/**Private instance variable for random number generator */
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	int leftWall = ((getWidth() - WIDTH) / 2);
	int rightWall = ((getWidth() - WIDTH) / 2) + WIDTH;
	int topWall = (getHeight() - HEIGHT) / 2;
	int bottomWall = ((getHeight() - HEIGHT) / 2) + HEIGHT;
	
}

