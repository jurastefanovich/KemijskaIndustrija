package com.example.stefanovic.kemijskaindustrija.Controllers.Chemical;

import com.example.stefanovic.kemijskaindustrija.DataBase.ChemicalRepository;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.math.BigDecimal;

public class ChemicalView  {

    @FXML
    public BarChart<BigDecimal, BigDecimal> chemicalDangerLevelBarChart;
    @FXML
    public Label supplierIDLabel;
    @FXML
    public Label supplierNameLabel;
    @FXML
    public Label supplierNameLabel1;
    @FXML
    public Label supplierNameLabel11;
    @FXML
    public Label supplierNameLabel2;
    private Chemical thisChemical;
    @FXML
    void goBackToChemicalsScene(ActionEvent event) {

    }

    @FXML
    void initialize(){

    }

    private void exequteBarChart(){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Chemical", thisChemical.getDangerLevel()));
//        series.getData().add(new XYChart.Data<>("Average", getAverageDangerLevel()));

//        chemicalDangerLevelBarChart.getData().add(series);
    }
}
