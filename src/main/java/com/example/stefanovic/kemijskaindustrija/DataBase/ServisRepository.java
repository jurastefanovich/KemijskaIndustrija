package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Exception.ServiceBookedForDateException;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import com.example.stefanovic.kemijskaindustrija.Model.Service;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public interface ServisRepository extends EquipmentRepository{

    static Service getServiceFromLine(String line) {
        String[] lines = line.split(" ");
        Equipment equipment = EquipmentRepository.getEquipmentById(Long.parseLong(lines[3]));
        return new Service(Long.parseLong(lines[0]), lines[1], lines[2],equipment, LocalDate.parse(lines[4], DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    default List<Service> getAllServices(){
        List<Service> services = new ArrayList<>();
        try {
            Connection connection = DBController.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select  * from service");

            while (rs.next()){
                Service service = getServiceInfo(rs);
                services.add(service);
            }

            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return services;
    }

    default Service getServiceInfo(ResultSet rs) throws Exception {
        Service service = new Service();
        service.setId(rs.getLong("id"));
        service.setTitle(rs.getString("title"));
        service.setDescription(rs.getString("description"));
        service.setDateOfService(rs.getDate("date_of_service").toLocalDate());
        Equipment equipment = EquipmentRepository.getEquipmentById(rs.getLong("equipment_id"));
        service.setEquipment(equipment);
        return service;
    }

    default void saveService(Service service) throws Exception {
        if (service.getId() != null){
            SerializationRepository.writeToTxtFile(Main.SERVICES_FILE, getServiceById(service.getId()));
            updateService(service);
            SerializationRepository.writeToTxtFile(Main.SERVICES_FILE, service);
            SerializationRepository.prepareServiceForSerialization();
        }
        else{
            if(isDateIsBooked(service.getDateOfService()))
            {
                throw new ServiceBookedForDateException("Termin rezerviran, izaberite drugi");
            }
            saveNewService(service);
            SerializationRepository.writeToTxtFile(Main.SERVICES_FILE, service);
        }
    }

    default Service getServiceById(Long id){
        String sql = "select  * from service where id = ?";
        Service service = null;
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1,id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                service =  getServiceInfo(rs);
            }
            connection.close();
        } catch (Exception e) {
            //ADD LOGGER
        }
        return service;
    }

    default boolean isDateIsBooked( LocalDate dateOfService)  {
        try {
            Connection connection = DBController.connectToDatabase();
            String sql = "SELECT * FROM service where date_of_service = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(dateOfService));
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    default void updateService(Service service) throws SQLException {
        String sql = "UPDATE service SET title = ?, description = ?, date_of_service = ?, equipment_id = ? WHERE id = ?";
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, service.getTitle());
            preparedStatement.setString(2, service.getDescription());
            preparedStatement.setDate(3, Date.valueOf(service.getDateOfService()));
            preparedStatement.setLong(4, service.getEquipment().getId());
            preparedStatement.setLong(5, service.getId());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private static void saveNewService(Service service) {
        String SQL_INSERT = "INSERT INTO service (title, description,date_of_service, equipment_id) VALUES(?, ?,?,?)";
        try {
            Connection connection = DBController.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
            preparedStatement.setString(1, service.getTitle());
            preparedStatement.setString(2, service.getDescription());
            preparedStatement.setDate(3, Date.valueOf(service.getDateOfService()));
            preparedStatement.setLong(4, service.getEquipment().getId());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
