package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Authentication.AccessLevel;
import com.example.stefanovic.kemijskaindustrija.Authentication.Account;
import com.example.stefanovic.kemijskaindustrija.Exception.AccountException;
import com.example.stefanovic.kemijskaindustrija.Model.User;
import org.slf4j.MDC;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    static List<User> getAllUsers(){
        List<User> userList = new ArrayList<>();
        try{
            Connection con = DBController.connectToDatabase();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from KORISNIK");
            while (rs.next()){
                User user  = getUserInfo(rs);
                userList.add(user);
            }
            con.close();
        } catch (Exception e) {
//            logger.error("Error getting users from DB!");
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
            System.out.println("Error getting user info");
            System.out.println(e.getMessage());
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
//            logger.error("Error getting single user with ID: " + userID);
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

    static boolean isUser(){return getLoggedInUser().getAccount().accessLevel() == AccessLevel.USER;}
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
//            logger.error(e.getMessage());
        } catch (Exception e ) {
//            logger.error(DataBaseMessages.ERROR_GETTING_SINGLE_USER.getMessage());
        }
        return loggedInUser;
    }

    static User getUserByEmail(String email){
        User user = null;
        try {
            Connection con = DBController.connectToDatabase();
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM KORISNIK WHERE EMAIL = ?");
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                user  = getUserInfo(rs);
            }
            con.close();
        } catch (Exception e) {
//            logger.error("Error getting logged in user");
        }
        return user;
    }

    static void createAccount(User user){
        try{
            Connection con = DBController.connectToDatabase();
            PreparedStatement pstmt = con.prepareStatement(
                    "insert into KORISNIK (name,last_name, date_of_birth,email,password, username,access_level) values(?,?,?,?,?,?,?)");
            pstmt.setString(1,user.getName());
            pstmt.setString(2,user.getLastName());
            pstmt.setDate(3, Date.valueOf(user.getDateOfBirth()));
            pstmt.setString(4, user.getAccount().email());
            pstmt.setString(5, user.getAccount().password());
            pstmt.setString(6,user.getAccount().userName());
            pstmt.setString(7, "USER");

            pstmt.executeUpdate();
            con.close();

        } catch (RuntimeException e){
            System.out.println("error serializing to file + " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new AccountException(DataBaseMessages.ERROR_CREATING_ACCOUNT.getMessage());

        }
    }

    static void updateUserInformation(User user, User selectedUser){
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
        }catch (RuntimeException e){
            System.out.println("Error serializing user file " + e.getMessage() );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
