package com.example.stefanovic.kemijskaindustrija.Controllers.AuthControllers;

import com.example.stefanovic.kemijskaindustrija.Authentication.AuthMessages;
import com.example.stefanovic.kemijskaindustrija.Authentication.AuthRepository;
import com.example.stefanovic.kemijskaindustrija.Authentication.Credentials;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public final class ForgotPassword extends Credentials implements AuthRepository {
    @FXML
    private TextField emailTextField;
    @FXML
    private Label passwordLabel;
    @FXML
    public void initialize(){

    }
    public void getPassword(){
        passwordLabel.setText(getLoginCredentialMap().getOrDefault(emailTextField.getText(), AuthMessages.NO_ACCOUNT.getMessage()));
    }
    public void showLoginScreen() throws IOException {
        Main.showScreen("Login.fxml");
    }
}
