package com.controller;

import com.view.GameBoardView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;


/**
 * JFrame which holds main game JComponent board and sets initial position
 * for itself
 * @author Harkirtan Singh-modified
 */
public class GameFrame extends JFrame implements WindowFocusListener {

    public final int TWO = 2;
    public final String DEF_TITLE = "space = start/pause   <-/-> = move " +
            "left/right   " +
            "esc = menu   m = start/stop music";
    private GameBoardView m_gameBoard;
    private boolean m_gaming;

    /**
     * Getter for the game board
     * @return gameBoard which is a JComponent with game running on it
     */
    private GameBoardView getM_gameBoard() {
        return m_gameBoard;
    }

    /**
     * Setter for the game board
     * @param m_gameBoard gameBoard which is a JComponent with game running on it
     */
    private void setM_gameBoard(GameBoardView m_gameBoard) {
        this.m_gameBoard = m_gameBoard;
    }

    /**
     * Getter for the gaming state of the game
     * @return true if the game is playing
     */
    private boolean isM_gaming() {
        return m_gaming;
    }

    /**
     * Setter for the gaming state of the game
     * @param m_gaming  true if the game is playing
     */
    private void setM_gaming(boolean m_gaming) {
        this.m_gaming = m_gaming;
    }

    /**
     * Constructor which creates a new game board object, adds it to itself
     * and initializes position
     * @param ballType the ball type passed to game board
     * @param linecount the amount of brick lines passed to game board
     */
    public GameFrame(int linecount, int ballType) {
        super();
        setM_gaming(false);
        this.setLayout(new BorderLayout());
        setM_gameBoard(new GameBoardView(linecount, ballType));
        this.add(getM_gameBoard(), BorderLayout.CENTER);
        initialize();
        this.addWindowFocusListener(this);
    }

    /**
     * Initialises GameFrame with default settings, title, location
     */
    private void initialize() {
        this.setTitle(DEF_TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.autoLocate();
        this.setVisible(true);
    }

    /**
     * Sets location of GameFrame relative to screen
     */
    private void autoLocate() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (size.width - this.getWidth()) / TWO;
        int y = (size.height - this.getHeight()) / TWO;
        this.setLocation(x, y);
    }

    /**
     * Sets gaming to true when window gains focus for the first time
     * @param windowEvent the window gaining focus
     */
    @Override
    public void windowGainedFocus(WindowEvent windowEvent) {
        // the first time the frame loses focus is because it has been
        // disposed to install the GameBoard,
        // so went it regains the focus it's ready to play.
        // of course calling a method such as 'onLostFocus' is useful only if
        // the GameBoard as been displayed at least once
        setM_gaming(true);
    }

    /**
     * If gaming is true when window loses focus then regain focus
     * @param windowEvent the window losing focus
     */
    @Override
    public void windowLostFocus(WindowEvent windowEvent) {
        if (isM_gaming())
            getM_gameBoard().OnLostFocus();

    }
}
