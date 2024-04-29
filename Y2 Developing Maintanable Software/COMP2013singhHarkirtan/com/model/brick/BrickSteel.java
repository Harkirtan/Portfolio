package com.model.brick;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Brick class that creates a steel brick
 * @author Harkirtan Singh-modified
 */
public class BrickSteel extends Brick {

    private static final Color DEF_INNER = new Color(203, 203, 201);
    private static final Color DEF_BORDER = Color.BLACK;
    private static final int STEEL_STRENGTH = 1;
    private static final double STEEL_PROBABILITY = 0.4;

    private Random m_steelRandom;
    /**
     * Getter for the steel random
     * @return the steel random
     */
    private Random getM_steelRandom() {
        return m_steelRandom;
    }

    /**
     * Setter for the steel random
     * @param m_steelRandom the random to set
     */
    private void setM_steelRandom(Random m_steelRandom) {
        this.m_steelRandom = m_steelRandom;
    }

    /**
     * Implemented getter for the rectangular shape of this Brick
     * @return the shape of this brick
     */
    @Override
    public Shape getBrick() {
        return super.getM_brickFace();
    }

    /**
     * Constructor to initialise default Steel Brick
     * @param point the initial point of the brick
     * @param size the dimension of the brick
     */
    public BrickSteel(Point point, Dimension size) {
        super(point, size, DEF_BORDER, DEF_INNER, STEEL_STRENGTH);
        setM_steelRandom(new Random());
        setM_brickFace(super.getM_brickFace());
    }

    /**
     * Implemented generator of the brick shape
     * @param pos the position of the rectangle
     * @param size the height and width dimension of the brick
     * @return rectangle shape of the brick
     */
    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos, size);
    }

    /**
     * Assesses if brick is broken
     * @param dir the direction of where the brick was hiy
     * @param point the point of impact
     * @return boolean if brick is not broken
     */
    @Override
    public boolean setImpact(Point2D point, int dir) {
        if (super.getM_broken())
            return false;
        impact();
        return super.getM_broken();
    }

    /**
     * Randomly break brick using STEEL_PROBABILITY constant
     */
    public void impact() {
        if (getM_steelRandom().nextDouble() < STEEL_PROBABILITY) {
            super.impact();
        }
    }

}
