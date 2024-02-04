package com.example.stefanovic.kemijskaindustrija.Files;

import com.example.stefanovic.kemijskaindustrija.Model.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class ToSerializable <K,V> implements Serializable {
    private static final long serialVersionUID = 208101344790072837L;
    private Map<K,V> genericMap;
    private Long idOfAlteredProduct;

    public Long getIdOfAlteredProduct() {
        return idOfAlteredProduct;
    }

    public void setIdOfAlteredProduct(Long idOfAlteredProduct) {
        this.idOfAlteredProduct = idOfAlteredProduct;
    }

    private LocalDateTime dateOfEntry;
    private User authorOfEntry;
    public ToSerializable(Map<K, V> genericMap, LocalDateTime dateOfEntry, User user) {
        this.genericMap = genericMap;
        this.dateOfEntry = dateOfEntry;
        this.authorOfEntry = user;
    }

    public ToSerializable(Map<K, V> genericMap, Long idOfAlteredProduct, User authorOfEntry) {
        this.genericMap = genericMap;
        this.idOfAlteredProduct = idOfAlteredProduct;
        this.authorOfEntry = authorOfEntry;
    }

    public ToSerializable(Map<K, V> genericMap, User authorOfEntry) {
        this.genericMap = genericMap;
        this.authorOfEntry = authorOfEntry;
    }

    public LocalDateTime getDateOfEntry() {
        return dateOfEntry;
    }

    public Map<K, V> getGenericMap() {
        return genericMap;
    }

    public User getAuthorOfEntry() {
        return authorOfEntry;
    }

    @Override
    public String toString() {
        return "ToSerializable{" +
                "genericMap=" + genericMap +
                ", dateOfEntry=" + dateOfEntry +
                ", authorOfEntry=" + authorOfEntry +
                '}' + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToSerializable<?, ?> that = (ToSerializable<?, ?>) o;
        return Objects.equals(genericMap, that.genericMap) && Objects.equals(idOfAlteredProduct, that.idOfAlteredProduct) && Objects.equals(dateOfEntry, that.dateOfEntry) && Objects.equals(authorOfEntry, that.authorOfEntry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genericMap, idOfAlteredProduct, dateOfEntry, authorOfEntry);
    }

    public void setDateOfEntry(LocalDateTime dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }
}