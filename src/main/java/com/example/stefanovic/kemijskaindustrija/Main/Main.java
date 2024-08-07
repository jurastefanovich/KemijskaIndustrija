package com.example.stefanovic.kemijskaindustrija.Main;

import com.example.stefanovic.kemijskaindustrija.Authentication.AccessLevel;
import com.example.stefanovic.kemijskaindustrija.Authentication.Account;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Model.User;
import com.example.stefanovic.kemijskaindustrija.Threads.EquipmentHealthbarThread;
import com.example.stefanovic.kemijskaindustrija.Threads.ServiceThread;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    private static Stage mainStage;
    public static final String LOGIN_FILE = "dat/login.txt";
    public static final String SAFETY_PROTOCOL_FILE = "dat/safetyProtocols.txt";
    public static final String SAFETY_PROTOCOL_STEP_FILE = "dat/safetyProtocolSteps.txt";
    public static final String SERIALIZED_CHANGES = "dat/serializedChanges.dat";
    public static final String USERS_FILE = "dat/userChanges.txt";
    public static final String SERIALIZE_SAFETY_PROTOCOL_STEP = "dat/safetyProtocolStep.dat";

    public static Logger logger = LoggerFactory.getLogger(Main.class);

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

//        var healthBarThread = new Timeline(new KeyFrame(Duration.minutes(2), e-> Platform.runLater(new EquipmentHealthbarThread())));
//        healthBarThread.setCycleCount(Timeline.INDEFINITE);
//        healthBarThread.play();
//
//        var serviceThread = new Timeline(new KeyFrame(Duration.minutes(3), e-> Platform.runLater(new ServiceThread())));
//        serviceThread.setCycleCount(Timeline.INDEFINITE);
//        serviceThread.play();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(new EquipmentHealthbarThread(), 0, 2, TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(new ServiceThread(), 0, 3, TimeUnit.MINUTES);
        Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdown));
    }


    public static Stage getMainStage(){
        return mainStage;
    }

    public static void main(String[] args) {
        if(!UserRepository.isUserPressent("admin")){
            Account account = new Account("admin", "admin","admin", AccessLevel.ADMIN);
            User user = new User("ADMIN","ADMIN", LocalDate.parse("1998-03-03"),account);
            UserRepository.createAccount(user);
        }
        launch();
    }
}