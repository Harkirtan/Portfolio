package com.view;

import com.controller.GameFrame;
import com.model.ball.BallFactory;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

/**
 * Creates JavaFX stage that allows user to select which ball to use and how
 * many brick lines to play with
 * @author Harkirtan Singh
 */
public class StartOption {
    private ComboBox<String> m_ballType;
    private ComboBox<Integer> m_lineCount;
    private int m_btype = 1;
    private int m_lcount = 3;
    public final int MIN_WIDTH = 300;
    public final int SIZE = 200;
    public final int VBOX = 10;
    public final int SPACING = 10;
    public final int BALL_TYPE = 1;

    /**
     * Getter for the ball type combo box
     * @return the combo box which holds data on the ball type
     */
    private ComboBox<String> getM_ballType() {
        return m_ballType;
    }

    /**
     * Setter for the ball type combo box
     * @param m_ballType the combo box which holds data on the ball type
     */
    private void setM_ballType(ComboBox<String> m_ballType) {
        this.m_ballType = m_ballType;
    }

    /**
     * Getter for the line count combo box
     * @return the combo box which holds data on the line count
     */
    private ComboBox<Integer> getM_lineCount() {
        return m_lineCount;
    }

    /**
     * Setter for the line count combo box
     * @param m_lineCount  the combo box which holds data on the line count
     */
    private void setM_lineCount(ComboBox<Integer> m_lineCount) {
        this.m_lineCount = m_lineCount;
    }

    /**
     * Getter for the assigned ball type
     * @return the integer which will be used to determine ball type
     */
    private int getM_btype() {
        return m_btype;
    }

    /**
     * Setter for the assigned ball type
     * @param m_btype the integer which will be used to determine ball type
     */
    private void setM_btype(int m_btype) {
        this.m_btype = m_btype;
    }

    /**
     * Getter for the assigned line count
     * @return the integer which will be used to determine line count
     */
    private int getM_lcount() {
        return m_lcount;
    }

    /**
     * Setter for the assigned line count
     * @param m_lcount  the integer which will be used to determine line count
     */
    private void setM_lcount(int m_lcount) {
        this.m_lcount = m_lcount;
    }

    /**
     * Adds all labels, combo boxes with default values of ball types and
     * line amount and set buttons to a VBox to add to the Stage's scene
     */
    public void displayOption()
    {
        Stage window = new Stage();
        window.centerOnScreen();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Launch Options");
        window.setMinWidth(MIN_WIDTH);

        BallFactory factory = new BallFactory();
        Point p = new Point(0,0);

        Label ballLabel = new Label();
        ballLabel.setText("Ball Type");
        setM_ballType(new ComboBox<>());
        getM_ballType().getItems().addAll(
                (factory.getBall(1, p).getNAME()),
                (factory.getBall(2, p).getNAME())
        );
        getM_ballType().setPromptText("DEFAULT");
        Button selectBall = new Button("Set Ball");
        selectBall.setOnAction(e -> chooseBall());
        HBox ballLayout = new HBox(ballLabel, getM_ballType(), selectBall);
        ballLayout.setAlignment(Pos.CENTER);
        ballLayout.setSpacing(SPACING);

        Label lineLabel = new Label();
        lineLabel.setText("Line Amount");
        setM_lineCount(new ComboBox<>());
        getM_lineCount().getItems().addAll(
                1,
                2,
                3,
                4,
                5,
                6,
                7
        );
        getM_lineCount().setPromptText("DEFAULT");
        Button selectLine = new Button("Set LineCount");
        selectLine.setOnAction(e -> chooseLineCount());
        HBox lineLayout = new HBox(lineLabel, getM_lineCount(), selectLine);
        lineLayout.setAlignment(Pos.CENTER);
        lineLayout.setSpacing(SPACING);

        Button close = new Button("LAUNCH!");
        close.setOnAction(e -> {
            SwingUtilities.invokeLater(() -> {
                GameFrame frame = new GameFrame(this.getM_lcount(), this.getM_btype());
            });
            window.close();

        });

        VBox layout = new VBox(VBOX);
        layout.getChildren().addAll(ballLayout, lineLayout , close);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(SPACING);

        ballLabel.setId("bLabel");
        getM_ballType().setId("bType");
        selectBall.setId("sBall");
        lineLabel.setId("lLabel");
        getM_lineCount().setId("lType");
        selectLine.setId("sLine");
        close.setId("close");

        Scene scene = new Scene(layout, (SIZE+SIZE+SIZE), SIZE);
        scene.getStylesheets().add("start_option.css");
        window.setScene(scene);
        window.show();
    }
    /**
     * When the set button is clicked checks if comboBox has any value
     * selected and updates the ball type variable with value
     */
    private void chooseBall(){
        if(getM_ballType().getValue() == null){return;}
        if(getM_ballType().getValue() == "Blue") {
            this.setM_btype((BALL_TYPE+BALL_TYPE));}
        else if(getM_ballType().getValue() == "Rubber") {
            this.setM_btype(BALL_TYPE);}
    }
    /**
     * When the set button is clicked checks if comboBox has any value
     * selected and updates the line count variable
     */
    private void chooseLineCount(){
        if(getM_lineCount().getValue() == null){return;}
        else
            this.setM_lcount(getM_lineCount().getValue());

    }

}
