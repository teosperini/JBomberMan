module com.jbomberman.jbomberman {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;

    opens org.jbomberman to javafx.fxml;
    exports org.jbomberman;
    exports org.jbomberman.model;
}