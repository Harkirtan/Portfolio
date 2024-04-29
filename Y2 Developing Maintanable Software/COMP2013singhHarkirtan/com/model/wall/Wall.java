package com.model.wall;

import com.model.ball.Ball;
import com.model.ball.BallFactory;
import com.model.brick.Brick;
import com.model.brick.Crack;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Generates levels in form of brick array, generates the ball and manages
 * ball/paddle impacts
 * Added singleton to this class
 * @author Harkirtan Singh-modified
 */
public class Wall {

    public final int LEVELS_COUNT = 6;
    public final int THREE = 3;
    public final int CLAY_RED = 1;
    public final int CLAY_BLUE = 2;
    public final int CEMENT = 3;
    public final int STEEL = 4;
    public final int PADDLE_WIDTH = 150;
    public final int PADDLE_HEIGHT = 10;
    public final int FIVE = 5;
    public final int TWO = 2;

    private static Wall m_wall;

    private Random m_rnd;
    private Rectangle m_area;
    private Brick[] m_bricks;
    private Ball m_ball;
    private Paddle m_player;
    private Brick[][] m_levels;
    private int m_level;
    private Point m_startPoint;
    private int m_brickCount;
    private int m_ballCount;
    private boolean m_ballLost;

    /**
     * Getter for random
     * @return random object
     */
    private Random getM_rnd() {
        return m_rnd;
    }

    /**
     * Setter for random
     * @param m_rnd  random object
     */
    private void setM_rnd(Random m_rnd) {
        this.m_rnd = m_rnd;
    }

    /**
     * Getter for area of wall
     * @return area of the wall
     */
    private Rectangle getM_area() {
        return m_area;
    }

    /**
     * Setter for area
     * @param m_area area of the wall
     */
    private void setM_area(Rectangle m_area) {
        this.m_area = m_area;
    }

    /**
     * Getter for brick array
     * @return brick array that holds all bricks per level
     */
    public Brick[] getM_bricks() {
        return m_bricks;
    }

    /**
     * Setter for brick array
     * @param m_bricks the brick array that holds all bricks per level
     */
    private void setM_bricks(Brick[] m_bricks) {
        this.m_bricks = m_bricks;
    }

    /**
     * Getter for ball
     * @return the ball currently being used
     */
    public Ball getM_ball() {
        return m_ball;
    }

    /**
     * Setter for ball
     * @param m_ball the ball to set
     */
    private void setM_ball(Ball m_ball) {
        this.m_ball = m_ball;
    }

    /**
     * Getter for paddle player
     * @return the player's paddle
     */
    public Paddle getM_player() {
        return m_player;
    }

    /**
     * Setter for paddle player
     * @param m_player the paddle to set
     */
    private void setM_player(Paddle m_player) {
        this.m_player = m_player;
    }

    /**
     * Getter for levels
     * @return the 2d brick array which holds all levels
     */
    private Brick[][] getM_levels() {
        return m_levels;
    }

    /**
     * Setter for levels
     * @param m_levels the 2d brick array which holds all levels
     */
    private void setM_levels(Brick[][] m_levels) {
        this.m_levels = m_levels;
    }

    /**
     * Getter for level count
     * @return the amount of levels
     */
    public int getM_level() {
        return m_level;
    }

    /**
     * Setter for level count
     * @param m_level the amount of levels
     */
    private void setM_level(int m_level) {
        this.m_level = m_level;
    }

    /**
     * Getter for start point
     * @return the start coordinates
     */
    private Point getM_startPoint() {
        return m_startPoint;
    }

    /**
     * Setter for start point
     * @param m_startPoint the start coordinates
     */
    private void setM_startPoint(Point m_startPoint) {
        this.m_startPoint = m_startPoint;
    }

    /**
     * Getter for amount of bricks
     * @return amount of bricks
     */
    public int getM_brickCount() {
        return m_brickCount;
    }

    /**
     * Setter for amount of bricks
     * @param m_brickCount the amount of bricks
     */
    private void setM_brickCount(int m_brickCount) {
        this.m_brickCount = m_brickCount;
    }

