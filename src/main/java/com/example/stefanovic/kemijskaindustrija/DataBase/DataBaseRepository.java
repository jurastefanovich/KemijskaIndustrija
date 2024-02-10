package com.example.stefanovic.kemijskaindustrija.DataBase;

public interface DataBaseRepository {

    <T> void saveToDatabase(T object) throws Exception;

}
