package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Files.ToSerializable;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Address;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;
import com.example.stefanovic.kemijskaindustrija.Threads.SerializeFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ChemicalRepository extends DataBaseRepository{
    Logger logger = LoggerFactory.getLogger(Main.class);

    default   Chemical getChemicalById(Long chem_id){
        Chemical chemical = null;
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement pstmt = connection.prepareStatement("select  * from chemical where id = ? ");
            pstmt.setLong(1, chem_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                chemical = getChemicalInfo(rs);
            }

            connection.close();
        } catch (Exception e) {
            logger.info("Error while trying to get chemical by id " + chem_id + " from database");
            logger.error(e.getMessage());
        }

        return chemical;
    }

    default List<Chemical> getChemicalList(){
        List<Chemical> chemicalList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DBController.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select  * from chemical");

            while (rs.next()){
                Chemical chemical = getChemicalInfo(rs);
                chemicalList.add(chemical);
            }

            connection.close();
        } catch (Exception e) {
            logger.info("Error while trying to get chemical list from database");
            logger.error(e.getMessage());
        }
        return chemicalList;
    }

    default Chemical getChemicalInfo(ResultSet rs){
        Chemical chemical = null;
        try{
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            Double quantity = rs.getDouble("quantity");
            String quantity_unit = rs.getString("quantity_unit");
            String instructions = rs.getString("instructions");
            BigDecimal danger_level = BigDecimal.valueOf(rs.getDouble("danger_level"));
            chemical = new Chemical(id,name,quantity,quantity_unit,instructions,danger_level);
        }catch (SQLException e){
            logger.info("Error while trying to get chemical information  from database");
            logger.error(e.getMessage());
        }
        return chemical;
    }
    default BigDecimal getAverageDangerLevel(){
        List<Chemical> chemicals = getChemicalList();
        BigDecimal totalDangerLevel = chemicals.stream()
                .map(Chemical::getDangerLevel)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalDangerLevel.divide(BigDecimal.valueOf(chemicals.size()), 2, BigDecimal.ROUND_HALF_UP);
    };


    default void saveChemical(Chemical chemical)  {
        if (chemical.getId() != null){
            SerializationRepository.prepareObjectForSerialization(getChemicalById(chemical.getId()));
            updateChemical(chemical);
            SerializationRepository.prepareObjectForSerialization(chemical);
        }else{
            saveToDatabase(chemical);
        }
    }


    @Override
    default <T> void saveToDatabase(T object)  {
        String SQL_INSERT = "INSERT INTO chemical (name, quantity, quantity_unit, instructions, danger_level) VALUES(?,?,?,?,?)";
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement(SQL_INSERT);
            exequteChemicalQuerry(stmt, (Chemical) object);
            stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            logger.info("Error while trying to save chemical entity to database");
            logger.error(e.getMessage());
        }
    }

    default void updateChemical(Chemical chemical) {
        String SQL_UPDATE = "update chemical set name = ?, quantity = ?, quantity_unit =?,  instructions = ?, danger_level = ? where ID = ? ";
        try {
            Connection con = DBController.connectToDatabase();
            PreparedStatement preparedStatement = con.prepareStatement(SQL_UPDATE);
            exequteChemicalQuerry(preparedStatement, chemical);
            preparedStatement.setLong(6, chemical.getId());
            preparedStatement.executeUpdate();
            con.close();
        } catch (Exception e) {
            logger.info("Error while trying to update chemical entity");
            logger.error(e.getMessage());
        }
    }

    static void exequteChemicalQuerry(PreparedStatement stmt, Chemical chemical)  {
        try {
            stmt.setString(1, chemical.getName());
            stmt.setDouble(2, chemical.getQuantity());
            stmt.setString(3,chemical.getQuantityUnit());
            stmt.setString(4, chemical.getInstructions());
            stmt.setDouble(5, chemical.getDangerLevel().doubleValue());
        } catch (SQLException e) {
            logger.info("Error while trying to execute chemical query");
            logger.error(e.getMessage());
        }
    }



}
