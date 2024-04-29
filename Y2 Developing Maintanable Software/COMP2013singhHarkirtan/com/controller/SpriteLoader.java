package com.controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Opens sprite sheet and returns images with sprites loaded depending on if
 * it is the paddle or the balls
 * @author Harkirtan Singh
 */
public class SpriteLoader {

    private BufferedImage m_image;
    public final int[] R_B = {384, 136, 10, 10};
    public final int[] B_B = {16, 368, 18, 18};
    public final int[] PLA = {288, 512, 80, 10};
    public final int TWO = 2;
    public final int THREE = 3;

    /**
     * Getter for the loaded image
     * @return the image
     */
    public BufferedImage getM_image(){return m_image;}

    /**
     * Setter for the loaded image
     * @param i the image
     */
    public void setM_image(BufferedImage i){m_image = i;}

    /**
     * Loads image from specified path
     * @param path the path of the image to be opened
     */
    public void loadImage(String path) throws IOException {
        setM_image(ImageIO.read(Objects.requireNonNull(getClass().getResource(path))));
    }

    /**
     * Loads a ball sub-image from the image stored in this class
     * @param bType the type of the ball to be loaded
     * @return the sub image containing the requested ball
     */
    public BufferedImage grabBall(int bType) {
        BufferedImage img;
        if (bType == 1) {
            img = getM_image().getSubimage(R_B[0], R_B[1], R_B[TWO], R_B[THREE]);
        } else{
            img = getM_image().getSubimage(B_B[0], B_B[1], B_B[TWO], B_B[THREE]);
        }
        return img;
    }

    /**
     * Loads the paddle sub-image from the image stored in this class
     * @return the sub image with the paddle
     */
    public BufferedImage grabPaddle() {
        BufferedImage img;
        img = getM_image().getSubimage(PLA[0], PLA[1], PLA[TWO], PLA[THREE]);
        return img;
    }

}
