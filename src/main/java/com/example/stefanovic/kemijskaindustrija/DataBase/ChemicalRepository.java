package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Controllers.utils.Methods;
import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Address;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface ChemicalRepository extends DataBaseRepository{

    /**
     * Method gives back Chemical object from a single line in a file
     * @param line line from a file representing Chemical data
     * @return Chemical
     */
    static Chemical getChemicalObject(String line) {
        String[] split = line.split(" ");
        return new Chemical(Long.parseLong(split[0]), Methods.concatenateWithSpaces(split[1]),
                Double.valueOf(split[2]),split[3],Methods.concatenateWithUnderscore(split[4]),
                new BigDecimal(split[5]));
    }

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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            System.out.println(e.getMessage());
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
            SerializationRepository.writeToTxtFile(Main.CHEMICALS_FILE, getChemicalById(chemical.getId()));
            updateChemical(chemical);
            SerializationRepository.writeToTxtFile(Main.CHEMICALS_FILE, chemical);
            SerializationRepository.prepareChemicalsForSerialization();
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }



}
