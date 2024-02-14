package com.example.stefanovic.kemijskaindustrija.Controllers.Chemical;

import com.example.stefanovic.kemijskaindustrija.Controllers.Navigation.NavBar;
import com.example.stefanovic.kemijskaindustrija.DataBase.ChemicalRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.math.BigDecimal;

public class ChemicalView implements ChemicalRepository, UserRepository {

    @FXML
    public AnchorPane root;

    @FXML
    private   BarChart<String, Number> chemicalDangerLevelBarChart;

    @FXML
    public Text chemicalIdLabel;

    @FXML
    public Text chemicalInstructionsLabel;

    @FXML
    public Text chemicalQuantityLabel;

    @FXML
    public Text chemicalQuantityUnitLabel;
    @FXML
    public Text chemicalNameLabel;

    private Chemical chemical;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    void deleteChemical() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "If you delete this entity you can not get it back!", ButtonType.OK, ButtonType.CANCEL );
        alert.showAndWait().ifPresent(response ->{
            if (response.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)){
                DBController.deleteEntity(this.chemical.getId(), "chemical");
                goBackToChemicalsScene();
            }
        });
    }

    @FXML
    void goBackToChemicalsScene() {
        NavBar navBar = new NavBar();
        navBar.showChemicalList();
    }

    @FXML
    void goToChemicalEditView() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("ChemicalInput.fxml"));
            Parent parent = loader.load();
            ChemicalInput chemicalInput = loader.getController();
            chemicalInput.initialize(chemical.getId());
            root.getChildren().clear();
            root.getChildren().setAll(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize(){

    }

    public void initialize(long chemical_id){
        boolean is_admin = UserRepository.isAdmin();
        chemical = getChemicalById(chemical_id);
        chemicalIdLabel.setText(String.valueOf(chemical.getId()));
        chemicalInstructionsLabel.setText(chemical.getInstructions());
        chemicalQuantityLabel.setText(String.valueOf(chemical.getQuantity()));
        chemicalQuantityUnitLabel.setText(chemical.getQuantityUnit());
        chemicalNameLabel.setText(chemical.getName());
        exequteBarChart(chemical);
//        deleteButton.setVisible(is_admin);
//        editButton.setVisible(is_admin);
    }

    private void exequteBarChart(Chemical chemical){
        XYChart.Series<String, Number> averageSeries = createAverageSeries();
        XYChart.Series<String, Number> currentChemicalSeries = createCurrentChemicalSeries(chemical);
        chemicalDangerLevelBarChart.getData().addAll(averageSeries, currentChemicalSeries);
    }

    private XYChart.Series<String, Number> createAverageSeries() {
        BigDecimal averageDangerLevel = getAverageDangerLevel();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average Danger Level");
        series.getData().add(new XYChart.Data<>("Average", averageDangerLevel));
        return series;
    }

    private XYChart.Series<String, Number> createCurrentChemicalSeries(Chemical currentChemical) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Current Chemical Danger Level");
        series.getData().add(new XYChart.Data<>(currentChemical.getName(), currentChemical.getDangerLevel()));
        return series;
    }
}
