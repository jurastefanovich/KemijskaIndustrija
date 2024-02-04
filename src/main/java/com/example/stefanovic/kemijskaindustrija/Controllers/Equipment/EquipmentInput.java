package com.example.stefanovic.kemijskaindustrija.Controllers.Equipment;

import com.example.stefanovic.kemijskaindustrija.Controllers.InputErrorMessages;
import com.example.stefanovic.kemijskaindustrija.Controllers.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.EquipmentRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.InputException;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import com.example.stefanovic.kemijskaindustrija.Model.EquipmentType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class EquipmentInput implements EquipmentRepository {

    @FXML
    public AnchorPane root;
    @FXML
    public Label descErrorLabel;

    @FXML
    public TextArea equipmenetDescTextField;

    @FXML
    public Label equipmenetErrorLabel;

    @FXML
    public ListView<EquipmentType> equipmentTypeListView;

    @FXML
    public TextField equipmentTypeSearch;

    @FXML
    public TextField equipmentnameTextField;

    @FXML
    public Label nameErrorLabel;

    @FXML
    private Label selectedType;
    @FXML
    public AnchorPane successMessage;
    @FXML
    public AnchorPane errorMessage;
    @FXML
    private Text equipmentInputTitle;
    @FXML
    public Button interactionButton;

    @FXML
    public Label equipmentIdLabel;
    @FXML
    public Label equipemntIdLabelTitle;
    private Equipment equipment;
    ObservableList<EquipmentType> data = FXCollections.observableArrayList(EquipmentType.values());
    public void initialize(){
        equipmentIdLabel.setText("");
        equipemntIdLabelTitle.setText("");
        successMessage.setVisible(false);
        errorMessage.setVisible(false);
        resetErrors();
        initializeSearch();
        equipmentTypeListView.setItems(data);
        setSelectedType();

    }

    private void setSelectedType() {
        equipmentTypeListView.setOnMouseClicked(event -> {
            selectedType.setText(String.valueOf(equipmentTypeListView.getSelectionModel().getSelectedItem()));
                equipmentTypeSearch.clear();
        });

    }

    private void initializeSearch() {
        equipmentTypeSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<EquipmentType> filteredData = data.filtered(option -> option.getEquipmentType().toLowerCase().contains(newValue.toLowerCase()));
            equipmentTypeListView.setItems(filteredData);
        });
    }

    private void resetErrors(){
        Methods.resetErorrs(nameErrorLabel,descErrorLabel,equipmenetErrorLabel);
    }
    @FXML
    void saveNewEquipement() {
        resetErrors();
        var id = getId();
        try{
            Equipment equipment1 = new Equipment(equipmentnameTextField.getText(), equipmenetDescTextField.getText(), EquipmentType.valueOf(selectedType.getText()));
            if (id != null){
                equipment1.setId((Long) id);
            }
            checkForErrors(equipment1.getType());
            saveToDatabase(equipment1);
            successMessage.setVisible(true);
        }catch (InputException e){
            //
        }catch (IllegalArgumentException e){
            equipmentTypeSearch.setStyle("-fx-background-color: red");
            equipmenetErrorLabel.setText(InputErrorMessages.EMPTY_FIELD.getMessage());
        } catch (Exception e) {
            errorMessage.setVisible(true);
//            throw new RuntimeException(e);/
        }
    }
    private Object getId(){
        if (equipmentIdLabel.getText().isEmpty()){
            return null;
        }
        else{
            return Long.valueOf(equipmentIdLabel.getText());
        }
    }
    private void checkForErrors(EquipmentType type) throws InputException {
        Methods.checkTextField(equipmentnameTextField, nameErrorLabel);
        Methods.checkTextArea(equipmenetDescTextField, descErrorLabel);
        if (type == null){
            equipmentTypeSearch.setStyle("-fx-border-color: red");
            equipmenetErrorLabel.setText(InputErrorMessages.EMPTY_FIELD.getMessage());
        }
    }


    public void initialize(long id){
        try {
            equipment = getEquipmentById(id);
            equipmentIdLabel.setText(String.valueOf(id));
            equipmentIdLabel.setVisible(false);
            equipmentnameTextField.setText(equipment.getName());
            equipmenetDescTextField.setText(equipment.getDescription());
            selectedType.setText(String.valueOf(equipment.getType()));
            equipmentInputTitle.setText("Update " + equipment.getName());
            interactionButton.setText("update");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
