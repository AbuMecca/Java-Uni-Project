package xo_game;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.AudioClip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * GameController:
 * Manages the X&O game flow with timer, scoreboard, image symbols,
 * and background/voice audio. Audio is optional: if any sound file
 * is missing or cannot be played, that sound is simply skipped.
 */
public class GameController {
    private static final int GRID_SIZE = 3;

    private final Stage primaryStage;
    private final Board board = new Board();
    private Player p1, p2, current;
    private Label nameLabel, timerLabel;
    private TurnTimer turnTimer;
    private final DatabaseManager db = new DatabaseManager();
    private int gamesPlayed = 0;

    // Audio (may be null if a file is missing/unplayable)
    private AudioClip bgMusic, xClick, oClick;

    public GameController(Stage stage) {
        this.primaryStage = stage;
    }

    public void startGame() {
        // 1) Prompt for tags
        String tag1 = prompt("Player 1: enter your tag");
        String tag2 = prompt("Player 2: enter your tag");
        // 2) Pick symbol images
        Image img1 = chooseImage(tag1 + ": choose your symbol");
        Image img2 = chooseImage(tag2 + ": choose your symbol");
        // 3) Load & play audio
        initAudio();
        // 4) Randomize who is X/O
        List<Character> syms = Arrays.asList('X', 'O');
        Collections.shuffle(syms);
        p1 = new Player(tag1, syms.get(0), img1);
        p2 = new Player(tag2, syms.get(1), img2);
        current = p1;
        // 5) Build the UI
        buildUI();
    }

    private String prompt(String text) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setHeaderText(text);
        return dlg.showAndWait()
            .orElseThrow(() -> new RuntimeException("Cancelled"));
    }

    private Image chooseImage(String title) {
        FileChooser fc = new FileChooser();
        fc.setTitle(title);
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.gif"),
            new FileChooser.ExtensionFilter("All files", "*.*")
        );
        java.io.File f = fc.showOpenDialog(primaryStage);
        if (f == null) throw new RuntimeException("No image selected");
        return new Image(f.toURI().toString());
    }

    /**
     * Loads audio from the /audio folder on the classpath.
     * Background music is now bg.mp3 (much smaller than bg.wav).
     * Any missing or unplayable file is skipped so the game still runs.
     */
    private void initAudio() {
        bgMusic = loadClip("/audio/bg.mp3");
        xClick  = loadClip("/audio/X.wav");
        oClick  = loadClip("/audio/O.wav");

        if (bgMusic != null) {
            bgMusic.setCycleCount(AudioClip.INDEFINITE);
            bgMusic.play();
        }
    }

    /** Loads one AudioClip, returning null if it is missing or can't be played. */
    private AudioClip loadClip(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.out.println("Audio not found (skipping): " + path);
                return null;
            }
            return new AudioClip(url.toExternalForm());
        } catch (Exception e) {
            System.out.println("Could not load audio (skipping): " + path);
            return null;
        }
    }

    private void buildUI() {
        BorderPane root = new BorderPane();

        // Top bar: timer + current player
        timerLabel = new Label("Time: 10");
        timerLabel.setStyle("-fx-font-size:18px;");
        nameLabel  = new Label("Player: " + current.getGamerTag());
        nameLabel.setStyle("-fx-font-size:18px;");
        HBox top = new HBox(20, timerLabel, nameLabel);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(10));
        root.setTop(top);

        // Center: 3x3 grid
        GridPane grid = new GridPane();
        for (int i = 0; i < GRID_SIZE; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / GRID_SIZE);
            cc.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(cc);

            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0 / GRID_SIZE);
            rc.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(rc);
        }
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                Button b = new Button();
                b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                // default black background + white border
                b.setBackground(new Background(
                    Collections.singletonList(
                        new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)
                    ), Collections.emptyList()
                ));
                b.setBorder(new Border(
                    new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY, new BorderWidths(3))
                ));
                final int rr = r, cc = c;
                b.setOnAction(e -> cellClick(b, rr, cc));
                grid.add(b, c, r);
            }
        }
        root.setCenter(grid);

        // Bottom: scoreboard button
        Button sb = new Button("View Scoreboard");
        sb.setOnAction(e -> new ScoreboardView(db).show());
        BorderPane.setAlignment(sb, Pos.CENTER);
        root.setBottom(sb);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("X&O Local");
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(e -> {
            if (turnTimer != null) turnTimer.stop();
            if (bgMusic != null) bgMusic.stop();
            Platform.exit();
        });
        primaryStage.show();

        turnTimer = new TurnTimer(timerLabel, this::onTimeout);
        turnTimer.start();
    }

    private void cellClick(Button b, int r, int c) {
        // skip if already played
        if (!b.getBackground().getImages().isEmpty()) return;

        // 1) sound (skip silently if the clip is missing)
        char sym = current.getSymbol();
        AudioClip clip = (sym == 'X' ? xClick : oClick);
        if (clip != null) clip.play();

        // 2) paint with a BackgroundImage that fills the button
        BackgroundImage bi = new BackgroundImage(
            current.getSymbolImage(),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(1, 1, true, true, false, false)
        );
        b.setBackground(new Background(
            Collections.singletonList(
                new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)
            ),
            Collections.singletonList(bi)
        ));
        b.setBorder(new Border(
            new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(3))
        ));

        // 3) game logic
        board.setCell(r, c, sym);
        turnTimer.stop();

        if (board.hasWinner(sym)) {
            showResult(current);
        } else if (board.isFull()) {
            showResult(null);
        } else {
            switchPlayer();
            turnTimer.start();
        }
    }

    private void onTimeout() {
        switchPlayer();
        turnTimer.start();
    }

    private void switchPlayer() {
        current = (current == p1 ? p2 : p1);
        nameLabel.setText("Player: " + current.getGamerTag());
    }

    private void showResult(Player winner) {
        String msg = (winner == null ? "Draw!" : winner.getGamerTag() + " wins!");
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();

        db.saveGame(new Score(
            p1.getGamerTag(),
            p2.getGamerTag(),
            winner == null ? "Draw" : winner.getGamerTag()
        ));

        if (++gamesPlayed >= 10) {
            db.resetGames();
            gamesPlayed = 0;
        }

        resetBoard();
    }

    private void resetBoard() {
        board.clear();
        GridPane grid = (GridPane) ((BorderPane) primaryStage
            .getScene().getRoot()).getCenter();
        grid.getChildren().forEach(node -> {
            Button b = (Button) node;
            b.setBackground(new Background(
                Collections.singletonList(
                    new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)
                ), Collections.emptyList()
            ));
            b.setBorder(new Border(
                new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, new BorderWidths(3))
            ));
        });
    }
}
