package com.example.stefanovic.kemijskaindustrija.Controllers.Equipment;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.InputErrorMessages;
import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
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
        selectedType.setText("");
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
        Methods.resetOutlines(equipmentnameTextField,equipmentTypeSearch,equipmenetDescTextField);
    }
    @FXML
    void saveNewEquipement() {
        resetErrors();
        var id = getId();

        try{
            checkForErrors();
            Equipment equipment1 = new Equipment(Methods.capitalizeFirstLetter(equipmentnameTextField.getText()),
                    Methods.capitalizeFirstLetter(equipmenetDescTextField.getText()),
                    EquipmentType.valueOf(selectedType.getText()));

            if (id != null){
                equipment1.setId((Long) id);
            }

            confirmation(equipment1);

        }catch (IllegalArgumentException e){
//            ADD LOGGER
        }


    }

    private void confirmation(Equipment equipment) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Potvrdite unos podataka: " + equipment.toString(), ButtonType.OK, ButtonType.CANCEL );
        alert.showAndWait().ifPresent(response ->{
            if (response.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)){
                try {
                    saveToDatabase(equipment);
                    successMessage.setVisible(true);
                } catch (Exception e) {
                    errorMessage.setVisible(true);
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Object getId(){
        if (equipmentIdLabel.getText().isEmpty()){
            return null;
        }
        else{
            return Long.valueOf(equipmentIdLabel.getText());
        }
    }
    private void checkForErrors() {
        try {
            Methods.checkTextField(equipmentnameTextField, nameErrorLabel);
            Methods.checkTextArea(equipmenetDescTextField, descErrorLabel);
            Methods.checkSearch(selectedType, equipmenetErrorLabel,equipmentTypeSearch);
        } catch (InputException e) {
//            ADD LOGGER
        }

    }


    public void initialize(long id){
        try {
            equipment = EquipmentRepository.getEquipmentById(id);
            equipmentIdLabel.setText(String.valueOf(id));
            equipmentIdLabel.setVisible(false);
            successMessage.setVisible(false);
            errorMessage.setVisible(false);
            equipmentnameTextField.setText(equipment.getName());
            equipmenetDescTextField.setText(equipment.getDescription());
            selectedType.setText(String.valueOf(equipment.getType()));
            equipmentInputTitle.setText("Update " + equipment.getName());
            interactionButton.setText("Update");
        } catch (Exception e) {
//            ADD LOGGER
        }
    }

}
