package com.example.stefanovic.kemijskaindustrija.Controllers.AuthControllers;

import com.example.stefanovic.kemijskaindustrija.Authentication.AuthInput;
import com.example.stefanovic.kemijskaindustrija.Authentication.AuthRepository;
import com.example.stefanovic.kemijskaindustrija.Authentication.Credentials;
import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.Controllers.Navigation.NavBar;
import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.AccountException;
import com.example.stefanovic.kemijskaindustrija.Exception.PasswordException;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.slf4j.MDC;

import java.io.IOException;

public final class Login extends Credentials implements AuthRepository {

    NavBar navBar = new NavBar();
    @FXML
    private Label emailErrorLabel;
    @FXML
    private Label passwordErrorLabel;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML public GridPane frame;

    public void initialize(){
        UserRepository.getAllUsers();
        DBController.rewriteAllToTxtFile();
    }

    @FXML
    public void ulogirajKorisnika(){
        String email = emailTextField.getText();
        String pass = passwordTextField.getText();
        User korisnik = findUserByEmail(email, getUserList());
        Methods.resetErorrs(emailErrorLabel,passwordErrorLabel);
        try {
            if(AuthInput.checkLoginCredetials(getLoginCredentialMap(),email,pass)){
                MDC.put("userId", String.valueOf(korisnik.getId()));
                navBar.showHomeScreen();
            }
        } catch (Exception e) {
            if(e instanceof PasswordException) {
                passwordErrorLabel.setText(((PasswordException) e).getMessage());
            }
            if(e instanceof AccountException) {
                emailErrorLabel.setText(e.getMessage());
            }
            else{
                passwordErrorLabel.setText("Please make sure your account exists before trying to log in");
                Main.logger.error("Error trying to log in " + e.getMessage());
            }
        }
    }
    public void showForgottenPasswordScene() throws IOException {
        Main.showScreen("ForgottenPassword.fxml");
    }
    public void showSignUpPasswordScene() throws IOException{
        Main.showScreen("SignUp.fxml");
    }
}
