package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Exception.SaveToDataBaseException;
import com.example.stefanovic.kemijskaindustrija.Exception.ServiceBookedForDateException;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.Chemical;
import com.example.stefanovic.kemijskaindustrija.Model.Equipment;
import com.example.stefanovic.kemijskaindustrija.Model.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public interface ServisRepository extends EquipmentRepository{
    Logger logger = LoggerFactory.getLogger(Main.class);

    static List<Service> getAllServices(){
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
            logger.info("Error while trying to get all service from database");
            logger.error(e.getMessage());
        }

        return services;
    }

    static Service getServiceInfo(ResultSet rs)  {
        Service service = new Service();
        try {
            service.setId(rs.getLong("id"));
            service.setTitle(rs.getString("title"));
            service.setDescription(rs.getString("description"));
            service.setDateOfService(rs.getDate("date_of_service").toLocalDate());
            Equipment equipment = EquipmentRepository.getEquipmentById(rs.getLong("equipment_id"));
            service.setEquipment(equipment);
        } catch (SQLException e) {
            logger.info("Error while trying to get service information from result set");
            logger.error(e.getMessage());
        }

        return service;
    }

    default void saveService(Service service) throws ServiceBookedForDateException {
        Equipment equipment = service.getEquipment();
        if (service.getId() != null){
            SerializationRepository.prepareObjectForSerialization(getServiceById(service.getId()));
            updateService(service);
            SerializationRepository.prepareObjectForSerialization(service);
            saveToDatabase(equipment);
        }
        else{
            if(isDateIsBooked(service.getDateOfService()))
            {
                throw new ServiceBookedForDateException("Termin rezerviran, izaberite drugi");
            }
            saveNewService(service);
            SerializationRepository.prepareObjectForSerialization(service);
            equipment.setInService(true);
            saveToDatabase(equipment);
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
            logger.info("Error while trying to get service by id: " + id);
            logger.error(e.getMessage());
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
            logger.info("Error while trying to see if a date is booked");
            logger.error(e.getMessage());
            return false;
        }
    }

    default void updateService(Service service) {
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
            logger.info("Error while trying to update service");
            logger.error(e.getMessage());
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
            logger.info("Error while trying to save new service to database");
            logger.error(e.getMessage());
        }
    }

}
