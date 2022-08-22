package smalls.javafxinventorysystem.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.controller.AddPartController;
import smalls.javafxinventorysystem.model.Inventory;

import java.io.IOException;

public class AddPartWindow {

    private Stage stage;
    private Inventory inv;

    public AddPartWindow(Stage stage) {
        this.stage = stage;
        AddPartController ctrl = new AddPartController();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(ctrl);
        loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/partWindow.fxml"));

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
        if (this.stage.isShowing()) {
            stage.toFront();
        } else {
            this.stage.show();
        }
    }
}
