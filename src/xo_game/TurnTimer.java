/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xo_game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * TurnTimer:
 * Displays a 10s countdown in a Label, then runs a callback.
 */
public class TurnTimer {
    private final Timeline timeline;
    private int timeLeft;
    private final Label timerLabel;
    private final Runnable onTimeout;

    public TurnTimer(Label timerLabel, Runnable onTimeout) {
        this.timerLabel = timerLabel;
        this.onTimeout  = onTimeout;
        timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> tick())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /** Start (or restart) the 10s countdown. */
    public void start() {
        timeLeft = 10;
        timerLabel.setText("Time: " + timeLeft);
        timeline.playFromStart();
    }

    /** Stop the countdown. */
    public void stop() {
        timeline.stop();
    }

    /** Called once per second by the Timeline. */
    private void tick() {
        timeLeft--;
        timerLabel.setText("Time: " + timeLeft);
        if (timeLeft <= 0) {
            stop();
            onTimeout.run();
        }
    }
}