    /**
     * Getter for ball count
     * @return current ball count
     */
    public int getM_ballCount() {
        return m_ballCount;
    }

    /**
     * Setter for ball count
     * @param m_ballCount amount of balls to be set
     */
    private void setM_ballCount(int m_ballCount) {
        this.m_ballCount = m_ballCount;
    }

    /**
     * Gets if ball is lost
     * @return true if current ball is lost
     */
    public boolean getM_ballLost() {return m_ballLost;}

    /**
     * Sets if ball is lost
     * @param m_ballLost boolean to be set for if the ball is lost
     */
    private void setM_ballLost(boolean m_ballLost) {
        this.m_ballLost = m_ballLost;
    }

    /**
     * Set ball X speed
     * @param s the x speed of the ball
     */
    public void setBallXSpeed(int s) {
        getM_ball().setM_speedX(s);
    }

    /**
     * Set ball Y speed
     * @param s the y speed of the ball
     */
    public void setBallYSpeed(int s) {
        getM_ball().setM_speedY(s);
    }

    /**
     * Singleton instance getter which creates a new wall if one is not created
     * @return wall instance object
     */
    public synchronized static Wall getInstance(){
        if (m_wall == null){
            m_wall = new Wall();
        }
        return m_wall;
    }

    /**ry
     * Initializer to generate wall with amount of bricks and lines
     * @param drawArea the rectangle in which the wall will be made
     * @param ballPos the initial position of the ball
     * @param brickCount the total amount of bricks
     * @param lineCount the amount of lines per wall
     */
    public void initialise(Rectangle drawArea, int brickCount, int lineCount,
                           Point ballPos) {

        this.setM_startPoint( new Point(ballPos));

        setM_levels(makeLevels(drawArea, brickCount, lineCount));
        setM_level(0);

        setM_ballCount(THREE);
        setM_ballLost(false);

        setM_rnd(new Random());


        setM_player(new Paddle((Point) ballPos.clone(), PADDLE_WIDTH,
                PADDLE_HEIGHT, drawArea));
        setM_area(drawArea);
    }

    /**
     * Creates a ball from factory and sets its position and speed
     * @param ballPos the coordinates of the ball
     * @param ballT the type of the ball
     */
    public void makeWallBall(Point2D ballPos, int ballT) {
        BallFactory ballFactory = new BallFactory();
        setM_ball(ballFactory.getBall(ballT, ballPos));
        int speedX, speedY;
        do {
            speedX = getM_rnd().nextInt(FIVE) - TWO;
        } while (speedX == 0);
        do {
            speedY = -getM_rnd().nextInt(THREE);
        } while (speedY == 0);

        getM_ball().setSpeed(speedX, speedY);
    }

    /**
     * Creates levels and adds them to the Brick[][] array
     * @param dA the draw area where levels will be made
     * @param brCount the amount of bricks per level
     * @param lCount the amount of lines per level
     * @return Brick[][] with all levels
     */
    public Brick[][] makeLevels(Rectangle dA, int brCount, int lCount) {
        Brick[][] tmp = new Brick[LEVELS_COUNT][];
        LevelManager levelType = new LevelManager();
        tmp[0] = levelType.makeArrowLevel(dA, brCount, lCount, CLAY_BLUE, CLAY_RED);
        tmp[1] = levelType.makeRandomLevel(dA, brCount, lCount, CLAY_BLUE, CLAY_RED);
        tmp[2] = levelType.makeSingleTypeLevel(dA, brCount, lCount, CLAY_RED);
        tmp[3] = levelType.makeAsymmetricalLevel(dA, brCount, lCount, CLAY_RED, CEMENT);
        tmp[4] = levelType.makeAltTypeLevel(dA, brCount, lCount, STEEL, CLAY_RED);
        tmp[5] = levelType.makeAsymmetricalLevel(dA, brCount, lCount, STEEL, CEMENT);
        return tmp;
    }

