package com.model.wall;

import com.model.brick.Brick;
import com.model.brick.BrickClayRed;
import com.model.brick.BrickFactory;

import java.awt.*;

/**
 * Level builder to generate various level types in form of a brick array
 * @author Harkirtan Singh
 */
public class LevelManager {

    private final int TWO = 2;

    private final double BRICK_DIMENSION_RATIO = 6/2;
    /**
     * Creates a uniform level of 1 brick type
     * @param drawArea the draw area where bricks will be fit in
     * @param brickCnt the amount of bricks
     * @param lineCnt the amount of brick lines
     * @param type the type of brick to be used for this level
     * @return brick array with generated level
     */
    public Brick[] makeSingleTypeLevel(Rectangle drawArea, int brickCnt, int lineCnt, int type) {
        // if brickCount is not divisible by line count,brickCount is adjusted to the biggest multiple of lineCount smaller then brickCount
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / BRICK_DIMENSION_RATIO;

        brickCnt += lineCnt / TWO;

        Brick[] tmp = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen, (int) brickHgt);
        Point p = new Point();

        int i;
        for (i = 0; i < tmp.length; i++) {
            int line = i / brickOnLine;
            if (line == lineCnt)
                break;
            double x = (i % brickOnLine) * brickLen;
            x = (line % TWO == 0) ? x : (x - (brickLen / TWO));
            double y = (line) * brickHgt;
            p.setLocation(x, y);
            tmp[i] = makeLevelBrick(p, brickSize, type);
        }

