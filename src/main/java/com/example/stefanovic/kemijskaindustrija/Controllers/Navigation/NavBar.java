package com.example.stefanovic.kemijskaindustrija.Controllers.Navigation;

import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NavBar {

    @FXML
    private Text chemSafeLogo;

    @FXML
    private Hyperlink inventoryLink;

    @FXML
    private Hyperlink allUsersLink;
    @FXML
    public void initialize(){
        boolean isAdmin = UserRepository.isAdmin();
        chemSafeLogo.setOnMouseClicked(event -> showHomeScreen());
        inventoryLink.setVisible(isAdmin);
        allUsersLink.setVisible(isAdmin);
    }

    public void showSingUpScreen() {
        try {
            Main.showScreen("SingUp.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show singup.fxml");
            Main.logger.error(e.getMessage());
        }
    }
    public void showLoginScreen() {
        try {
            Main.showScreen("Login.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show login");
            Main.logger.error(e.getMessage());
        }
    }
    public void showForgottenPasswordScreen(){
        try {
            Main.showScreen("ForgottenPassword.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show ForgottenPassword.fxml");
            Main.logger.error(e.getMessage());
        }
    }

    public void logout() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to log out?", ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response->{
            if ( response.getButtonData().equals(ButtonBar.ButtonData.OK_DONE))
            {
                try {
                    Main.showScreen("Login.fxml");
                } catch (IOException e) {
                    Main.logger.info("Error trying to log out");
                    Main.logger.error(e.getMessage());
                }
            }
        });

    }

    public void showChemicalList()  {
        try {
            Main.showScreen("ChemicalScene.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show ChemicalScene.fxml");
            Main.logger.error(e.getMessage());
        }
    }

    public void showInventory(){
        try {
            Main.showScreen("Inventory.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show Inventory.fxml");
            Main.logger.error(e.getMessage());
        }
    }

    public void showMyProfile() {
        try {
            Main.showScreen("UserProfile.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show UserProfile.fxml");
            Main.logger.error(e.getMessage());
        }
    }
    public void showHomeScreen(){
        try {
            Main.showScreen("HomePage.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show UserProfile.fxml");
            Main.logger.error(e.getMessage());
        }
    }
    public void showUsersList() {
        try {
            Main.showScreen("UsersScene.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show UserProfile.fxml");
            Main.logger.error(e.getMessage());
        }
    }

    public void showProfileEdit(){
        try {
            Main.showScreen("UserEdit.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show UserProfile.fxml");
            Main.logger.error(e.getMessage());
        }
    }
    public void showUserProfile(){
        try {
            Main.showScreen("UserProfile.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show UserProfile.fxml");
            Main.logger.error(e.getMessage());
        }
    }

    public void showAddChemical() {
        try {
            Main.showScreen("ChemicalInput.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show ChemicalInput.fxml");
            Main.logger.error(e.getMessage());
        }
    }




    public void addProtocol() {
        try {
            Main.showScreen("ProtocolInput.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show ProtocolInput.fxml");
            Main.logger.error(e.getMessage());
        }
    }

    public void showProtocolList() {
        try {
            Main.showScreen("ProtocolList.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show ProtocolList.fxml");
            Main.logger.error(e.getMessage());
        }
    }

    public void showEquipmentInput() {
        try {
            Main.showScreen("EquipementInput.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show EquipementInput.fxml");
            Main.logger.error(e.getMessage());
        }
    }

    public void showEquipmenList() {
        try {
            Main.showScreen("EquipmentList.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show EquipmentList.fxml");
            Main.logger.error(e.getMessage());
        }
    }

    public void goToServiceInput() {
        try {
            Main.showScreen("ServiceInput.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show ServiceInput.fxml");
            Main.logger.error(e.getMessage());
        }
    }

    public void goToAllServices() {
        try {
            Main.showScreen("ServiceList.fxml");
        } catch (IOException e) {
            Main.logger.info("Error trying to show ServiceList.fxml");
            Main.logger.error(e.getMessage());
        }
    }
}
