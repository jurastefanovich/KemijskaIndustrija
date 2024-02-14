package com.example.stefanovic.kemijskaindustrija.Controllers.Servis;

import com.example.stefanovic.kemijskaindustrija.Controllers.Equipment.EquipmentDetails;
import com.example.stefanovic.kemijskaindustrija.Controllers.Navigation.NavBar;
import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.EquipmentRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.ServisRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocolStep;
import com.example.stefanovic.kemijskaindustrija.Model.Service;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServisList implements ServisRepository, EquipmentRepository {
    Logger logger = LoggerFactory.getLogger(Main.class);

    @FXML
    AnchorPane root;
    @FXML
    ComboBox<Equipment> equipmentSearchField;

    @FXML
    TableColumn<Service, String> servicesDateTableColumn;

    @FXML
    TableColumn<Service, String> servicesEquipmentTableColumn;

    @FXML
    TableColumn<Service, String> servicesTitleTableColumn;

    @FXML
    TableView<Service> servicesTableView;

    @FXML
    TextField titleSearchField;
    private List<Service> services;
    private List<Equipment> equipmentList;


    @FXML
    void initialize(){
        this.services = ServisRepository.getAllServices();
        this.equipmentList = getAllEquipmenet();

        equipmentSearchField.setItems(FXCollections.observableList(equipmentList));
        servicesTitleTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        servicesDateTableColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getDateOfService())));
        servicesEquipmentTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEquipment().getName() + " " + data.getValue().getEquipment().getType() ));
        servicesTableView.setItems(FXCollections.observableList(services));

        showDetails();
    }
    @FXML
    void filterServicesView() {
        try {
            Equipment type = equipmentSearchField.getValue();
            String title = titleSearchField.getText();
            var servicesFilter = services.stream().filter(service ->
                    (title == null || title.isEmpty() || service.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                    (type == null || service.getEquipment().equals(type))
            ).toList();
            servicesTableView.setItems(FXCollections.observableList(servicesFilter));
        } catch (NullPointerException e) {
            logger.info("Error trying to filter an equipment list");
            logger.error(e.getMessage());
        }
    }


    private void showDetails() {
        servicesTableView.setOnMouseClicked(event ->{
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && UserRepository.isAdmin()){
                Service service = servicesTableView.getSelectionModel().getSelectedItem();
                if (service != null ){
                    try {
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("ServiceInput.fxml"));
                        Parent parent = loader.load();
                        ServisInput servisInput = loader.getController();
                        servisInput.initialize(service.getId());
                        root.getChildren().clear();
                        root.getChildren().setAll(parent);
                    } catch (IOException e) {
                        Main.logger.info("Error while trying to show service details");
                        Main.logger.error(e.getMessage());
                    }

                }
            }
        });
    }

    @FXML
    void deleteService() {
        Service service = servicesTableView.getSelectionModel().getSelectedItem();
        Long id = service.getId();
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to remove this service?", ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response->{
            if ( response.getButtonData().equals(ButtonBar.ButtonData.OK_DONE))
            {
                try {
                    DBController.deleteEntity(id, "service");
                } catch (Exception e) {
                    logger.info("Error while trying to delete service on frontend");
                    logger.error(e.getMessage());
                }
                NavBar navBar = new NavBar();
                navBar.goToAllServices();
            }
        });
    }



}
