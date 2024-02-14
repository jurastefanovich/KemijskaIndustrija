package com.example.stefanovic.kemijskaindustrija.Threads;

import com.example.stefanovic.kemijskaindustrija.DataBase.SerializationRepository;
import com.example.stefanovic.kemijskaindustrija.Files.ToSerializable;
import com.example.stefanovic.kemijskaindustrija.Main.Main;

import java.util.ArrayList;
import java.util.List;

public class DeserializeFiles implements Runnable, SerializationRepository {
    private static List<ToSerializable> toSerializableList = new ArrayList<>();
    @Override
    public void run() {}

    public List<ToSerializable> deserialize(){
        synchronized (toSerializableList){
            System.out.println("Executed deserialization in thread");
            toSerializableList = getDesirialized(Main.SERIALIZED_CHANGES);
            notifyAll();
            return toSerializableList;
        }

    }
}
