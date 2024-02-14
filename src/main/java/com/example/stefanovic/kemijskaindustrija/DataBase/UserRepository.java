package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Authentication.AccessLevel;
import com.example.stefanovic.kemijskaindustrija.Authentication.Account;
import com.example.stefanovic.kemijskaindustrija.Authentication.AuthMessages;
import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.Exception.AccountException;
import com.example.stefanovic.kemijskaindustrija.Exception.EmailException;
import com.example.stefanovic.kemijskaindustrija.Exception.UsernameTakenException;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import com.example.stefanovic.kemijskaindustrija.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Logger logger = LoggerFactory.getLogger(Main.class);

    static List<User> getAllUsers(){
        List<User> userList = new ArrayList<>();
        try{
            Connection con = DBController.connectToDatabase();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from KORISNIK ");
            while (rs.next()){
                User user  = getUserInfo(rs);
                userList.add(user);
            }
            con.close();
        } catch (Exception e) {
            logger.error("Error getting users from DB!");
        }
        return userList;
    }
    static User getUserInfo(ResultSet rs){
        User user = null;
        try{
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String prezime = rs.getString("last_name");
            LocalDate datumRodjenja = rs.getDate("date_of_birth").toLocalDate();
            Account account =  AccountRepository.getSingleAccount(rs);
            var builder = new User.UserBuilder();
            builder.setAccount(account);
            builder.setLastName(prezime);
            builder.setName(name);
            builder.setDatOfBirth(datumRodjenja);
            builder.setId(id);
            user = builder.build();
        }catch (SQLException e){
            logger.info("Error while trying to get user information from database");
            logger.error(e.getMessage());
        }
        return user;
    }


    static Optional<User> getSingleUser(long userID)  {
        try {
            Connection con = DBController.connectToDatabase();
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM KORISNIK  WHERE ID = ?");
            pstmt.setLong(1, userID);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                return Optional.of(getUserInfo(rs));
            }
            con.close();
        } catch (Exception e) {
            logger.error("Error getting single user with ID: " + userID);
        }
        return Optional.empty();
    }

    static long getUserID() throws AccountException {
        if (MDC.get("userId") == null){
            throw new AccountException("No user has logged in");
        }
        return Long.parseLong(MDC.get("userId"));
    }
//    This can be deleted after testing delete entity method
    static void deleteUser(long userId){
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement("delete from KORISNIK  where ID = ?");
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    static boolean isUser() throws NullPointerException{
        return getLoggedInUser().getAccount().accessLevel() == AccessLevel.USER;
    }
    static boolean isAdmin(){
        return !isUser();
    }
    static User getLoggedInUser(){
        User loggedInUser = null;
        try {
            Connection con = DBController.connectToDatabase();
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM KORISNIK  WHERE ID = ?");
            pstmt.setLong(1, getUserID());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                loggedInUser  = getUserInfo(rs);
            }
            con.close();

        }catch (AccountException e) {
            logger.error(e.getMessage());
        } catch (Exception e ) {
            logger.error(DataBaseMessages.ERROR_GETTING_SINGLE_USER.getMessage());
        }
        return loggedInUser;
    }

    /**
     * Method checks if a username is already stored in a database for any user that isn't current
     * @param username username we want to check
     * @param user_id id of the user we want to exclude from the search
     */
    static void checkIfUsernamCanBeEdited(String username, Long user_id) throws Exception {
        String sqlQuery = "SELECT * FROM KORISNIK WHERE username = ? AND id <> ?";
        Connection connection = DBController.connectToDatabase();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1,username);
        preparedStatement.setLong(2,user_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            throw new UsernameTakenException(AuthMessages.USERNAME_TAKEN.getMessage());
        }
    }

    /**
     * Method checks if an email is already stored in a database for any user that isn't current
     * @param email email we want to check
     * @param user_id id of the user we want to exclude from the search
     */
    static void checkIfEmailCanBeEdited(String email, Long user_id) throws EmailException {
        String sqlQuery = "SELECT * FROM KORISNIK WHERE email = ? AND id <> ?";
        try{
            Connection connection = DBController.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,email);
            preparedStatement.setLong(2,user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                throw new EmailException(AuthMessages.EMAIL_TAKEN.getMessage());
            }
        }catch (Exception e) {
            logger.info("Error while trying checking if an email can be edited");
            logger.error(e.getMessage());
        }
    }

    static void createAccount(User user){
        String sqlQuery = "insert into KORISNIK (name,last_name, date_of_birth,email,password, username,access_level) values(?,?,?,?,?,?,?)";
        try{
            Connection con = DBController.connectToDatabase();
            PreparedStatement pstmt = con.prepareStatement(sqlQuery);
            pstmt.setString(1,user.getName());
            pstmt.setString(2,user.getLastName());
            pstmt.setDate(3, Date.valueOf(user.getDateOfBirth()));
            pstmt.setString(4, user.getAccount().email());
            pstmt.setString(5, user.getAccount().password());
            pstmt.setString(6,user.getAccount().userName());
            pstmt.setString(7, String.valueOf(user.getAccount().accessLevel()));

            pstmt.executeUpdate();
            con.close();
            SerializationRepository.prepareObjectForSerialization(user);
        } catch (RuntimeException e){
            logger.info("Error while trying serialize user to file");
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.info("Error while trying to create a new account");
            logger.error(e.getMessage());

        }
    }

    static boolean isUserPressent(String email){
        try{
            Connection connection = DBController.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from korisnik where email = ?");
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        }catch (Exception e){
            logger.info("Error while trying to see if a user is present in a database");
            logger.error(e.getMessage());
            return false;
        }
    }

    static void updateUserInformation(User user)  {
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement("update KORISNIK set " +
                    "name = ?, " +
                    "last_name = ?, " +
                    "date_of_birth =?, " +
                    "email = ?, " +
                    "password = ?, " +
                    "username = ?, " +
                    "access_level = ? " +
                    "where ID = ? ");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setDate(3, Date.valueOf(user.getDateOfBirth()));
            preparedStatement.setString(4, user.getAccount().email());
            preparedStatement.setString(5, user.getAccount().password());
            preparedStatement.setString(6, user.getAccount().userName());
            preparedStatement.setString(7, String.valueOf(user.getAccount().accessLevel()));
            preparedStatement.setLong(8, user.getId());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            logger.info("Error while trying to update user information");
            logger.error(e.getMessage());
        }

    }


}
