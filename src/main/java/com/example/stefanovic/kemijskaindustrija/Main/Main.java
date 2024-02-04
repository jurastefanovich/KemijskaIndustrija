package com.example.stefanovic.kemijskaindustrija.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private static Stage mainStage;
    public static final String LOGIN_FILE = "dat/login.txt";
    public static final String PRODUCT_SERIAL_FILE = "dat/serializedProducts.dat";
    public static final String USERS_SERIAL_FILE = "dat/serializedUsers.dat";

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Login.fxml")));
        Scene scene = new Scene(root);

        stage.setTitle("Kemijska Industrija");
        stage.setScene(scene);
        stage.show();

    }
    public static void showScreen(String screenPath) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(screenPath)));
        Scene scene = new Scene(root);
        getMainStage().setScene(scene);
        getMainStage().show();
    }

    public static Stage getMainStage(){
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }
}