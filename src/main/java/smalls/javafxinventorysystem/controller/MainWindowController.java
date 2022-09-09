package smalls.javafxinventorysystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.model.*;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

/**
 * displays full inventory of parts and products.
 * <p>
 * allows the user to view all parts and products,
 * delete parts and products, and close the program.
 * also allows the user to open windows to add and
 * modify parts and products.
 *
 * @author Khalid Smalls
 */
public class MainWindowController implements Initializable {
    @FXML private TextField partSearchField;
    @FXML private TextField productSearchField;
    @FXML private TableView<Part> partsTable;
    @FXML private TableView<Product> productsTable;
    private NumberFormat currencyFormat;
    private final Stage stage;

    private final UnaryOperator<TextFormatter.Change> lengthFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.length() > 35) {
            return null;
        }
        return change;
    };

    /**
     * class constructor.
     * <p>
     * creates a new stage to pass to part and product windows
     * at the appropriate time.
     */
    public MainWindowController() {
        stage = new Stage();
    }

    /**
     * allows for customization of nodes after the scene graph is constructed,
     * but before the scene is displayed.
     * <p>
     * initializes tableviews and currency formatter.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPartTable();
        initProductTable();
        partSearchField.setTextFormatter(new TextFormatter<>(lengthFilter));
        productSearchField.setTextFormatter(new TextFormatter<>(lengthFilter));
        currencyFormat = NumberFormat.getCurrencyInstance();
    }

    /**
     * searches for a part by id number or name.
     * <p>
     * first, attempts to lookup part by id. If this is successful the part is
     * added as the sole member of an observable list created and set to the
     * parts table, then selected. if unsuccessful lookup by name is attempted.
     * If a part or parts are found that match the search string, they are added
     * to an observable list created and set to the parts table. Otherwise,
     * a "part not found" placeholder is displayed.
     *
     * RUNTIME_ERROR - IllegalArgumentException - thrown when the partsTable
     *                 is passed a null value to the setItems() method. The updateItem
     *                 functional interface in the initPartTable method is where
     *                 the error originated due to not being able to format null
     *                 to a number. It was fixed by adding the "if(p != null)"
     *                 check around the two lines adding the part to the new
     *                 observable list and selecting it. This error also had to
     *                 be corrected for the product search method in this class
     *                 as well as the part search methods in the add and modify
     *                 product window controllers.
     */
    @FXML private void onPartSearch() {
        partsTable.setPlaceholder(new Text("Part not found"));
        ObservableList<Part> partList = FXCollections.observableArrayList();
        String searchString = partSearchField.getText();
        boolean isInt = true;
        try {
            int id = Integer.parseInt(searchString);
            Part p = Inventory.lookupPart(id);
            if (p != null) {
                partList.add(p);
                partsTable.getSelectionModel().select(p);
            }
            partsTable.setItems(partList);

        } catch (NumberFormatException e) {
            isInt = false;
        }
        if (!isInt) {
            partList = Inventory.lookupPart(searchString);
            partsTable.setItems(partList);
            if (partList.size() == 1) {
                partsTable.getSelectionModel().select(partList.get(0));
            }
        }

    }

    /**
     * searches for a product by id number or name.
     * <p>
     * first, attempts to lookup product by id. If this is successful the product is
     * added as the sole member of an observable list created and set to the
     * product table, then selected. if unsuccessful lookup by name is attempted.
     * If one or more products are found that match the search string, they are added
     * to an observable list created and set to the product table. Otherwise,
     * a "Product not found" placeholder is displayed.
     */
    @FXML private void onProductSearch() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        productsTable.setPlaceholder(new Text("Product not found"));
        String searchString = productSearchField.getText();
        boolean isInt = true;
        try {
            int id = Integer.parseInt(searchString);
            Product p = Inventory.lookupProduct(id);
            if (p != null) {
                productList.add(p);
                productsTable.getSelectionModel().select(p);
            }
            productsTable.setItems(productList);

        } catch (NumberFormatException e) {
            isInt = false;
        }
        if (!isInt) {
            productList = Inventory.lookupProduct(searchString);
            productsTable.setItems(productList);
            if (productList.size() == 1) {
                productsTable.getSelectionModel().select(productList.get(0));
            }
        }
    }

    /**
     * displays the "addPart" window.
     * <p>
     * <code>FXMLLoader</code> displays <code>partWindow</code> and assigns
     * <code>AddPartController</code> to it. Also, passes <code>partWindowLabelText</code>
     * to the <code>AddPartController</code>.
     */
    @FXML private void onAddPart() {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new AddPartController("Add Part"));
        loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/view/partWindow.fxml"));

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
     * displays the "modifyPart" window.
     * <p>
     * <code>FXMLLoader</code> displays <code>partWindow</code> and assigns
     * <code>ModifyPartController</code> to it. Also, passes <code>windowLabelText</code>
     * and the <code>Part</code> to be modified to <code>ModifyPartController</code>.
     */
    @FXML private void onModifyPart() {
        Part p = partsTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(new ModifyPartController(p, "Modify Part"));
            loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/view/partWindow.fxml"));

            try {
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "There was a problem loading the window").showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a part").showAndWait();
        }
    }

    /**
     * attempts to delete a selected <code>Part</code>.
     * <p>
     * prompts user for confirmation before deleting part.
     * prompts user if he/she failed to select a part to
     * be deleted.
     */
    @FXML private void onDeletePart() {
        try {
            Part p = (Part) partsTable.getSelectionModel().getSelectedItem();
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + p.getName() + "?");
            Optional<ButtonType> result = confirmDelete.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (!Inventory.deletePart(p)) {
                    new Alert(Alert.AlertType.ERROR, "There was a problem deleting the selected part").showAndWait();
                }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Please select a part to delete").showAndWait();
        }
    }

    /**
     * displays the "addProduct" window.
     * <p>
     * <code>FXMLLoader</code> displays <code>productWindow</code> and assigns
     * <code>AddProductController</code> to it. Also, passes <code>productWindowLabelText</code>
     * to <code>AddProductController</code>.
     */
    @FXML private void onAddProduct() {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new AddProductController("Add Product"));
        loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/view/productWindow.fxml"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "There was a problem loading the window");
        }
    }

    /**
     * displays the "modifyProduct" window.
     * <p>
     * <code>FXMLLoader</code> displays <code>productWindow</code> and assigns
     * <code>ModifyProductController</code> to it. Also, passes <code>productWindowLabelText</code>
     * and the <code>Product</code> to be modified to <code><ModifyProductController</code>.
     */
    @FXML private void onModifyProduct() {
        Product p = productsTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(new ModifyProductController(p, "Modify Product"));
            loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/view/productWindow.fxml"));

            try {
                Parent root = loader.load();
                Scene scene = new Scene(root);
                this.stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "There was a problem loading the window").showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a product").showAndWait();
        }
    }

    /**
     * attempts to delete the selected product.
     * <p>
     * prompts the user for confirmation.
     * also will alert user if he/she attempts to
     * delete a product that has associated parts and abort.
     */
    @FXML private void onDeleteProduct() {
        try {
            Product p = (Product)productsTable.getSelectionModel().getSelectedItem();
            if (p.getAllAssociatedParts().size() > 0) {
                new Alert(Alert.AlertType.ERROR, "Error: Cannot delete a product that has associated parts").showAndWait();
                return;
            }
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + p.getName() + "?");
            Optional<ButtonType> result = confirmDelete.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (!Inventory.deleteProduct(p)) {
                    new Alert(Alert.AlertType.ERROR, "There was a problem deleting the selected product").showAndWait();
                }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Please select a part to delete").showAndWait();
        }
    }

    /**
     * closes main window, exits program.
     * <p>
     * prompts user before exiting.
     *
     * @param event allows access to the stage, so that it may be closed
     */
   @FXML private void onClose(ActionEvent event) {
       Alert confirmClose = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to close the program?");
       Optional<ButtonType> result = confirmClose.showAndWait();
       if (result.isPresent() && result.get() == ButtonType.OK) {
           ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
       }
   }

    /**
     * helper method initializes part table.
     */
   private void initPartTable() {
       partsTable.setItems(Inventory.getAllParts());
       TableColumn<Part, Integer> partIdColumn = new TableColumn<Part, Integer>("Part ID");
       partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
       TableColumn<Part, String> partNameColumn = new TableColumn<Part, String>("Part Name");
       partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
       TableColumn<Part, Integer> partInvColumn = new TableColumn<Part, Integer>("Inventory");
       partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
       TableColumn<Part, Double> partPriceColumn = new TableColumn<Part, Double>("Price/Cost per Unit");
       partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

       partsTable.getColumns().setAll(Arrays.asList(partIdColumn, partNameColumn, partInvColumn, partPriceColumn));

       partIdColumn.prefWidthProperty().bind(partsTable.widthProperty().multiply(0.15));
       partNameColumn.prefWidthProperty().bind(partsTable.widthProperty().multiply(.29));
       partInvColumn.prefWidthProperty().bind(partsTable.widthProperty().multiply(.20));
       partPriceColumn.prefWidthProperty().bind(partsTable.widthProperty().multiply(.36));
       partIdColumn.setResizable(false);
       partNameColumn.setResizable(false);
       partInvColumn.setResizable(false);
       partPriceColumn.setResizable(false);

       //set currency format on price cell
       partPriceColumn.setCellFactory(cell -> new TableCell<Part, Double>() {
           @Override
           protected void updateItem(Double price, boolean empty) {
               super.updateItem(price, empty);
               if (empty) {
                   setText(null);
               } else {
                   setText(currencyFormat.format(price));
               }
           }
       });
   }//END of initPartTable

    /**
     * helper method initializes product table.
     */
    private void initProductTable() {
        productsTable.setItems(Inventory.getAllProducts());
        TableColumn<Product, Integer> productIdColumn = new TableColumn<Product, Integer>("Product ID");
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Product, String> productNameColumn = new TableColumn<Product, String>("Product Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product,Integer> productInvColumn = new TableColumn<Product,Integer>("Inventory");
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Product,Double> productPriceColumn = new TableColumn<Product,Double>("Price/Cost per Unit");
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTable.getColumns().setAll(Arrays.asList(productIdColumn, productNameColumn, productInvColumn, productPriceColumn));

        productIdColumn.prefWidthProperty().bind(productsTable.widthProperty().multiply(0.18));
        productNameColumn.prefWidthProperty().bind(productsTable.widthProperty().multiply(.29));
        productInvColumn.prefWidthProperty().bind(productsTable.widthProperty().multiply(.20));
        productPriceColumn.prefWidthProperty().bind(productsTable.widthProperty().multiply(.33));
        productIdColumn.setResizable(false);
        productNameColumn.setResizable(false);
        productInvColumn.setResizable(false);
        productPriceColumn.setResizable(false);

        productPriceColumn.setCellFactory(cell -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }//END of initProductTable

}