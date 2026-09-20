/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xo_game;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class XO_Game extends Application {
    
@Override
    public void start(Stage primaryStage) {
        new GameController(primaryStage).startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
