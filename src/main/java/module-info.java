module com.example.gravitysimulator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.gravitysimulator to javafx.fxml;
    exports com.example.gravitysimulator;
}