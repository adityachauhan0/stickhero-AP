module com.example.stick {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.stick to javafx.fxml;
    exports com.example.stick;
}