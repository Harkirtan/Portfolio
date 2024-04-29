package com.model.brick;

import java.awt.*;

/**
 * Factory class to create different bricks
 * @author Harkirtan Singh
 */
public class BrickFactory {

    private final int RED_CLAY = 1;
    private final int BLUE_CLAY = 2;
    private final int CEMENT = 3;
    private final int STEEL = 4;


    /**
     * Generates specified brick with type, coordinates and size
     * @param point initial position of the brick
     * @param type type of the brick
     * @param size dimension of brick
     * @return returns selected brick from type
     */
    public Brick getBrickInstance(int type, Point point, Dimension size){
        if(type == RED_CLAY)
            return new BrickClayRed(point, size);
        else if(type == BLUE_CLAY)
            return new BrickClayBlue(point, size);
        else if(type == CEMENT)
            return new BrickCement(point, size);
        else if(type == STEEL)
            return new BrickSteel(point, size);

        else
            throw new IllegalArgumentException(String.format("Unknown " +
                    "Type:%d\n", type));

    }
}
