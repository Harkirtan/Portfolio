package com.view;

import com.controller.ScoreManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.*;

/**
 * Creates JavaFX stages of 2 types, 1 that displays a message and 1 which can
 * take input
 * @author Harkirtan Singh
 */
public class AlertBox {

    private final int TEXTFIELD_SIZE = 150;
    private final int MIN_WIDTH = 400;
    private final int VBOX_VALUE = 10;

    /**
     * Display a stage with the message passed
     * @param title the string title of the stage
     * @param message the string that will be shown on the stage
     */
    public void display(String title, String message)
    {
        Stage window = new Stage();
        window.centerOnScreen();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(MIN_WIDTH);

        Label label = new Label();
        label.setText(message);
        Button close = new Button("Close");
        close.setOnAction(e -> window.close());

        VBox layout = new VBox(VBOX_VALUE);
        layout.getChildren().addAll(label, close);
        layout.setAlignment(Pos.CENTER);

        label.setId("label");
        close.setId("close");

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("alert.css");
        window.setScene(scene);
        window.show();
    }
    /**
     * Display a stage with the message passed and take string input to pass to
     * score manager
     * @param title the string title of the stage
     * @param message the string that will be shown on the stage
     * @param score the object which does score keeping
     */
    public void displayInput(String title, String message, ScoreManager score)
    {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.centerOnScreen();
        window.setMinWidth(MIN_WIDTH);

        Label label = new Label();
        label.setText(message);
        Button close = new Button("Close");
        close.setOnAction(e -> window.close());
        TextField text = new TextField();
        text.setMaxWidth(TEXTFIELD_SIZE);
        Button set = new Button("Set Name");
        set.setOnAction(e -> score.updateHighscore(text.getText()));

        VBox layout = new VBox(VBOX_VALUE);
        layout.getChildren().addAll(label, text, set, close);
        layout.setAlignment(Pos.CENTER);

        label.setId("label");
        set.setId("set");
        close.setId("close");

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("alert.css");
        window.setScene(scene);
        window.show();
    }


}
