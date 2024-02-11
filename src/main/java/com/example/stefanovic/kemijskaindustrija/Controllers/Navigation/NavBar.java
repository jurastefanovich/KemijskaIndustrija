package com.example.stefanovic.kemijskaindustrija.Controllers.Navigation;

import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;

import java.io.IOException;

public class NavBar {

    @FXML
    private MenuItem AdminView;
    @FXML
    private Text chemSafeLogo;
    @FXML
    public void initialize(){
//        AdminView.setVisible(UserRepository.isAdmin());
        chemSafeLogo.setOnMouseClicked(event -> showHomeScreen());
    }

    public void showSingUpScreen() throws IOException{
        Main.showScreen("SingUp.fxml");
    }
    public void showLoginScreen() throws IOException{
        Main.showScreen("Login.fxml");
    }
    public void showForgottenPasswordScreen() throws IOException{
        Main.showScreen("ForgottenPassword.fxml");
    }

    public void logout() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to log out?", ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response->{
            if ( response.getButtonData().equals(ButtonBar.ButtonData.OK_DONE))
            {
                try {
                    Main.showScreen("Login.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void showChemicalList()  {
        try {
            Main.showScreen("ChemicalScene.fxml");
        } catch (IOException e) {
            System.out.println("Error showing chem list");
            throw new RuntimeException(e);
        }
    }

    public void showInventory(){
        try {
            Main.showScreen("Inventory.fxml");
        } catch (IOException e) {
            System.out.println("Error showing inventory list");
            throw new RuntimeException(e);
        }
    }

    public void showMyProfile() {
        try {
            Main.showScreen("UserProfile.fxml");
        } catch (IOException e) {
            System.out.println("Error showing user profile");
            throw new RuntimeException(e);
        }
    }
    public void showHomeScreen(){
        try {
            Main.showScreen("HomePage.fxml");
        } catch (IOException e) {
            System.out.println("Error showing home screen");
            throw new RuntimeException(e);
        }
    }
    public void showUsersList() {
        try {
            Main.showScreen("UsersScene.fxml");
        } catch (IOException e) {
            System.out.println("Error showing supplier scene");
            throw new RuntimeException(e);
        }
    }

    public void showProfileEdit(){
        try {
            Main.showScreen("UserEdit.fxml");
        } catch (IOException e) {
            System.out.println("Error showing user edit");
            throw new RuntimeException(e);
        }
    }
    public void showUserProfile(){
        try {
            Main.showScreen("UserProfile.fxml");
        } catch (IOException e) {
            System.out.println("Error showing user profile");
            throw new RuntimeException(e);
        }
    }

    public void showAddChemical() {
        try {
            Main.showScreen("ChemicalInput.fxml");
        } catch (IOException e) {
            System.out.println("Error showing chemical input");
            throw new RuntimeException(e);
        }
    }




    public void addProtocol() {
        try {
            Main.showScreen("ProtocolInput.fxml");
        } catch (IOException e) {
            System.out.println("Error showing protocol input");
            throw new RuntimeException(e);
        }
    }

    public void showProtocolList() {
        try {
            Main.showScreen("ProtocolList.fxml");
        } catch (IOException e) {
            System.out.println("Error showing protocol list");
            throw new RuntimeException(e);
        }
    }

    public void showEquipmentInput() {
        try {
            Main.showScreen("EquipementInput.fxml");
        } catch (IOException e) {
            System.out.println("Error showing equipment input");
            throw new RuntimeException(e);
        }
    }

    public void showEquipmenList() {
        try {
            Main.showScreen("EquipmentList.fxml");
        } catch (IOException e) {
            System.out.println("Error showing equipment input");
            throw new RuntimeException(e);
        }
    }

    public void goToServiceInput() {
        try {
            Main.showScreen("ServiceInput.fxml");
        } catch (IOException e) {
            System.out.println("Error showing service input");
            throw new RuntimeException(e);
        }
    }

    public void goToAllServices() {
        try {
            Main.showScreen("ServiceList.fxml");
        } catch (IOException e) {
            System.out.println("Error showing equipment input");
            throw new RuntimeException(e);
        }
    }
}
