package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocol;
import com.example.stefanovic.kemijskaindustrija.Model.SafetyProtocolStep;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface SafetyProtocolRepository{


    default SafetyProtocol getSafetyProtocolByIdWithSteps(long protocolId) throws Exception {
        SafetyProtocol safetyProtocol = null;

        try (Connection connection = DBController.connectToDatabase()) {
            safetyProtocol = fetchSafetyProtocolWithSteps(connection, protocolId);
        } catch (SQLException e) {
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

    default List<SafetyProtocolStep> getSafetyProtocolSteps(Long id){
        List<SafetyProtocolStep> safetyProtocolSteps = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DBController.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select  * from safety_protocol_step where safety_protocol_id = " + id);
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
            String desc = resultSet.getString("name");
            Boolean isCritical = resultSet.getBoolean("is_critical");
            safetyProtocolStep = new SafetyProtocolStep(id,desc,isCritical);
        }catch (SQLException e){
            //
        }
        return safetyProtocolStep;
    }

    default void safeSafetyProtocol(SafetyProtocol safetyProtocol) throws Exception{
        String SQL_INSERT = "INSERT INTO SAFETY_PROTOCOL  (name) VALUES(?)";
        Connection connection = DBController.connectToDatabase();
        connection.setAutoCommit(false);
        try (PreparedStatement preparedStatement =  connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)){
            exequteSafetyProtocolQuerry(preparedStatement, safetyProtocol);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int safetyProtocolId = generatedKeys.getInt(1);
                    safetyProtocol.setId((long) safetyProtocolId);
                    saveSafetyProtocolSteps(connection, safetyProtocol);
                } else {
                    throw new SQLException("Failed to retrieve generated ID for SafetyProtocol");
                }
            }
        }
        connection.commit();
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


    private static void exequteSafetyProtocolQuerry(PreparedStatement stmt, SafetyProtocol safetyProtocol) throws RuntimeException{
        try {
            stmt.setString(1, safetyProtocol.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    default void removeSafetyProtocol(long protocolId) throws Exception {
        try (Connection connection = DBController.connectToDatabase()) {
            connection.setAutoCommit(false);
            deleteSafetyProtocolSteps(connection, protocolId);
            deleteSafetyProtocol(connection, protocolId);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSingleProtocolStep(long protocolId) throws Exception {
        String deleteStepsQuery = "DELETE FROM safety_protocol_step WHERE id = ?";
        Connection connection = DBController.connectToDatabase();
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteStepsQuery)) {
            preparedStatement.setLong(1, protocolId);
            preparedStatement.executeUpdate();
        }
    }

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

    default void updateSafetyProtocol(SafetyProtocol safetyProtocol){

    }
}
