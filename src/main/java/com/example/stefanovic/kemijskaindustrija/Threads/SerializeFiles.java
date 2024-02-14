package com.example.stefanovic.kemijskaindustrija.Threads;

import com.example.stefanovic.kemijskaindustrija.DataBase.SafetyProtocolRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.SerializationRepository;
import com.example.stefanovic.kemijskaindustrija.Files.ToSerializable;
import com.example.stefanovic.kemijskaindustrija.Main.Main;

import java.util.ArrayList;
import java.util.List;

public class SerializeFiles implements Runnable, SerializationRepository {
    private static List<ToSerializable> toSerializableList = new ArrayList<>();
    @Override
    public void run() {

    }

    public static synchronized void serialize(ToSerializable serializableList){
        DeserializeFiles deserializeFiles = new DeserializeFiles();
        toSerializableList = deserializeFiles.deserialize();
        toSerializableList.add(serializableList);
        SerializationRepository.serializeToFile(toSerializableList, Main.SERIALIZED_CHANGES);
    }
}
