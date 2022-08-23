package smalls.javafxinventorysystem.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.controller.AddPartController;
import smalls.javafxinventorysystem.model.Inventory;

import java.io.IOException;

public class AddPartWindowLoader {

    private final Stage stage;
    private final String WINDOW_LABEL_TEXT = "Add Part";

    public AddPartWindowLoader(Stage stage) {
        this.stage = stage;
        AddPartController ctrl = new AddPartController(WINDOW_LABEL_TEXT);
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

    public void show() {
        if (this.stage.isShowing()) {
            stage.toFront();
        } else {
            this.stage.show();
        }
    }
}
