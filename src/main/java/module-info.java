module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example to javafx.fxml;
    opens org.example.Controller to javafx.fxml;
    exports org.example;
    exports org.example.Controller;
    exports org.example.Model.HighScores;
    exports org.example.Model.Snake;
}
