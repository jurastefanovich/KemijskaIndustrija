package com.example.stefanovic.kemijskaindustrija.Threads;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.EquipmentRepository;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class EquipmentHealthbarThread implements Runnable,EquipmentRepository {
    @Override
    public void run() {
    
        Equipment equipment = Methods.getRandomItemFromList(getAllEquipmenet());
        equipment.reduceHealth();
        saveToDatabase(equipment);

        //If an equipment entity isn't in service and it's health is below 50%
        if (equipment.getHealthBar() < 50 && !equipment.getInService()){
            Notifications notifications = Notifications.create()
                    .darkStyle()
                    .title("Equipment at health critical!")
                    .text("Equipment by id: " + equipment.getId() + " needs immediate servicing!\nPlease report a service for this equipment")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5.0))
                    .position(Pos.BOTTOM_RIGHT);

            notifications.showWarning();
        }


    }
}
