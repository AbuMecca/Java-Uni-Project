/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xo_game;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ScoreboardView {
    private final Stage stage;
    private final DatabaseManager db;
    private final TableView<Score> table = new TableView<>();

    public ScoreboardView(DatabaseManager dbManager) {
        this.db = dbManager;
        stage = new Stage();
        stage.setTitle("Scoreboard");

        TableColumn<Score, String> c1 = new TableColumn<>("Player 1");
        c1.setCellValueFactory(new PropertyValueFactory<>("player1"));

        TableColumn<Score, String> c2 = new TableColumn<>("Player 2");
        c2.setCellValueFactory(new PropertyValueFactory<>("player2"));

        TableColumn<Score, String> c3 = new TableColumn<>("Winner");
        c3.setCellValueFactory(new PropertyValueFactory<>("winner"));

        table.getColumns().addAll(c1, c2, c3);

        Button reset = new Button("Reset");
        reset.setOnAction(e -> {
            db.resetGames();
            table.getItems().clear();
        });

        VBox root = new VBox(10, table, reset);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(400, 400);
        stage.setScene(new Scene(root));
    }

    public void show() {
        List<Score> scores = db.fetchAllGames();
        table.getItems().setAll(scores);
        stage.show();
    }
}

