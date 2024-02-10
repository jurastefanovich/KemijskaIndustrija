package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Authentication.Hashing;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.User;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class DBController implements UserRepository, Hashing{
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

    public static void deleteEntity(Long entity_id, String table_name) throws Exception {
        Connection connection = DBController.connectToDatabase();
        PreparedStatement deleteStudent = connection.prepareStatement("delete from " + table_name +" where id = ?");
        deleteStudent.setString(1, String.valueOf(entity_id));
        deleteStudent.executeUpdate();
        connection.close();
    }

    public static void rewriteAllToTxtFile(){
        List<User> userList = UserRepository.getAllUsers();
        try(BufferedWriter bf = new BufferedWriter(new FileWriter(Main.LOGIN_FILE))){
            userList.forEach(user -> {
                try {
                    bf.write(user.getAccount().email() + '\n');
                    String password = Hashing.hashPassword(user.getAccount().password());
                    bf.write(password+ '\n');
                } catch (IOException | NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            });
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
