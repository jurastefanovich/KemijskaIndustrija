package com.example.stefanovic.kemijskaindustrija.Files;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.SerializationRepository;
import com.example.stefanovic.kemijskaindustrija.Threads.DeserializeFiles;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class InventroyController implements SerializationRepository {

    @FXML
   TableColumn<ToSerializable, String> inventoryAuthorOfChangeTableColumn;
    @FXML
   TableColumn<ToSerializable, String> inventoryObjectChange;

    @FXML
    TableColumn<ToSerializable, String> inventoryDateOfChangeTableColumn;


    @FXML
    TableColumn<ToSerializable, String> inventoryInventoryTableColumn;

    @FXML
    TableView<ToSerializable> inventoryTableView;

    @FXML
    void initialize(){
        DeserializeFiles deserializeFiles = new DeserializeFiles();
        inventoryTableView.setItems(FXCollections.observableList(deserializeFiles.deserialize()));
        inventoryInventoryTableColumn.setCellValueFactory(data -> new SimpleStringProperty(removeBrackets(data.getValue().getGenericMap().values().toString())));
        inventoryDateOfChangeTableColumn.setCellValueFactory(data -> new SimpleStringProperty(parseLocalDateTime(String.valueOf(data.getValue().getGenericMap().keySet()))));
        inventoryObjectChange.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmailOfAuthor()));
        inventoryAuthorOfChangeTableColumn.setCellValueFactory(data -> new SimpleStringProperty(Methods.capitalizeFirstLetter(data.getValue().getClassName())));
    }

    private String removeBrackets(String string){
        return string.substring(1, string.length() - 1);
    }

    private String parseLocalDateTime(String dateString){
        dateString = removeBrackets(dateString).substring(0, 19);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
        return formattedDateTime;
    }

}
