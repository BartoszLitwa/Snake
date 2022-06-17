package org.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.App;
import org.example.Model.HighScores.HighScore;
import org.example.Model.HighScores.HighScoreHandler;
import org.example.Model.Snake.SnakeSettings;

import java.io.IOException;

public class SaveHighScoreController {
    @FXML
    TextField tfName;

    @FXML
    Label lbScore, lbTime, lbAppleScore, lbApple2Score, lbSnakeLength;

    @FXML
    Button btnSave;

    private SnakeSettings settings;
    private HighScore highScore;
    private HighScoreHandler highScoreHandler;

    public SaveHighScoreController(SnakeSettings settings, HighScore highScore) {
        this.settings = settings;
        this.highScore = highScore;
        highScoreHandler = new HighScoreHandler();
    }

    @FXML
    public void initialize(){
        lbScore.setText("SCORE: " + highScore.getScore());
        lbTime.setText("TIME: " + String.format("%02d", (int)Math.floor(highScore.Time / 60)) + ":"
                + String.format("%02d", (int)Math.floor(highScore.Time % 60)) + ":"
                + String.format("%.0f", (highScore.Time - (int)highScore.Time)* 10));
        lbAppleScore.setText("POINTS: " + highScore.Score);
        lbApple2Score.setText("SHORT POINTS: " + highScore.ShortScore);
        lbSnakeLength.setText("SNAKE LENGTH: " + highScore.SnakeLength);

        // Setup initial values
        btnSave.setOnMouseClicked(event -> {
            highScore.Name = tfName.getText();
            // Save score
            highScoreHandler.saveHighScores(highScore);

            try {
                // Close the window
                var stage = (Stage) btnSave.getScene().getWindow();
                stage.close();

                App.setRoot("mainPage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
