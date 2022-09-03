module smalls.javafxinventorysystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens smalls.javafxinventorysystem to javafx.fxml;
    exports smalls.javafxinventorysystem;
    exports smalls.javafxinventorysystem.controller;
    exports smalls.javafxinventorysystem.model;
    opens smalls.javafxinventorysystem.controller to javafx.fxml;

}