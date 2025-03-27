module Integrador {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens controllers to javafx.fxml;
    exports application;
    exports controllers;
}
