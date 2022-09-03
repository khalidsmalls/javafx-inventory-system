package smalls.javafxinventorysystem.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.controller.MainWindowController;
import java.io.IOException;

public class MainWindowLoader {

    private final Stage stage;

    public MainWindowLoader(Stage primaryStage) {
        this.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new MainWindowController());
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
        if (this.stage.isShowing()) {
            stage.toFront();
        } else {
            this.stage.show();
        }
    }


}//END of class
