package com.view;

import java.awt.*;
import java.awt.font.FontRenderContext;

/**
 * Holds the 3 rectangles: continue, restart, exit and text for the game menu
 * @author Harkirtan Singh
 */
public class GameMenu {

    public final String CONTINUE = "Continue";
    public final String RESTART = "Restart";
    public final String EXIT = "Exit";
    public final String PAUSE = "Pause Menu";
    public final int TEXT_SIZE = 30;
    public final Color MENU_COLOR = new Color(0, 255, 0);
    public final int TWO = 2;
    public final int EIGHT = 8;
    public final int TEN = 10;
    public final double THREE = 3.0;


    private Font m_menuFont;
    private Rectangle m_continueButtonRect;
    private Rectangle m_exitButtonRect;
    private Rectangle m_restartButtonRect;
    private int m_strLen;

    /**
     * Getter for the continue button rectangle
     * @return the Rectangle of the continue button
     */
    public Rectangle getM_continueButton() {return m_continueButtonRect;}

    /**
     * Getter for the exit button rectangle
     * @return the Rectangle of the exit button
     */
    public Rectangle getM_exitButton() {return m_exitButtonRect;}

    /**
     * Getter for the restart button rectangle
     * @return the Rectangle of the restart button
     */
    public Rectangle getM_restartButton() {return m_restartButtonRect;}

    /**
     * Getter for the menu font
     * @return the Font of the menu
     */
    private Font getM_menuFont() {
        return m_menuFont;
    }

    /**
     * Setter for the menu font
     * @param m_menuFont the Font of the menu
     */
    private void setM_menuFont(Font m_menuFont) {
        this.m_menuFont = m_menuFont;
    }

    /**
     * Setter for the continue button rectangle
     * @param m_continueButtonRect the Rectangle of the continue button
     */
    private void setM_continueButtonRect(Rectangle m_continueButtonRect) {this.m_continueButtonRect = m_continueButtonRect;}

    /**
     * Setter for the exit button rectangle
     * @param m_exitButtonRect the Rectangle of the exit button
     */
    private void setM_exitButtonRect(Rectangle m_exitButtonRect) {
        this.m_exitButtonRect = m_exitButtonRect;
    }

    /**
     * Setter for the restart button rectangle
     * @param m_restartButtonRect the Rectangle of the restart button
     */
    private void setM_restartButtonRect(Rectangle m_restartButtonRect) {
        this.m_restartButtonRect = m_restartButtonRect;
    }
    /**
     * Getter for the length of the string
     * @return the length of the string
     */
    private int getM_strLen() {
        return m_strLen;
    }

    /**
     * Setter for the length of the string
     * @param m_strLen the length of the string
     */
    private void setM_strLen(int m_strLen) {
        this.m_strLen = m_strLen;
    }

    /**
     * Draws and sets locations of the pause menu continue button, restart and
     * exit with font text
     * @param g2d the graphics used to draw
     * @param owner the owner of this menu, used to set dimensions
     */
    public void drawPauseMenu(Graphics2D g2d, GameBoardView owner) {
        setM_menuFont(new Font("Monospaced", Font.PLAIN, TEXT_SIZE));
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();


        g2d.setFont(getM_menuFont());
        g2d.setColor(MENU_COLOR);

        if (getM_strLen() == 0) {
            FontRenderContext frc = g2d.getFontRenderContext();
            setM_strLen(getM_menuFont().getStringBounds(PAUSE, frc).getBounds().width);
        }

        int x = (owner.getWidth() - getM_strLen()) / TWO;
        int y = owner.getHeight() / TEN;

        g2d.drawString(PAUSE, x, y);

        x = owner.getWidth() / EIGHT;
        y = owner.getHeight() / (EIGHT/TWO);


        if (getM_continueButton() == null) {
            FontRenderContext frc = g2d.getFontRenderContext();
            setM_continueButtonRect(getM_menuFont().getStringBounds(CONTINUE, frc).getBounds());
            getM_continueButton().setLocation(x, y - getM_continueButton().height);
        }

        g2d.drawString(CONTINUE, x, y);

        y *= TWO;

        if (getM_restartButton() == null) {
            setM_restartButtonRect((Rectangle) getM_continueButton().clone());
            getM_restartButton().setLocation(x, y - getM_restartButton().height);
        }

        g2d.drawString(RESTART, x, y);

        y *= THREE / TWO;

        if (getM_exitButton() == null) {
            setM_exitButtonRect((Rectangle) getM_continueButton().clone());
            getM_exitButton().setLocation(x, y - getM_exitButton().height);
        }

        g2d.drawString(EXIT, x, y);


        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);
    }


}
