package com.example.stefanovic.kemijskaindustrija.Controllers.Users;

import com.example.stefanovic.kemijskaindustrija.Controllers.Navigation.NavBar;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Class is reserved only for the logged in user
 * Other user information is displayed in the Single User Profile class
 */
public class UserProfile  implements UserRepository {
    NavBar navBar = new NavBar();
    @FXML
    public Button userEditButton;

    @FXML
    Button deleteUserButton;

    @FXML
    private AnchorPane root;
    @FXML
    public Label userAccessLevelLabel1;
    @FXML
    public Label userDoBLabel;
    @FXML
    public Label userEmailLabel;
    @FXML
    public Label userFirstNameLabel;
    @FXML
    public Label userLastNameLabel;
    @FXML
    public Label usernameLabel;

    private User user;

    public void initialize(){
        setUserInformation(UserRepository.getLoggedInUser());
    }


    @FXML
    void goBackToUserList(ActionEvent event) {
        navBar.showUsersList();
    }

    @FXML
    void deleteUser(){

    }

    @FXML
    void goToUserEditView() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("UserEdit.fxml"));
            UserEdit userEdit = loader.getController();
            Parent parent = loader.load();
            userEdit = loader.getController();
            userEdit.setInformation(user);
            root.getChildren().clear();
            root.getChildren().setAll(parent);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUserInformation(User user){
        this.user = user;
        userAccessLevelLabel1.setText(String.valueOf(user.getAccount().accessLevel()));
        userDoBLabel.setText(String.valueOf(user.getDateOfBirth()));
        userEmailLabel.setText(user.getAccount().email());
        userFirstNameLabel.setText(user.getName());
        userLastNameLabel.setText(user.getLastName());
        usernameLabel.setText(user.getAccount().userName());
    }


}
