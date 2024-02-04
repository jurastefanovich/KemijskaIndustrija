package com.example.stefanovic.kemijskaindustrija.Model;

public enum EquipmentType {
    MIXER("Mixer"),
    REACTOR("Reactor"),
    PUMP("Pump"),
    HEATER("Heater"),
    COOLER("Cooler"),
    SEPARATOR("Separator"),
    COMPRESSOR("Compressor"),
    TANK("Tank"),
    VALVE("Valve"),
    FILTER("Filter"),
    DISTILLATION_COLUMN("Distillation Column"),
    HEAT_EXCHANGER("Heat Exchanger"),
    AGITATOR("Agitator"),
    DRYER("Dryer"),
    CENTRIFUGE("Centrifuge"),
    OTHER("Other");

    private final String equipmentType;

    EquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentType() {
        return equipmentType;
    }
}