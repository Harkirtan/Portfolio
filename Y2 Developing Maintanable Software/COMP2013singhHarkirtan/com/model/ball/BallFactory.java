package com.model.ball;

import java.awt.geom.Point2D;

/**
 * Factory class to create different ball objects
 * @author Harkirtan Singh
 */
public class BallFactory {

    private final int RUBBER = 1;
    private final int BLUE = 2;
    /**
     * Generates a ball object with type and location
     * @param ballPos initial position of the ball
     * @param type type of the ball
     * @return returns selected ball object from parameter type
     */
    public Ball getBall(int type, Point2D ballPos){
        if(type == RUBBER)
            return new BallRubber(ballPos);
        if(type == BLUE)
            return new BallBlue(ballPos);
        else
            throw new IllegalArgumentException(String.format("Unknown Ball:%d\n", type));

    }

}
