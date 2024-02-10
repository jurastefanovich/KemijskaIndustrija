package com.example.stefanovic.kemijskaindustrija.Controllers.Servis;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.CustomComponent.CustomAlert;
import com.example.stefanovic.kemijskaindustrija.DataBase.EquipmentRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.ServisRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.InputException;
import com.example.stefanovic.kemijskaindustrija.Exception.ServiceBookedForDateException;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import com.example.stefanovic.kemijskaindustrija.Model.Service;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServisInput implements EquipmentRepository, ServisRepository {

    @FXML
    public AnchorPane feedbackErrorMessage;

    @FXML
    public AnchorPane feedbackSuccessMessage;

    @FXML
    public Button saveServiceButton;

    @FXML
    public Label serviceDateError;

    @FXML
    public DatePicker serviceDateInput;

    @FXML
    public Label serviceDescError;

    @FXML
    public TextArea serviceDescInput;

    @FXML
    public Label serviceEquipmentError;

    @FXML
    public ComboBox<String> serviceEquipmentInput;

    @FXML
    public Label serviceTitleError;

    @FXML
    public TextField serviceTitlenput;
    DateTimeFormatter customDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public void initialize(){
        removeFeedbacks();
        clearErrors();
        try {
            List<Equipment> equipmentList = getAllEquipmenet();
            serviceEquipmentInput.setItems(FXCollections.observableList(getEquipment(equipmentList)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        serviceDateInput.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate localDate) {
                try{
                   if (localDate != null) {
                       return customDateFormat.format(localDate);
                   }
               }catch (DateTimeParseException e){
                   serviceDateError.setText("Date needs to be in format DD/MM/YYYY");
               }
                return null;
            }

            @Override
            public LocalDate fromString(String s) {

                try{
                       if (s != null && !s.isEmpty()) {
                           return LocalDate.parse(s, customDateFormat);
                       }
                   }catch (DateTimeParseException e){
                       serviceDateError.setText("Date needs to be in format DD/MM/YYYY");
                   }

                return null;
            }
        });
    }

    private List<String> getEquipment(List<Equipment> equipmentList) {
        List<String> equipmentnames = new ArrayList<>();
        equipmentList.forEach(equipment -> equipmentnames.add(equipment.getId() + " " + equipment.getName() + " " + equipment.getType()));
        return  equipmentnames;
    }

    private void clearErrors(){
        Methods.resetErorrs(serviceDateError,serviceDescError,serviceEquipmentError,serviceTitleError);
        Methods.resetOutlines(serviceDescInput,serviceTitlenput,serviceDateInput, serviceEquipmentInput);
    }
    @FXML
    void saveNewService() {
        clearErrors();
        removeFeedbacks();
        try {
            checkErrors();
            Service service = new Service(serviceTitlenput.getText().toUpperCase(),
                serviceDescInput.getText().toUpperCase(),
                getEquipmentById(getEquipmentId(serviceEquipmentInput.getValue())),
                serviceDateInput.getValue());

                CustomAlert alert = new CustomAlert(Alert.AlertType.CONFIRMATION,
                "Confirm registering new service for date: " + serviceDateInput.getValue(),
                ButtonType.OK, ButtonType.CANCEL);

        alert.showAndWait().ifPresent(response->{
            if ( response.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)){
                try {
                    saveService(service);
                    feedbackSuccessMessage.setVisible(true);
                } catch (Exception e) {
                    if (e instanceof ServiceBookedForDateException){
                        serviceDateError.setText(e.getMessage());
                        serviceDateInput.setStyle("-fx-border-color: red;");
                    }else{
                        feedbackErrorMessage.setVisible(true);
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void removeFeedbacks(){
        feedbackSuccessMessage.setVisible(false);
        feedbackErrorMessage.setVisible(false);
    }

    private long getEquipmentId(String value) {
        return Long.parseLong(value.split(" ")[0]);
    }

    private void checkErrors() throws InputException {
           Methods.checkTextField(serviceTitlenput, serviceTitleError);
           Methods.checkTextArea(serviceDescInput, serviceDescError);
           Methods.checkDateField(serviceDateInput,serviceDateError);
           Methods.checkComboBox(serviceEquipmentInput, serviceEquipmentError);
    }

}
