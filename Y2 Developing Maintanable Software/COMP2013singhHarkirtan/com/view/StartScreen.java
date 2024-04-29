package com.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Creates a start screen using JavaFX
 * @author Harkirtan Singh
 */
public class StartScreen extends Application {

    public final int SIZE = 400;
    /**
     * Initialises scene and adds launch and exit button to it
     * @param stage the stage on which the scene will be added to
     */
    @Override
    public void start(Stage stage) {

        Platform.setImplicitExit(false);

        Button launch = new Button("Launch Game");

        launch.setOnAction(e -> {
            StartOption option = new StartOption();
            option.displayOption();
            stage.close();

        });
        Button close = new Button("Exit");
        close.setOnAction(e -> System.exit(0));

        Label title = new Label();
        title.setText("BreakOut");
        launch.setId("launch");
        title.setId("title");
        close.setId("close");

        VBox root = new VBox(title, launch,close);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, SIZE, SIZE);
        scene.getStylesheets().add("menusheet.css");
        stage.setTitle("MENU");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}