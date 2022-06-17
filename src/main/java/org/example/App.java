package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("mainPage"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Snake");
        stage.show();

        this.stage = stage;
    }

    public static Scene setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        return scene;
    }

    public static <T> Scene setRoot(String fxml, T controller) throws IOException {
        scene.setRoot(loadFXML(fxml, controller));
        return scene;
    }

    public static void setWindowSize(int width, int height) {
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public static void createNewWindow(String fxml, String title, int x, int y) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(loadFXML(fxml), x, y));
        stage.setTitle(title);
        stage.show();
    }

    public static <T> void createNewWindow(String fxml, String title, int x, int y, T controller) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(loadFXML(fxml, controller), x, y));
        stage.setTitle(title);
        stage.show();
    }

    public static <T> Stage loadStage(String fxml, String title, int x, int y, T controller) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(loadFXML(fxml, controller), x, y));
        stage.setTitle(title);
        return stage;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private static <T> Parent loadFXML(String fxml, T controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        fxmlLoader.setController(controller);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}