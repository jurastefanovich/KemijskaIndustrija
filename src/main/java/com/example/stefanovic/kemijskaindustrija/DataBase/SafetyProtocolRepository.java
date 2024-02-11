package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocol;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocolStep;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface SafetyProtocolRepository{


    static SafetyProtocol getSafetyProtocolFromString(String line) {
        String[] lines = line.split(" ");
        String[] name = new String[lines.length-1];
        for(int i = 1; i< lines.length;i++){
            name[i-1] = lines[i];
        }
        String concatenatedString = String.join(" ", name);
        return  new SafetyProtocol(Long.valueOf(lines[0]), concatenatedString);
    }

    static SafetyProtocolStep getSafetyProtocolStepFromString(String line) {
        String[] lines = line.split(" ");
        String[] description = new String[lines.length-2];
        for(int i = 1; i< lines.length-1;i++){
            description[i-1] = lines[i];
        }
        String concatenatedString = String.join(" ", description);
        return new SafetyProtocolStep(Long.valueOf(lines[0]), concatenatedString, Boolean.valueOf(lines[2]));
    }

    default SafetyProtocol getSafetyProtocolByIdWithSteps(long protocolId)  {
        SafetyProtocol safetyProtocol = null;
        try (Connection connection = DBController.connectToDatabase()) {
            safetyProtocol = fetchSafetyProtocolWithSteps(connection, protocolId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return safetyProtocol;
    }

    private SafetyProtocol fetchSafetyProtocolWithSteps(Connection connection, long protocolId) throws SQLException {
        SafetyProtocol safetyProtocol = null;

        String selectQuery = "SELECT * FROM safety_protocol p JOIN safety_protocol_step s ON p.id = s.safety_protocol_id WHERE p.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, protocolId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    if (safetyProtocol == null) {
                        safetyProtocol = new SafetyProtocol();
                        safetyProtocol.setId(resultSet.getLong("id"));
                        safetyProtocol.setName(resultSet.getString("name"));
                    }

                    SafetyProtocolStep step = new SafetyProtocolStep();
                    step.setId(resultSet.getLong("id"));
                    step.setDescription(resultSet.getString("description"));
                    step.setCritical(resultSet.getBoolean("is_critical"));

                    safetyProtocol.addProtocolStep(step);
                }
            }
        }

        return safetyProtocol;
    }

    default List<SafetyProtocol> getAllSafetyProcotols(){
        List<SafetyProtocol> safetyProtocols = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DBController.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select  * from safety_protocol");

            while (rs.next()){
                SafetyProtocol safetyProtocol = getSafetyProtocolInfo(rs);
                safetyProtocols.add(safetyProtocol);
            }
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return safetyProtocols;
    }
    default SafetyProtocol getSafetyProtocolInfo(ResultSet resultSet){
        SafetyProtocol safetyProtocol = null;
        try{
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            List<SafetyProtocolStep> safetyProtocolSteps = getSafetyProtocolSteps(id);
            safetyProtocol = new SafetyProtocol(id,name,safetyProtocolSteps);

        }catch (SQLException e){
            //
        }
        return safetyProtocol;
    }
    default SafetyProtocolStep getSafetyProtocolStepById(Long id){
        String query = "SELECT * FROM SAFETY_PROTOCOL_STEP where id =  ?";
        SafetyProtocolStep safetyProtocol = null;
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1,id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                safetyProtocol = getSafetyProtocolStep(rs);
            }
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return safetyProtocol;
    }

    default List<SafetyProtocolStep> getSafetyProtocolSteps(Long id){
        List<SafetyProtocolStep> safetyProtocolSteps = new ArrayList<>();
        String query = "select  * from safety_protocol_step where safety_protocol_id = ?";
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1,id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                SafetyProtocolStep safetyProtocol = getSafetyProtocolStep(rs);
                safetyProtocolSteps.add(safetyProtocol);
            }
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return safetyProtocolSteps;
    }

    default SafetyProtocolStep getSafetyProtocolStep(ResultSet resultSet){
        SafetyProtocolStep safetyProtocolStep = null;
        try{
            Long id = resultSet.getLong("id");
            String desc = resultSet.getString("description");
            Boolean isCritical = resultSet.getBoolean("is_critical");
            return new SafetyProtocolStep(id,desc,isCritical);
        }catch (SQLException e){
            e.printStackTrace();
            //ADD LOGGER
        }
        return safetyProtocolStep;
    }

    default void safeSafetyProtocol(SafetyProtocol safetyProtocol) {
        String SQL_INSERT = "INSERT INTO SAFETY_PROTOCOL  (name) VALUES(?)";
        try {
            Connection connection = DBController.connectToDatabase();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement =  connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            exequteSafetyProtocolQuerry(preparedStatement, safetyProtocol);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int safetyProtocolId = generatedKeys.getInt(1);
                    safetyProtocol.setId((long) safetyProtocolId);
                    saveSafetyProtocolSteps(connection, safetyProtocol);
                } else {
                    //ADD LOGGER failed to get generated keys for safety_protocols
                }
            }

            connection.commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    default void updateSafetyProcolName(SafetyProtocol safetyProtocol) {
        SafetyProtocol safetyProtocol1 = getSafetyProtocolByIdWithSteps(safetyProtocol.getId());
        SerializationRepository.writeToTxtFile(Main.SAFETY_PROTOCOL_FILE, safetyProtocol1);
        String sqlQuery = "UPDATE safety_protocol SET name = ? where id =?";
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, safetyProtocol.getName());
            preparedStatement.setLong(2, safetyProtocol.getId());
            preparedStatement.executeUpdate();
            SerializationRepository.writeToTxtFile(Main.SAFETY_PROTOCOL_FILE, safetyProtocol);
        } catch (Exception e) {
//            throw new RuntimeException(e);
            //Add logger
            System.err.println(e.getMessage());
        }
    }

    private void saveSafetyProtocolSteps(Connection connection, SafetyProtocol safetyProtocol) throws SQLException {
        String insertStepQuery = "INSERT INTO safety_protocol_step (safety_protocol_id, description, is_critical) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStepQuery)) {
            for (SafetyProtocolStep step : safetyProtocol.getSteps()) {
                preparedStatement.setLong(1, safetyProtocol.getId());
                preparedStatement.setString(2, step.getDescription());
                preparedStatement.setBoolean(3, step.getCritical());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    default void addNewSafetyProtocolStep(String desc, boolean isCritical, Long safetyProtocolId){
        String insertStepQuery = "INSERT INTO safety_protocol_step (safety_protocol_id, description, is_critical) VALUES (?, ?, ?)";
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStepQuery);
            preparedStatement.setLong(1,safetyProtocolId);
            preparedStatement.setString(2,desc);
            preparedStatement.setBoolean(3,isCritical);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            //ADD LOGGER
            throw new RuntimeException(e);
        }

    }

    private static void exequteSafetyProtocolQuerry(PreparedStatement stmt, SafetyProtocol safetyProtocol) throws RuntimeException{
        try {
            stmt.setString(1, safetyProtocol.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    default void removeSafetyProtocol(long protocolId) {
        try (Connection connection = DBController.connectToDatabase()) {
            connection.setAutoCommit(false);
            deleteSafetyProtocolSteps(connection, protocolId);
            deleteSafetyProtocol(connection, protocolId);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Used when removing a step from an already saved list
     * @param protocolId
     */
    default void deleteSingleProtocolStep(long protocolId)  {
        String deleteStepsQuery = "DELETE FROM safety_protocol_step WHERE id  = ?";
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteStepsQuery);
            preparedStatement.setLong(1, protocolId);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //ADD LOGGER
        }

    }

    /**
     * Used when deleting a safety protocol so we need to delete of it's assosiated steps
     * @param connection
     * @param protocolId
     * @throws SQLException
     */
    private void deleteSafetyProtocolSteps(Connection connection, long protocolId) throws SQLException {
        String deleteStepsQuery = "DELETE FROM safety_protocol_step WHERE safety_protocol_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteStepsQuery)) {
            preparedStatement.setLong(1, protocolId);
            preparedStatement.executeUpdate();
        }
    }

    private void deleteSafetyProtocol(Connection connection, long protocolId) throws SQLException {
        String deleteProtocolQuery = "DELETE FROM safety_protocol WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteProtocolQuery)) {
            preparedStatement.setLong(1, protocolId);
            preparedStatement.executeUpdate();
        }
    }

    default void updateSafetyProtocolStep(String description, boolean isCritical, Long id){
        SafetyProtocolStep oldStep = getSafetyProtocolStepById(id);
        SerializationRepository.writeToTxtFile(Main.SAFETY_PROTOCOL_STEP_FILE,oldStep);

        String updateString = "UPDATE SAFETY_PROTOCOL_STEP  SET description = ?, is_critical = ? where id = ?";
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(updateString);
            preparedStatement.setString(1, description);
            preparedStatement.setBoolean(2, isCritical);
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();
            connection.close();
            SerializationRepository.writeToTxtFile(Main.SAFETY_PROTOCOL_STEP_FILE, new SafetyProtocolStep(id, description, isCritical));

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
