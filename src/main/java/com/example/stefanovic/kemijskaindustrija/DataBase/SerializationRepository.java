package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Files.ToSerializable;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.*;
import com.example.stefanovic.kemijskaindustrija.Threads.SerializeFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

public interface SerializationRepository {
    Logger logger = LoggerFactory.getLogger(Main.class);

    static void serializeToFile(List<ToSerializable> toSerializableList, String filePath) throws RuntimeException{
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(toSerializableList);
            out.close();
        } catch (IOException e) {
            logger.info("Error while trying to serialize to file with path " + filePath);
            logger.error(e.getMessage());
        }
    }

    default List<ToSerializable> getDesirialized(String filepath){
        List<ToSerializable> readObject = new ArrayList<>();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filepath));
            readObject =  (List<ToSerializable>) in.readObject();
            in.close();
            return readObject;
        } catch (IOException | ClassNotFoundException e) {
            logger.info("Error while trying to fetch deserialized data");
            logger.error(e.getMessage());
        }
        return readObject;
    }

    /**
     * Method should be called on any input changes for any object (adding or editing)
     * @param filePath path of file to write to
     * @param object object to write to file
     * @param <T>
     */
    static <T> void writeToTxtFile(String filePath, T object){
        try(PrintWriter out = new PrintWriter((new FileWriter(new File(filePath), true)))){
            out.println(object.toString() +
                    "\n"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")) +
                    "\n"+ UserRepository.getLoggedInUser().getAccount().email());
        } catch (IOException e) {
            logger.info("Error while writing to file of path " + filePath);
            logger.error(e.getMessage());
        }
    }

    static <T> void prepareObjectForSerialization(T object){
        SerializeFiles serializeFiles = new SerializeFiles();
        Map<LocalDateTime, T> map = new HashMap<>();
        map.put(LocalDateTime.now(), object);
        ToSerializable toSerializable = new ToSerializable<>(map, UserRepository.getLoggedInUser().getAccount().email(), object.getClass().getSimpleName());
        serializeFiles.serialize(toSerializable);
    }

    static void prepareSafetyProtocolStep(){
        List<ToSerializable> toSerializableList = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(Path.of(Main.SAFETY_PROTOCOL_STEP_FILE))){
            String line;
            while ((line = reader.readLine()) != null){
                SafetyProtocolStep safetyProtocolStep = SafetyProtocolRepository.getSafetyProtocolStepFromString(line);
                LocalDateTime timeOfChange = LocalDateTime.parse(reader.readLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
                String authorOfChange = reader.readLine();

                Map<LocalDateTime, SafetyProtocolStep> safetyProtocolStepHashMap = new HashMap<>();
                safetyProtocolStepHashMap.put(timeOfChange, safetyProtocolStep);

                toSerializableList.add(new ToSerializable<>(safetyProtocolStepHashMap, safetyProtocolStep.getId(), authorOfChange, safetyProtocolStep.getClass().getSimpleName()));
            }
        } catch (IOException e) {
            logger.info("Error while writing to serialize safety protocol");
            logger.error(e.getMessage());
        }
        serializeToFile(toSerializableList, Main.SERIALIZE_SAFETY_PROTOCOL_STEP);
    }


}