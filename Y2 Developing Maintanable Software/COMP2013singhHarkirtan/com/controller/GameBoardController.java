package com.controller;
import com.view.AlertBox;
import com.view.GameBoardView;
import com.model.wall.Wall;
import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Controls the timer and wall logic such as impacts to be displayed on the view
 * of the game board
 * Created from previous GameBoard as a controller class
 * @author Harkirtan Singh-modified
 */
public class GameBoardController {

    public final int DEF_WIDTH = 600;
    public final int DEF_HEIGHT = 450;
    public final Point START = new Point(300, 430);
    public final int BRICK_COUNT = 10;
    public final int TIMER_DELAY = 10;

    private Timer m_gameTimer;
    private Wall m_wall;
    private String m_message;
    private SpriteLoader m_loadedImage;

    private MusicManager m_gameMusic;

    /**
     * Getter for the wall of the game
     * @return the wall instance used in game
     */
    public Wall getM_wall(){return this.m_wall;}

    /**
     * Getter the string object used to hold the output to display
     * @return the string object
     */
    public String getM_message(){return this.m_message;}

    /**
     * Getter for the game timer of the game
     * @return the timer which is used to pace the game
     */
    public Timer getM_gameTimer(){return this.m_gameTimer;}

    /**
     * Setter for the game timer of the game
     * @param m_gameTimer the timer which is used to pace the game
     */
    private void setM_gameTimer(Timer m_gameTimer) {
        this.m_gameTimer = m_gameTimer;
    }

    /**
     * Setter for the wall of the game
     * @param m_wall the wall used in game
     */
    private void setM_wall(Wall m_wall) {
        this.m_wall = m_wall;
    }

    /**
     * Setter for the string object used to hold the output to display
     * @param m_message the string object
     */
    private void setM_message(String m_message) {
        this.m_message = m_message;
    }

    /**
     * Getter for the music object that can start/stop music
     * @return the music manager object
     */
    private MusicManager getM_gameMusic() {return m_gameMusic;}

    /**
     * Getter for the music object that can start/stop music
     * @param m_gameMusic the music manager object
     */
    private void setM_gameMusic(MusicManager m_gameMusic) {
        this.m_gameMusic = m_gameMusic;
    }

    /**
     * Getter for the sprite loader
     * @return the sprite manager object
     */
    public SpriteLoader getM_loadedImage() {
        return m_loadedImage;
    }

    /**
     * Setter for the sprite loader
     * @param m_loadedImage the sprite manager object
     */
    public void setM_loadedImage(SpriteLoader m_loadedImage) {
        this.m_loadedImage = m_loadedImage;
    }

    /**
     * Constructor which creates a timer, manages logic for the game wall,
     * opens the score manager using alert box and updates the message when
     * needed
     * @param owner the view which uses this controller
     * @param ballType the type of ball to use
     * @param lineCount the amount of lines per wall
     */
    public GameBoardController(int lineCount, int ballType,
                               GameBoardView owner) {
        super();
        GameBoardView view = (owner);
        //Initialising new ScoreManager
        ScoreManager score = new ScoreManager();
        setM_message("Press SPACE to start");
        Wall wall = Wall.getInstance();
        wall.initialise((new Rectangle(0, 0, DEF_WIDTH, DEF_HEIGHT)),
                (BRICK_COUNT * lineCount), lineCount, START);
        setM_wall(wall);
        getM_wall().makeWallBall(START, ballType);

        initialiseMusic();
        initialiseSprites();
        //initialize the first level
        getM_wall().nextLevel();
        AlertBox alertPopup = new AlertBox();

        setM_gameTimer((new Timer(TIMER_DELAY, e -> {
            getM_wall().moveBall();
            getM_wall().findImpacts();
            int brickCount = getM_wall().getM_brickCount();
            int ballCount = getM_wall().getM_ballCount();
            setM_message(String.format("Bricks: %d Balls %d", brickCount,
                    ballCount));

            if (getM_wall().getM_ballLost()) {
                if (getM_wall().ballEnd()) {
                    ////
                    String alert = score.calculateTotalScore(getM_message());
                    if(score.getM_isHigher() == 0) {
                        Platform.runLater(() ->
                                alertPopup.display("Game Over", alert));
                    }
                    else if(score.getM_isHigher() == 1) {
                        Platform.runLater(() ->
                                alertPopup.displayInput("Game Over", alert, score));
                    }
                    getM_wall().wallReset();
                }
                getM_wall().gameReset();
                getM_gameTimer().stop();
            } else if (getM_wall().isDone()) {
                if (getM_wall().hasLevel()) {
                    ////
                    String alert = score.calculateCurrentScore(getM_message());
                    Platform.runLater(() ->
                            alertPopup.display(" Go to Next Level", alert));
                    ////
                    getM_gameTimer().stop();
                    getM_wall().gameReset();
                    getM_wall().wallReset();
                    getM_wall().nextLevel();
                } else {
                    ////
                    String alert = score.calculateTotalScore(getM_message());
                    if(score.getM_isHigher() == 0) {
                        Platform.runLater(() ->
                                alertPopup.display("ALL WALLS DESTROYED", alert));
                    }
                    else if(score.getM_isHigher() == 1) {
                        Platform.runLater(() ->
                                alertPopup.displayInput("ALL WALLS DESTROYED", alert, score));
                    }
                    getM_gameTimer().stop();
                }
            }
            view.repaint();
        })));
    }
    /**
     * Creates a new music manager object and starts music
     */
    private void initialiseMusic(){
        setM_gameMusic(new MusicManager());
        getM_gameMusic().initialiseMusic();
        getM_gameMusic().startMusic();
    }

    /**
     * Resets game to initial state
     */
    public void resetGame() {
        setM_message("Restarting Game...");
        getM_wall().gameReset();
        getM_wall().wallReset();
    }

    /**
     * Pauses music if running and vice-versa
     */
    public void alterMusic() {
        if(getM_gameMusic().isM_Playing()){
            getM_gameMusic().stopMusic();
        }
        else if(!getM_gameMusic().isM_Playing()){
            getM_gameMusic().startMusic();
        }
    }

    /**
     * Opens image from resource and saves it in itself as a buffered image
     */
    public void initialiseSprites(){
        SpriteLoader loader = new SpriteLoader();
        try {
            loader.loadImage("/breakout-5.png");
        } catch (IOException e){
            e.printStackTrace();
        }
        setM_loadedImage(loader);

    }
}
