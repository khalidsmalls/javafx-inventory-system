package smalls.javafxinventorysystem.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.controller.ModifyProductController;
import smalls.javafxinventorysystem.model.Product;

import java.io.IOException;

public class ModifyProductWindowLoader {
    private final String WINDOW_LABEL_TEXT = "Modify Product";
    private final Stage stage;

    public ModifyProductWindowLoader(Stage stage, Product product) {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new ModifyProductController(product, WINDOW_LABEL_TEXT));
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
