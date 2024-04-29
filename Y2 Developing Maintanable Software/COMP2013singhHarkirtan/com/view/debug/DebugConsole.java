package com.view.debug;

import com.model.ball.Ball;
import com.model.wall.Wall;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Stage class that sets its location and which adds DebugPanel
 * Converted to JavaFX
 * @author Harkirtan Singh-modified
 */
public class DebugConsole extends Stage {

    private static final String TITLE = "Debug Console";
    public final int SIZE = 200;

    private DebugPanel m_debugPanel;

    /**
     * Getter for the debugPanel
     * @return the debug panel that goes on this console
     */
    private DebugPanel getM_debugPanel() {
        return m_debugPanel;
    }

    /**
     * Setter for the debugPanel
     * @param m_debugPanel the GridPane to be set to this console
     */
    private void setM_debugPanel(DebugPanel m_debugPanel) {
        this.m_debugPanel = m_debugPanel;
    }

    /**
     * Constructor to initialise a debugConsole. Creates a debug panel
     * instance and appends to itself
     */
    public DebugConsole() {
        Wall wall = Wall.getInstance();
        initialize();
        setM_debugPanel(new DebugPanel());
        Scene scene = new Scene(getM_debugPanel(), (SIZE+SIZE), SIZE);
        scene.getStylesheets().add("debug.css");
        initializeStyle();
        this.sizeToScene();
        this.setScene(scene);


        setOnShown((event) -> {
            Ball b = wall.getM_ball();
            getM_debugPanel().setValues(b.getM_speedX(), b.getM_speedY());
        });


    }

    /**
     * Initialise title, modality, position for the debug Console
     */
    private void initialize() {
        this.setTitle(TITLE);
        this.initModality(Modality.APPLICATION_MODAL);
        this.centerOnScreen();

    }

    /**
     * Initialise id for elements to be passed to CSS files
     */
    private void initializeStyle() {
        (getM_debugPanel().getM_skipLevel()).setId("skip");
        (getM_debugPanel().getM_resetBalls()).setId("resetBalls");
        (getM_debugPanel().getM_ballXSpeed()).setId("Xslider");
        (getM_debugPanel().getM_ballYSpeed()).setId("Yslider");
    }




}