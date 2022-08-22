package smalls.javafxinventorysystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.controller.MainWindowController;
import smalls.javafxinventorysystem.model.Inventory;
import smalls.javafxinventorysystem.view.MainWindow;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {

        Inventory inv = Inventory.getInstance();
        inv.loadParts();
        MainWindow mainWindow = new MainWindow(stage);
        mainWindow.show(inv);
    }

    public static void main(String[] args) {
        launch();
    }
}


