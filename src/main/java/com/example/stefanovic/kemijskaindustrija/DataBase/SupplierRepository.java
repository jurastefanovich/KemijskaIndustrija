package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Model.Address;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;
import com.example.stefanovic.kemijskaindustrija.Model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface SupplierRepository extends DataBaseRepository {
    default Supplier getSupplierById(Long supplier_id){
        Supplier supplier = null;
        Connection connection = null;
        try {
            connection = DBController.connectToDatabase();
            PreparedStatement pstmt = connection.prepareStatement("select  * from supplier where id = ? ");
            pstmt.setLong(1, supplier_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                supplier = getSupplierInfo(rs);
            }
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return supplier;
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

    default void updateSupplier(Supplier supplier) throws SaveToDataBaseException{
        try{
            Connection connection = DBController.connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement("update supplier set name=?, city=?, street=?, street_number=? where id =?");
            exequteSupplierQuerry(stmt, supplier);
            stmt.setLong(5, supplier.getId());
            stmt.executeUpdate();
            connection.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new SaveToDataBaseException("Couldn't save to DB");
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

    @Override
    default <T> void saveToDatabase(T object) throws SaveToDataBaseException {
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO SUPPLIER  (name, city, street, street_number) VALUES(?,?,?,?)");
            exequteSupplierQuerry(stmt, (Supplier) object);
            stmt.executeUpdate();
            connection.close();

        } catch (Exception e) {
//            logger.error(e.getMessage());
            throw new SaveToDataBaseException("Couldn't save to DB");
        }
    }


    private static void exequteSupplierQuerry(PreparedStatement stmt, Supplier supplier) throws RuntimeException{
        try {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getAddress().city());
            stmt.setString(3, supplier.getAddress().street());
            stmt.setString(4, String.valueOf(supplier.getAddress().streetNumber()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
