package com.example.stefanovic.kemijskaindustrija.Controllers.Users;

import com.example.stefanovic.kemijskaindustrija.Authentication.AccessLevel;
import com.example.stefanovic.kemijskaindustrija.Controllers.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class UserEdit implements UserRepository{

    @FXML
    public AnchorPane acceessLevelAnchorPane;
    @FXML
    public Label confirmPasswordLabel;
    @FXML
    public TextField confirmPasswordTextField;
    @FXML
    public Label emailErrorLabel;
    @FXML
    public TextField emailTextField;
    @FXML
    public Label lastNameErrorLabel;
    @FXML
    public TextField lastNameTextField;
    @FXML
    public Label nameErrorLabel;
    @FXML
    public TextField nameTextField;
    @FXML
    public Label passwordErrorLabel;
    @FXML
    public TextField passwordTextField;
    @FXML
    public ComboBox<AccessLevel> accessLevelComboBox;
    @FXML
    public Label title;
    @FXML
    public Label userNameErrorLabel;
    @FXML
    public TextField userNameTextField;

    @FXML
    private Label confirmPasswordErrorLabel;
    private User user;

    @FXML
    void initialize(){
        resetLabels();
        setInformation(UserRepository.getLoggedInUser());
        checkIfEditingLoggedInUser();
//        setPasswordInfo();
    }

    public void setInformation(User user) {
        resetLabels();
        this.user = user;
        emailTextField.setText(user.getAccount().email());
        passwordTextField.setText(user.getAccount().password());
        userNameTextField.setText(user.getAccount().userName());
        nameTextField.setText(user.getName());
        lastNameTextField.setText(user.getLastName());

    }

    private void resetLabels(){
        Methods.resetErorrs(emailErrorLabel,nameErrorLabel,lastNameErrorLabel,userNameErrorLabel,passwordErrorLabel,confirmPasswordErrorLabel);

    }
    private boolean checkIfEditingLoggedInUser(){
       return user.getId().equals(UserRepository.getLoggedInUser().getId());
    }


    private void setPasswordInfo(){
        confirmPasswordTextField.setVisible(checkIfEditingLoggedInUser());
        passwordTextField.setVisible(checkIfEditingLoggedInUser());
        passwordErrorLabel.setVisible(checkIfEditingLoggedInUser());
        confirmPasswordLabel.setVisible(checkIfEditingLoggedInUser());
    }

    private void initializeAccessLevel(){

    }
}