        for (double y = brickHgt; i < tmp.length; i++, y += TWO * brickHgt) {
            double x = (brickOnLine * brickLen) - (brickLen / TWO);
            p.setLocation(x, y);
            tmp[i] = new BrickClayRed(p, brickSize);
        }
        return tmp;

    }

    /**
     * Creates an asymmetric checkerboard level of 2 brick types
     * @param drawArea the draw area where bricks will be fit in
     * @param brickCnt the amount of bricks
     * @param lineCnt the amount of brick lines
     * @param typeA the first type of brick to be used for this level
     * @param typeB the second type of brick to be used for this level
     * @return brick array with generated level
     */
    public Brick[] makeAsymmetricalLevel(Rectangle drawArea, int brickCnt, int lineCnt, int typeA, int typeB) {
        // if brickCount is not divisible by line count, brickCount is adjusted to the biggest multiple of lineCount smaller then brickCount
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        int centerLeft = brickOnLine / TWO - 1;
        int centerRight = brickOnLine / TWO + 1;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / BRICK_DIMENSION_RATIO;

        brickCnt += lineCnt / TWO;

        Brick[] tmp = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen, (int) brickHgt);
        Point p = new Point();

        int i;
        for (i = 0; i < tmp.length; i++) {
            int line = i / brickOnLine;
            if (line == lineCnt)
                break;
            int posX = i % brickOnLine;
            double x = posX * brickLen;
            x = (line % TWO == 0) ? x : (x - (brickLen / TWO));
            double y = (line) * brickHgt;
            p.setLocation(x, y);

            boolean b;
            b = ((line % TWO == 0 && i % TWO == 0) ||
                    (line % TWO != 0 && posX > centerLeft && posX <= centerRight));
            tmp[i] = b ? makeLevelBrick(p, brickSize, typeA) : makeLevelBrick(p, brickSize, typeB);
        }

        for (double y = brickHgt; i < tmp.length; i++, y += TWO * brickHgt) {
            double x = (brickOnLine * brickLen) - (brickLen / TWO);
            p.setLocation(x, y);
            tmp[i] = makeLevelBrick(p, brickSize, typeA);
        }
        return tmp;
    }

    /**
     * Creates an alternating layer level of 2 brick types
     * @param drawArea the draw area where bricks will be fit in
     * @param brickCnt the amount of bricks
     * @param lineCnt the amount of brick lines
     * @param typeA the first type of brick to be used for this level
     * @param typeB the second type of brick to be used for this level
     * @return brick array with generated level
     */
    public Brick[] makeAltTypeLevel(Rectangle drawArea, int brickCnt, int lineCnt, int typeA, int typeB) {
        // if brickCount is not divisible by line count,brickCount is adjusted to the biggest multiple of lineCount smaller then brickCount
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / BRICK_DIMENSION_RATIO;

        brickCnt += lineCnt / TWO;

        Brick[] tmp = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen, (int) brickHgt);
        Point p = new Point();

        int i;
        int t1 = 0;
        int t2 = 1;
        for (i = 0; i < tmp.length; i++) {
            int line = i / brickOnLine;
            if (line == lineCnt)
                break;
            double x = (i % brickOnLine) * brickLen;
            x = (line % TWO == 0) ? x : (x - (brickLen / TWO));
            double y = (line) * brickHgt;
            p.setLocation(x, y);
            if(line == t1) {
                tmp[i] = makeLevelBrick(p, brickSize, typeB);
            }
            else if(line == t2) {
                tmp[i] = makeLevelBrick(p, brickSize, typeA);
            }
            else {
                t1 = t1 + TWO;
                t2 = t2 + TWO;
                i--;
            }

        }

        for (double y = brickHgt; i < tmp.length; i++, y += TWO * brickHgt) {
            double x = (brickOnLine * brickLen) - (brickLen / TWO);
            p.setLocation(x, y);
            tmp[i] = makeLevelBrick(p, brickSize, typeA);
        }
        return tmp;

    }

    /**
     * Creates a symmetric checkerboard (or arrow) level of 2 brick types
     * @param drawArea the draw area where bricks will be fit in
     * @param brickCnt the amount of bricks
     * @param lineCnt the amount of brick lines
     * @param typeA the first type of brick to be used for this level
     * @param typeB the second type of brick to be used for this level
     * @return brick array with generated level
     */
    public Brick[] makeArrowLevel(Rectangle drawArea, int brickCnt, int lineCnt, int typeA, int typeB) {
        // if brickCount is not divisible by line count,brickCount is adjusted to the biggest multiple of lineCount smaller then brickCount
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / BRICK_DIMENSION_RATIO;

        brickCnt += lineCnt / TWO;

        Brick[] tmp = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen, (int) brickHgt);
        Point p = new Point();

        int i;
        boolean temp = true;
        for (i = 0; i < tmp.length; i++) {
            int line = i / brickOnLine;
            if (line == lineCnt)
                break;
            double x = (i % brickOnLine) * brickLen;
            x = (line % TWO == 0) ? x : (x - (brickLen / TWO));
            double y = (line) * brickHgt;
            p.setLocation(x, y);
            if(temp) {
                tmp[i] = makeLevelBrick(p, brickSize, typeB);
                temp = false;
            }
            else if(!temp) {
                tmp[i] = makeLevelBrick(p, brickSize, typeA);
                temp = true;
            }
        }

        for (double y = brickHgt; i < tmp.length; i++, y += TWO * brickHgt) {
            double x = (brickOnLine * brickLen) - (brickLen / TWO);
            p.setLocation(x, y);
            tmp[i] = makeLevelBrick(p, brickSize, typeB);
        }
        return tmp;

    }
    /**
     * Creates a randomly assigned level of 2 brick types
     * @param drawArea the draw area where bricks will be fit in
     * @param brickCnt the amount of bricks
     * @param lineCnt the amount of brick lines
     * @param typeA the first type of brick to be used for this level
     * @param typeB the second type of brick to be used for this level
     * @return brick array with generated level
     */
    public Brick[] makeRandomLevel(Rectangle drawArea, int brickCnt, int lineCnt, int typeA, int typeB) {
        // if brickCount is not divisible by line count,brickCount is adjusted to the biggest multiple of lineCount smaller then brickCount
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / BRICK_DIMENSION_RATIO;

        brickCnt += lineCnt / TWO;

        Brick[] tmp = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen, (int) brickHgt);
        Point p = new Point();

        int i;

        for (i = 0; i < tmp.length; i++) {
            int line = i / brickOnLine;
            if (line == lineCnt)
                break;
            double x = (i % brickOnLine) * brickLen;
            x = (line % TWO == 0) ? x : (x - (brickLen / TWO));
            double y = (line) * brickHgt;
            p.setLocation(x, y);
            if (Math.round(Math.random()) == 1.0)
                tmp[i] = makeLevelBrick(p, brickSize, typeB);

            else {
                tmp[i] = makeLevelBrick(p, brickSize, typeA);
            }
        }

        for (double y = brickHgt; i < tmp.length; i++, y += TWO * brickHgt) {
            double x = (brickOnLine * brickLen) - (brickLen / TWO);
            p.setLocation(x, y);
            tmp[i] = makeLevelBrick(p, brickSize, typeB);
        }
        return tmp;

    }



    /**
     * Uses a brick factory to generate bricks
     * @param type the type of the brick to generate
     * @param point the position of the brick
     * @param size the width and height of the brick
     * @return brick created by factory
    */
    private Brick makeLevelBrick(Point point, Dimension size, int type) {
        Brick out;
        BrickFactory brickFactory = new BrickFactory();
        out = brickFactory.getBrickInstance(type, point, size);

        return out;
    }
}
