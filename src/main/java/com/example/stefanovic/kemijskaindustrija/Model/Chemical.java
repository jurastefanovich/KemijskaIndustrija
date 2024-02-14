package com.example.stefanovic.kemijskaindustrija.Model;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Chemical extends Entitet implements Serializable {

    private static final long serialVersionUID = -437278459218234307L;
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

    public List<String> getQuantityUnitList(){
        String[] quantityUnits = {"m","kg","s","mol","cd","m²","m³","L","g","mm","ml","mg"};
        return Arrays.stream(quantityUnits).toList();
    }

    @Override
    public String toString() {
        return getId() + " " + getName() + " "
                + quantity + " " +  quantityUnit + " "
                + instructions + " " + dangerLevel;
    }
}