package com.example.stefanovic.kemijskaindustrija.Controllers.Supplier;

import com.example.stefanovic.kemijskaindustrija.Controllers.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.SupplierRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.InputException;
import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Model.Address;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;
import com.example.stefanovic.kemijskaindustrija.Model.Supplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SupplierEdit implements SupplierRepository {

    @FXML
    public ComboBox<Chemical> chemicalsComboBox;
    @FXML
    public Label cityErrorLabel;
    @FXML
    public Label nameErrorLabel;
    @FXML
    public Label streetErrorLabel;
    @FXML
    public Label streetNumberErrorLabel;
    @FXML
    public TextField supplierCityTextField1;
    @FXML
    public TextField supplierNameTextField;
    @FXML
    public TextField supplierStreetNameTextField;
    @FXML
    public TextField supplierStreetNumberTextField;
    @FXML
    public Label title;
    private Supplier supplier;

    @FXML
    void saveSupplier() {
        resetErrors();
        try {
            Long id = supplier.getId();
            Address address = new Address(supplierCityTextField1.getText(),
                    supplierStreetNameTextField.getText(),
                    Integer.valueOf(supplierStreetNumberTextField.getText()));

            Supplier supplier = new Supplier(supplierNameTextField.getText(),id,address);

            checkForErrors();
            updateSupplier(supplier);
//            feedbackLabel.setText("New supplier added!");
        } catch (SaveToDataBaseException | InputException e) {
            if (e instanceof SaveToDataBaseException){
//                feedbackLabel.setText(e.getMessage());
            }
            else{
                if (supplierNameTextField.getText().isEmpty()){
                    nameErrorLabel.setText(((InputException) e).getMessage());
                }
                if (supplierCityTextField1.getText().isEmpty()){
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
        Methods.resetErorrs(nameErrorLabel,streetErrorLabel,cityErrorLabel,streetNumberErrorLabel);
    }

    public void setSupplier(String id) {
        Methods.resetErorrs(cityErrorLabel,nameErrorLabel,streetErrorLabel,streetNumberErrorLabel);
        this.supplier = getSupplierById(Long.parseLong(id));
        supplierCityTextField1.setText(supplier.getAddress().city());
        supplierNameTextField.setText(supplier.getName());
        supplierStreetNameTextField.setText(supplier.getAddress().street());
        supplierStreetNumberTextField.setText(String.valueOf(supplier.getAddress().streetNumber()));
    }

    private void checkForErrors() throws InputException {
        Methods.checkTextField(supplierNameTextField,nameErrorLabel);
        Methods.checkTextField(supplierCityTextField1,cityErrorLabel);
        Methods.checkTextField(supplierStreetNameTextField,streetErrorLabel);
        Methods.checkTextField(supplierStreetNumberTextField,streetNumberErrorLabel);
    }
}
