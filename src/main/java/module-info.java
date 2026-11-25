module org.example.hospital_management_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.management;

    opens org.example.hospital_management_system to javafx.fxml;
    exports org.example.hospital_management_system;
}