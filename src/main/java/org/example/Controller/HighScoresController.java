package org.example.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.App;
import org.example.Model.HighScores.HighScore;
import org.example.Model.HighScores.HighScoreHandler;

import java.io.IOException;

public class HighScoresController {
    @FXML
    TableView<HighScore> highScoresTable;

    @FXML
    TableColumn<HighScore, String> colName, colBoard;

    @FXML
    TableColumn<HighScore, String> colScore, colTime, colPoints, colShortPoints, colLength;

    @FXML
    Button btnGoBack;

    private HighScoreHandler highScoreHandler = new HighScoreHandler();

    @FXML
    public void initialize() {
        // Setup columns
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().Name));
        colBoard.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().SizeX + "x" + cell.getValue().SizeY));
        colScore.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().OverallScore + ""));
        colTime.setCellValueFactory(cell -> new SimpleStringProperty(String.format("%02d", (int)Math.floor(cell.getValue().Time / 60)) + ":"
                + String.format("%02d", (int)Math.floor(cell.getValue().Time % 60)) + ":"
                + String.format("%.0f", (cell.getValue().Time - (int)cell.getValue().Time)* 10)));
        colPoints.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().Score + ""));
        colShortPoints.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().ShortScore + ""));
        colLength.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().SnakeLength + ""));

        var highScores = highScoreHandler.loadHighScore();
        highScores.sort((h1, h2) -> h2.getScore() - h1.getScore());

        highScoresTable.getItems().addAll(highScores);

        btnGoBack.setOnMouseClicked(event -> {
            try {
                App.setRoot("mainPage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
