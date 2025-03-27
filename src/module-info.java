module Integrador {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
    
    opens controllers to javafx.fxml;
    exports application;
    exports controllers;
}
