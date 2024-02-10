package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Files.ToSerializable;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface SerializationRepository {

    default void serializeToFile(List<ToSerializable> toSerializableList, String filePath) throws RuntimeException{
        try {
//            ObjectInputStream clear = new ObjectInputStream(new input(filePath, StandardOpenOption.TRUNCATE_EXISTING));
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(toSerializableList);
            out.close();
//            logger.info("Successfully serialized new list");
        } catch (IOException e) {
//            logger.error("Error writing to serializable file");
//            System.err.println(e);/
        }
    }

    /**
     * Method should be called on any input changes for any object (adding or editing)
     * @param filePath path of file to write to
     * @param object object to write to file
     * @param <T>
     */
    static <T> void writeToTxtFile(String filePath, T object){
        try(PrintWriter out = new PrintWriter((new FileWriter(new File(filePath), true)))){
            out.println(object.toString() +"/"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")) +"/"+ UserRepository.getLoggedInUser().getAccount().email());
        } catch (IOException e) {
//            logger.error("Error trying to write to file object {} ", object );
            throw new RuntimeException(e);
        }
    }
}