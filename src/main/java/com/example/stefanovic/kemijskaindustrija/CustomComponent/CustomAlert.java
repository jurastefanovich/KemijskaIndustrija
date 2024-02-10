package com.example.stefanovic.kemijskaindustrija.CustomComponent;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.MalformedURLException;

public class CustomAlert extends Alert {
    public CustomAlert(AlertType alertType) {
        super(alertType);
    }

    public CustomAlert(AlertType alertType, String s, ButtonType... buttonTypes) {
        super(alertType, s, buttonTypes);
    }

    private void initializeStyle() {
        // Set the stage style to transparent
        StageStyle stageStyle = StageStyle.TRANSPARENT;
        ((Stage) getDialogPane().getScene().getWindow()).initStyle(stageStyle);
        File f = new File("src/main/resources/com/example/stefanovic/kemijskaindustrija/Main/css/style.css");
        try {
            getDialogPane().getStylesheets().setAll(f.toURI().toURL().toExternalForm());
            getDialogPane().setHeader(null);
            getDialogPane().setGraphic(null);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
