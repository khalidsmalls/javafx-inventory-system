package smalls.javafxinventorysystem;

import javafx.application.Application;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.model.Inventory;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        Inventory inv = Inventory.getInstance();
        inv.loadParts();
        inv.loadProducts();
        DependencyManager.loadMainWindow(primaryStage);
    }

    public static void main(String[] args) {
        launch();
    }
}


