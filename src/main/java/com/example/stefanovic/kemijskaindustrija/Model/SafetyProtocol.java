package com.example.stefanovic.kemijskaindustrija.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SafetyProtocol  extends Entitet implements Serializable{
    private static final long serialVersionUID = -2602443921783658828L;
    List<SafetyProtocolStep> steps = new ArrayList<>();

    public SafetyProtocol(){}

    public SafetyProtocol(String name, List<SafetyProtocolStep> steps) {
        super(name);
        this.steps = steps;
    }
    public SafetyProtocol(Long id, String name, List<SafetyProtocolStep> steps) {
        super(id, name);
        this.steps = steps;
    }

    public SafetyProtocol(Long id, String name) {
        super(id, name);
    }



    public void addProtocolStep(SafetyProtocolStep protocolStep) {
        this.steps.add(protocolStep);
    }

    public void setSteps(List<SafetyProtocolStep> steps) {
        this.steps = steps;
    }

    public List<SafetyProtocolStep> getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        return getId() + " " + getName();
    }
}
