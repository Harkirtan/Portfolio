package com.model.brick;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Generates cracks in bricks as a general path
 * Extracted from Brick class
 * @author Harkirtan Singh-modified
 */
public class Crack {

    public final int TWO = 2;
    public final int BOUND_FIVE = 5;
    public final int CRACK_SECTIONS = 3;
    public final double JUMP_PROBABILITY = 0.7;

    public static final int LEFT = 10;
    public static final int RIGHT = 20;
    public static final int UP = 30;
    public static final int DOWN = 40;
    public static final int VERTICAL = 100;
    public static final int HORIZONTAL = 200;

    private Random m_rnd;

    private GeneralPath m_crack;

    private int m_crackDepth;
    private int m_steps;

    /**
     * Getter for the random object
     * @return the random object
     */
    private Random getM_rnd() {
        return m_rnd;
    }

    /**
     * Setter for the random object
     * @param m_rnd the random object to set
     */
    private void setM_rnd(Random m_rnd) {
        this.m_rnd = m_rnd;
    }

    /**
     * Setter for the crack path
     * @param m_crack the path of the crack to set
     */
    private void setM_crack(GeneralPath m_crack) {
        this.m_crack = m_crack;
    }

    /**
     * Getter for the crack path
     * @return the crack path
     */
    public GeneralPath getM_crack() {return m_crack;}

    /**
     * Getter for the crack depth
     * @return the size of the crack depth
     */
    private int getM_crackDepth() {
        return m_crackDepth;
    }

    /**
     * Setter for the crack depth
     * @param m_crackDepth the size of the crack depth to set
     */
    private void setM_crackDepth(int m_crackDepth) {
        this.m_crackDepth = m_crackDepth;
    }

    /**
     * Getter for the steps in the crack
     * @return the amount of the steps
     */
    private int getM_steps() {
        return m_steps;
    }

    /**
     * Setter for the steps in the crack
     * @param m_steps the amount of steps
     */
    private void setM_steps(int m_steps) {
        this.m_steps = m_steps;
    }

    /**
     * Generates a new Random object
     */
    public void makeRandom(){setM_rnd(new Random());}

    /**
     * Constructor for default crack path
     * @param crackDepth the depth of the crack
     * @param steps the steps of the crack
     */
    public Crack(int crackDepth, int steps) {

        setM_crack(new GeneralPath());
        this.setM_crackDepth(crackDepth);
        this.setM_steps(steps);

    }

    /**
     * Restore crack to original state
     */
    public void resetCrack() {
        getM_crack().reset();
    }

    /**
     * Generates a crack in a direction from the start point in a crack
     * @param point the impact point of the brick
     * @param brick the brick which will be cracked
     * @param direction the direction of the crack
     */
    protected void makeCrack(Point2D point, int direction, Brick brick) {
        Rectangle bounds = brick.getM_brickFace().getBounds();

        Point impact = new Point((int) point.getX(), (int) point.getY());
        Point start = new Point();
        Point end = new Point();


        switch (direction) {
            case LEFT:
                start.setLocation(bounds.x + bounds.width, bounds.y);
                end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height);
                Point tmp = makeRandomPoint(start, end, VERTICAL);
                makeCrack(impact, tmp);

                break;
            case RIGHT:
                start.setLocation(bounds.getLocation());
                end.setLocation(bounds.x, bounds.y + bounds.height);
                tmp = makeRandomPoint(start, end, VERTICAL);
                makeCrack(impact, tmp);

                break;
            case UP:
                start.setLocation(bounds.x, bounds.y + bounds.height);
                end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height);
                tmp = makeRandomPoint(start, end, HORIZONTAL);
                makeCrack(impact, tmp);
                break;
            case DOWN:
                start.setLocation(bounds.getLocation());
                end.setLocation(bounds.x + bounds.width, bounds.y);
                tmp = makeRandomPoint(start, end, HORIZONTAL);
                makeCrack(impact, tmp);

                break;

        }
    }

    /**
     * Generates a crack line jumping to a point and making steps along that
     * jump
     * @param start the start point of the line
     * @param end the end point of the line
     */
    protected void makeCrack(Point start, Point end) {

        GeneralPath path = new GeneralPath();


        path.moveTo(start.x, start.y);

        double w = (end.x - start.x) / (double) getM_steps();
        double h = (end.y - start.y) / (double) getM_steps();

        int bound = getM_crackDepth();
        int jump = bound * BOUND_FIVE;

        double x, y;

        for (int i = 1; i < getM_steps(); i++) {

            x = (i * w) + start.x;
            y = (i * h) + start.y + randomInBounds(bound);

            if (inMiddle(i, CRACK_SECTIONS, m_steps))
                y += jumps(jump);

            path.lineTo(x, y);

        }

        path.lineTo(end.x, end.y);
        getM_crack().append(path, true);
    }

    /**
     * Creates a random integer in bounds specified
     * @param bound the bound in which random will be generated
     * @return the value random in bounds
     */
    public int randomInBounds(int bound) {
        int n = (bound * TWO) + 1;
        return getM_rnd().nextInt(n) - bound;
    }

    /**
     * Checks if an integer is in middle of 2 values to simulate the steps
     * @param steps makes up lower
     * @param divisions makes up higher
     * @param i integer to check if it is in the middle
     * @return true if passed integer is in the middle
     */
    private boolean inMiddle(int i, int steps, int divisions) {
        int low = (steps / divisions);
        int up = low * (divisions - 1);

        return (i > low) && (i < up);
    }

    /**
     * Generates the amount of jumps to be done in the crack line
     * @param bound the bound for the probability of the jumps
     * @return amount of jumps
     */
    private int jumps(int bound) {

        if (getM_rnd().nextDouble() > JUMP_PROBABILITY)
            return randomInBounds(bound);
        return 0;

    }

    /**
     * Creates a random point between 2 points in direction specified
     * @param from the start point
     * @param to the end point
     * @param direction the direction in which the point should be created
     * @return random point
     */
    public Point makeRandomPoint(Point from, Point to, int direction) {

        Point out = new Point();
        int pos;

        switch (direction) {
            case HORIZONTAL -> {
                pos = getM_rnd().nextInt(to.x - from.x) + from.x;
                out.setLocation(pos, to.y);
            }
            case VERTICAL -> {
                pos = getM_rnd().nextInt(to.y - from.y) + from.y;
                out.setLocation(to.x, pos);
            }
        }
        return out;
    }

}
