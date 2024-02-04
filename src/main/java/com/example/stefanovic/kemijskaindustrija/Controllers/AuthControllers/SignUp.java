package com.example.stefanovic.kemijskaindustrija.Controllers.AuthControllers;

import com.example.stefanovic.kemijskaindustrija.Authentication.*;
import com.example.stefanovic.kemijskaindustrija.Controllers.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.AccountException;
import com.example.stefanovic.kemijskaindustrija.Exception.EmailException;
import com.example.stefanovic.kemijskaindustrija.Exception.PasswordException;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public final class SignUp extends Credentials implements AuthRepository {
    @FXML
    private Label accountCreatedLabel;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private Label userNameErrorLabel;
    @FXML
    private Label passwordErrorLabel;
    @FXML
    private Label confirmPassErrrorLabel;
    @FXML
    private Label imeErrorLabel;

    @FXML
    private Label lastNameErrorLabel;
    @FXML
    private Label datumRodjenjaErrorLabel;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private DatePicker datumRodenja;
    @FXML
    private TextField imeTextField;
    @FXML
    private TextField prezimeTextField;

    @FXML private AnchorPane frame;
    @FXML
    public void initialize(){}

    public void signUp()  {
        Methods.resetErorrs(accountCreatedLabel, userNameErrorLabel, emailErrorLabel, passwordErrorLabel, confirmPassErrrorLabel,datumRodjenjaErrorLabel,imeErrorLabel,lastNameErrorLabel);
        String email = emailTextField.getText();
        String userName = userNameTextField.getText();
        String pass = passwordField.getText();
        String passConfirm = confirmPasswordField.getText();
        String ime = imeTextField.getText();
        String prezime = prezimeTextField.getText();
        LocalDate datumRodjenja = safeGetDateFromDatePicker(datumRodenja);

        try{
            List<String> userNameList = getUserList().stream().map(user -> user.getAccount().userName()).toList();
//            Authenticating fields
            AuthInput.checkUserName(userNameList, userName);
            AuthInput.checkEmail(getLoginCredentialMap().keySet().stream().toList(), email);
            AuthInput.checkPassword(pass,passConfirm);
            AuthInput.inputChecker(String.valueOf(datumRodjenja));
            AuthInput.inputChecker(ime);
            AuthInput.inputChecker(prezime);

            Account account = new Account(email,pass,userName, AccessLevel.USER);
            User user = new User(ime,prezime,datumRodjenja,account);
            UserRepository.createAccount(user);

            accountCreatedLabel.setText(AuthMessages.SUCCESFFUL_ACCOUNT_CREATION.getMessage());

        }catch (PasswordException | NullPointerException | AccountException | EmailException e){

            if(e instanceof EmailException) {
                emailErrorLabel.setText(((EmailException) e).getMessage());
            }

            if (e instanceof PasswordException) {
                passwordErrorLabel.setText(((PasswordException) e).getMessage());
            }

            if(e instanceof AccountException) {
                if (((AccountException) e).getMessage().equals(AuthMessages.EMAIL_TAKEN.getMessage())){
                    emailErrorLabel.setText(((AccountException) e).getMessage());
                }
                if (((AccountException) e).getMessage().equals(AuthMessages.USERNAME_TAKEN.getMessage())){
                    userNameErrorLabel.setText(((AccountException) e).getMessage());
                }
                else{
                    accountCreatedLabel.setText(((AccountException) e).getMessage());
                }
            }
            if (e instanceof NullPointerException){
                if (emailTextField.getText().isEmpty()) {emailErrorLabel.setText(((NullPointerException) e).getMessage());}
                if (passwordField.getText().isEmpty()) {passwordErrorLabel.setText(((NullPointerException) e).getMessage());}
                if (confirmPasswordField.getText().isEmpty()) {confirmPassErrrorLabel.setText(((NullPointerException) e).getMessage());}
                if (userNameTextField.getText().isEmpty()) {userNameErrorLabel.setText(((NullPointerException) e).getMessage());}
                if (imeTextField.getText().isEmpty()) {imeErrorLabel.setText(((NullPointerException) e).getMessage());}
                if (prezimeTextField.getText().isEmpty()) {lastNameErrorLabel.setText(((NullPointerException) e).getMessage());}
                if (datumRodenja.getPromptText() == null) {datumRodjenjaErrorLabel.setText(((NullPointerException) e).getMessage());}
            }

        }
    }

    //if the user enters a date manually the DatePicker won't return an error
    private static LocalDate safeGetDateFromDatePicker(DatePicker datePicker) {
        return datePicker.getConverter().fromString(datePicker.getEditor().getText());
    }

    public void gotToLogin() throws IOException {
        Main.showScreen("Login.fxml");
    }

}
