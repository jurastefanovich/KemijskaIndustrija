package com.example.stefanovic.kemijskaindustrija.Threads;

import com.example.stefanovic.kemijskaindustrija.Comparator.DateComparator;
import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.EquipmentRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.ServisRepository;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import com.example.stefanovic.kemijskaindustrija.Model.Service;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ServiceThread  implements Runnable, EquipmentRepository{
    DateComparator dateComparator = new DateComparator();
    @Override
    public void run() {
            Set<Service> servicesList = new HashSet<>(ServisRepository.getAllServices());
            servicesList.stream().forEach(service -> {
                if (dateComparator.compareTo(service.getDateOfService(), LocalDate.now())){
                    DBController.deleteEntity(service.getId(), "SERVICE");

                    Notifications notifications = Notifications.create()
                            .darkStyle()
                            .title("Service removed!")
                            .text("Service with id: " + service.getId() + " has ended on " + service.getDateOfService())
                            .graphic(null)
                            .hideAfter(Duration.seconds(5.0))
                            .position(Pos.BOTTOM_RIGHT);

                    notifications.showInformation();

                    Equipment equipment = service.getEquipment();
                    equipment.setInService(false);
                    equipment.setHealthBar(100.0);

                    saveToDatabase(equipment);
                }
            });
    }
}
