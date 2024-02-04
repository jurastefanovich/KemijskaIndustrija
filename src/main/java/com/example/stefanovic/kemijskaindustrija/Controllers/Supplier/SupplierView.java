package com.example.stefanovic.kemijskaindustrija.Controllers.Supplier;

import com.example.stefanovic.kemijskaindustrija.Controllers.NavBar;
import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.DataBaseRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.SupplierRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Supplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SupplierView implements SupplierRepository, DataBaseRepository {
    NavBar navBar = new NavBar();
    @FXML
    public AnchorPane root;
    @FXML
    public Label supplierAddressLabel;
    @FXML
    public Label supplierChemicalNumLabel;
    @FXML
    public Label supplierIDLabel;
    @FXML
    public Label supplierNameLabel;

    @FXML
    public Button supplierEditButton;

    @FXML
    public Button supplierDeleteButton;
    private Supplier supplier;

    @FXML
    public void initialize(){

    }
    @FXML
    void goBackToSupplierScene() {
        navBar.showSupplierList();
    }

    @FXML
    void goToSupplierEditView(){
        if (supplier.getId() != null ){
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("SupplierEdit.fxml"));
                Parent parent = loader.load();
                SupplierEdit supplierEdit = loader.getController();
                supplierEdit.setSupplier(String.valueOf(supplier.getId()));
                root.getChildren().clear();
                root.getChildren().setAll(parent);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @FXML
    public void deleteSupplier(){
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to remove this supplier?", ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response->{
            if ( response.getButtonData().equals(ButtonBar.ButtonData.OK_DONE))
            {
                try {
                    DBController.deleteEntity(supplier.getId(), "supplier");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                goBackToSupplierScene();
            }
        });
    }
    protected void setSupplierIDLabel(String id){
//        UNCOMMENT BEFORE PRODUCTION
//        supplierEditButton.setVisible(UserRepository.isAdmin());
        supplierDeleteButton.setVisible(UserRepository.isAdmin());
        this.supplier = getSupplierById(Long.parseLong(id));
        supplierIDLabel.setText(String.valueOf(supplier.getId()));
        supplierNameLabel.setText(supplier.getName());
        supplierAddressLabel.setText(String.valueOf(supplier.getAddress()));
    }
}
