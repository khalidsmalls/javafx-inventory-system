package smalls.javafxinventorysystem;

import javafx.application.Application;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.model.Inventory;
import smalls.javafxinventorysystem.view.MainWindowLoader;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {

        Inventory inv = Inventory.getInstance();
        inv.loadParts();
        MainWindowLoader mainWindowLoader = new MainWindowLoader(primaryStage);
        mainWindowLoader.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


