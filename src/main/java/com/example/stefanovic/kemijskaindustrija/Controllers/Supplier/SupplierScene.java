package com.example.stefanovic.kemijskaindustrija.Controllers.Supplier;

import com.example.stefanovic.kemijskaindustrija.Controllers.Users.UserProfile;
import com.example.stefanovic.kemijskaindustrija.DataBase.SupplierRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Supplier;
import com.example.stefanovic.kemijskaindustrija.Model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class SupplierScene  implements SupplierRepository{

    @FXML
    public AnchorPane root;
    @FXML
    public TableView<Supplier> supplierTableView;
    @FXML
    public TableColumn<Supplier, String> supplierAddressTableColumn;
    @FXML
    public TableColumn<Supplier, String> supplierNameTableColumn;
    @FXML
    public TextField searchByNameTextField;

    private List<Supplier> allSuppliers;

    private void setSupplierTableView(List<Supplier> allSuppliers){
        supplierTableView.setItems(FXCollections.observableList(allSuppliers));
    }

    @FXML
    void filterSupplierList() {
        var filtrirano = allSuppliers.stream()
                .filter(supplier -> supplier.getName().contains(searchByNameTextField.getText())).toList();
        setSupplierTableView(filtrirano);
    }

    @FXML
    void initialize(){
        allSuppliers = getSupplierList();
        supplierAddressTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress().toString()));
        supplierNameTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        setSupplierTableView(allSuppliers);
        showSupplierDetails();
    }

    private void showSupplierDetails() {
        supplierTableView.setOnMouseClicked(event ->{
            //Add && UserRepository.isAdmin()
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 ){
                Supplier supplier = supplierTableView.getSelectionModel().getSelectedItem();
                if (supplier != null ){
                    try {
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("SupplierView.fxml"));
                        Parent parent = loader.load();
                        SupplierView supplierView = loader.getController();
                        supplierView.setSupplierIDLabel(String.valueOf(supplier.getId()));
                        root.getChildren().clear();
                        root.getChildren().setAll(parent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
    }
}
