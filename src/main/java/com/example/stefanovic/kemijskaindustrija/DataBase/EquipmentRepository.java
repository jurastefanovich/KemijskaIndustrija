package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import com.example.stefanovic.kemijskaindustrija.Model.EquipmentType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface EquipmentRepository  {
    default Equipment getEquipmentById(long id) throws Exception{
        Equipment equipment = null;
        try {
            Connection con = DBController.connectToDatabase();
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM EQUIPMENT  WHERE ID = ?");
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                equipment  = getEquipmentInfo(rs);
            }
            con.close();
        } catch (Exception e) {
            throw new Exception(DataBaseMessages.EQUIPMENT_ID_DB_ERROR.getMessage());
        }
        return equipment;
    }
    default List<Equipment> getAllEquipmenet() throws Exception {
        List<Equipment> chemicalList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DBController.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select  * from equipment");
            while (rs.next()){
                Equipment chemical = getEquipmentInfo(rs);
                chemicalList.add(chemical);
            }
            connection.close();
        } catch (Exception e) {
            throw new Exception(DataBaseMessages.ERROR_GETTING_EQUIPMENT.getMessage());
        }

        return chemicalList;
    }
    private Equipment getEquipmentInfo(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        EquipmentType type = EquipmentType.valueOf(rs.getString("type"));
        return new Equipment(id, name, description, type);
    }

    default void saveToDatabase(Equipment equipment) throws Exception{
        if (equipment.getId() != null){
            updateEquipment( equipment);
        }
        else{
            saveNewEquipment(equipment);
        }
    }

    default void updateEquipment(Equipment equipment) {
        String sql = "UPDATE equipment SET name = ?, description = ?, type = ? WHERE id = ?";
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, equipment.getName());
            preparedStatement.setString(2, equipment.getDescription());
            preparedStatement.setString(3, String.valueOf(equipment.getType()));
            preparedStatement.setLong(4, equipment.getId() );
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        SerializationRepository.writeToTxtFile(Main.EQUIPMENT_FILE, equipment);
    }

    private static void saveNewEquipment(Equipment equipment)  {
        String SQL_INSERT = "INSERT INTO equipment (name, description,type) VALUES(?, ?,?)";
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement(SQL_INSERT);
            exequteEquipmentQuerry(stmt, equipment);
            stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        SerializationRepository.writeToTxtFile(Main.EQUIPMENT_FILE, equipment);

    }
    private static void exequteEquipmentQuerry(PreparedStatement stmt, Equipment equipment) throws SQLException{
        stmt.setString(1, equipment.getName());
        stmt.setString(2, equipment.getDescription());
        stmt.setString(3, String.valueOf(equipment.getType()));
    }

    default void deleteEquipmentFromDB(long equipmentId) throws Exception {
        Connection connection = DBController.connectToDatabase();
        PreparedStatement deleteService = connection.prepareStatement("DELETE FROM service WHERE EQUIPMENT_ID = ?");
        PreparedStatement deleteEquipment = connection.prepareStatement("DELETE FROM equipment WHERE ID = ?");
        connection.setAutoCommit(false);

        deleteService.setLong(1, equipmentId);
        deleteService.executeUpdate();

        deleteEquipment.setLong(1, equipmentId);
        deleteEquipment.executeUpdate();

        connection.commit();
    }
}
