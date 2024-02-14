package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import com.example.stefanovic.kemijskaindustrija.Model.EquipmentTypes.Glassware;
import com.example.stefanovic.kemijskaindustrija.Model.EquipmentTypes.Miscellaneous;
import com.example.stefanovic.kemijskaindustrija.Model.EquipmentTypes.TitranosEquipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface EquipmentRepository  {

    Logger logger = LoggerFactory.getLogger(Main.class);

    static Equipment getEquipmentById(long id) {
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
            logger.info("Error while trying to get equipment by ID: " + id + " from database");
            logger.error(e.getMessage());
        }
        return equipment;
    }
    default List<Equipment> getAllEquipmenet() {
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
            logger.info("Error while trying to get equipment list from database");
            logger.error(e.getMessage());
        }

        return chemicalList;
    }
    static Equipment getEquipmentInfo(ResultSet rs) {
        Equipment equipment = null;
        try {
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            String type = rs.getString("type");
            Double health = rs.getDouble("health");
            Boolean isInService = rs.getBoolean("is_in_service");
            equipment = new Equipment(id, name, description, type,health,isInService);

            if (equipment.getHealthBar() == null){
                equipment.setHealthBar(100.0);
            }
        } catch (SQLException e) {
            logger.info("Error while trying to get equipment list from database");
            logger.error(e.getMessage());
        }

        return equipment;
    }

    default void saveToDatabase(Equipment equipment){
        if (equipment.getId() != null){
            SerializationRepository.prepareObjectForSerialization(getEquipmentById(equipment.getId()));
            updateEquipment(equipment);
            SerializationRepository.prepareObjectForSerialization(equipment);
        }
        else{
            saveNewEquipment(equipment);
        }
    }

    default void updateEquipment(Equipment equipment) {
        String sql = "UPDATE equipment SET name = ?, description = ?, type = ?, health = ?, is_in_service=? WHERE id = ?";
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, equipment.getName());
            preparedStatement.setString(2, equipment.getDescription());
            preparedStatement.setString(3, String.valueOf(equipment.getType()));
            preparedStatement.setDouble(4, equipment.getHealthBar());
            preparedStatement.setBoolean(5, equipment.getInService());
            preparedStatement.setLong(6, equipment.getId() );

            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            logger.info("Error while trying to update equipment entity");
            logger.error(e.getMessage());
        }

    }

    private static void saveNewEquipment(Equipment equipment)  {
        String SQL_INSERT = "INSERT INTO equipment (name, description,type, health) VALUES(?, ?,?,?)";
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement(SQL_INSERT);
            exequteEquipmentQuerry(stmt, equipment);
            stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            logger.info("Error while trying to save equipment entity");
            logger.error(e.getMessage());
        }
    }
    private static void exequteEquipmentQuerry(PreparedStatement stmt, Equipment equipment) {
        try {
            stmt.setString(1, equipment.getName());
            stmt.setString(2, equipment.getDescription());
            stmt.setString(3, String.valueOf(equipment.getType()));
            stmt.setDouble(4, 100.0);
        } catch (SQLException e) {
            logger.info("Error while trying to execute equipment entity query");
            logger.error(e.getMessage());
        }
    }

    default void deleteEquipmentFromDB(long equipmentId)  {
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement deleteService = connection.prepareStatement("DELETE FROM service WHERE EQUIPMENT_ID = ?");
            PreparedStatement deleteEquipment = connection.prepareStatement("DELETE FROM equipment WHERE ID = ?");
            connection.setAutoCommit(false);

            deleteService.setLong(1, equipmentId);
            deleteService.executeUpdate();

            deleteEquipment.setLong(1, equipmentId);
            deleteEquipment.executeUpdate();

            connection.commit();
        } catch (Exception e) {
            logger.info("Error while trying to delete equipment entity");
            logger.error(e.getMessage());
        }
    }

    static List<String> getAllEquipmentTypes() {
        List<String> equipmentTypes = new ArrayList<>();
        Glassware[] glassware = Glassware.values();
        Miscellaneous[] miscellaneous = Miscellaneous.values();
        TitranosEquipment[] titranosEquipments = TitranosEquipment.values();

        equipmentTypes.addAll(Arrays.stream(glassware).map(Enum::toString).collect(Collectors.toSet()));
        equipmentTypes.addAll(Arrays.stream(miscellaneous).map(Enum::toString).collect(Collectors.toSet()));
        equipmentTypes.addAll(Arrays.stream(titranosEquipments).map(Enum::toString).collect(Collectors.toSet()));

        return equipmentTypes;
    }
}
