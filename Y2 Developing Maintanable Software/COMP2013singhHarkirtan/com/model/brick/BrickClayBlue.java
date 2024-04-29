package com.model.brick;

import java.awt.*;

/**
 * Brick class that creates a blue clay brick
 * @author Harkirtan Singh
 */
public class BrickClayBlue extends Brick {

    private static final Color DEF_INNER = new Color(34, 34, 178);
    private static final Color DEF_BORDER = Color.GRAY;
    private static final int CLAY_STRENGTH = 1;


    /**
     * Implemented getter for the rectangular shape of this Brick
     * @return the shape of this brick
     */
    @Override
    public Shape getBrick() {
        return super.getM_brickFace();
    }

    /**
     * Constructor to initialise default Blue Clay Brick
     * @param point the initial point of the brick
     * @param size the dimension of the brick
     */
    public BrickClayBlue(Point point, Dimension size) {
        super(point, size, DEF_BORDER, DEF_INNER, CLAY_STRENGTH);
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


}
