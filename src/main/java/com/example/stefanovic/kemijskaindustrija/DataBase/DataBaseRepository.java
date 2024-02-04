package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Model.Supplier;

public interface DataBaseRepository {

    <T> void saveToDatabase(T object) throws Exception;

}
