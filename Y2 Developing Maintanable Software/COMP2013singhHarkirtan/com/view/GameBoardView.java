package com.view;

import com.controller.GameBoardController;
import com.controller.SpriteLoader;
import com.model.ball.Ball;
import com.model.brick.Brick;
import com.model.wall.Paddle;
import com.view.debug.DebugConsole;
import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * JComponent which acts as a view as holds the graphics to paint itself, the
 * ball, paddle, menu and wall objects on the screen
 * Created from previous GameBoard as a view class
 * @author Harkirtan Singh-modified
 */
public class GameBoardView extends JComponent implements KeyListener,
        MouseListener, MouseMotionListener{

    public static final int DEF_WIDTH = 600;
    public static final int DEF_HEIGHT = 450;
    public static final Color BG_COLOR = new Color(50, 200, 200);
    public final int PADDLE_WIDTH = 150;
    public final int PADDLE_HEIGHT = 10;
    public final int MESSAGE_X = 250;
    public final int MESSAGE_Y = 225;
    public final float ALPHA = 0.55f;

    private DebugConsole m_debugConsole;
    private GameMenu m_menu;
    private int ballType;

    private boolean m_showPauseMenu;
    private GameBoardController m_controller;
    private String m_message;

    /**
     * Getter for debug console object
     * @return the debug console
     */
    private DebugConsole getM_debugConsole() {
        return m_debugConsole;
    }

    /**
     * Setter for debug console object
     * @param m_debugConsole the debug console
     */
    private void setM_debugConsole(DebugConsole m_debugConsole) {
        this.m_debugConsole = m_debugConsole;
    }

    /**
     * Getter for the menu object
     * @return the menu
     */
    private GameMenu getM_menu() {
        return m_menu;
    }

    /**
     * Setter for the menu object
     * @param m_menu the menu
     */
    private void setM_menu(GameMenu m_menu) {
        this.m_menu = m_menu;
    }

    /**
     * Getter for the pause menu state
     * @return true if pause menu should be shown
     */
    private boolean isM_showPauseMenu() {
        return m_showPauseMenu;
    }

    /**
     * Setter for the pause menu state
     * @param m_showPauseMenu true if pause menu should be shown
     */
    private void setM_showPauseMenu(boolean m_showPauseMenu) {
        this.m_showPauseMenu = m_showPauseMenu;
    }

    /**
     * Getter for the controller of this view
     * @return the controller object
     */
    private GameBoardController getM_controller() {
        return m_controller;
    }

    /**
     * Setter for the controller of this view
     * @param m_controller the controller object
     */
    private void setM_controller(GameBoardController m_controller) {
        this.m_controller = m_controller;
    }

    /**
     * Setter for the string object used to hold the output to display
     * @param m_message the string object
     */
    private void setM_message(String m_message) {
        this.m_message = m_message;
    }

    /**
     * Getter for the current ball type
     * @return the ball type value
     */
    private int getM_ballType(){return ballType;}

    /**
     * Getter for the current ball type
     * @param btype the ball type value
     */
    private void setM_ballType(int btype){this.ballType = btype;}

    /**
     * Constructor which initialises itself, the controller and debug
     * console
     * @param bType the ball type to use in the game
     * @param lcount the amount of lines to have per level
     */
    public GameBoardView(int lcount, int bType) {
        this.initialize();
        setM_showPauseMenu(false);

        setM_ballType(bType);
        GameBoardController controller = new GameBoardController(lcount,
                bType, this);
        this.setM_controller(controller);
        Platform.runLater(() ->
                setM_debugConsole(new DebugConsole()));
        //Initialise Menu
        setM_menu(new GameMenu());

    }
    /**
     * Initialises this game view with dimensions and adds input listeners
     */
    private void initialize() {
        this.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

    }

    /**
     * Paints the game elements, namely ball, wall, paddle and pause menu
     * @param g the graphics used to paint the elements
     */
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        clear(g2d);

        g2d.setColor(Color.BLUE);
        g2d.drawString(getM_controller().getM_message(), MESSAGE_X, MESSAGE_Y);

        drawBall(getM_controller().getM_wall().getM_ball(), g2d);

        for (Brick b : getM_controller().getM_wall().getM_bricks())
            if (!b.getM_broken())
                drawBrick(b, g2d);

        drawPlayer(getM_controller().getM_wall().getM_player(), g2d);

        if (isM_showPauseMenu()) {
            drawMenu(g2d);
        }


        Toolkit.getDefaultToolkit().sync();
    }
    /**
     * Clears board and sets background colour
     * @param g2d the graphics used to paint
     */
    private void clear(Graphics2D g2d) {
        Color tmp = g2d.getColor();
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(tmp);
    }
    /**
     * Paints brick using the colours stored in each brick
     * @param g2d the graphics used to paint
     */
    private void drawBrick(Brick brick, Graphics2D g2d) {
        Color tmp = g2d.getColor();

        g2d.setColor(brick.getM_innerColor());
        g2d.fill(brick.getBrick());

        g2d.setColor(brick.getM_borderColor());
        g2d.draw(brick.getBrick());


        g2d.setColor(tmp);
    }
    /**
     * Paints ball using sprite loader
     * @param g2d the graphics used to paint
     */
    private void drawBall(Ball ball, Graphics2D g2d) {
        Color tmp = g2d.getColor();

        Shape s = ball.getM_ballFace();

        g2d.setColor(tmp);
        SpriteLoader loader = new SpriteLoader();
        try {
            loader.loadImage("/breakout-5.png");
        } catch (IOException e){
            e.printStackTrace();
        }
        Rectangle r = s.getBounds();

        BufferedImage img;
        img = getM_controller().getM_loadedImage().grabBall(getM_ballType());
        g2d.drawImage(img, (int)r.getX(), (int)r.getY(), null);
    }

    /**
     * Paints player using sprite loader
     * @param g2d the graphics used to paint
     */
    private void drawPlayer(Paddle p, Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = p.getM_paddleFace();

        Rectangle r = s.getBounds();

        BufferedImage img;
        img= getM_controller().getM_loadedImage().grabPaddle();
        g2d.drawImage(img, (int)r.getX(), (int)r.getY(), PADDLE_WIDTH,
                PADDLE_HEIGHT,  null);

        g2d.setColor(tmp);
    }

    /**
     * Paints menu after obscuring game
     * @param g2d the graphics used to paint
     */
    private void drawMenu(Graphics2D g2d) {
        obscureGameBoard(g2d);
        getM_menu().drawPauseMenu(g2d, this);
    }

    /**
     * Paints board a darker colour
     * @param g2d the graphics used to paint
     */
    private void obscureGameBoard(Graphics2D g2d) {

        Composite tmp = g2d.getComposite();
        Color tmpColor = g2d.getColor();

        AlphaComposite ac =
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA);
        g2d.setComposite(ac);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, DEF_WIDTH, DEF_HEIGHT);

        g2d.setComposite(tmp);
        g2d.setColor(tmpColor);
    }

    /**
     * Not implemented
     */
    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    /**
     * Based on the key pressed moves paddle left/right, pauses game, opens
     * menu, starts/pauses music and opens debug console
     * @param e the key pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            getM_controller().getM_wall().getM_player().moveLeft();
        }
        if (code == KeyEvent.VK_RIGHT) {
            getM_controller().getM_wall().getM_player().moveRight();
        }
        if (code == KeyEvent.VK_SPACE) {
            if (!isM_showPauseMenu())
                if (getM_controller().getM_gameTimer().isRunning())
                    getM_controller().getM_gameTimer().stop();
                else
                    getM_controller().getM_gameTimer().start();
        }
        if (code == KeyEvent.VK_ESCAPE) {
            setM_showPauseMenu(!isM_showPauseMenu());
            repaint();
            getM_controller().getM_gameTimer().stop();
        }
        if (code == KeyEvent.VK_M) {
            getM_controller().alterMusic();
        }
        if (code == KeyEvent.VK_F1) {
            if (e.isAltDown() && e.isShiftDown()) {
                Platform.runLater(() -> getM_debugConsole().show());
            }
        }
    }

    /**
     * Once user lets go of a key, the paddle stops moving
     * @param keyEvent the key released
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        m_controller.getM_wall().getM_player().stop();
    }

    /**
     * Depending on where user clicks mouse in the menu, different actions
     * are taken: continue, restart game and exit
     * @param mouseEvent the mouse event clicked
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();

        if (!isM_showPauseMenu())
            return;
        if (getM_menu().getM_continueButton().contains(p)) {
            setM_showPauseMenu(false);
           repaint();
        } else if (getM_menu().getM_restartButton().contains(p)) {
            getM_controller().resetGame();
            setM_showPauseMenu(false);
            repaint();
        } else if (getM_menu().getM_exitButton().contains(p)) {
            System.exit(0);
        }
    }

    /**
     * Not implemented
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    /**
     * Not implemented
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    /**
     * Not implemented
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    /**
     * Not implemented
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    /**
     * Not implemented
     */
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
    }

    /**
     * Depending on if user moves mouse on a rectangle in the menu, a hand
     * cursor is put on the mouse
     * @param mouseEvent the mouse event moved
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if (getM_menu().getM_exitButton() != null && isM_showPauseMenu()) {
            if (getM_menu().getM_exitButton().contains(p) ||
                    getM_menu().getM_continueButton().contains(p) ||
                    getM_menu().getM_restartButton().contains(p))
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else
                this.setCursor(Cursor.getDefaultCursor());
        } else {
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * When focus is lost, stop the timer and inform user
     */
    public void OnLostFocus() {
        getM_controller().getM_gameTimer().stop();
        setM_message("Focus Lost");
        repaint();
    }


}
