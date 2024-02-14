package com.example.stefanovic.kemijskaindustrija.Controllers.Servis;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.CustomComponent.CustomAlert;
import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.EquipmentRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.ServisRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.IllegalStringLengthException;
import com.example.stefanovic.kemijskaindustrija.Exception.InputException;
import com.example.stefanovic.kemijskaindustrija.Exception.ServiceBookedForDateException;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocolStep;
import com.example.stefanovic.kemijskaindustrija.Model.Service;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServisInput implements EquipmentRepository, ServisRepository {
    Logger logger = LoggerFactory.getLogger(Main.class);

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

    private Long serviceId;
    DateTimeFormatter customDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public void initialize(long id){
        Service service = getServiceById(id);
        this.serviceId = id;
        serviceTitlenput.setText(service.getTitle());
        serviceDescInput.setText(service.getDescription());
        serviceDateInput.setValue(service.getDateOfService());
        serviceEquipmentInput.setPromptText(service.getEquipment().toString());
        serviceEquipmentInput.setValue(service.getEquipment().toString());
    }

    public void initialize(){
        removeFeedbacks();
        clearErrors();
        List<Equipment> equipmentList = getAllEquipmenet().stream().filter(equipment -> !equipment.getInService()).toList();
        serviceEquipmentInput.setItems(FXCollections.observableList(getEquipment(equipmentList)));

        serviceDateInput.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate localDate) {
                try{
                   if (localDate != null) {
                       return customDateFormat.format(localDate);
                   }
               }catch (DateTimeParseException e){
                   serviceDateError.setText("Date needs to be in format DD/MM/YYYY");
                    logger.info("Wrong date parsing in service input");
                    logger.error(e.getMessage());
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
                        logger.info("Wrong date parsing in service input");
                        logger.error(e.getMessage());
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
                EquipmentRepository.getEquipmentById(getEquipmentId(serviceEquipmentInput.getValue())),
                serviceDateInput.getValue());

            if (serviceId != null){
                service.setId(serviceId);
            }

            CustomAlert alert = new CustomAlert(Alert.AlertType.CONFIRMATION,
            "Confirm registering new service for date: " + serviceDateInput.getValue(),
            ButtonType.OK, ButtonType.CANCEL);

        alert.showAndWait().ifPresent(response->{
            if ( response.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)){
                try {
                    saveService(service);
                    feedbackSuccessMessage.setVisible(true);
                } catch (ServiceBookedForDateException e) {
                        serviceDateError.setText(e.getMessage());
                        serviceDateInput.setStyle("-fx-border-color: red;");
                }
            }
        });
        } catch (Exception e) {
            feedbackErrorMessage.setVisible(true);
            logger.info("Error trying to save to database");
            logger.error(e.getMessage());
        }
    }

    private void removeFeedbacks(){
        feedbackSuccessMessage.setVisible(false);
        feedbackErrorMessage.setVisible(false);
    }

    private long getEquipmentId(String value) {
        return Long.parseLong(value.split(" ")[0]);
    }

    private void checkErrors()  {
        try {
            Methods.checkTextField(serviceTitlenput, serviceTitleError);
            Methods.checkTextArea(serviceDescInput, serviceDescError);
            Methods.checkDateField(serviceDateInput,serviceDateError);
            Methods.checkComboBox(serviceEquipmentInput, serviceEquipmentError);
            Methods.checkStringLength(serviceTitlenput, serviceTitleError);
        } catch (InputException | IllegalStringLengthException e) {
            logger.info("Wrong input format in service input");
            logger.error(e.getMessage());
        }
    }



}
