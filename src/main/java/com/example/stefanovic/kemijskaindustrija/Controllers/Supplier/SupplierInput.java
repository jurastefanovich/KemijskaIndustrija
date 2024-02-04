package com.example.stefanovic.kemijskaindustrija.Controllers.Supplier;

import com.example.stefanovic.kemijskaindustrija.Controllers.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.SupplierRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.InputException;
import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Model.Supplier;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SupplierInput implements SupplierRepository {
    @FXML
    public Label cityErrorLabel;
    @FXML
    public Label nameErrorLabel;
    @FXML
    public Label streetErrorLabel;
    @FXML
    public Label streetNumberErrorLabel;
    @FXML
    public TextField supplierCityTextField;
    @FXML
    public TextField supplierNameTextField;
    @FXML
    public TextField supplierStreetNameTextField;
    @FXML
    public TextField supplierStreetNumberTextField;
    @FXML
    private Label feedbackLabel;

    public void initialize(){
        resetErrors();
    }
    @FXML
    void saveNewSupplier() {
        resetErrors();
        try {
            Supplier supplier = new Supplier(
                    supplierNameTextField.getText(),
                    supplierCityTextField.getText(),
                    supplierStreetNameTextField.getText(),
                    Integer.valueOf(supplierStreetNumberTextField.getText()));
            checkForErrors();
            saveToDatabase(supplier);
            feedbackLabel.setText("New supplier added!");
        } catch (SaveToDataBaseException | InputException e) {
            if (e instanceof SaveToDataBaseException){
                feedbackLabel.setText(e.getMessage());
            }
            else{
                if (supplierNameTextField.getText().isEmpty()){
                    nameErrorLabel.setText(((InputException) e).getMessage());
                }
                if (supplierCityTextField.getText().isEmpty()){
                    cityErrorLabel.setText(((InputException) e).getMessage());

                }
                if (supplierStreetNameTextField.getText().isEmpty()){
                    streetErrorLabel.setText(((InputException) e).getMessage());

                }
                if (supplierStreetNumberTextField.getText().isEmpty()){
                    streetNumberErrorLabel.setText(((InputException) e).getMessage());
                }

            }
        } catch (NumberFormatException e){
            streetNumberErrorLabel.setText("Field must be a number");
        }
    }
    private void resetErrors(){
        Methods.resetErorrs(nameErrorLabel,streetErrorLabel,cityErrorLabel,streetNumberErrorLabel,feedbackLabel);
    }

    private void checkForErrors() throws InputException {
        Methods.checkTextField(supplierNameTextField,nameErrorLabel);
        Methods.checkTextField(supplierCityTextField,cityErrorLabel);
        Methods.checkTextField(supplierStreetNameTextField,streetErrorLabel);
        Methods.checkTextField(supplierStreetNumberTextField,streetNumberErrorLabel);
    }


}
