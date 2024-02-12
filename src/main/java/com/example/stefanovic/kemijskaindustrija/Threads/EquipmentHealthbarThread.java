package com.example.stefanovic.kemijskaindustrija.Threads;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.EquipmentRepository;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;


public class EquipmentHealthbarThread implements Runnable,EquipmentRepository {
    @Override
    public void run() {
        Equipment equipment = Methods.getRandomItemFromList(getAllEquipmenet());
        equipment.reduceHealth();
        saveToDatabase(equipment);
    }
}
