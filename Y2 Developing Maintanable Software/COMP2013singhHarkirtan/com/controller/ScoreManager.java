package com.controller;

import com.model.brick.Brick;
import com.model.wall.Wall;

import java.io.*;

/**
 * Performs score keeping such as current score and total score. Updates
 * highscore file if totalscore is higher than one stored on file
 * @author Harkirtan Singh
 */
public class ScoreManager {
    private int m_totalScore;
    private int m_currentScore;
    private int m_highScore;
    private int m_isHigher = 0;

    /**
     * Getter for the total score of the current player
     * @return the value of the total score
     */
    public int getM_totalScore() {
        return m_totalScore;
    }

    /**
     * Setter for the total score of the current player
     * @param m_totalScore the value of the total score
     */
    private void setM_totalScore(int m_totalScore) {
        this.m_totalScore = m_totalScore;
    }

    /**
     * Getter for the current score of the player
     * @return the value of the current score
     */
    public int getM_currentScore() {
        return m_currentScore;
    }

    /**
     * Setter for the current score of the player
     * @param m_currentScore the value of the current score
     */
    private void setM_currentScore(int m_currentScore) {
        this.m_currentScore = m_currentScore;
    }

    /**
     * Getter for the high score to be loaded from file
     * @return the value of the high score
     */
    private int getM_highScore() {
        return m_highScore;
    }

    /**
     * Setter for the high score to be added to file
     * @param m_highScore the value of the high score
     */
    public void setM_highScore(int m_highScore) {
        this.m_highScore = m_highScore;
    }

    /**
     * Getter for if the total score is higher than highscore
     * @return true if totalscore is higher than highscore
     */
    public int getM_isHigher() {
        return m_isHigher;
    }

    /**
     * Setter for if the total score is higher than highscore
     * @param m_isHigher true if totalscore is higher than highscore
     */
    private void setM_isHigher(int m_isHigher) {
        this.m_isHigher = m_isHigher;
    }

    /**
     * Checks how many bricks are broken in the wall using the wall instance
     * and adds to current score
     * @param message the message to append the current score to
     * @return the message with current score on it
     */
    public String calculateCurrentScore(String message) {

        Wall wall = Wall.getInstance();
        for (Brick brick : wall.getM_bricks()) {
            if (brick.getM_broken()) {
                int temp = getM_currentScore();
                temp += 1;
                setM_currentScore(temp);
            }
        }
        message = String.format("CurrentScore: %d", getM_currentScore());

        return message;
    }

    /**
     * Checks how many bricks are broken in the wall and adds to current score
     * then updates total score.
     * Checks if total score is higher than high score from file
     * @param message the message to append the total score to
     * @return the message with total score on it
     */
    public String calculateTotalScore(String message) {

        Wall wall = Wall.getInstance();
        for (Brick brick : wall.getM_bricks())
            if (brick.getM_broken()) {
                int temp = getM_currentScore();
                temp += 1;
                setM_currentScore(temp);
            }

        int tScore = getM_totalScore();
        int cScore = getM_currentScore();
        tScore += cScore;
        setM_totalScore(tScore);
        String temp = null;
        BufferedReader reader = null;
        try {
            FileReader readFile = new FileReader("highscore.dat");
            reader = new BufferedReader(readFile);
            temp = reader.readLine();
        } catch (Exception e) {

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert temp != null;
        setM_highScore(Integer.parseInt(temp.split(":")[1]));
        if (getM_totalScore() > getM_highScore()) {
            setM_isHigher(1);
            message = String.format("New HighScore %d, Enter Name:",
                    getM_totalScore());
        }

        else{
            message = String.format("TotalScore: %d, HighScore: " + temp,
                    getM_totalScore());
        }

        setM_currentScore(0);

        return message;
    }
    /**
     * Updates file highscore.dat with name and highscore of player in format
     * name:score permanently
     * @param message the message with the data to add to file
     */
    public void updateHighscore(String message) {

        String temp = null;
        temp = String.format(message + ":" + (this.getM_totalScore()));
        File scoreFile = new File("highscore.dat");
        if (!scoreFile.exists()) {
            try {
                scoreFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter writer = null;
        try {
            FileWriter writeFile = new FileWriter(scoreFile);
            writer = new BufferedWriter(writeFile);
            writer.write(temp);
        } catch (Exception e) {
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        setM_isHigher(0);
        setM_totalScore(0);

    }
}
