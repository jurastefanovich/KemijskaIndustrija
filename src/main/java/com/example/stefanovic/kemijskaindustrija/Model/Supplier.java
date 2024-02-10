//package com.example.stefanovic.kemijskaindustrija.Model;
//
//import java.util.List;
//
//public class Supplier extends Entitet {
//
//    private Address address;
//    private List<Chemical> chemicalList;
//
//    public Supplier(String name, String city, String street, Integer streetNumber ){
//        super(name);
//        this.address = new Address(city,street,streetNumber);
//    }
//
//    public Supplier(String name, Long id, Address address) {
//        super(id,name);
//        this.address = address;
//    }
//
//    public Supplier(String name, Address address, List<Chemical> chemicalList) {
//        super(name);
//        this.address = address;
//        this.chemicalList = chemicalList;
//    }
//
//    public Supplier(String name, Long id, Address address, List<Chemical> chemicalList) {
//        super(id,name);
//        this.address = address;
//        this.chemicalList = chemicalList;
//    }
//    public Address getAddress() {return address;}
//    public List<Chemical> getChemicalList() {return chemicalList;}
//
//    @Override
//    public String toString() {
//        return getName() + " " + address +  " " + chemicalList;
//    }
//}
