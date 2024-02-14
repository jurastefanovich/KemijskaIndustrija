package com.example.stefanovic.kemijskaindustrija.Controllers.Equipment;

import com.example.stefanovic.kemijskaindustrija.DataBase.EquipmentRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class EquipmentList implements EquipmentRepository {

    @FXML
    AnchorPane root;

    @FXML
    TableColumn<Equipment, String > equipmentDescriptionTC;

    @FXML
    TableColumn<Equipment, Double > equipmentHealthTC;

    @FXML
    TableColumn<Equipment, String > equipmentNameTC;

    @FXML
    TableView<Equipment> equipmentTableView;

    @FXML
    TableColumn<Equipment, String> equipmentTypeTC;

    @FXML
    TableColumn<Equipment, Long> equipmentIDTC;

    @FXML
    TextField nameTextField;

    @FXML
    private ComboBox<String> searchEquipmentTypeComboBox;

    private List<Equipment> equipment;
    public void initialize(){
        equipment = getAllEquipmenet();
        equipmentTableView.setItems(FXCollections.observableList(equipment));
        equipmentNameTC.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getName()));
        equipmentDescriptionTC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        equipmentTypeTC.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getType())));
        equipmentHealthTC.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getHealthBar()));
        equipmentIDTC.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        showDetails();
        searchEquipmentTypeComboBox.setItems(FXCollections.observableList(EquipmentRepository.getAllEquipmentTypes()));
    }

    private void showDetails() {
        equipmentTableView.setOnMouseClicked(event ->{
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && UserRepository.isAdmin()){
                Equipment equipment1 = equipmentTableView.getSelectionModel().getSelectedItem();
                if (equipment1 != null ){
                    try {
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("EquipmentDetails.fxml"));
                        Parent parent = loader.load();
                        EquipmentDetails equipmentDetails = loader.getController();
                        equipmentDetails.initialize(equipment1.getId());
                        root.getChildren().clear();
                        root.getChildren().setAll(parent);
                    } catch (IOException e) {
                        Main.logger.info("Error while trying to delete equipment on frontend");
                        Main.logger.error(e.getMessage());
                    }

                }
            }
        });
    }

    @FXML
    public void filterList() {
        try {
            String type = searchEquipmentTypeComboBox.getValue();
            String name = nameTextField.getText();

            var equipmentFilter = equipment.stream().filter(equipment ->
                    (name == null || name.isEmpty() || equipment.getName().toLowerCase().contains(name.toLowerCase())) &&
                    (type == null || type.isEmpty() || equipment.getType().toString().toLowerCase().equals(type.toLowerCase()))
            ).toList();

            equipmentTableView.setItems(FXCollections.observableList(equipmentFilter));
        } catch (NullPointerException e) {
            logger.info("Error trying to filter an equipment list");
            logger.error(e.getMessage());
        }
    }
}
