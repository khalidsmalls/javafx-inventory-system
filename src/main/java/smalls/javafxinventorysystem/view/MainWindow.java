package smalls.javafxinventorysystem.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import smalls.javafxinventorysystem.MainApplication;
import smalls.javafxinventorysystem.controller.MainWindowController;
import smalls.javafxinventorysystem.model.Inventory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class MainWindow {

    private Stage stage;
    private Inventory inv;

    public MainWindow(Stage stage) {
        this.stage = stage;
        MainWindowController ctrl = new MainWindowController();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(ctrl);
        loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/mainWindow.fxml"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            this.stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show(Inventory inv) {
        this.inv = inv;
        this.stage.show();
    }
}
