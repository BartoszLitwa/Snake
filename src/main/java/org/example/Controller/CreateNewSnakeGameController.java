package org.example.Controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.App;
import org.example.Model.Snake.SnakeSettings;

import java.io.IOException;

public class CreateNewSnakeGameController {
    @FXML
    TextField tfBoardSizeX, tfBoardSizeY, tfSpeed, tfLength;

    @FXML
    CheckBox cbMoveToOtherSide;

    private SnakeSettings settings = new SnakeSettings();

    @FXML
    public void initialize(){
        // Setup initial values
        tfBoardSizeX.setText(settings.sizeX + "");
        tfBoardSizeY.setText(settings.sizeY + "");
        tfSpeed.setText(settings.speed + "");
        tfLength.setText(settings.length + "");
        cbMoveToOtherSide.setSelected(settings.moveToOtherSide);

        setup();
    }

    private void setup(){
        // Add listeners for text fields to accept only numbers
        tfBoardSizeX.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("\\d*")){
                tfBoardSizeX.setText(newValue);

                if(newValue != "" && Integer.parseInt(newValue) > 0){
                    settings.sizeX = Integer.parseInt(newValue);
                }
            }else{
                tfBoardSizeX.setText(oldValue);
            }
        });
        tfBoardSizeY.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("\\d*")){
                tfBoardSizeY.setText(newValue);

                if(newValue != "" && Integer.parseInt(newValue) > 0){
                    settings.sizeY = Integer.parseInt(newValue);
                }
            }else{
                tfBoardSizeY.setText(oldValue);
            }
        });
        tfSpeed.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("\\d*")){
                tfSpeed.setText(newValue);
                if(newValue != "" && Integer.parseInt(newValue) > 0){
                    settings.speed = Integer.parseInt(newValue);
                }
            }else{
                tfSpeed.setText(oldValue);
            }
        });
        tfLength.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("\\d*")){
                tfLength.setText(newValue);

                if(newValue != "" && Integer.parseInt(newValue) > 0){
                    settings.length = Integer.parseInt(newValue);
                }
            }else{
                tfLength.setText(oldValue);
            }
        });
    }

    private void setSettings(){
        settings.sizeX = Integer.parseInt(tfBoardSizeX.getText());
        settings.sizeY = Integer.parseInt(tfBoardSizeY.getText());
        settings.speed = Integer.parseInt(tfSpeed.getText());
        settings.length = Integer.parseInt(tfLength.getText());
        settings.moveToOtherSide = cbMoveToOtherSide.isSelected();
    }

    @FXML
    public void createNewGame(){
        setSettings();

        try {
            // Close the window
            var stage = (Stage) tfBoardSizeX.getScene().getWindow();
            stage.close();

            var scene = App.setRoot("snakePage", new SnakeController(settings));
            App.setWindowSize(640, 692);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
