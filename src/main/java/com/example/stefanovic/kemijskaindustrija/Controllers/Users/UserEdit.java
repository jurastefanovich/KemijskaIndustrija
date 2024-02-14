package com.example.stefanovic.kemijskaindustrija.Controllers.Users;

import com.example.stefanovic.kemijskaindustrija.Authentication.AccessLevel;
import com.example.stefanovic.kemijskaindustrija.Authentication.Account;
import com.example.stefanovic.kemijskaindustrija.Authentication.AuthInput;
import com.example.stefanovic.kemijskaindustrija.Controllers.AuthControllers.SignUp;
import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.DataBase.SerializationRepository;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.*;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.w3c.dom.CDATASection;

import java.util.List;
import java.util.Optional;

public class UserEdit implements UserRepository, UserFunctionlities{
    @FXML
    public AnchorPane acceessLevelAnchorPane;
    @FXML
    public AnchorPane confirmPasswordAnchorPane;
    @FXML
    public AnchorPane passwordAnchorPane;
    @FXML
    public TextField confirmPasswordTextField;
    @FXML
    public Label emailErrorLabel;
    @FXML
    public TextField emailTextField;
    @FXML
    public Label lastNameErrorLabel;
    @FXML
    public TextField lastNameTextField;
    @FXML
    public Label nameErrorLabel;
    @FXML
    public TextField nameTextField;
    @FXML
    public Label passwordErrorLabel;
    @FXML
    public TextField passwordTextField;
    @FXML
    public ComboBox<AccessLevel> accessLevelComboBox;
    @FXML
    public Label title;
    @FXML
    private Label accessLevelErrorLabel;
    @FXML
    public Label userNameErrorLabel;
    @FXML
    public TextField userNameTextField;

    @FXML
    private Label confirmPasswordErrorLabel;
    private User user;

    @FXML
    void initialize(){
        User loggedInUser = UserRepository.getLoggedInUser();
        resetLabels();
        setInformation(UserRepository.getLoggedInUser());
        checkIfEditingLoggedInUser(this.user.getId());
    }

    public void setInformation(User user) {
        accessLevelComboBox.setItems(FXCollections.observableArrayList(List.of(AccessLevel.values())));
        resetLabels();
        this.user = user;
        setVisibleAnchors(this.user.getId());
        emailTextField.setText(user.getAccount().email());
        passwordTextField.setText(user.getAccount().password());
        confirmPasswordTextField.setText(user.getAccount().password());
        userNameTextField.setText(user.getAccount().userName());
        nameTextField.setText(user.getName());
        lastNameTextField.setText(user.getLastName());
        accessLevelComboBox.setPromptText(String.valueOf(user.getAccount().accessLevel()));

    }

    private void setVisibleAnchors(Long user_id) {
        acceessLevelAnchorPane.setVisible(UserRepository.isAdmin());
        passwordAnchorPane.setVisible(checkIfEditingLoggedInUser(user_id));
        confirmPasswordAnchorPane.setVisible(checkIfEditingLoggedInUser(user_id));
    }

    private void resetLabels(){
        Methods.resetErorrs(emailErrorLabel,nameErrorLabel,lastNameErrorLabel,userNameErrorLabel,passwordErrorLabel,confirmPasswordErrorLabel,accessLevelErrorLabel);
        Methods.resetOutlines(emailTextField,nameTextField,lastNameTextField,userNameTextField,passwordTextField,confirmPasswordTextField);
    }



    private void checkIsAdminChangingAccessLevel(AccessLevel originalAccessLevel, AccessLevel updatedAccessLevel, boolean isOwnProfile) throws IllegalAccessLevelChangeException{
        if (originalAccessLevel.equals(AccessLevel.ADMIN) && updatedAccessLevel.equals(AccessLevel.USER) && isOwnProfile){
            throw new IllegalAccessLevelChangeException("An admin can't change his own access level");
        }
    }

    @FXML
    private void saveUpdatedUser(){
        resetLabels();
        Optional<User> original = UserRepository.getSingleUser(this.user.getId());
        if (original.isPresent()){
            String email = emailTextField.getText();
            String userName = userNameTextField.getText();
            String password = passwordTextField.getText();
            String confirmPassword = confirmPasswordTextField.getText();
            String firstName = nameTextField.getText();
            String lastName = lastNameTextField.getText();
            AccessLevel accessLevel = getAccessLevel();
            authenticate(email,userName,password,confirmPassword,firstName,lastName,original.get().getAccount().accessLevel(),accessLevel,original);


        }
    }
    private AccessLevel getAccessLevel(){
        if(accessLevelComboBox.getValue() == null){
            return this.user.getAccount().accessLevel();
        }
        return accessLevelComboBox.getValue();
    }
    private void authenticate(String email, String userName, String password, String confirmPassword,
                              String firstName, String lastName, AccessLevel oldAccessLevel,
                              AccessLevel newAccessLevel,Optional<User> original) {
        resetLabels();
        try {
            checkIfFieldAreEmpty();
            UserRepository.checkIfEmailCanBeEdited(email, this.user.getId());
            AuthInput.checkEmailAddress(email);
            UserRepository.checkIfUsernamCanBeEdited(userName, this.user.getId());
            String confirmationPassword = confirmPassword.isEmpty() ? original.get().getAccount().password() : confirmPassword;
            AuthInput.checkPassword(password,confirmationPassword);
            checkIsAdminChangingAccessLevel(oldAccessLevel, newAccessLevel, checkIfEditingLoggedInUser(this.user.getId()));
            Account account = new Account(email, password, userName, newAccessLevel);
            User updated = new User(original.get().getId(), firstName,lastName, original.get().getDateOfBirth(), account);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to update this user?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response->{
                if ( response.getButtonData().equals(ButtonBar.ButtonData.YES))
                {
                    Optional<User> optionalUser = UserRepository.getSingleUser(updated.getId());
                    Account oldAccount = new Account(optionalUser.get().getAccount().email(), optionalUser.get().getAccount().password(), optionalUser.get().getAccount().userName(),  optionalUser.get().getAccount().accessLevel());
                    User oldUser = new User(optionalUser.get().getId(), optionalUser.get().getName(), optionalUser.get().getLastName(), optionalUser.get().getDateOfBirth(),oldAccount );

                    SerializationRepository.prepareObjectForSerialization(oldUser);
                    UserRepository.updateUserInformation(updated);
                    SerializationRepository.prepareObjectForSerialization(updated);
                }
            });

        } catch (EmailException e){
            emailErrorLabel.setText(e.getMessage());
        }catch (PasswordException e){
            passwordErrorLabel.setText(e.getMessage());
        }catch (IllegalAccessLevelChangeException e){
            accessLevelErrorLabel.setText(e.getMessage());
        }catch (UsernameTakenException e) {
            userNameErrorLabel.setText(e.getMessage());
        }catch (InputException | NullPointerException e) {
            logger.info("Exception occurred in the user edit view");
            logger.error(e.getMessage());
        }

    }

    private void checkIfFieldAreEmpty() throws InputException {
        Methods.checkTextField(userNameTextField, userNameErrorLabel);
        Methods.checkTextField(emailTextField, emailErrorLabel);
        Methods.checkTextField(passwordTextField, passwordErrorLabel);
        Methods.checkTextField(nameTextField, nameErrorLabel);
        Methods.checkTextField(lastNameTextField, lastNameErrorLabel);
    }

}
