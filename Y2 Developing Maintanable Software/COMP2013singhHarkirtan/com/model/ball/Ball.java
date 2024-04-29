package com.model.ball;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

/**
 * Abstract ball class used to initialise other balls which sets properties
 * like the speed of the ball, and it's location
 * @author Harkirtan Singh-modified
 */
abstract public class Ball {

    private final int TWO = 2;

    private Shape m_ballFace;
    private Point2D m_center;
    private Point2D m_up;
    private Point2D m_down;
    private Point2D m_left;
    private Point2D m_right;

    private int m_speedX;
    private int m_speedY;

    /**
     * Getter for the up Point of the ball
     * @return the top point of the ball
     */
    public Point2D getM_up() {
        return m_up;
    }

    /**
     * Getter for the down Point of the ball
     * @return the bottom point of the ball
     */
    public Point2D getM_down() {
        return m_down;
    }

    /**
     * Getter for the left Point of the ball
     * @return the left point of the ball
     */
    public Point2D getM_left() {
        return m_left;
    }

    /**
     * Getter for the right Point of the ball
     * @return the right point of the ball
     */
    public Point2D getM_right() {
        return m_right;
    }

    /**
     * Abstract method returns the name of the ball
     * @return implemented name of the ball
     */
    public abstract String getNAME();

    /**
     * Setter for the x speed of the ball
     * @param s the x value for the speed
     */
    public void setM_speedX(int s) {
        m_speedX = s;
    }

    /**
     * Setter for the y speed of the ball
     * @param s the y value for the speed
     */
    public void setM_speedY(int s) {
        m_speedY = s;
    }

    /**
     * Getter for the x speed of the ball
     * @return the x value of the speed
     */
    public int getM_speedX() {
        return m_speedX;
    }

    /**
     * Getter for the y speed of the ball
     * @return the y value of the speed
     */
    public int getM_speedY() {
        return m_speedY;
    }

    /**
     * Sets speed value for both x and y
     * @param x the x speed of the ball
     * @param y the y speed of the ball
     */
    public void setSpeed(int x, int y) {
        m_speedX = x;
        m_speedY = y;
    }

    /**
     * Getter for position of the ball
     * @return the center point of the ball
     */
    public Point2D getM_center() {
        return m_center;
    }

    /**
     * Updates the 4 ball Points using the center point
     * @param height height of the ball
     * @param width width of the ball
     */
    public void setPoints(double width, double height) {
        getM_up().setLocation(getM_center().getX(),
                getM_center().getY() - (height / TWO));

        getM_down().setLocation(getM_center().getX(),
                getM_center().getY() + (height / TWO));

        getM_left().setLocation(getM_center().getX() - (width / TWO),
                getM_center().getY());

        getM_right().setLocation(getM_center().getX() + (width / TWO),
                getM_center().getY());
    }

    /**
     * Getter for the ball face
     * @return the shape of the ball
     */
    public Shape getM_ballFace() {
        return m_ballFace;
    }

    /**
     * Setter for the ball face
     * @param m_ballFace the shape of the ball to set
     */
    private void setM_ballFace(Shape m_ballFace) {
        this.m_ballFace = m_ballFace;
    }

    /**
     * Setter for the up Point of the ball
     * @param m_up the top point of the ball
     */
    private void setM_up(Point2D m_up) {
        this.m_up = m_up;
    }

    /**
     * Setter for the down Point of the ball
     * @param m_down the bottom point of the ball
     */
    private void setM_down(Point2D m_down) {
        this.m_down = m_down;
    }

    /**
     * Setter for the left Point of the ball
     * @param m_left the left point of the ball
     */
    private void setM_left(Point2D m_left) {
        this.m_left = m_left;
    }

    /**
     * Setter for the right Point of the ball
     * @param m_right the right point of the ball
     */
    private void setM_right(Point2D m_right) {
        this.m_right = m_right;
    }

    /**
     * Setter for the center Point of the ball
     * @param m_center the middle point of the ball
     */
    private void setM_center(Point2D m_center) {
        this.m_center = m_center;
    }

    /**
     * Constructor of the ball which sets dimensions, points and speed of the
     * ball
     * @param center the center coordinates of the ball
     * @param radiusX the width radius
     * @param radiusY the height radius
     */
    public Ball(Point2D center, int radiusX, int radiusY) {
        setM_center(center);

        setM_up(new Point2D.Double());
        setM_down(new Point2D.Double());
        setM_left(new Point2D.Double());
        setM_right(new Point2D.Double());


        getM_up().setLocation(center.getX(), center.getY() - (radiusY / TWO));
        getM_down().setLocation(center.getX(), center.getY() + (radiusY / TWO));

        getM_left().setLocation(center.getX() - (radiusX / TWO), center.getY());
        getM_right().setLocation(center.getX() + (radiusX / TWO), center.getY());

        setM_ballFace(makeBall(center, radiusX, radiusY));
        setM_speedX(0);
        setM_speedY(0);
    }

    /**
     * Abstract method to create the shape of the ball of size x and y at
     * position center
     * @param center the center coordinates of the ball
     * @param radiusX the width radius
     * @param radiusY the height radius
     * @return the shape of the ball
     */
    protected abstract Shape makeBall(Point2D center, int radiusX, int radiusY);

    /**
     * Moves ball to center point and updates ball shape at that location
     */
    public void moveBall() {
        RectangularShape tmp = (RectangularShape) getM_ballFace();
        double x = (getM_center().getX() + getM_speedX());
        double y = (getM_center().getY() + getM_speedY());
        getM_center().setLocation(x,y);
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        x = (getM_center().getX() - (w / TWO));
        y = (getM_center().getY() - (h / TWO));
        tmp.setFrame(x, y, w, h);
        setPoints(w, h);


        setM_ballFace(tmp);
    }

    /**
     * Reverse speed of X to have ball move in opposite direction
     */
    public void reverseX() {
        int x = getM_speedX();
        x *= -1;
        setM_speedX(x);
    }

    /**
     * Reverse speed of Y to have ball move in opposite direction
     */
    public void reverseY() {

        int y = getM_speedY();
        y *= -1;
        setM_speedY(y);
    }

    /**
     * Moves ball to point specified and updates ball shape at that location
     * @param p the point to move the ball to
     */
    public void moveTo(Point p) {
        getM_center().setLocation(p);

        RectangularShape tmp = (RectangularShape) getM_ballFace();
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        double x = (getM_center().getX() - (w / TWO));
        double y = (getM_center().getY() - (h / TWO));
        tmp.setFrame(x, y, w, h);
        setM_ballFace(tmp);
    }



}
