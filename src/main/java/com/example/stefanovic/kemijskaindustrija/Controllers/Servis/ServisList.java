package com.example.stefanovic.kemijskaindustrija.Controllers.Servis;

import com.example.stefanovic.kemijskaindustrija.DataBase.EquipmentRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.ServisRepository;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import com.example.stefanovic.kemijskaindustrija.Model.Service;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class ServisList implements ServisRepository, EquipmentRepository {

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

    @FXML
    void initialize(){
        try {
            equipmentSearchField.setItems(FXCollections.observableList(getAllEquipmenet()));
            servicesTitleTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
            servicesDateTableColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getDateOfService())));
            servicesEquipmentTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEquipment().getName() + " " + data.getValue().getEquipment().getType() ));
            servicesTableView.setItems(FXCollections.observableList(ServisRepository.getAllServices()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void filterServicesView(ActionEvent event) {

    }

}
