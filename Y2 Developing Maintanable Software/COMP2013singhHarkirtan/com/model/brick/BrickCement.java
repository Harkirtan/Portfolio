package com.model.brick;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 * Brick class that creates a cement brick
 * @author Harkirtan Singh-modified
 */
public class BrickCement extends Brick {

    private static final Color DEF_INNER = new Color(147, 147, 147);
    private static final Color DEF_BORDER = new Color(217, 199, 175);
    private static final int CEMENT_STRENGTH = 2;

    private Crack m_crack;

    /**
     * Getter for cement brick crack
     * @return the Crack object which holds data on the crack
     */
    private Crack getM_crack() {
        return m_crack;
    }

    /**
     * Setter for crack
     * @param m_crack the Crack object to set to this brick
     */
    private void setM_crack(Crack m_crack) {
        this.m_crack = m_crack;
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
     * Constructor to initialise default Cement Brick with colours and set up
     * the crack object
     * @param point the initial point of the brick
     * @param size the dimension of the brick
     */
    public BrickCement(Point point, Dimension size) {
        super(point, size, DEF_BORDER, DEF_INNER, CEMENT_STRENGTH);
        setM_crack(new Crack(DEF_CRACK_DEPTH, DEF_STEPS));
        this.setM_brickFace(super.getM_brickFace());
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
     * Implemented checks if brick is broken, if not adds a random crack from
     * impact in the direction it was hit
     * @param dir the direction of where the brick was hit
     * @param point the point of impact
     * @return boolean if brick is not broken
     */
    @Override
    public boolean setImpact(Point2D point, int dir) {
        if (super.getM_broken())
            return false;
        super.impact();
        if (!super.getM_broken()) {
            getM_crack().makeRandom();
            getM_crack().makeCrack(point, dir, this);
            updateBrick();
            return false;
        }
        return true;
    }


    /**
     * Update brick shape with crack
     */
    private void updateBrick() {
        if (!super.getM_broken()) {
            GeneralPath gp = getM_crack().getM_crack();
            gp.append(super.getM_brickFace(), false);
            this.setM_brickFace(gp);
        }
    }

    /**
     * Restores brick to original state removing cracks
     */
    public void repair() {
        super.repair();
        getM_crack().resetCrack();
        this.setM_brickFace(super.getM_brickFace());
    }
}
