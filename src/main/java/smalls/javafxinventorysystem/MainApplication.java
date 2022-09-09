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
 * loads the main application window.
 * <p>
 * Javadoc is included in the top level directory
 * in the javadoc directory.
 * <p>
 * FUTURE_ENHANCEMENT - 1. a class or framework that could encapsulate the fxmlLoader logic
 *                         as well as handling dependencies passed to windows. For example,
 *                         assigning a controller, loading the fxml file, and passing the
 *                         selected product to the modify product window. In a more advanced
 *                         application a controllerFactory might be used to set up custom
 *                         controllers for fxml files.
 *                      2. providing persistent storage with a database would be a clear and
 *                         obvious enhancement.
 *
 * @see MainWindowController - for description of RUNTIME_ERROR above onPartSearch method
 *
 * @author Khalid Smalls
 */
public class MainApplication extends Application {

    /**
     * the primary stage, initialized in the start method
     */
    Stage stage;

    /**
     * application start method.
     * <p>
     * responsible for loading and displaying <code>mainWindow</code> as well
     * as setting its controller.
     *
     * @param primaryStage the stage to build the main app window on
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new MainWindowController());
        loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/view/mainWindow.fxml"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "There was a problem loading the window").showAndWait();
        }
    }

    /**
     * main method, launches application.
     *
     * @param args the arguments passed to the main method
     */
    public static void main(String[] args) {
        launch();
    }
}


