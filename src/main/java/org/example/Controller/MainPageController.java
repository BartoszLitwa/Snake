package org.example.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.App;

import java.io.IOException;

public class MainPageController {
    @FXML
    public void ExitGame() {
        System.exit(0);
    }

    @FXML
    public void StartNewGame() {
        try {
            App.createNewWindow("createNewSnakeGamePage", "Create New Snake Game", 400, 400);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void OpenHighScoresPage() {
        try {
            App.setRoot("highScoresPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
