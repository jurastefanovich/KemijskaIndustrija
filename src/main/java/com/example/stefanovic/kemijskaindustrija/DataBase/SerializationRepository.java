package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Files.ToSerializable;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public interface SerializationRepository {

    static void serializeToFile(List<ToSerializable> toSerializableList, String filePath) throws RuntimeException{
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(toSerializableList);
            out.close();
            System.out.println("Serializtion successful");
        } catch (IOException e) {
            //ADD LOGGER
            System.err.println(e);
        }
    }



    default List<ToSerializable> getAllDeserializedChanges(){
        List<ToSerializable> toSerializableList = new ArrayList<>();
        List<String> filePaths = Arrays.asList(Main.SERIALIZE_CHEMICAL,Main.SERIALIZE_EQUIPMENT, Main.SERIALIZE_SERVICE,
                Main.USERS_SERIAL_FILE,Main.SERIALIZE_SAFETY_PROTOCOL_STEP, Main.SERIALIZE_SAFETY_PROTOCOL);
        filePaths.forEach(s -> {
            getDesirialized(s).forEach(toSerializable -> toSerializableList.add(toSerializable));
        });
        return toSerializableList;
    }
    default List<ToSerializable> getDesirialized(String filepath){
        List<ToSerializable> readObject = new ArrayList<>();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filepath));
            readObject =  (List<ToSerializable>) in.readObject();
            in.close();
            return readObject;
        } catch (IOException | ClassNotFoundException e) {
//            logger.error("Error reading from serialization file");
            System.err.println(e);
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
//            logger.error("Error trying to write to file object {} ", object );
            throw new RuntimeException(e);
        }
    }

    static void prepareChemicalsForSerialization(){
        List<ToSerializable> toSerializableList = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(Path.of(Main.CHEMICALS_FILE))){
            String line;
            while ((line = reader.readLine()) != null){
                Chemical chemical = ChemicalRepository.getChemicalObject(line);
                LocalDateTime timeOfChange = LocalDateTime.parse(reader.readLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
                String authorOfChange = reader.readLine();

                Map<LocalDateTime, Chemical> chemicalMap = new HashMap<>();
                chemicalMap.put(timeOfChange, chemical);

                toSerializableList.add(new ToSerializable<>(chemicalMap, chemical.getId(), authorOfChange, chemical.getClass().getSimpleName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        serializeToFile(toSerializableList, Main.SERIALIZE_CHEMICAL);

    }


    static void prepareEquipmentForSerialization(){
        List<ToSerializable> toSerializableList = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(Path.of(Main.EQUIPMENT_FILE))){
            String line;
            while ((line = reader.readLine()) != null){
                Equipment equipment = EquipmentRepository.getEquipmentFromLine(line);
                LocalDateTime timeOfChange = LocalDateTime.parse(reader.readLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
                String authorOfChange = reader.readLine();

                Map<LocalDateTime, Equipment> equipmentMap = new HashMap<>();
                equipmentMap.put(timeOfChange, equipment);

                toSerializableList.add(new ToSerializable<>(equipmentMap, equipment.getId(), authorOfChange,equipment.getClass().getSimpleName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        serializeToFile(toSerializableList, Main.SERIALIZE_EQUIPMENT);
    }

    static void prepareUserForSerialization(){
        List<ToSerializable> toSerializableList = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(Path.of(Main.USERS_FILE))){
            String line;
            while ((line = reader.readLine()) != null){
                User user = UserRepository.getUserFromLine(line);
                LocalDateTime timeOfChange = LocalDateTime.parse(reader.readLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
                String authorOfChange = reader.readLine();

                Map<LocalDateTime, User> userMap = new HashMap<>();
                userMap.put(timeOfChange, user);

                toSerializableList.add(new ToSerializable<>(userMap, user.getId(), authorOfChange, user.getClass().getSimpleName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        serializeToFile(toSerializableList, Main.USERS_SERIAL_FILE);
    }

    static void prepareServiceForSerialization(){
        List<ToSerializable> toSerializableList = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(Path.of(Main.SERVICES_FILE))){
            String line;
            while ((line = reader.readLine()) != null){
                Service service = ServisRepository.getServiceFromLine(line);
                LocalDateTime timeOfChange = LocalDateTime.parse(reader.readLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
                String authorOfChange = reader.readLine();

                Map<LocalDateTime, Service> serviceMap = new HashMap<>();
                serviceMap.put(timeOfChange, service);

                toSerializableList.add(new ToSerializable<>(serviceMap, service.getId(), authorOfChange, service.getClass().getSimpleName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        serializeToFile(toSerializableList, Main.SERIALIZE_SERVICE);
    }

    static void prepareSafetyProtocol(){
        List<ToSerializable> toSerializableList = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(Path.of(Main.SAFETY_PROTOCOL_FILE))){
            String line;
            while ((line = reader.readLine()) != null){
                SafetyProtocol safetyProtocol = SafetyProtocolRepository.getSafetyProtocolFromString(line);
                LocalDateTime timeOfChange = LocalDateTime.parse(reader.readLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
                String authorOfChange = reader.readLine();

                Map<LocalDateTime, SafetyProtocol> safetyProtocolHashMap = new HashMap<>();
                safetyProtocolHashMap.put(timeOfChange, safetyProtocol);

                toSerializableList.add(new ToSerializable<>(safetyProtocolHashMap, safetyProtocol.getId(), authorOfChange, safetyProtocol.getClass().getSimpleName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        serializeToFile(toSerializableList, Main.SERIALIZE_SAFETY_PROTOCOL);
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
            e.printStackTrace();
        }
        serializeToFile(toSerializableList, Main.SERIALIZE_SAFETY_PROTOCOL_STEP);
    }


}