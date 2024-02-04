package com.example.stefanovic.kemijskaindustrija.Controllers;

import com.example.stefanovic.kemijskaindustrija.Main.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class HomePage {

    @FXML
    public ImageView chemicalImage;

    @FXML
    public ImageView supplierImage;

    public void initialize(){
        supplierImage.addEventHandler(MouseEvent.MOUSE_CLICKED, gotToSupplierListView);
        chemicalImage.addEventHandler(MouseEvent.MOUSE_CLICKED, gotToChemicalListView);
    }

    EventHandler<MouseEvent> gotToSupplierListView = event -> {
        try {
            Main.showScreen("SupplierScene.fxml");
        } catch (IOException e) {
            System.out.println("Error showing supplier list");
            throw new RuntimeException(e);
        }
    };

    EventHandler<MouseEvent> gotToChemicalListView = event -> {
        try {
            Main.showScreen("ChemicalScene.fxml");
        } catch (IOException e) {
            System.out.println("Error showing chem list");
            throw new RuntimeException(e);
        }
    };
}
