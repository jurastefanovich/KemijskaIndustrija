package com.example.stefanovic.kemijskaindustrija.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Service extends Entitet implements Serializable {

    private static final long serialVersionUID = 9094773718535369688L;
    private String title;
    private String description;
    private Equipment equipment;
    private LocalDate dateOfService;
    public Service(){}
    public Service(String title, String description, Equipment equipment, LocalDate dateOfService) {
        this.title = title;
        this.description = description;
        this.equipment = equipment;
        this.dateOfService = dateOfService;
    }

    public Service(Long id, String title, String description, Equipment equipment, LocalDate dateOfService) {
        super(id);
        this.title = title;
        this.description = description;
        this.equipment = equipment;
        this.dateOfService = dateOfService;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public LocalDate getDateOfService() {
        return dateOfService;
    }

    public void setDateOfService(LocalDate dateOfService) {
        this.dateOfService = dateOfService;
    }
}
