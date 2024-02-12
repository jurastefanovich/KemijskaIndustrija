package com.example.stefanovic.kemijskaindustrija.Threads;

import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.SerializationRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.ServisRepository;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ServiceThread  implements Runnable{
    DateComparator dateComparator = new DateComparator();
    @Override
    public void run() {
        List<Service> servicesList = ServisRepository.getAllServices();
        servicesList.stream().forEach(service -> {
            if (dateComparator.compareTo(service.getDateOfService(), LocalDate.now())){
                DBController.deleteEntity(service.getId(), "SERVICE");
            }
        });
    }
}
