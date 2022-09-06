package smalls.javafxinventorysystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.controller.MainWindowController;
import smalls.javafxinventorysystem.model.Inventory;

import java.io.IOException;

/**
 * loads the main application window of my
 * C482 javafx inventory system project
 *
 * @author Khalid Smalls
 */
public class MainApplication extends Application {
    Stage stage;

    @Override
    public void start(Stage primaryStage) {

        Inventory inv = Inventory.getInstance();
        inv.loadParts();
        inv.loadProducts();
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new MainWindowController());
        loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/mainWindow.fxml"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "There was a problem loading the window").showAndWait();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}


