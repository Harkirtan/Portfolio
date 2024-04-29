package com.view.debug;

import com.model.wall.Wall;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;

/**
 * GridPane class which holds elements to debug the game
 * Converted to JavaFX
 * @author Harkirtan Singh-modified
 */
public class DebugPanel extends GridPane {
    private final int SLIDER_TOP = 4;
    private final int SLIDER_BOTTOM = 0;
    private Slider m_ballXSpeed;
    private Slider m_ballYSpeed;
    private Button m_skipLevel;
    private Button m_resetBalls;
    private Wall m_wall;

    /**
     * Getter for ball X speed slider
     * @return the slider for the x speed of current ball
     */
    public Slider getM_ballXSpeed() {
        return m_ballXSpeed;
    }

    /**
     * Setter for ball X speed slider
     * @param m_ballXSpeed  the slider for the x speed of current ball
     */
    private void setM_ballXSpeed(Slider m_ballXSpeed) {
        this.m_ballXSpeed = m_ballXSpeed;
    }

    /**
     * Getter for ball Y speed slider
     * @return the slider for the y speed of current ball
     */
    public Slider getM_ballYSpeed() {
        return m_ballYSpeed;
    }

    /**
     * Setter for ball Y speed slider
     * @param m_ballYSpeed  the slider for the y speed of current ball
     */
    private void setM_ballYSpeed(Slider m_ballYSpeed) {
        this.m_ballYSpeed = m_ballYSpeed;
    }

    /**
     * Getter for skip level button
     * @return the button that allows user to skip level
     */
    public Button getM_skipLevel() {
        return m_skipLevel;
    }

    /**
     * Setter for skip level button
     * @param m_skipLevel the button that allows user to skip level
     */
    private void setM_skipLevel(Button m_skipLevel) {
        this.m_skipLevel = m_skipLevel;
    }

    /**
     * Getter for reset balls button
     * @return the button that allows user to reset balls
     */
    public Button getM_resetBalls() {
        return m_resetBalls;
    }

    /**
     * Setter for reset balls button
     * @param m_resetBalls  the button that allows user to reset balls
     */
    private void setM_resetBalls(Button m_resetBalls) {
        this.m_resetBalls = m_resetBalls;
    }

    /**
     * Getter for the wall
     * @return wall instance
     */
    private Wall getM_wall() {
        return m_wall;
    }

    /**
     * Setter for the wall
     * @param wall wall instance
     */
    private void setM_wall(Wall wall) {
        this.m_wall = wall;
    }

    /**
     * Constructor to generate 4 main elements, namely,
     * the Skip Level, Reset Balls buttons and the 2 ball speed sliders
     *  and add them to itself
     */
    public DebugPanel() {

        this.setM_wall(Wall.getInstance());

        setM_skipLevel(makeButton("Skip Level: " + getM_wall().getM_level()));
        getM_skipLevel().setOnAction((event) -> checkLevel());
        GridPane.setConstraints(getM_skipLevel(), 1 ,0);

        setM_resetBalls(makeButton("Reset Balls"));
        getM_resetBalls().setOnAction((event) -> getM_wall().resetBallCount());
        GridPane.setConstraints(getM_resetBalls(), 0 ,0);

        setM_ballXSpeed(makeSlider());
        getM_ballXSpeed().valueProperty().addListener((observableValue,
                                                       number, t1) ->
                getM_wall().setBallXSpeed((int) getM_ballXSpeed().getValue()));
        GridPane.setConstraints(getM_ballXSpeed(), 0 ,1);

        setM_ballYSpeed(makeSlider());
        getM_ballYSpeed().valueProperty().addListener((observableValue,
                                                       number, t1) ->
                getM_wall().setBallYSpeed((int) getM_ballYSpeed().getValue()));
        GridPane.setConstraints(getM_ballYSpeed(), 1 ,1);

        this.getChildren().addAll(getM_skipLevel(), getM_ballXSpeed());
        this.getChildren().addAll( getM_resetBalls(), getM_ballYSpeed());
        this.setAlignment(Pos.CENTER);

    }

    /**
     * General button initializer
     * @param title the title to set of the button
     * @return new button with title
     */
    private Button makeButton(String title){

        return new Button(title);
    }

    /**
     * General slider initializer that ranges from 0,4
     * @return slider with default settings
     */
    private Slider makeSlider(){
        Slider slider = new Slider(SLIDER_BOTTOM, SLIDER_TOP, SLIDER_BOTTOM);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1);
        slider.setBlockIncrement(1);
        return slider;
    }

    /**
     * Sets values for the ball speed
     * @param x the x speed of the ball
     * @param y the y speed of the ball
     */
    public void setValues(int x, int y) {
        getM_ballXSpeed().setValue(x);
        getM_ballYSpeed().setValue(y);
    }

    /**
     * Performs a check to see if there is a level and skips to it if possible
     */
    private void checkLevel() {
        if (!getM_wall().hasLevel()) {
            getM_skipLevel().setText("N/A");
        } else
        {
            getM_wall().gameReset();
            getM_wall().nextLevel();
            getM_skipLevel().setText("Skip Level: " + getM_wall().getM_level());
        }
    }
}


