package com.model.brick;

import com.model.ball.Ball;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Abstract brick class used to initialise other bricks, holds
 * @author Harkirtan Singh-modified
 */
abstract public class Brick {

    public final int DEF_CRACK_DEPTH = 1;
    public final int DEF_STEPS = 35;

    public static final int UP_IMPACT = 100;
    public static final int DOWN_IMPACT = 200;
    public static final int LEFT_IMPACT = 300;
    public static final int RIGHT_IMPACT = 400;

    private Shape m_brickFace;

    private Color m_border;
    private Color m_inner;

    private int m_fullStrength;
    private int m_strength;

    private boolean m_broken;

    /**
     * Getter for the brick shape
     * @return the shape of the brick
     */
    public Shape getM_brickFace(){ return this.m_brickFace;}

    /**
     * Setter for the brick shape
     * @param m_brickFace shape to set to the brick
     */
    public void setM_brickFace(Shape m_brickFace) {
        this.m_brickFace = m_brickFace;
    }

    /**
     * Setter for border
     * @param m_border the colour of the border
     */
    private void setM_border(Color m_border) {
        this.m_border = m_border;
    }

    /**
     * Setter for the inner brick colour
     * @param m_inner the colour to set the brick's inner to
     */
    private void setM_inner(Color m_inner) {
        this.m_inner = m_inner;
    }

    /**
     * Getter for fullStrength of brick
     * @return the max strength of the brick
     */
    private int getM_fullStrength() {
        return m_fullStrength;
    }

    /**
     * Setter for full strength of brick
     * @param m_fullStrength the max strength of the brick
     */
    private void setM_fullStrength(int m_fullStrength) {
        this.m_fullStrength = m_fullStrength;
    }

    /**
     * Getter for current strength of brick
     * @return the current strength of the brick
     */
    public int getM_strength() {
        return m_strength;
    }

    /**
     * Setter for current strength of brick
     * @param m_strength the current strength of the brick
     */
    private void setM_strength(int m_strength) {
        this.m_strength = m_strength;
    }

    /**
     * Getter for the status of broken
     * @return true if broken
     */
    public boolean getM_broken() {
        return m_broken;
    }

    /**
     * Setter for the broken status of the brick
     * @param m_broken true if brick is to be set to broken
     */
    private void setM_broken(boolean m_broken) {
        this.m_broken = m_broken;
    }

    /**
     * Abstract method to get the implemented brick's shape
     * @return the shape of the brick
     */
    public abstract Shape getBrick();

    /**
     * Getter for border colour of brick
     * @return the colour of the border
     */
    public Color getM_borderColor() {
        return m_border;
    }

    /**
     * Getter for inner colour of brick
     * @return the colour of the inner of the brick
     */
    public Color getM_innerColor() {
        return m_inner;
    }

    /**
     * Constructor to create a brick with parameters specified
     * @param size the height and width of the brick
     * @param border the colour of the brick border
     * @param inner the colour of the brick inner
     * @param pos the position of the brick
     * @param strength the current and max strength of the brick
     */
    public Brick(Point pos, Dimension size, Color border, Color inner, int strength)
    {
        setM_broken(false);
        setM_brickFace(makeBrickFace(pos, size));
        setM_border(border);
        setM_inner(inner);

        setM_strength(strength);
        setM_fullStrength(getM_strength());

    }

    /**
     * Abstract method to create the brick shape
     * @param pos the initial coordinates of brick
     * @param size the height and width of the brick
     */
    protected abstract Shape makeBrickFace(Point pos, Dimension size);

    /**
     * Finds on which side the ball impacts the brick
     * @param b the ball that impacts the brick
     * @return the side of impact
     */
    public int findImpact(Ball b) {
        if (getM_broken())
            return 0;
        int out = 0;
        if (getM_brickFace().contains(b.getM_right()))
            out = LEFT_IMPACT;
        else if (getM_brickFace().contains(b.getM_left()))
            out = RIGHT_IMPACT;
        else if (getM_brickFace().contains(b.getM_up()))
            out = DOWN_IMPACT;
        else if (getM_brickFace().contains(b.getM_down()))
            out = UP_IMPACT;
        return out;
    }
    /**
     * Checks if brick is not broken then impacts brick
     * @param point the point of the impact
     * @param dir the side of the brick on which the impact was
     * @return the state of the brick, broken or not broken
     */
    public boolean setImpact(Point2D point, int dir) {
        if (getM_broken())
            return false;
        impact();
        return getM_broken();
    }

    /**
     * Restores current strength of brick to max strength
     */
    public void repair() {
        setM_broken(false);
        setM_strength(getM_fullStrength());
    }

    /**
     * Damages brick by 1 and if the strength of the brick is 0 declare brick
     * as broken
     */
    public void impact() {
        int temp = getM_strength();
        temp--;
        setM_strength(temp);
        if(temp == 0)
            setM_broken(true);

    }



}





