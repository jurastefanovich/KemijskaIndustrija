package com.example.stefanovic.kemijskaindustrija.Model;

import java.util.Objects;

public class Equipment extends Entitet {

    private String description;
    private EquipmentType type;
    private Integer healthBar;
    public Equipment(String description, EquipmentType type) {
        this.description = description;
        this.type = type;
    }

    public Equipment(Long id, String description, EquipmentType type) {
        super(id);
        this.description = description;
        this.type = type;
    }

    public Equipment(String name, String description, EquipmentType type) {
        super(name);
        this.description = description;
        this.type = type;
    }

    public Equipment(Long id, String name, String description, EquipmentType type) {
        super(id, name);
        this.description = description;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EquipmentType getType() {
        return type;
    }

    public void setType(EquipmentType type) {
        this.type = type;
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
    public String toString() { return getId() +" "+ getName() +" "+ description +" "+ type; }
}
