package com.example.stefanovic.kemijskaindustrija.Model;

import java.util.Objects;

public class SafetyProtocolStep extends Entitet{
   String description;
   Boolean isCritical;
    public SafetyProtocolStep(){}
    public SafetyProtocolStep(String description, Boolean isCritical) {
        this.description = description;
        this.isCritical = isCritical;
    }

    public SafetyProtocolStep(Long id, String description, Boolean isCritical) {
        super(id);
        this.description = description;
        this.isCritical = isCritical;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCritical() {
        return isCritical;
    }

    public void setCritical(Boolean critical) {
        isCritical = critical;
    }

    public void setSafetyProtocolStep(){

    }

    @Override
    public String toString() {
        return "SafetyProtocolStep{" +
                "description='" + description + '\'' +
                ", isCritical=" + isCritical +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SafetyProtocolStep that = (SafetyProtocolStep) o;

        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}