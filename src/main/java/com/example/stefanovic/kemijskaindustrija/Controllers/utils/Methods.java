package com.example.stefanovic.kemijskaindustrija.Controllers.utils;

import com.example.stefanovic.kemijskaindustrija.Exception.IllegalStringLengthException;
import com.example.stefanovic.kemijskaindustrija.Exception.InputException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.*;

/**
 * Class designed to hold a list of general use methods that can be called and used in all classes
 */
public class Methods {

    public static <T> T getRandomItemFromList(List<T> list){
        Random random = new Random();
        int randomIndex = 0;
        if (!list.isEmpty()){
            randomIndex = random.nextInt(list.size());
            return list.get(randomIndex);
        }
        return list.get(randomIndex);
    }

    /**
     * Method for re-setting a list of error labels
     * @param labels list of labels
     */
    public static void resetErorrs(Label... labels){
        List<Label> labelList = Arrays.stream(labels).toList();
        labelList.forEach(label -> label.setText(""));
    }

    public static void resetOutlines(Control... control){
        List<Control> controlList = Arrays.stream(control).toList();
        controlList.forEach(control1 -> control1.setStyle("-fx-border-color: transparent;"));

    }

    /**
     * Method for clearing input areas of forms
     * @param textFields a list of textFields
     */
    public static void clearInput(TextInputControl... textFields){
        List<TextInputControl> textInputControlList = Arrays.stream(textFields).toList();
        textInputControlList.forEach(TextInputControl::clear);
    }

    /**
     * Method for checking if a required textField is filled
     * @param textField
     * @param errorLabel - accompanying error label
     * @throws Exception
     */
    public static void checkTextField(TextField textField, Label errorLabel) throws InputException{
        if (textField.getText().isEmpty()){
            textField.setStyle("-fx-border-color: red;");
            errorLabel.setText(InputErrorMessages.EMPTY_FIELD.getMessage());
            throw new InputException(InputErrorMessages.EMPTY_FIELD.getMessage());
        }
    }

    public static void checkLabel(Label label, Label errorLabel) throws InputException{
        if (label.getText().isEmpty()){
            label.setStyle("-fx-border-color: red;");
            errorLabel.setText(InputErrorMessages.EMPTY_FIELD.getMessage());
            throw new InputException(InputErrorMessages.EMPTY_FIELD.getMessage());
        }
    }

    public static void checkSearch(Label label, Label errorLabel, TextField textField) throws InputException{
        if (label.getText().isEmpty() || Objects.equals(label.getText(), "")){
            textField.setStyle("-fx-border-color: red;");
            errorLabel.setText(InputErrorMessages.EMPTY_FIELD.getMessage());
            throw new InputException(InputErrorMessages.EMPTY_FIELD.getMessage());
        }
    }

    public static void checkTextArea(TextArea textArea, Label errorLabel) throws InputException{
        if (textArea.getText().isEmpty()){
            textArea.setStyle("-fx-border-color: red;");
            errorLabel.setText(InputErrorMessages.EMPTY_FIELD.getMessage());
            throw new InputException(InputErrorMessages.EMPTY_FIELD.getMessage());
        }
    }

    public static void checkDateField(DatePicker datePicker, Label errorLabel) throws  InputException{
        if (datePicker.getValue() == null){
            errorLabel.setText(InputErrorMessages.EMPTY_FIELD.getMessage());
            datePicker.setStyle("-fx-border-color: red;");
            throw new InputException(InputErrorMessages.EMPTY_FIELD.getMessage());
        }
    }

    public static void setFeedbackInvisible(AnchorPane... anchorPane){
        Arrays.stream(anchorPane).toList().forEach(anchorPane1 -> anchorPane1.setVisible(false));
    }

    public static void checkComboBox(ComboBox comboBox, Label erroLabel) throws InputException{
        if (comboBox.getValue() == null){
            erroLabel.setText(InputErrorMessages.EMPTY_FIELD.getMessage());
            comboBox.setStyle("-fx-border-color: red;");
            throw new InputException(InputErrorMessages.EMPTY_FIELD.getMessage());
        }
    }

    /**
     * Method checks if table view is empty
     * @param tableView to check
     * @return boolean
     */
    public static boolean isTableViewEmpty(TableView tableView) {
        return tableView.getItems().isEmpty();
    }

    public static String capitalizeFirstLetter(String string){
        if (string == null || string.isEmpty()){
            return string;
        }
        String firstLetter = string.substring(0,1).toUpperCase();
        return firstLetter + string.substring(1);
    }

    /**
     * Adds padding to any component that extends Control
     * @param controls unlimited list of component
     */
    public static void addPadding(Control... controls) {
        List<Control> controlList = Arrays.asList(controls);
        controlList.forEach(control -> control.setStyle("-fx-padding: 5px"));
    }

    public static void checkStringLength(TextField equipmentnameTextField, Label nameErrorLabel) throws IllegalStringLengthException {
        if (equipmentnameTextField.getText().length() > 40){
            nameErrorLabel.setText("Text too long");
            nameErrorLabel.setStyle("-fx-background-color: red;");
            throw new IllegalStringLengthException("Text too long");
        }
    }
}
