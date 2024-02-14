package com.example.stefanovic.kemijskaindustrija.Controllers.SafetyProcotol;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.InputErrorMessages;
import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.SafetyProtocolRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.IllegalStringLengthException;
import com.example.stefanovic.kemijskaindustrija.Exception.InputException;
import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocol;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocolStep;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class SafetyProtocolInput implements SafetyProtocolRepository {

    @FXML
    public CheckBox isCritialCheckBox;

    @FXML
    public Button removeProcotolStepButton;

    @FXML
    private TableView<SafetyProtocolStep> safetyProcotolTableView;

    @FXML
    private TableColumn<SafetyProtocolStep, String> safetyProtocolDescTableColumn;

    @FXML
    public Label safetyProtocolNameErrorLabel;

    @FXML
    public TextField safetyProtocolNameTextField;

    @FXML
    public Button safetyProtocolSaveButton;

    @FXML
    public Label safetyProtocolStepErrorLabel;

    @FXML
    public TextArea safteProcotolStepDescTextField;
    public SafetyProtocol safetyProtocol;

    private List<SafetyProtocolStep> steps = new ArrayList<>();

    public void initialize(){
        Methods.resetErorrs(safetyProtocolNameErrorLabel,safetyProtocolStepErrorLabel);
    }

    @FXML
    void addSafetyStepToList() {
        Methods.resetErorrs(safetyProtocolStepErrorLabel);
        String description = safteProcotolStepDescTextField.getText();
        if (description.isEmpty()){
            safetyProtocolStepErrorLabel.setText(InputErrorMessages.EMPTY_FIELD.getMessage());
            safteProcotolStepDescTextField.setStyle("-fx-border-color: red;");
        }
        else{
            SafetyProtocolStep safetyProtocolStep = new SafetyProtocolStep(description, isCritialCheckBox.isSelected());
            steps.add(safetyProtocolStep);
            updateTableView(steps);
        }
    }

    @FXML
    void removeProcotolStep(ActionEvent event) {
        SafetyProtocolStep selectedStep = safetyProcotolTableView.getSelectionModel().getSelectedItem();
        if (selectedStep != null) {
            steps.remove(selectedStep);
            updateTableView(steps);
        }
    }

    @FXML
    void saveSafetyProtocol() {
        resetErrors();
        SafetyProtocol safetyProtocol1 = new SafetyProtocol(safetyProtocolNameTextField.getText(), steps);
        try {
            checkForErrors();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm adding new safety protocol.", ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait().ifPresent(respose->{
                if (respose.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)){
                    safeSafetyProtocol(safetyProtocol1);
                }
            });
        } catch (InputException | IllegalStringLengthException e) {
            logger.error("Couldn't save empty protocol steps");
        }
    }
    private void resetErrors(){
        Methods.resetErorrs(safetyProtocolNameErrorLabel,safetyProtocolStepErrorLabel);
        safetyProtocolNameTextField.setStyle("-fx-background-color: transparent");
        safteProcotolStepDescTextField.setStyle("-fx-background-color: transparent");
    }
    private void updateTableView(List<SafetyProtocolStep> steps){
        safetyProtocolDescTableColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDescription()));
        safetyProcotolTableView.setItems(FXCollections.observableList(steps));
    }

    private void checkForErrors() throws InputException, IllegalStringLengthException {
        Methods.checkTextField(safetyProtocolNameTextField, safetyProtocolNameErrorLabel);
        if(Methods.isTableViewEmpty(safetyProcotolTableView)){
            safetyProtocolStepErrorLabel.setText(InputErrorMessages.EMPTY_FIELD.getMessage());
            safteProcotolStepDescTextField.setStyle("-fx-border-color: red;");
            throw new InputException(InputErrorMessages.EMPTY_FIELD.getMessage());
        }
        Methods.checkStringLength(safetyProtocolNameTextField, safetyProtocolNameErrorLabel);

    }



}
