package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Authentication.Hashing;
import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Supplier;
import com.example.stefanovic.kemijskaindustrija.Model.User;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class DBController implements UserRepository, Hashing,  SupplierRepository{
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

    public static List<User> getAllUsers() {return UserRepository.getAllUsers();}

    public static void rewriteAllToTxtFile(){
        List<User> userList = getAllUsers();
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

    public static void linkChemicalAndSupplier(long chemical_id, long supplier_id) throws SaveToDataBaseException{
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO SUPPLIER_HAS_CHEMICAL   (supplier_id, chemical_id) VALUES(?,?)");
            stmt.setLong(1, supplier_id);
            stmt.setLong(2, chemical_id);
            stmt.executeUpdate();
            connection.close();

        } catch (Exception e) {
//            logger.error(e.getMessage());
            throw new SaveToDataBaseException("Couldn't save to DB");
        }
    }


}
