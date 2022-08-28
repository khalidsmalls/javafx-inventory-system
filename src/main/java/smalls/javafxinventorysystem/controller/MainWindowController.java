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
import smalls.javafxinventorysystem.model.*;
import smalls.javafxinventorysystem.view.AddPartWindowLoader;
import smalls.javafxinventorysystem.view.AddProductWindowLoader;
import smalls.javafxinventorysystem.view.ModifyPartWindowLoader;
import smalls.javafxinventorysystem.view.ModifyProductWindowLoader;

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

    @FXML private void onPartSearch() {
        String searchString = partSearchField.getText();
        boolean isInt = true;
        try {
            int id = Integer.parseInt(searchString);
            Part p = inv.lookupPart(id);
            ObservableList<Part> partList = FXCollections.observableArrayList();
            partList.add(p);
            partsTable.setItems(partList);
            partsTable.getSelectionModel().select(p);
        } catch (Exception e) {
            isInt = false;
        }
        if (!isInt) {
            ObservableList<Part> partList = inv.lookupPart(searchString);
            partsTable.setItems(partList);
            if (partList.size() == 1) {
                partsTable.getSelectionModel().select(partList.get(0));
            }
        }
        partsTable.setPlaceholder(new Text("Part not found"));
    }

    @FXML private void onProductSearch() {
        String searchString = productSearchField.getText();
        boolean isInt = true;
        try {
            int id = Integer.parseInt(searchString);
            Product p = inv.lookupProduct(id);
            ObservableList<Product> productList = FXCollections.observableArrayList();
            productList.add(p);
            productsTable.setItems(productList);
            productsTable.getSelectionModel().select(p);
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
        productsTable.setPlaceholder(new Text("Part not found"));
    }

    @FXML private void onAddPart() {
        AddPartWindowLoader win = new AddPartWindowLoader(stage);
        win.show();
    }

    @FXML private void onModifyPart() {
        Part p = partsTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            ModifyPartWindowLoader win = new ModifyPartWindowLoader(stage, p);
            win.show();
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
//        AddProductWindowLoader win = new AddProductWindowLoader(stage);
//        win.show();
        inv.dbTest();
    }

    @FXML private void onModifyProduct() {
        Product p = productsTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            ModifyProductWindowLoader win = new ModifyProductWindowLoader(stage, p);
            win.show();
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
       ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();

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

}