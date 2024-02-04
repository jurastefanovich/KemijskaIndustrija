package com.example.stefanovic.kemijskaindustrija.Controllers;

import com.example.stefanovic.kemijskaindustrija.Exception.InputException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class Methods {
    /**
     * Method for re-setting a list of error labels
     * @param labels list of labels
     */
    public static void resetErorrs(Label... labels){
        List<Label> labelList = Arrays.stream(labels).toList();
        labelList.forEach(label -> label.setText(""));
    }

    public static void addPadding(Label ...labels){
        List<Label> labelList = Arrays.stream(labels).toList();
        labelList.forEach(label -> label.setPadding(new Insets(10, 0, 10, 5)));
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

    public static void checkTextArea(TextArea textArea, Label errorLabel) throws InputException{
        if (textArea.getText().isEmpty()){
            textArea.setStyle("-fx-border-color: red;");
            errorLabel.setText(InputErrorMessages.EMPTY_FIELD.getMessage());
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

    public static String getTextFieldString(TextField textField){
        return textField.getText();
    }
}
