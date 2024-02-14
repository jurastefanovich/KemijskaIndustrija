package com.example.stefanovic.kemijskaindustrija.Controllers.Chemical;

import com.example.stefanovic.kemijskaindustrija.Controllers.Equipment.EquipmentDetails;
import com.example.stefanovic.kemijskaindustrija.DataBase.ChemicalRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class ChemicalScene implements ChemicalRepository {

    @FXML
    public AnchorPane root;

    @FXML
    public TableView<Chemical> chemicalTableView;

    @FXML
    public TextField searchByNameTextField;
    @FXML
    public TableColumn<Chemical, BigDecimal> chemicalDangerLevelTableColumn;
    @FXML
    public TableColumn<Chemical, String> chemicalNameTableColumn;

    private List<Chemical> allChemicals;

    @FXML
    void filterChemicalsList() {
        var filtrirano = allChemicals.stream().filter(chemical -> chemical.getName().toLowerCase()
                .contains(searchByNameTextField.getText().toLowerCase()))
                .toList();
        setTableView(filtrirano);
    }

    @FXML
    void initialize(){
        allChemicals = getChemicalList();
        chemicalDangerLevelTableColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDangerLevel()));
        chemicalNameTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        setTableView(allChemicals);
        viewChemicalDetails();
    }

    private void setTableView(List<Chemical> chemicalList){
        chemicalTableView.setItems(FXCollections.observableList(chemicalList));
    }

    private void viewChemicalDetails(){
        chemicalTableView.setOnMouseClicked(event ->{
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && UserRepository.isAdmin()){
                Chemical chemical = chemicalTableView.getSelectionModel().getSelectedItem();
                if (chemical != null ){
                    try {
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("ChemicalView.fxml"));
                        Parent parent = loader.load();
                        ChemicalView chemicalView = loader.getController();
                        chemicalView.initialize(chemical.getId());
                        root.getChildren().clear();
                        root.getChildren().setAll(parent);
                    } catch (IOException e) {
                        logger.error("Error occurred while trying to open chemical " + e.getMessage());
                    }
                }
            }
        });
    }
}
