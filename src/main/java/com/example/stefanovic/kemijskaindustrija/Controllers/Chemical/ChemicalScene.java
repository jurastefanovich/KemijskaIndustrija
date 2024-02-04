package com.example.stefanovic.kemijskaindustrija.Controllers.Chemical;

import com.example.stefanovic.kemijskaindustrija.DataBase.ChemicalRepository;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;
import com.example.stefanovic.kemijskaindustrija.Model.Supplier;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class ChemicalScene {

    @FXML
    public TableView<Chemical> chemicalTableView;

    @FXML
    public ComboBox<BigDecimal> dangerLevelComboBox;
    @FXML
    public TextField searchByNameTextField;
    @FXML
    public TableColumn<Chemical, BigDecimal> chemicalDangerLevelTableColumn;
    @FXML
    public TableColumn<Chemical, String> chemicalNameTableColumn;
    @FXML
    public TableColumn<Chemical, String> chemicalSupplierTableColumn;

    private List<Chemical> allChemicals;

    @FXML
    void filterChemicalsList() {
        var filtrirano = allChemicals.stream()
                .filter(chemical -> chemical.getDangerLevel().equals(dangerLevelComboBox.getValue()))
                .filter(chemical -> chemical.getName().contains(searchByNameTextField.getText())).toList();
        setTableView(filtrirano);
    }

    @FXML
    void initialize(){

//        allChemicals = getChemicalList();
        chemicalDangerLevelTableColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDangerLevel()));
        chemicalSupplierTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSupplier().toString()));
        chemicalNameTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        setTableView(allChemicals);
    }

    private void setTableView(List<Chemical> chemicalList){
        chemicalTableView.setItems(FXCollections.observableList(chemicalList));
    }
}
