package com.example.stefanovic.kemijskaindustrija.Controllers.SafetyProcotol;

import com.example.stefanovic.kemijskaindustrija.Controllers.NavBar;
import com.example.stefanovic.kemijskaindustrija.Controllers.Supplier.SupplierView;
import com.example.stefanovic.kemijskaindustrija.DataBase.SafetyProtocolRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocol;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocolStep;
import com.example.stefanovic.kemijskaindustrija.Model.Supplier;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class SafetyProtocolView implements SafetyProtocolRepository {

    @FXML
    private TableColumn<SafetyProtocolStep, String> descriptionTableColumn;

    @FXML
    private TableColumn<SafetyProtocolStep, String> isCriticalTableColumn;

    @FXML
    private TableView<SafetyProtocolStep> listOfSafetyStepsTableView;

    @FXML
    private Text protocolIdText;

    @FXML
    private Text protocolNameText;

    @FXML
    private AnchorPane root;

    @FXML
    private Text titleText;

    private SafetyProtocol safetyProtocol;


    @FXML
    void goToEdit() {

    }

    @FXML
    void goBack(){
        NavBar navBar = new NavBar();
        navBar.showProtocolList();
    }

    @FXML
    void removeStepFromList() {
        //get id from table
        listOfSafetyStepsTableView.setOnMouseClicked(event ->{
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 ){
                SafetyProtocolStep safetyProtocolStep = listOfSafetyStepsTableView.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want delete this step?", ButtonType.OK, ButtonType.CANCEL);
                alert.showAndWait().ifPresent(response->{
                    if ( response.getButtonData().equals(ButtonBar.ButtonData.OK_DONE))
                    {
                        try {
                            Main.showScreen("Login.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                if (safetyProtocolStep != null ){
                    try {

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        removeSafetyProtocol();
    }

    public void viewSafetyProtocolDetails(long id) {
        try {
            this.safetyProtocol = getSafetyProtocolByIdWithSteps(id);
            titleText.setText("Details for safety protocol");
            protocolIdText.setText(String.valueOf(safetyProtocol.getId()));
            protocolNameText.setText(safetyProtocol.getName());
            listOfSafetyStepsTableView.setItems(FXCollections.observableList(safetyProtocol.getSteps()));
            isCriticalTableColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getCritical())));
            descriptionTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
            removeStepFromList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteProtocol() {
        try {
            removeSafetyProtocol(safetyProtocol.getId());
            goBack();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
