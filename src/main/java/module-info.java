module com.example.stefanovic.kemijskaindustrija {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires org.controlsfx.controls;


    exports com.example.stefanovic.kemijskaindustrija.Main;
    exports com.example.stefanovic.kemijskaindustrija.Controllers.AuthControllers;
    exports com.example.stefanovic.kemijskaindustrija.Controllers.Chemical;
    exports com.example.stefanovic.kemijskaindustrija.Controllers.Users;
    exports com.example.stefanovic.kemijskaindustrija.Controllers.Equipment;
    exports com.example.stefanovic.kemijskaindustrija.DataBase;
    exports com.example.stefanovic.kemijskaindustrija.Authentication;
    exports com.example.stefanovic.kemijskaindustrija.Exception;

    exports com.example.stefanovic.kemijskaindustrija.Model;

    opens com.example.stefanovic.kemijskaindustrija.DataBase to javafx.fxml;
    opens com.example.stefanovic.kemijskaindustrija.Main to javafx.fxml;
    opens com.example.stefanovic.kemijskaindustrija.Authentication to javafx.fxml;
    opens com.example.stefanovic.kemijskaindustrija.Exception to javafx.fxml;
    opens com.example.stefanovic.kemijskaindustrija.Model to javafx.fxml;
    opens com.example.stefanovic.kemijskaindustrija.Controllers.AuthControllers to javafx.fxml;
    opens com.example.stefanovic.kemijskaindustrija.Controllers.Chemical to javafx.fxml;
    opens com.example.stefanovic.kemijskaindustrija.Controllers.Users to javafx.fxml;
    exports com.example.stefanovic.kemijskaindustrija.Controllers.SafetyProcotol;
    opens com.example.stefanovic.kemijskaindustrija.Controllers.SafetyProcotol to javafx.fxml;
    opens com.example.stefanovic.kemijskaindustrija.Controllers.Equipment to javafx.fxml;
    exports com.example.stefanovic.kemijskaindustrija.Controllers.HomePage;
    opens com.example.stefanovic.kemijskaindustrija.Controllers.HomePage to javafx.fxml;
    exports com.example.stefanovic.kemijskaindustrija.Controllers.Navigation;
    opens com.example.stefanovic.kemijskaindustrija.Controllers.Navigation to javafx.fxml;
    exports com.example.stefanovic.kemijskaindustrija.Controllers.utils;
    opens com.example.stefanovic.kemijskaindustrija.Controllers.utils to javafx.fxml;
    exports com.example.stefanovic.kemijskaindustrija.Controllers.Servis;
    opens com.example.stefanovic.kemijskaindustrija.Controllers.Servis to javafx.fxml;
    exports com.example.stefanovic.kemijskaindustrija.Files;
    opens com.example.stefanovic.kemijskaindustrija.Files to javafx.fxml;
}