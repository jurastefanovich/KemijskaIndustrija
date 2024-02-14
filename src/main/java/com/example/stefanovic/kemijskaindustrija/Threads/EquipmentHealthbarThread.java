package com.example.stefanovic.kemijskaindustrija.Threads;

import com.example.stefanovic.kemijskaindustrija.DataBase.EquipmentRepository;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.util.List;


public class EquipmentHealthbarThread implements Runnable,EquipmentRepository {
    @Override
    public void run() {
        List<Equipment> equipmentList = getAllEquipmenet();

        for (Equipment equipment : equipmentList) {
            equipment.reduceHealth();
            saveToDatabase(equipment);
            if (equipment.getHealthBar() < 50 && !equipment.getInService()) {
                Platform.runLater(()->{
                    Notifications notifications = Notifications.create()
                            .darkStyle()
                            .title("Equipment at health critical!")
                            .text("Equipment by id: " + equipment.getId() + " needs immediate servicing!\nPlease report a service for this equipment")
                            .graphic(null)
                            .hideAfter(Duration.seconds(5.0))
                            .position(Pos.BOTTOM_RIGHT);

                    notifications.showWarning();
                });

            }
        }
    }
}
