package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Model.Address;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;
import com.example.stefanovic.kemijskaindustrija.Model.Supplier;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface ChemicalRepository extends DataBaseRepository{
    default Chemical getChemicalInfo(ResultSet rs){
        Chemical chemical = null;
        try{
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String quantity = rs.getString("quantity");
            String quantity_unit = rs.getString("quantity_unit");
//            Long supplier_id = rs.getLong("supplier_id");
//            Supplier supplier = SupplierRepository.getSupplierById(supplier_id);
            String instructions = rs.getString("instructions");
            BigDecimal danger_level = BigDecimal.valueOf(rs.getDouble("danger_level"));
//            chemical = new Chemical(id,name,quantity,quantity_unit,supplier,instructions,danger_level);
        }catch (SQLException e){
            //
        }
        return chemical;
    }

    default  Chemical getChemicalById(Long chem_id){
        Chemical chemical = null;
        Connection connection = null;
        try {
            connection = DBController.connectToDatabase();
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

    static void exequteChemicalQuerry(PreparedStatement stmt, Chemical chemical) throws RuntimeException{
        try {
            stmt.setString(1, chemical.getQuantity());
            stmt.setString(2, chemical.getQuantityUnit());
            stmt.setString(3, chemical.getInstructions());
            stmt.setDouble(4,Double.parseDouble(String.valueOf(chemical.getDangerLevel())));
            stmt.setString(5, chemical.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    default void updateChemical(Chemical chemical){
        try{
            Connection con = DBController.connectToDatabase();
            PreparedStatement preparedStatement = con.prepareStatement(
                    "update item set name = ?, quantity = ?, quantity_unit =?, " +
                    "supplier_id = ?, instructions = ?, danger_level = ? where ID = ? ");
            exequteChemicalQuerry(preparedStatement, chemical);
            preparedStatement.setLong(7, chemical.getId());
            preparedStatement.executeUpdate();
            con.close();

        } catch (RuntimeException e){
            //
        }catch (Exception e){
            //
        }
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
    default BigDecimal getAverageDangerLevel(){
        List<Chemical> chemicalList = getChemicalList();

        BigDecimal[] totalWithCount
                = chemicalList.stream()
                .filter(bd -> bd != null)
                .map(bd -> new BigDecimal[]{bd.getDangerLevel(), BigDecimal.ONE})
                .reduce((a, b) -> new BigDecimal[]{a[0].add(b[0]), a[1].add(BigDecimal.ONE)})
                .get();

        BigDecimal dangerLevel = totalWithCount[0].divide(totalWithCount[1], 10);

       return dangerLevel;
    };
    default List<Chemical> getSupplierChemicals(Long supplier_id){
        List<Chemical> chemicalList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DBController.connectToDatabase();
            PreparedStatement pstmt = connection.prepareStatement("select  * from chemical where supplier_id = ? ");
            pstmt.setLong(1, supplier_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                chemicalList.add(getChemicalInfo(rs));
            }

            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return chemicalList;
    }


    default List<Chemical> getAllChemicalForSupplier(long supplier_id){
        List<Chemical> chemicalList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DBController.connectToDatabase();
            PreparedStatement pstmt = connection.prepareStatement(
                    "select * from chemical " +
                    "join  SUPPLIER_HAS_CHEMICAL on chemical.id = SUPPLIER_HAS_CHEMICAL.chemical_id" +
                    "join supplier on supplier.id = SUPPLIER_HAS_CHEMICAL" +
                    "where supplier.id = ?  ");
            pstmt.setLong(1, supplier_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                chemicalList.add(getChemicalInfo(rs));
            }
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return chemicalList;
    }

    default int getNumberOfChemicalsOfSupplier(long supplier_id){
        return getAllChemicalForSupplier(supplier_id).size();
    }
    @Override
    default <T> void saveToDatabase(T object) throws SaveToDataBaseException {
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO chemical (quantity," +
                            "quantity_unit, instructions, danger_level, name)" +
                            " VALUES(?, ?,?,?,?)");
            exequteChemicalQuerry(stmt, (Chemical) object);
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            System.out.println("Result set");
            System.out.println(generatedKeys.next());
            connection.close();

        } catch (Exception e) {
            throw new SaveToDataBaseException(DataBaseMessages.SAVE_ERROR.getMessage());
        }
    }

    default List<Supplier> getSupplierList(){
        List<Supplier> suppliers = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DBController.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select  * from supplier");

            while (rs.next()){
                Supplier supplier = getSupplierInfo(rs);
                suppliers.add(supplier);
            }

            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return suppliers;
    }

    static Supplier getSupplierInfo(ResultSet rs){
        Supplier supplier = null;
        try{
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String city = rs.getString("city");
            String street = rs.getString("street");
            Integer street_number = rs.getInt("street_number");
            Address address = new Address(city,street,street_number);
//            List<Chemical> chemicalList = ChemicalRepository.getSupplierChemicals(id) ;
            supplier = new Supplier(name, id, address);

        }catch (SQLException e){
            //
        }
        return supplier;
    }
}
