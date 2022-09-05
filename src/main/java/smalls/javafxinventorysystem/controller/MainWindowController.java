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

public class MainWindowController implements Initializable {
    @FXML private TextField partSearchField;
    @FXML private TextField productSearchField;
    @FXML private TableView<Part> partsTable;
    @FXML private TableView<Product> productsTable;
    private final Inventory inv;
    private NumberFormat currencyFormat;
    private final Stage stage;

    /**
     * class constructor -
     * gets inventory instance and creates new stage
     * to pass to part and product windows
     */
    public MainWindowController() {
        inv = Inventory.getInstance();
        stage = new Stage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPartTable();
        initProductTable();
        currencyFormat = NumberFormat.getCurrencyInstance();
    }

    /**
     * searches for a part by id number or name.
     * <p>
     * first, attempts to lookup part by id. If this is successful the part is
     * added as the sole member of an observable list created and set to the
     * parts table. if unsuccessful lookup by name is attempted. If a part or
     * parts are found that match the search string, they are added to an
     * observable list created and set to the parts table. Otherwise,
     * a "part not found" placeholder is displayed.
     */
    @FXML private void onPartSearch() {
        partsTable.setPlaceholder(new Text("Part not found"));
        ObservableList<Part> partList = FXCollections.observableArrayList();
        String searchString = partSearchField.getText();
        boolean isInt = true;
        try {
            int id = Integer.parseInt(searchString);
            Part p = inv.lookupPart(id);
            if (p != null) {
                partList.add(p);
                partsTable.getSelectionModel().select(p);
            }
            partsTable.setItems(partList);

        } catch (NumberFormatException e) {
            isInt = false;
        }
        if (!isInt) {
            partList = inv.lookupPart(searchString);
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
     * product table. if unsuccessful lookup by name is attempted. If one or more
     * products are found that match the search string, they are added to an
     * observable list created and set to the product table. Otherwise,
     * a "product not found" placeholder is displayed.
     */
    @FXML private void onProductSearch() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        productsTable.setPlaceholder(new Text("Product not found"));
        String searchString = productSearchField.getText();
        boolean isInt = true;
        try {
            int id = Integer.parseInt(searchString);
            Product p = inv.lookupProduct(id);
            if (p != null) {
                productList.add(p);
                productsTable.getSelectionModel().select(p);
            }
            productsTable.setItems(productList);

        } catch (NumberFormatException e) {
            isInt = false;
        }
        if (!isInt) {
            productList = inv.lookupProduct(searchString);
            productsTable.setItems(productList);
            if (productList.size() == 1) {
                productsTable.getSelectionModel().select(productList.get(0));
            }
        }
    }

    /**
     * displays the "addPart" window
     * <p>
     * <code>FXMLLoader</code> instance displays <code>partWindow</code> and assigns
     * <code>AddPartController</code> to it.
     */
    @FXML private void onAddPart() {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new AddPartController("Add Part"));
        loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/partWindow.fxml"));

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
     * <code>FXMLLoader</code> instance displays <code>partWindow</code> and assigns
     * <code>ModifyPartController</code> to it.
     */
    @FXML private void onModifyPart() {
        Part p = partsTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(new ModifyPartController(p, "Modify Part"));
            loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/partWindow.fxml"));

            try {
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
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
                inv.deletePart(p);
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Please select a part to delete").showAndWait();
        }
    }

    /**
     * displays the "addProduct" window.
     * <p>
     * <code>FXMLLoader</code> instance displays <code>productWindow</code> and assigns
     * <code>AddProductController</code> to it.
     */
    @FXML private void onAddProduct() {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new AddProductController("Add Product"));
        loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/productWindow.fxml"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * displays the "modifyProduct" window.
     * <p>
     * <code>FXMLLoader</code> instance displays <code>productWindow</code> and assigns
     * <code>ModifyProductController</code> to it.
     */
    @FXML private void onModifyProduct() {
        Product p = productsTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(new ModifyProductController(p, "Modify Product"));
            loader.setLocation(getClass().getResource("/smalls/javafxinventorysystem/productWindow.fxml"));

            try {
                Parent root = loader.load();
                Scene scene = new Scene(root);
                this.stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a product").showAndWait();
        }
    }

    /**
     * attempts to delete the selected product.
     * <p>
     * prompts the user for confirmation.
     * also will alert user if he/she attempts to delete a product that has
     * associated parts and abort.
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
                inv.deleteProduct(p);
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Please select a part to delete").showAndWait();
        }
    }

    /**
     * closes window
     *
     * @param e allows access to the stage, so that it may be closed
     */
   @FXML private void onClose(ActionEvent e) {
       ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();

   }

   //initialize part table
   private void initPartTable() {
       partsTable.setItems(inv.getAllParts());
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

    //initialize product table
    private void initProductTable() {
        productsTable.setItems(inv.getAllProducts());
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