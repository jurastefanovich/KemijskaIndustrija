package com.example.stefanovic.kemijskaindustrija.Model;

import java.math.BigDecimal;

public class Chemical extends Entitet {
    private String quantity;
    private String quantityUnit;
    private Supplier supplier;
    private String instructions;
    private BigDecimal dangerLevel;

    public Chemical(String name, String quantity, String quantityUnit, Supplier supplier, String instructions, BigDecimal dangerLevel) {
        super(name);
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
        this.supplier = supplier;
        this.instructions = instructions;
        this.dangerLevel = dangerLevel;
    }

    public Chemical( Long id, String name, String quantity, String quantityUnit, Supplier supplier, String instructions, BigDecimal dangerLevel) {
        super(id,name);
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
        this.supplier = supplier;
        this.instructions = instructions;
        this.dangerLevel = dangerLevel;
    }
    public String getInstructions() {return instructions;}
    public BigDecimal getDangerLevel() {return dangerLevel;}
    public String getQuantityUnit() {return quantityUnit;}
    public String getQuantity() {return quantity;}
    public Supplier getSupplier() {return supplier;}

    @Override
    public String toString() {
        return getName() + " " + quantity + " " +  quantityUnit + " " + supplier + " " + instructions + " " + dangerLevel;
    }
}