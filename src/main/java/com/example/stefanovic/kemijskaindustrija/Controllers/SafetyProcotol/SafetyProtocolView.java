package com.example.stefanovic.kemijskaindustrija.Controllers.SafetyProcotol;

import com.example.stefanovic.kemijskaindustrija.Controllers.NavBar;
import com.example.stefanovic.kemijskaindustrija.DataBase.SafetyProtocolRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocol;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocolStep;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
//        removeSafetyProtocol();
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
