package smalls.javafxinventorysystem.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.controller.ModifyPartController;
import smalls.javafxinventorysystem.model.Part;
import java.io.IOException;

public class ModifyPartWindowLoader {

    private Stage stage;
    private final String WINDOW_LABEL_TEXT = "Modify Part";

    public ModifyPartWindowLoader(Stage stage, Part part) {
        this.stage = stage;
        ModifyPartController ctrl = new ModifyPartController(part, WINDOW_LABEL_TEXT);
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
