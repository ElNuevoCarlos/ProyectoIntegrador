module Integrador {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
	requires javafx.base;
	
	opens model to javafx.base;
    opens controllers to javafx.fxml;
    exports application;
    exports controllers;
}
