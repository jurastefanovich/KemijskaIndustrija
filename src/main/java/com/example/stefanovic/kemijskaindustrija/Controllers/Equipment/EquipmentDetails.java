package com.example.stefanovic.kemijskaindustrija.Controllers.Equipment;

import com.example.stefanovic.kemijskaindustrija.Controllers.Navigation.NavBar;
import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.EquipmentRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class EquipmentDetails implements EquipmentRepository {
    Logger logger = LoggerFactory.getLogger(Main.class);

    NavBar navBar = new NavBar();
    @FXML
    public AnchorPane root;
    @FXML
    private Button equipmentDeleteButton;

    @FXML
    private Text equipmentDescriptionText;

    @FXML
    private Button equipmentEditButton;

    @FXML
    public Text equipmentIdText;

    @FXML
    public Text equipmentNameText;

    @FXML
    public Text equipmentTypeText;
    private Equipment equipment;
    @FXML
    void goToEquipmentEditView() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("EquipementInput.fxml"));
            Parent parent = loader.load();
            EquipmentInput equipmentInput = loader.getController();
            equipmentInput.initialize(equipment.getId());
            root.getChildren().clear();
            root.getChildren().setAll(parent);
        } catch (IOException e) {
            logger.info("Error while trying to go to edit equipment view");
            logger.error(e.getMessage());
        }
    }

    @FXML
    void goToEquipmentListView() {
        navBar.showEquipmenList();
    }

    public void initialize(long id){
        try {
            equipment = EquipmentRepository.getEquipmentById(id);
            equipmentNameText.setText(equipment.getName());
            equipmentIdText.setText(String.valueOf(equipment.getId()));
            equipmentDescriptionText.setText(equipment.getDescription());
            equipmentTypeText.setText(String.valueOf(equipment.getType()));
        } catch (Exception e) {
            logger.info("Error while trying to initialize equipment with ID: " + id);
            logger.error(e.getMessage());
        }
    }

    @FXML
    void deleteEquipment() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to remove this equipment?", ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response->{
            if ( response.getButtonData().equals(ButtonBar.ButtonData.OK_DONE))
            {
                try {
                   deleteEquipmentFromDB(Long.parseLong(equipmentIdText.getText()));
                } catch (Exception e) {
                    logger.info("Error while trying to delete equipment on frontend");
                    logger.error(e.getMessage());
                }
                goToEquipmentListView();
            }
        });

    }

}
