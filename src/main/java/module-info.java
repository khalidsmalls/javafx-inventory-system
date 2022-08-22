module smalls.javafxinventorysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens smalls.javafxinventorysystem to javafx.fxml;
    exports smalls.javafxinventorysystem;
    exports smalls.javafxinventorysystem.controller;
    exports smalls.javafxinventorysystem.model;
    exports smalls.javafxinventorysystem.view;
    opens smalls.javafxinventorysystem.controller to javafx.fxml;
    opens smalls.javafxinventorysystem.view to javafx.fxml;
}