package com.example.stefanovic.kemijskaindustrija.Threads;

import com.example.stefanovic.kemijskaindustrija.DataBase.SerializationRepository;
import com.example.stefanovic.kemijskaindustrija.Files.ToSerializable;
import com.example.stefanovic.kemijskaindustrija.Main.Main;

import java.util.List;

public class DeserializeFiles implements Runnable, SerializationRepository {
    @Override
    public void run() {

    }

    public synchronized List<ToSerializable> deserialize(){
        System.out.println("Executed deserialization in thread");
        notifyAll();
        return getDesirialized(Main.SERIALIZED_CHANGES);
    }
}
