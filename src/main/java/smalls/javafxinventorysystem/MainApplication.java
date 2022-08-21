package smalls.javafxinventorysystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.controller.MainWindowController;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            MainWindowController ctrl = new MainWindowController();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("mainWindow.fxml"));
            loader.setController(ctrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("JavaFX Inventory System");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}


