package com.model.ball;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Ball class that creates a larger blue ball, setting name and radius
 * @author Harkirtan Singh
 */
public class BallBlue extends Ball {

    public final int TWO = 2;
    public final String NAME = "Blue";
    public static final int RADIUS = 18;

    /**
     * Implemented getter for name of this ball
     * @return NAME of the ball
     */
    @Override
    public String getNAME() {
        return this.NAME;
    }


    /**
     * Constructor of the ball which sets location, size of the ball using
     * super constructor
     * @param center the center point to place the ball in
     */
    public BallBlue(Point2D center) {super(center, RADIUS, RADIUS);}

    /**
     * Implemented creator of the shape of the ball using dimension specified
     * @param center the center coordinates of the ball
     * @param radiusX the width radius
     * @param radiusY the height radius
     * @return Ellipse shape of the ball
     */
    @Override
    protected Shape makeBall(Point2D center, int radiusX, int radiusY) {

        double x = center.getX() - (radiusX / TWO);
        double y = center.getY() - (radiusY / TWO);

        return new Ellipse2D.Double(x, y, radiusX, radiusY);
    }
}
