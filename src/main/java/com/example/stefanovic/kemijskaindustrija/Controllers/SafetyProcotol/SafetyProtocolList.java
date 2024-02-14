package com.example.stefanovic.kemijskaindustrija.Controllers.SafetyProcotol;
import com.example.stefanovic.kemijskaindustrija.DataBase.SafetyProtocolRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocol;
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

public class SafetyProtocolList implements SafetyProtocolRepository {

    @FXML
    public AnchorPane root;
    @FXML
    public TableColumn<SafetyProtocol, String> safetyProtocolNameTableColumn;

    @FXML
    public TableColumn<SafetyProtocol, String> safetyProtocolNumberOfStepsTableColumn;

    @FXML
    public TableView<SafetyProtocol> safetyProtocolTableColumn;

    private List<SafetyProtocol> safetyProtocolList;
    @FXML
    private TextField safetyProtocolSearchField;
    @FXML
    public void initialize(){
        this.safetyProtocolList = getAllSafetyProcotols();
        safetyProtocolNameTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        safetyProtocolNumberOfStepsTableColumn.setCellValueFactory(data ->new SimpleStringProperty(String.valueOf(data.getValue().getSteps().size())));
        safetyProtocolTableColumn.setItems(FXCollections.observableList(safetyProtocolList));
        showSupplierDetails();
    }

    private void showSupplierDetails() {
        safetyProtocolTableColumn.setOnMouseClicked(event ->{
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && UserRepository.isAdmin()){
                SafetyProtocol safetyProtocol = safetyProtocolTableColumn.getSelectionModel().getSelectedItem();
                if (safetyProtocol != null ){
                    try {
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("ProtocolView.fxml"));
                        Parent parent = loader.load();
                        SafetyProtocolView safetyProtocolView = loader.getController();
                        safetyProtocolView.viewSafetyProtocolDetails(safetyProtocol.getId());
                        root.getChildren().clear();
                        root.getChildren().setAll(parent);
                    } catch (IOException | NullPointerException e) {
                        Main.logger.info("Error trying to show details of protocol");
                        Main.logger.error(e.getMessage());
                    }

                }
            }
        });
    }
    @FXML
    void searchSafetyProtocol() {
        try {
            String name = safetyProtocolSearchField.getText();
            var equipmentFilter = safetyProtocolList.stream().filter(protocol -> name == null || name.isEmpty() || protocol.getName().toLowerCase().contains(name.toLowerCase())).toList();
            safetyProtocolTableColumn.setItems(FXCollections.observableList(equipmentFilter));
        } catch (NullPointerException e) {
            logger.info("Error trying to filter an service list");
            logger.error(e.getMessage());
        }
    }
}
