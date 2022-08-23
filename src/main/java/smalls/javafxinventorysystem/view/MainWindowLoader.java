package smalls.javafxinventorysystem.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.controller.MainWindowController;
import smalls.javafxinventorysystem.model.Inventory;

import java.io.IOException;

public class MainWindowLoader {

    private Stage stage;
    //private Inventory inv;

    public MainWindowLoader(Stage stage) {
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

    public void show() {
        //this.inv = inv;
        if (this.stage.isShowing()) {
            stage.toFront();
        } else {
            this.stage.show();
        }
    }


}//END of class
