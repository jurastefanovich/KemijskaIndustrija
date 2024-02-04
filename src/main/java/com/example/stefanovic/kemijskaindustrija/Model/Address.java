package com.example.stefanovic.kemijskaindustrija.Model;

public record Address(String city, String street, Integer streetNumber) {
    @Override
    public String toString() {
        return  city + ' ' + street + ' ' + streetNumber;
    }
}
