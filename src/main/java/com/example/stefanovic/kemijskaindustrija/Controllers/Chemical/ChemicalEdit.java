package com.example.stefanovic.kemijskaindustrija.Controllers.Chemical;

import com.example.stefanovic.kemijskaindustrija.Controllers.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.ChemicalRepository;
import com.example.stefanovic.kemijskaindustrija.Model.Supplier;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ChemicalEdit {

    @FXML
    public TextField chemicalNameTextField;
    @FXML
    public TextField dangerLevelTextField;
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
    public ComboBox<Supplier> supplierComboBox;
    @FXML
    public Label title;

    @FXML
    void initialize(){
        Methods.resetErorrs(nameErrorLabel, quanitityErrorLabel, quantityUnitErrorLabel);

    }

}
