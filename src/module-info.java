module Integrador {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires transitive javafx.graphics;
    requires jakarta.mail;
    requires jakarta.activation;

    opens model to javafx.base;
    opens controllers to javafx.fxml;
    opens controllers.manager to javafx.fxml;

    exports application;
    exports controllers;
}
