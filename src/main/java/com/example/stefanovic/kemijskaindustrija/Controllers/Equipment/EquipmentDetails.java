package com.example.stefanovic.kemijskaindustrija.Controllers.Equipment;

import com.example.stefanovic.kemijskaindustrija.Controllers.NavBar;
import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.EquipmentRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import com.example.stefanovic.kemijskaindustrija.Model.EquipmentType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class EquipmentDetails implements EquipmentRepository {

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
            throw new RuntimeException(e);
        }
    }

    @FXML
    void goToEquipmentListView() {
        navBar.showEquipmenList();
    }

    public void initialize(long id){
        try {
            equipment = getEquipmentById(id);
            equipmentNameText.setText(equipment.getName());
            equipmentIdText.setText(String.valueOf(equipment.getId()));
            equipmentDescriptionText.setText(equipment.getDescription());
            equipmentTypeText.setText(String.valueOf(equipment.getType()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void deleteEquipment() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to remove this equipment?", ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response->{
            if ( response.getButtonData().equals(ButtonBar.ButtonData.OK_DONE))
            {
                try {
                    DBController.deleteEntity(Long.valueOf(equipmentIdText.getText()), "equipment");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                goToEquipmentListView();
            }
        });

    }

}
