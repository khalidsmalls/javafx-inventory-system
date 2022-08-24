package smalls.javafxinventorysystem.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.controller.AddProductController;

import java.io.IOException;

public class AddProductWindowLoader {

    private Stage stage;
    private final String PRODUCT_WINDOW_LABEL_TEXT = "Add Product";

    public AddProductWindowLoader(Stage stage) {
        this.stage = stage;
        AddProductController ctrl = new AddProductController(PRODUCT_WINDOW_LABEL_TEXT);
        FXMLLoader loader = new FXMLLoader();
        loader.setController(ctrl);
        loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/productWindow.fxml"));

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
