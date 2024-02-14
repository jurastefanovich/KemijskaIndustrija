package com.example.stefanovic.kemijskaindustrija.Controllers.Users;

import com.example.stefanovic.kemijskaindustrija.Authentication.AccessLevel;
import com.example.stefanovic.kemijskaindustrija.Controllers.Navigation.NavBar;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class UsersScene implements UserRepository {
    NavBar navBar = new NavBar();
//    UserProfile userProfile = new UserProfile();


    @FXML
    private AnchorPane root;

    @FXML
    public ComboBox<AccessLevel> accessLevelComboBox;
    @FXML
    public TextField searchByNameTextField;
    @FXML
    public TableColumn<User, AccessLevel> userAccessLevelTableColumn;
    @FXML
    public TableColumn<User, LocalDate> userDateOfBirthTableColumn;
    @FXML
    public TableColumn<User, String> userFullNameTableColumn;
    @FXML
    public TableView<User> usersTableView;

    private List<User> allUsers;

    private void setUsersTableView(List<User> allUsers){
        usersTableView.setItems(FXCollections.observableList(allUsers));
    }
    @FXML
    void filterUsersList() {
        var filtrirano = allUsers.stream()
                .filter(user -> user.getAccount().accessLevel().equals(accessLevelComboBox.getValue()))
                .filter(user -> user.getFullName().contains(searchByNameTextField.getText())).toList();
        setUsersTableView(filtrirano);
    }

    @FXML
    void initialize(){
        allUsers = UserRepository.getAllUsers().stream().filter(user -> !user.getId().equals(UserRepository.getUserID())).toList();
        userAccessLevelTableColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAccount().accessLevel()));
        userFullNameTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFullName()));
        userDateOfBirthTableColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDateOfBirth()));
        accessLevelComboBox.setItems(FXCollections.observableList(Arrays.stream(AccessLevel.values()).toList()));
        setUsersTableView(allUsers);
        showUserDetails();

    }

    /**
     * Features like this are reserved for admin
     */
    private void showUserDetails() {
        usersTableView.setOnMouseClicked(event ->{
            //Add && UserRepository.isAdmin()
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 ){
                User user = usersTableView.getSelectionModel().getSelectedItem();
                if (user != null ){
                    try {
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("UserProfile.fxml"));
                    UserProfile userProfile = loader.getController();
                    Parent parent = loader.load();
                    userProfile = loader.getController();
                    userProfile.setUserInformation(user);
                    root.getChildren().clear();
                    root.getChildren().setAll(parent);

                    } catch (IOException e) {
                        logger.info("Exception occurred trying to view user profile");
                        logger.error(e.getMessage());
                    }

                }
            }
        });
    }
}
