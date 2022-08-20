module smalls.javafxinventorysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens smalls.javafxinventorysystem to javafx.fxml;
    exports smalls.javafxinventorysystem;
    exports smalls.javafxinventorysystem.controller;
    opens smalls.javafxinventorysystem.controller to javafx.fxml;
}