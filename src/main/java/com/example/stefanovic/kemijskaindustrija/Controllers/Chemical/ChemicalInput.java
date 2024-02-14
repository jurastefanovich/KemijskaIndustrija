package com.example.stefanovic.kemijskaindustrija.Controllers.Chemical;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.ChemicalRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.ChemicalInputFormatException;
import com.example.stefanovic.kemijskaindustrija.Exception.IllegalStringLengthException;
import com.example.stefanovic.kemijskaindustrija.Exception.InputException;
import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.InputMismatchException;

public class ChemicalInput implements ChemicalRepository {
    Logger logger = LoggerFactory.getLogger(Main.class);

    @FXML
    public AnchorPane chemicalErrorFeedback;

    @FXML
    public TextField chemicalNameTextField;

    @FXML
    public AnchorPane chemicalSuccessFeedback;

    @FXML
    public ComboBox<String> dangerLevelComboBox;

    @FXML
    public Label dangerLevelErrorLabel;

    @FXML
    public TextArea instructionsTextField;

    @FXML
    public Label nameErrorLabel;

    @FXML
    public Label quanitityErrorLabel;

    @FXML
    public TextField quantityTextField;

    @FXML
    public Label quantityUnitErrorLabel;

    @FXML
    public TextField quantityUnitTextField;

    @FXML
    public Button saveButton;

    @FXML
    public Label title;

    private Long id;

    private BigDecimal dangerLevel;

    @FXML
    void initialize(){
        resetErrors();
        Methods.setFeedbackInvisible(chemicalSuccessFeedback,chemicalErrorFeedback);
        setDangerLevel();
    }

    private void setDangerLevel() {
        String[] dangerLevelSize = new String[10];
        for (int i = 0; i<dangerLevelSize.length;i++){
            dangerLevelSize[i] = String.valueOf((i+1));
        }
        dangerLevelComboBox.setItems(FXCollections.observableList(Arrays.stream(dangerLevelSize).toList()));
    }

    @FXML
    void saveNewChemical() {
        Methods.setFeedbackInvisible(chemicalSuccessFeedback,chemicalErrorFeedback);
        resetErrors();
        String dangerLevelInput = dangerLevelComboBox.getValue() == null ? String.valueOf(this.dangerLevel) : dangerLevelComboBox.getValue();
        try {
            checkForErrors();

            Chemical chemical = new Chemical(chemicalNameTextField.getText(),
                    Double.parseDouble(quantityTextField.getText()),
                    quantityUnitTextField.getText(),
                    instructionsTextField.getText(),
                    new BigDecimal(dangerLevelInput));

            checkChemicalInput();

            if (id != null){
                chemical.setId(id);
            }

            confirmation(chemical);
        } catch (NumberFormatException  e) {
                quanitityErrorLabel.setText("Needs to be a number!");
                quantityTextField.setStyle("-fx-border-color: red;");
        }
        catch (ChemicalInputFormatException e) {
            nameErrorLabel.setText(e.getMessage());
            chemicalNameTextField.setStyle("-fx-border-color: red;");
        } catch (IllegalStringLengthException | InputException e) {
            logger.error("Error occurred while inputting a new chemical " + e.getMessage());
        }

    }

    private void checkChemicalInput() throws ChemicalInputFormatException {
        if (!chemicalNameTextField.getText().isEmpty()){
            String firstLetter = chemicalNameTextField.getText().substring(0,1);
            if (firstLetter.matches("\\d+")){
                throw new ChemicalInputFormatException("Chemical name can't start with a number!");
            }
        }

    }

    private void resetErrors(){
        Methods.resetErorrs(nameErrorLabel,quanitityErrorLabel,quantityUnitErrorLabel,dangerLevelErrorLabel);
        Methods.resetOutlines(chemicalNameTextField,quantityTextField,quantityUnitTextField, dangerLevelComboBox);

    }

    private void checkForErrors() throws InputException, IllegalStringLengthException {
        Methods.checkTextField(chemicalNameTextField, nameErrorLabel);
        Methods.checkTextField(quantityTextField, quanitityErrorLabel);
        Methods.checkTextField(quantityUnitTextField, quantityUnitErrorLabel);
        Methods.checkComboBox(dangerLevelComboBox, dangerLevelErrorLabel);
        Methods.checkStringLength(chemicalNameTextField, nameErrorLabel);
    }


    public void initialize(Long id) {
        this.id = id;
        Chemical chemical = getChemicalById(id);
        saveButton.setText("Update");
        title.setText("Update information for " + chemical.getName());
        chemicalNameTextField.setText(chemical.getName());
        quantityTextField.setText(String.valueOf(chemical.getQuantity()));
        quantityUnitTextField.setText(chemical.getQuantityUnit());
        instructionsTextField.setText(chemical.getInstructions());
        dangerLevelComboBox.setPromptText(String.valueOf(chemical.getDangerLevel()));
        this.dangerLevel = chemical.getDangerLevel();
    }

    private void confirmation(Chemical chemical){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Potvrdite unos podataka: " + chemical.toString(), ButtonType.OK, ButtonType.CANCEL );
        alert.showAndWait().ifPresent(response ->{
            if (response.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)){
                saveChemical(chemical);
                chemicalSuccessFeedback.setVisible(true);
            }
        });
    }
}
