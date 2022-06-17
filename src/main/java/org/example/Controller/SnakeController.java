package org.example.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.App;
import org.example.Model.HighScores.HighScoreHandler;
import org.example.Model.Snake.SnakeHandler;
import org.example.Model.Snake.SnakeSettings;
import org.example.Model.Snake.SnakeStatus;

import java.io.IOException;

public class SnakeController {
    private SnakeSettings settings;

    private GraphicsContext gc;

    @FXML
    Label lbAppleScore, lbApple2Score, lbSnakeLength, lbScore, lbTime;

    @FXML
    Canvas canvas;

    @FXML
    VBox container;

    Timeline timeline;
    Thread threadTimer;

    boolean pressedCtrl = false, pressedShift = false, pressedQ = false;
    KeyCode lastPressedMoveKey = KeyCode.DOWN;

    private int height = 640, width = 640, squareX, squareY;

    private SnakeHandler snakeHandler;
    private HighScoreHandler highScoreHandler;
    private float time = 0.1f;

    public SnakeController(SnakeSettings settings) {
        this.settings = settings;
        squareX = width / settings.sizeX;
        squareY = height / settings.sizeY;

        snakeHandler = new SnakeHandler(settings);
        highScoreHandler = new HighScoreHandler();
    }

    @FXML
    private void initialize() {
        // Get 2d graphics context for drawing on canvas
        gc = canvas.getGraphicsContext2D();

        // Scene is not assigned in initialize since it is still during fxmlloaded.load()
        canvas.sceneProperty().addListener((observable, oldValue, newValue) -> {
            // set events handler when we finally get the scene....
            if(newValue != null){
                newValue.addEventHandler(KeyEvent.KEY_PRESSED, event -> onKeyPressed(event));
                newValue.addEventHandler(KeyEvent.KEY_RELEASED, event -> onKeyReleased(event));

                newValue.getWindow().widthProperty().addListener((observable2, oldValue2, newValue2) -> {
                    width = (int) newValue2.doubleValue();
                    squareX = width / settings.sizeX;
                    canvas.setWidth(width);
                });
                newValue.getWindow().heightProperty().addListener((observable2, oldValue2, newValue2) -> {
                    height = (int) newValue2.doubleValue() - 50;
                    squareY = height / settings.sizeY;
                    canvas.setHeight(height);
                });
            }
        });


        setup();

        run();
        startTimer();
    }

    private void startTimer(){
        threadTimer = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(100);
                    // Go back to UI thread to update the label.
                    Platform.runLater(() -> {
                        time += 0.1f;
                        lbTime.setText("TIME: " + String.format("%02d", (int)Math.floor(time / 60)) + ":"
                                + String.format("%02d", (int)Math.floor(time % 60)) + ":"
                                + String.format("%.0f", (time - (int)time)* 10));
                    });
                    snakeHandler.setCurrentScore(time);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }});
        threadTimer.start();
    }

    private void setup(){
        // Setup initial values
        lbAppleScore.setText("0");
        lbApple2Score.setText("0");
        lbScore.setText("0");
        lbTime.setText("00:00:00");

        lbSnakeLength.setText(settings.length + "");
    }

    @FXML
    public void onKeyPressed(KeyEvent event){
        var code = event.getCode();
        // Set it only when we pressed move key
        if (code == KeyCode.UP || code == KeyCode.DOWN || code == KeyCode.LEFT || code == KeyCode.RIGHT) {
            lastPressedMoveKey = code;
        }

        switch (code) {
            case CONTROL:
            case COMMAND:
            case ALT:
                pressedCtrl = true;
                break;
            case SHIFT:
                pressedShift = true;
                break;
            case Q:
                pressedQ = true;
                break;
            default:
                break;
        }
    }

    @FXML
    public void onKeyReleased(KeyEvent event){
        switch (event.getCode()) {
            case CONTROL:
            case COMMAND:
            case ALT:
                pressedCtrl = false;
                break;
            case SHIFT:
                pressedShift = false;
                break;
            case Q:
                pressedQ = false;
                break;
            default:
                break;
        }
    }

    private void run(){
        drawBackgroud();

        // Set timelien to run according to set speed
        timeline = new Timeline(new KeyFrame(Duration.millis(500 / settings.speed), event -> loopRun()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void loopRun() {
        if(shouldCloseGame()){
            try {
                App.setRoot("mainPage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        drawBackgroud();
        // Draw Snake
        drawSnake();

        drawPoints();

        // Move snake and check if he ate points or died
        var status = snakeHandler.move(lastPressedMoveKey);
        var currentScore = snakeHandler.getCurrentScore();

        // If snake died
        if(status == SnakeStatus.DEAD){
            gc.setFill(Color.RED);
            gc.setFont(new javafx.scene.text.Font(50));
            gc.fillText("GAME OVER", width / 2 - 100, height / 2);

            // Stop all animations
            timeline.stop();
            threadTimer.interrupt();

            try {
                App.createNewWindow("saveHighScorePage", "Save High Score", 400, 400,
                        new SaveHighScoreController(settings, snakeHandler.setCurrentScore(time)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Update score table
        lbAppleScore.setText(currentScore.Score + "");
        lbApple2Score.setText(currentScore.ShortScore + "");
        lbSnakeLength.setText(snakeHandler.getSnakeSize() + "");
        lbScore.setText("SCORE: " + currentScore.getScore() + "");

        var points = snakeHandler.generatePoints(settings.pointsOnBoard);
        // Reset last pressed move key
        lastPressedMoveKey = KeyCode.UNDEFINED;
    }

    private void drawBackgroud(){
        for (int i = 0; i < settings.sizeX; i++) {
            for (int j = 0; j < settings.sizeY; j++) {
                gc.setFill((j + i) % 2 == 0 ? Color.color(0, 0.3, 0) : Color.color(0, 0.5, 0));
                // Canvas 640x640, so we need to scale it
                // and place colors in correct places
                gc.fillRect(i * squareX, j * squareY, squareX, squareY);
            }
        }
    }

    private boolean shouldCloseGame(){
        return pressedCtrl && pressedShift && pressedQ;
    }

    private void drawPoints(){
        var points = snakeHandler.getPoints();

        for (int i = 0; i < points.size(); i++) {
            var point = points.get(i);
            var image = new Image(snakeHandler.getImgForPoint(i));
            gc.drawImage(image, point.X * squareX, point.Y * squareY, squareX, squareY);
        }
    }

    private void drawSnake(){
        // Draw snake
        for (int i = 0; i < snakeHandler.getSnake().size(); i++) {
            var snakePart = snakeHandler.getSnake().get(i);
            var imgPath = snakeHandler.getImgForPart(i);

            var img = new Image(imgPath);
            gc.drawImage(img, snakePart.X * squareX, snakePart.Y * squareY, squareX, squareY);
        }
    }
}
