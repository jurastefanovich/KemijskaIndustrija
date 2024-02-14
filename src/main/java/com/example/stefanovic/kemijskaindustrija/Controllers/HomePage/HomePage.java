package com.example.stefanovic.kemijskaindustrija.Controllers.HomePage;

import com.example.stefanovic.kemijskaindustrija.Controllers.Navigation.NavBar;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.SnapshotView;

import java.io.IOException;

public class HomePage {

    @FXML
    ImageView chemicalImage;

    @FXML
    ImageView equipmentImage;

    @FXML
    ImageView protocolImage;

    @FXML
    ImageView serviceImage;

    @FXML
    ImageView usersImage;
    NavBar navBar = new NavBar();


    public void initialize(){
        chemicalImage.addEventHandler(MouseEvent.MOUSE_CLICKED, goToChemicalListView);
        equipmentImage.addEventHandler(MouseEvent.MOUSE_CLICKED, goToEqupmentListView);
        protocolImage.addEventHandler(MouseEvent.MOUSE_CLICKED, goToProtocolListView);
        serviceImage.addEventHandler(MouseEvent.MOUSE_CLICKED, goToServiceListView);
        usersImage.addEventHandler(MouseEvent.MOUSE_CLICKED, goToUsersList);

    }


    EventHandler<MouseEvent> goToChemicalListView = event -> {navBar.showChemicalList();};
    EventHandler<MouseEvent> goToEqupmentListView = event -> {navBar.showEquipmenList();};
    EventHandler<MouseEvent> goToProtocolListView = event -> {navBar.showProtocolList();};
    EventHandler<MouseEvent> goToServiceListView = event -> {navBar.goToAllServices();};
    EventHandler<MouseEvent> goToUsersList = event -> {navBar.showUsersList();};


}
