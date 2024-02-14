package com.example.stefanovic.kemijskaindustrija.Model;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class Equipment <T>  extends Entitet implements HealthBarInterface, Serializable {

    private static final long serialVersionUID = -7064329956452093883L;
    private String description;
    private T type;
    private Double healthBar;
    private Boolean isInService;
    public Equipment(String description, T type, Double healthBar) {
        this.description = description;
        this.type = type;
        this.healthBar = healthBar;
    }

    public Equipment(Long id, String description, T type,Double healthBar) {
        super(id);
        this.description = description;
        this.type = type;
        this.healthBar = healthBar;

    }

    public Equipment(String name, String description, T type,Double healthBar) {
        super(name);
        this.description = description;
        this.type = type;
        this.healthBar = healthBar;

    }
    public Equipment(String name, String description, T type) {
        super(name);
        this.description = description;
        this.type = type;

    }

    public Equipment(Long id, String name, String description, T type,Double healthBar) {
        super(id, name);
        this.description = description;
        this.type = type;
        this.healthBar = healthBar;

    }

    public Equipment(String description, T type, Double healthBar, Boolean isInService) {
        this.description = description;
        this.type = type;
        this.healthBar = healthBar;
        this.isInService = isInService;
    }

    public Equipment(Long id, String description, T type, Double healthBar, Boolean isInService) {
        super(id);
        this.description = description;
        this.type = type;
        this.healthBar = healthBar;
        this.isInService = isInService;
    }

    public Equipment(String name, String description, T type, Double healthBar, Boolean isInService) {
        super(name);
        this.description = description;
        this.type = type;
        this.healthBar = healthBar;
        this.isInService = isInService;
    }

    public Equipment(Long id, String name, String description, T type, Double healthBar, Boolean isInService) {
        super(id, name);
        this.description = description;
        this.type = type;
        this.healthBar = healthBar;
        this.isInService = isInService;
    }

    public Boolean getInService() {
        return isInService;
    }

    public void setInService(Boolean inService) {
        isInService = inService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public Double getHealthBar() {
        return healthBar;
    }

    public void setHealthBar(Double healthBar) {
        this.healthBar = healthBar;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Equipment equipment = (Equipment) object;
        return Objects.equals(description, equipment.description) && type == equipment.type;
    }

    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), description, type); }

    @Override
    public String toString() { return getId() +" "
            + getName()
            +" "+ description
            +" "+ type + " " + healthBar +" "+ isInService;  }

    @Override
    public void reduceHealth() {
        Double originalHealth = getHealthBar();
        Random random = new Random();
        //HealthBar can't be reduced more than this
        Double plateau = (50/100)*originalHealth;
        Double reducer = plateau + (originalHealth - plateau) * random.nextDouble();

        this.healthBar -= reducer;

        if (this.healthBar <= 0){
            this.healthBar = (double) 0;
        }
    }
}
