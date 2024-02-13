package com.example.stefanovic.kemijskaindustrija.Model;

import java.io.Serializable;
import java.util.Objects;

public abstract class Entitet {


    private String name;
    private Long id;

    public Entitet(){}
    public Entitet(Long id) {
        this.id = id;
    }
    public Entitet(String name){this.name = name;}
    public Entitet(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Entitet entitet = (Entitet) object;
        return Objects.equals(name, entitet.name) && Objects.equals(id, entitet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}
