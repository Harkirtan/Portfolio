package com.model.wall;

import com.model.ball.Ball;

import java.awt.*;

/**
 * Player paddle is made in the form of a rectangle.
 * Manages the movement logic for the paddle
 * @author Harkirtan Singh-modified
 */
public class Paddle {

    private final int TWO = 2;

    public final int DEF_MOVE_AMOUNT = 5;

    private Rectangle m_paddleFace;
    private Point m_ballPoint;
    private int m_moveAmount;
    private int m_min;
    private int m_max;

    /**
     * Getter for the paddleFace shape
     * @return the shape of the paddle
     */
    public Rectangle getM_paddleFace() {return m_paddleFace;}

    /**
     * Setter for the paddleFace shape
     * @param m_paddleFace the shape of the paddle
     */
    private void setM_paddleFace(Rectangle m_paddleFace) {
        this.m_paddleFace = m_paddleFace;
    }

    /**
     * Getter for the ball's position
     * @return the coordinates of the ball
     */
    private Point getM_ballPoint() {
        return m_ballPoint;
    }

    /**
     * Setter for the ball's position
     * @param m_ballPoint the coordinates of the ball
     */
    private void setM_ballPoint(Point m_ballPoint) {
        this.m_ballPoint = m_ballPoint;
    }

    /**
     * Getter for the paddle's move amount
     * @return the amount the paddle moves
     */
    private int getM_moveAmount() {
        return m_moveAmount;
    }

    /**
     * Setter for the paddle's move amount
     * @param m_moveAmount the amount the paddle moves
     */
    private void setM_moveAmount(int m_moveAmount) {
        this.m_moveAmount = m_moveAmount;
    }

    /**
     * Getter for the paddle's minimum
     * @return the minimum of the paddle
     */
    private int getM_min() {
        return m_min;
    }

    /**
     * Setter for the paddle's minimum
     * @param m_min  the minimum of the paddle
     */
    private void setM_min(int m_min) {
        this.m_min = m_min;
    }

    /**
     * Getter for the paddle's maximum
     * @return the maximum of the paddle
     */
    private int getM_max() {
        return m_max;
    }

    /**
     * Setter for the paddle's maximum
     * @param m_max the maximum of the paddle
     */
    private void setM_max(int m_max) {
        this.m_max = m_max;
    }

    /**
     * Constructor for the paddle that creates a non-moving rectangular paddle
     * @param ballPoint the coordinates of the ball
     * @param width the width of the paddle
     * @param height the height of the paddle
     * @param container the shape of the paddle
     */
    public Paddle(Point ballPoint, int width, int height, Rectangle container) {
        this.setM_ballPoint(ballPoint);
        setM_moveAmount(0);
        setM_paddleFace(makeRectangle(width, height));
        setM_min(container.x + (width / TWO));
        setM_max(getM_min() + container.width - width);

    }

    /**
     * Generates the paddle rectangle shape
     * @param height the height of the paddle that is set
     * @param width the width of the paddle that is set
     * @return rectangular shape of the paddle
     */
    public Rectangle makeRectangle(int width, int height) {
        int x = (int) (getM_ballPoint().getX() - (width / TWO));
        int y = (int) getM_ballPoint().getY();
        Point p = new Point(x, y);
        return new Rectangle(p, new Dimension(width, height));
    }

    /**
     * Checks for impact with the ball using the ball point
     * @param b the ball which is checked for impact with the paddle
     * @return true if impacted
     */
    public boolean impact(Ball b) {
        return getM_paddleFace().contains(b.getM_center()) &&
                getM_paddleFace().contains(b.getM_down());
    }

    /**
     * Checks if moving the paddle will move it into the ball and if not move
     * the paddle with move amount
     */
    public void movePaddle() {
        double x = getM_ballPoint().getX() + getM_moveAmount();
        if (x < getM_min() || x > getM_max())
            return;
        getM_ballPoint().setLocation(x, getM_ballPoint().getY());
        int xValue = getM_ballPoint().x - (int) getM_paddleFace().getWidth() / TWO;
        int yValue = getM_ballPoint().y;
        getM_paddleFace().setLocation(xValue , yValue);
    }

    /**
     * Changes which way paddle moves, left here
     */
    public void moveLeft() {
        setM_moveAmount(-DEF_MOVE_AMOUNT);
    }

    /**
     * Changes which way paddle moves, right here
     */
    //Fixed typo
    public void moveRight() {
        setM_moveAmount(DEF_MOVE_AMOUNT);
    }

    /**
     * Changes which way paddle moves, stops
     */
    public void stop() {
        setM_moveAmount(0);
    }

    /**
     * Moves paddle to a specific location
     * @param p the point to move to
     */
    public void moveTo(Point p) {
        getM_ballPoint().setLocation(p);
        int xValue = getM_ballPoint().x - (int) getM_paddleFace().getWidth() / TWO;
        int yValue = getM_ballPoint().y;
        getM_paddleFace().setLocation(xValue, yValue);
    }
}