    /**
     * Move player and ball
     */
    public void moveBall() {
        getM_player().movePaddle();
        getM_ball().moveBall();
    }

    /**
     * Check ball and paddle collision, reverse ball movement if impacted.
     * If ball is lost then reduce ball count
     */
    public void findImpacts() {
        if (getM_player().impact(getM_ball())) {
            getM_ball().reverseY();
        } else if (impactWall()) {
            // for efficiency reverse is done into method impactWall
            // because for every brick program checks for horizontal and vertical impacts
            int temp = getM_brickCount();
            setM_brickCount(temp--);
            setM_brickCount(temp);
        } else if (impactBorder()) {
            getM_ball().reverseX();
        } else if (getM_ball().getM_center().getY() < getM_area().getY()) {
            getM_ball().reverseY();
        } else if (getM_ball().getM_center().getY() >
                getM_area().getY() + getM_area().getHeight()) {
            int temp = getM_ballCount();
            setM_ballCount(temp--);
            setM_ballCount(temp);
            setM_ballLost(true);
        }
    }

    /**
     * Checks if ball impacts wall then impact the brick from side of collision
     * @return true if ball impacts a brick
     */
    public boolean impactWall() {
        for (Brick b : getM_bricks()) {
            switch (b.findImpact(getM_ball())) {

                //Vertical Impact
                case Brick.UP_IMPACT -> {
                    getM_ball().reverseY();
                    return b.setImpact(getM_ball().getM_down(), Crack.UP);
                }
                case Brick.DOWN_IMPACT -> {
                    getM_ball().reverseY();
                    return b.setImpact(getM_ball().getM_up(), Crack.DOWN);
                }

                //Horizontal Impact
                case Brick.LEFT_IMPACT -> {
                    getM_ball().reverseX();
                    return b.setImpact(getM_ball().getM_right(), Crack.RIGHT);
                }
                case Brick.RIGHT_IMPACT -> {
                    getM_ball().reverseX();
                    return b.setImpact(getM_ball().getM_left(), Crack.LEFT);
                }
            }
        }
        return false;
    }

    /**
     * Checks if ball impacts edge of screen
     * @return true if ball impacts the edge
     */
    public boolean impactBorder() {
        Point2D p = getM_ball().getM_center();
        return ((p.getX() < getM_area().getX()) ||
                (p.getX() > (getM_area().getX() + getM_area().getWidth())));
    }

    /**
     * Restore ball and player to original positions and reset ball speeds
     */
    public void gameReset() {
        getM_player().moveTo(getM_startPoint());
        getM_ball().moveTo(getM_startPoint());
        int speedX, speedY;
        do {
            speedX = getM_rnd().nextInt(FIVE) - TWO;
        } while (speedX == 0);
        do {
            speedY = -getM_rnd().nextInt(THREE);
        } while (speedY == 0);

        getM_ball().setSpeed(speedX, speedY);
        setM_ballLost(false);
    }

    /**
     * Restore, repair wall and reset ball count
     */
    public void wallReset() {
        for (Brick b : getM_bricks())
            b.repair();
        setM_brickCount(getM_bricks().length);
        setM_ballCount(THREE);
    }

    /**
     * Checks if balls are finished
     * @return true if ball count is 0
     */
    public boolean ballEnd() {
        return getM_ballCount() == 0;
    }

    /**
     * Checks if there are anymore bricks
     * @return true if brick count is 0
     */
    public boolean isDone() {
        return getM_brickCount() == 0;
    }

    /**
     * Skip to next level in array
     */
    public void nextLevel() {
        Brick[][] temp = getM_levels();
        int level = getM_level();
        setM_bricks(temp[level++]);
        setM_level(level);
        this.setM_brickCount(getM_bricks().length);
    }

    /**
     * Check if there is a next level
     * @return true if there is no more levels
     */
    public boolean hasLevel() {
        return getM_level() < getM_levels().length;
    }


    /**
     * Restore ball count to original
     */
    public void resetBallCount() {
        setM_ballCount(THREE);
    }

}
