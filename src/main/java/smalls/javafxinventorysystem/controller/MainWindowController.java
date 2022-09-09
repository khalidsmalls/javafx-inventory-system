package smalls.javafxinventorysystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.DependencyManager;
import smalls.javafxinventorysystem.model.*;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class MainWindowController implements Initializable {
    @FXML private TextField partSearchField;
    @FXML private TextField productSearchField;
    @FXML private TableView<Part> partsTable;
    @FXML private TableView<Product> productsTable;
    private final Inventory inv;
    private NumberFormat currencyFormat;
    private final Stage stage;

    private final UnaryOperator<TextFormatter.Change> lengthFilter = change -> {
        if (change.getControlNewText().length() > 35) {
            return null;
        }
        return change;
    };

    public MainWindowController() {
        inv = Inventory.getInstance();
        stage = new Stage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPartTable();
        initProductTable();
        partSearchField.setTextFormatter(new TextFormatter<>(lengthFilter));
        productSearchField.setTextFormatter(new TextFormatter<>(lengthFilter));
        currencyFormat = NumberFormat.getCurrencyInstance();
    }

    @FXML private void onPartSearch() {
        partsTable.setPlaceholder(new Text("Part not found"));
        String searchString = partSearchField.getText();
        boolean isInt = true;
        try {
            int id = Integer.parseInt(searchString);
            Part p = inv.lookupPart(id);
            ObservableList<Part> partList = FXCollections.observableArrayList();
            partsTable.setItems(partList);
            if (p != null) {
                partList.add(p);
                partsTable.getSelectionModel().select(p);
            }
        } catch (NumberFormatException e) {
            isInt = false;
        }
        if (!isInt) {
            ObservableList<Part> partList = inv.lookupPart(searchString);
            partsTable.setItems(partList);
            if (partList.size() == 1) {
                partsTable.getSelectionModel().select(partList.get(0));
            }
        }
    }

    @FXML private void onProductSearch() {
        productsTable.setPlaceholder(new Text("Product not found"));
        String searchString = productSearchField.getText();
        boolean isInt = true;
        try {
            int id = Integer.parseInt(searchString);
            Product p = inv.lookupProduct(id);
            ObservableList<Product> productList = FXCollections.observableArrayList();
            productsTable.setItems(productList);
            if (p != null) {
                productList.add(p);
                productsTable.getSelectionModel().select(p);
            }
        } catch (Exception e) {
            isInt = false;
        }
        if (!isInt) {
            ObservableList<Product> productList = inv.lookupProduct(searchString);
            productsTable.setItems(productList);
            if (productList.size() == 1) {
                productsTable.getSelectionModel().select(productList.get(0));
            }
        }
    }

    @FXML private void onAddPart() {
        DependencyManager.loadAddPart(stage);
    }

    @FXML private void onModifyPart() {
        Part p = partsTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            DependencyManager.loadModifyPart(stage);
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a part").showAndWait();
        }
    }

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

    @FXML private void onAddProduct() {
        DependencyManager.loadAddProduct(stage);
    }

    @FXML private void onModifyProduct() {
        Product p = productsTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            DependencyManager.loadModifyProduct(stage);
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a product").showAndWait();
        }
    }

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

   @FXML private void onClose(ActionEvent e) {
       Alert confirmClose = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to close the program?");
       Optional<ButtonType> result = confirmClose.showAndWait();
       if (result.isPresent() && result.get() == ButtonType.OK) {
           ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
       }
   }

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

    public Part getSelectedPart() {
        return (Part) partsTable.getSelectionModel().getSelectedItem();
    }

    public Product getSelectedProduct() {
        return (Product) productsTable.getSelectionModel().getSelectedItem();
    }

}