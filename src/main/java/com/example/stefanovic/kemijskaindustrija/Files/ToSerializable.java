package com.example.stefanovic.kemijskaindustrija.Files;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class ToSerializable <K,V> implements Serializable {
    private static final long serialVersionUID = 208101344790072837L;
    private Map<K,V> genericMap;
    private Long idOfAlteredProduct;
    private String className;

    public Long getIdOfAlteredProduct() {
        return idOfAlteredProduct;
    }

    public void setIdOfAlteredProduct(Long idOfAlteredProduct) {
        this.idOfAlteredProduct = idOfAlteredProduct;
    }

    private LocalDateTime dateOfEntry;
    private String emailOfAuthor;

    public ToSerializable(Map<K, V> genericMap, Long idOfAlteredProduct, String emailOfAuthor, String className) {
        this.genericMap = genericMap;
        this.idOfAlteredProduct = idOfAlteredProduct;
        this.emailOfAuthor = emailOfAuthor;
        this.className = className;
    }

    public ToSerializable(Map<K, V> genericMap, Long idOfAlteredProduct, String emailOfAuthor) {
        this.genericMap = genericMap;
        this.idOfAlteredProduct = idOfAlteredProduct;
        this.emailOfAuthor = emailOfAuthor;
    }


    public LocalDateTime getDateOfEntry() {
        return dateOfEntry;
    }

    public Map<K, V> getGenericMap() {
        return genericMap;
    }

    public String getEmailOfAuthor() {
        return emailOfAuthor;
    }


    public void setGenericMap(Map<K, V> genericMap) {
        this.genericMap = genericMap;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setEmailOfAuthor(String emailOfAuthor) {
        this.emailOfAuthor = emailOfAuthor;
    }

    @Override
    public String toString() {
        return "ToSerializable{" +
                "genericMap=" + genericMap +
                ", dateOfEntry=" + dateOfEntry +
                ", authorOfEntry=" + emailOfAuthor +
                '}' + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToSerializable<?, ?> that = (ToSerializable<?, ?>) o;
        return Objects.equals(genericMap, that.genericMap) && Objects.equals(idOfAlteredProduct, that.idOfAlteredProduct) && Objects.equals(dateOfEntry, that.dateOfEntry) && Objects.equals(emailOfAuthor, that.emailOfAuthor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genericMap, idOfAlteredProduct, dateOfEntry, emailOfAuthor);
    }

    public void setDateOfEntry(LocalDateTime dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }
}