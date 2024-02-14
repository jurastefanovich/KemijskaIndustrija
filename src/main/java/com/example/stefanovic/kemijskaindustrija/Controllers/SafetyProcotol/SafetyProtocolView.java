package com.example.stefanovic.kemijskaindustrija.Controllers.SafetyProcotol;

import com.example.stefanovic.kemijskaindustrija.Controllers.Navigation.NavBar;
import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.SafetyProtocolRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.SerializationRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.InputException;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocol;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocolStep;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

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

    private List<SafetyProtocolStep> steps;

    void editSingleSafetyStep(SafetyProtocolStep safetyProtocolStep){
        Stage newStage = new Stage();
        newStage.setTitle("Edit safety step");
        VBox vBox = new VBox();
        vBox.setStyle("-fx-padding: 10px");
        Text title = new Text("Edit safety step");
        TextField descriptionInput = new TextField();
        descriptionInput.setText(safetyProtocolStep.getDescription());
        Label errorLabel = new Label("error_label");
        errorLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red");
        resetErrors(descriptionInput,errorLabel);
        CheckBox checkBox = new CheckBox("Is this step critical");
        checkBox.setSelected(safetyProtocolStep.getCritical());
        Button saveButton = new Button("Update");

        saveButton.setOnAction(event ->{
            updateOldSafetyStep(descriptionInput.getText(), checkBox.isSelected(), safetyProtocolStep.getId());
        });


        StackPane newRoot = new StackPane();
        vBox.getChildren().addAll(title,descriptionInput,errorLabel,saveButton);
        newRoot.getChildren().addAll(vBox);

        newStage.setScene(new Scene(newRoot, 300, 150));
        newStage.show();
    }

    private void updateOldSafetyStep(String descriptionInput, boolean selected, Long id) {
        if(descriptionInput.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Can't add empty description", ButtonType.OK);
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to update this step?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response->{
                if(response.getButtonData().equals(ButtonBar.ButtonData.YES)){
                    updateSafetyProtocolStep(Methods.capitalizeFirstLetter(descriptionInput),selected, id);
                    viewSafetyProtocolDetails(safetyProtocol.getId());
                    SerializationRepository.prepareSafetyProtocolStep();
                }
            });
        }
    }

    @FXML
    void addNewSafetyStep(){
        Stage newStage = new Stage();
        newStage.setTitle("Add new protocol step");
        VBox vBox = new VBox();
        vBox.setStyle("-fx-padding: 10px");
        Text title = new Text("Add new protocol step description");
        TextField descriptionInput = new TextField();
        Button saveButton = new Button("Add");
        CheckBox isCritical = new CheckBox("Is step critical");

        Methods.addPadding(isCritical, saveButton, descriptionInput);

        saveButton.setOnAction(event ->{
           addNewProtocolStep(descriptionInput.getText(),isCritical.isSelected());
        });
        StackPane newRoot = new StackPane();
        vBox.getChildren().addAll(title,descriptionInput,saveButton,isCritical);
        newRoot.getChildren().addAll(vBox);

        newStage.setScene(new Scene(newRoot, 300, 150));
        newStage.show();
    }

    private void addNewProtocolStep(String descriptionInput, boolean selected) {
        if(descriptionInput.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Can't add empty description", ButtonType.OK);
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to add step to this protocol:\n\n"+descriptionInput, ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response->{
                if(response.getButtonData().equals(ButtonBar.ButtonData.YES)){
                    addNewSafetyProtocolStep(Methods.capitalizeFirstLetter(descriptionInput),selected, safetyProtocol.getId());
                    viewSafetyProtocolDetails(safetyProtocol.getId());
                }
            });
        }
    }

    @FXML
    void goToEdit() {

        Stage newStage = new Stage();
        newStage.setTitle("Edit protocol");
        VBox vBox = new VBox();
        vBox.setStyle("-fx-padding: 10px");

        Text title = new Text("Edit selected protocol");
        TextField nameInput = new TextField(safetyProtocol.getName());
        Label errorLabel = new Label("error_label");
        errorLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red");
        resetErrors(nameInput,errorLabel);

        Button saveButton = new Button("Update");

        saveButton.setOnAction(event ->{
            updateProtocolName(safetyProtocol, nameInput.getText(),nameInput,errorLabel);
        });


        StackPane newRoot = new StackPane();
        vBox.getChildren().addAll(title,nameInput,errorLabel,saveButton);
        newRoot.getChildren().addAll(vBox);

        newStage.setScene(new Scene(newRoot, 300, 150));
        newStage.show();
    }

    private void updateProtocolName(SafetyProtocol oldProtocol, String newName, TextField textField,Label errorLabel){
        resetErrors(textField,errorLabel);
        if(!oldProtocol.getName().equals(newName)){
            try {
                Methods.checkTextField(textField, errorLabel);
                oldProtocol.setName(Methods.capitalizeFirstLetter(newName));

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want chnage this protocols name to " + newName + "?",
                        ButtonType.YES, ButtonType.NO);

                alert.showAndWait().ifPresent(response ->{
                    if ( response.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                        updateSafetyProcolName(safetyProtocol);
                        viewSafetyProtocolDetails(safetyProtocol.getId());
                        SerializationRepository.prepareObjectForSerialization(safetyProtocol);
                    }
                });
            } catch (InputException e) {
                logger.info("Error trying to update protocol name");
                logger.error(e.getMessage());
            }
        }
    }
    private void resetErrors(TextField textField,Label errorLabel){
        Methods.resetOutlines(textField);
        Methods.resetErorrs(errorLabel);
    }

    @FXML
    void goBack(){
        NavBar navBar = new NavBar();
        navBar.showProtocolList();
    }

    @FXML
    void removeStepFromList() {
        SafetyProtocolStep selectedStep = listOfSafetyStepsTableView.getSelectionModel().getSelectedItem();
        if (selectedStep != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to remove this step", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response->{
                if (response.getButtonData().equals(ButtonBar.ButtonData.YES)){
                    steps.remove(selectedStep);
                    deleteSingleProtocolStep(selectedStep.getId());
                    listOfSafetyStepsTableView.setItems(FXCollections.observableList(steps));
                }
            });
        }
    }

    public void viewSafetyProtocolDetails(long id) {
        this.steps = getSafetyProtocolSteps(id);
        this.safetyProtocol = getSafetyProtocolByIdWithSteps(id);
        titleText.setText("Details for safety protocol");
        protocolIdText.setText(String.valueOf(safetyProtocol.getId()));
        protocolNameText.setText(safetyProtocol.getName());
        listOfSafetyStepsTableView.setItems(FXCollections.observableList(steps));
        isCriticalTableColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getCritical())));
        descriptionTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        activateSafetyStepView();
    }

    private void activateSafetyStepView() {
        listOfSafetyStepsTableView.setOnMouseClicked(event ->{
            //Add && UserRepository.isAdmin()
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 ){
                SafetyProtocolStep safetyProtocolStep = listOfSafetyStepsTableView.getSelectionModel().getSelectedItem();
                if (safetyProtocolStep != null ){

                   editSingleSafetyStep(safetyProtocolStep);
                }
            }
        });
    }

    @FXML
    public void deleteProtocol() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want delete this protocol?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response ->{
            if ( response.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                removeSafetyProtocol(safetyProtocol.getId());
                goBack();
            }
        });
    }
}
