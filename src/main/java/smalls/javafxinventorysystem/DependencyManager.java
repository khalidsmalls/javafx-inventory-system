package smalls.javafxinventorysystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import smalls.javafxinventorysystem.controller.*;
import java.io.IOException;
import java.util.Objects;

/**
 * Encapsulates fxmlLoader logic and handles dependency injection
 * for controller classes.
 * <p>
 * Responsible for matching fxml file with corresponding controller
 * class and passing the controller class any required dependencies
 * before applying css and loading the fxml file.
 *
 */
public class DependencyManager {

    /**
     * allows other controllers access to the selected part or
     * product through getters
     */
    private static final MainWindowController mainAppCtrl = new MainWindowController();

    /**
     * displays main application window and sets the corresponding
     * controller
     *
     * @param stage the stage to set the scene on.
     */
    public static void loadMainWindow(Stage stage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DependencyManager.class.getResource("view/mainWindow.fxml"));
        loader.setController(mainAppCtrl);

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(DependencyManager.class.getResource("styles.css")).toExternalForm()
            );
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * displays part window and sets <code>AddPartController</code> as
     * its controller. passes "Add Part" label text to the controller.
     *
     * @param stage the stage to set the scene on. passed
     *              from main application window.
     */
    public static void loadAddPart(Stage stage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DependencyManager.class.getResource("view/partWindow.fxml"));
        loader.setController(new AddPartController("Add Part"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(DependencyManager.class.getResource("styles.css")).toExternalForm()
            );
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * displays part window and assigns <code>ModifyPartController</code> as its
     * controller. passes the selected part to be modified and the "Modify Part"
     * label text to the controller.
     *
     * @param stage the stage to set the scene on. passed from main
     *              application window.
     */
    public static void loadModifyPart(Stage stage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DependencyManager.class.getResource("view/partWindow.fxml"));
        loader.setController(new ModifyPartController(mainAppCtrl.getSelectedPart(), "Modify Part"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(DependencyManager.class.getResource("styles.css")).toExternalForm()
            );
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * displays product window and sets <code>AddProductController</code> as its
     * controller. Passes "Add Product" label text to the controller.
     *
     * @param stage the stage to set the scene on. passed from main
     *              application window.
     */
    public static void loadAddProduct(Stage stage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DependencyManager.class.getResource("view/productWindow.fxml"));
        loader.setController(new AddProductController("Add Product"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(DependencyManager.class.getResource("styles.css")).toExternalForm()
            );
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * displays product window and sets <code>ModifyProductController</code> as its
     * controller. passes selected product to be modified and "Modify Product" label text
     * to the controller.
     *
     * @param stage the stage to set the scene on. passed from main
     *              application window.
     */
    public static void loadModifyProduct(Stage stage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DependencyManager.class.getResource("productWindow.fxml"));
        loader.setController(new ModifyProductController(mainAppCtrl.getSelectedProduct(), "Modify Product"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(DependencyManager.class.getResource("styles.css")).toExternalForm()
            );
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
