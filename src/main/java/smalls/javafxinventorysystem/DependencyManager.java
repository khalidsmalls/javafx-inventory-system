package smalls.javafxinventorysystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import smalls.javafxinventorysystem.controller.*;
import java.io.IOException;
import java.util.Objects;

public class DependencyManager {

    private static final MainWindowController mainAppCtrl = new MainWindowController();

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
