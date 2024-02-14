package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Authentication.Hashing;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class DBController implements UserRepository, Hashing{
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    protected static Connection connectToDatabase() throws Exception{
        Properties dbConfiguration = new Properties();
        dbConfiguration.load(new FileInputStream("dat/database.properties"));
        Connection con = DriverManager.getConnection(
                dbConfiguration.getProperty("dbURL"),
                dbConfiguration.getProperty("username"),
                dbConfiguration.getProperty("password")
        );
        return con;
    }

    public static void deleteEntity(Long entity_id, String table_name)  {
        try{
            Connection connection = DBController.connectToDatabase();
            PreparedStatement deleteStudent = connection.prepareStatement("delete from " + table_name +" where id = ?");
            deleteStudent.setString(1, String.valueOf(entity_id));
            deleteStudent.executeUpdate();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Error trying to delete " + table_name + " with ID: " + entity_id);
            //ADD LOGGER
        }

    }

    public static void rewriteAllToTxtFile(){
        List<User> userList = UserRepository.getAllUsers();
        try(BufferedWriter bf = new BufferedWriter(new FileWriter(Main.LOGIN_FILE))){
            userList.forEach(user -> {
                try {
                    bf.write(user.getAccount().email() + '\n');
                    String password = Hashing.hashPassword(user.getAccount().password());
                    bf.write(password+ '\n');
                } catch (IOException | NoSuchAlgorithmException | NullPointerException e) {
                    logger.error("An error occurred while trying to get account information " + e.getMessage());
                }
            });
        }catch (IOException e) {
            logger.error("An error occurred while trying to rewrite all accounts to file " + e.getMessage());
        }
    }

}
