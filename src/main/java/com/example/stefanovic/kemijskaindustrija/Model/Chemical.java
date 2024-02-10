package com.example.stefanovic.kemijskaindustrija.Model;

import java.math.BigDecimal;

public class Chemical extends Entitet {
    private Double quantity;
    private String quantityUnit;
    private String instructions;
    private BigDecimal dangerLevel;

    public Chemical(String name, Double quantity, String quantityUnit, String instructions, BigDecimal dangerLevel) {
        super(name);
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
        this.instructions = instructions;
        this.dangerLevel = dangerLevel;
    }

    public Chemical( Long id, String name, Double quantity, String quantityUnit, String instructions, BigDecimal dangerLevel) {
        super(id,name);
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
        this.instructions = instructions;
        this.dangerLevel = dangerLevel;
    }
    public String getInstructions() {return instructions;}
    public BigDecimal getDangerLevel() {return dangerLevel;}
    public String getQuantityUnit() {return quantityUnit;}
    public Double getQuantity() {return quantity;}

//    Maybe this should be added to a DB and able to be edited
    public String[] getQuantityUnitList(){
        String[] quantityUnits = {"m","kg","s","mol","cd","m²","m³","L","g","mm","ml","mg"};
        return quantityUnits;
    }

    @Override
    public String toString() {
        return getName() + " " + quantity + " " +  quantityUnit + " " + instructions + " " + dangerLevel;
    }
}