package com.example.stefanovic.kemijskaindustrija.Controllers.Chemical;

import com.example.stefanovic.kemijskaindustrija.Controllers.InputErrorMessages;
import com.example.stefanovic.kemijskaindustrija.Controllers.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.ChemicalRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.SupplierRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;
import com.example.stefanovic.kemijskaindustrija.Model.Supplier;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.util.List;

public class ChemicalInput implements ChemicalRepository {

    @FXML
    private TextField chemicalNameTextField;
    @FXML
    private TextField dangerLevelTextField;
    @FXML
    private TextArea instructionsTextField;
    @FXML
    private Label nameErrorLabel;
    @FXML
    private Label quanitityErrorLabel;
    @FXML
    private TextField quantityTextField;
    @FXML
    private Label quantityUnitErrorLabel;
    @FXML
    private TextField quantityUnitTextField;
    @FXML
    private ComboBox<Supplier> supplierComboBox;
    @FXML
    public Label feedbackLabel;

    @FXML
    void initialize(){
        resetErrors();
        supplierComboBox.setItems(FXCollections.observableList(getSupplierList()));
    }

    @FXML
    void saveNewChemical() {
        resetErrors();
        try {
            Chemical chemical = new Chemical(chemicalNameTextField.getText(),
                    quantityTextField.getText(),
                    quantityUnitTextField.getText(),
                    getChemicalSupplier(),
                    instructionsTextField.getText(),
                    BigDecimal.valueOf(Long.parseLong(dangerLevelTextField.getText())));
            checkForErrors();
            saveToDatabase(chemical);
//            saveToDatabase(chemical);
        } catch (SaveToDataBaseException e) {
            feedbackLabel.setText(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Supplier getChemicalSupplier(){
        System.out.println(supplierComboBox.getValue());
        return supplierComboBox.getValue();
    }

    private void resetErrors(){
        Methods.resetErorrs(nameErrorLabel,quanitityErrorLabel,quantityUnitErrorLabel);
    }

    private void checkForErrors() throws Exception{
        Methods.checkTextField(chemicalNameTextField, nameErrorLabel);
        Methods.checkTextField(quantityTextField, quanitityErrorLabel);
        Methods.checkTextField(quantityUnitTextField, quantityUnitErrorLabel);
    }



}
