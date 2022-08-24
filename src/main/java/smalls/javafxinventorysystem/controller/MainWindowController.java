package smalls.javafxinventorysystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML private TextField partSearchField;
    @FXML private TextField productSearchField;
    @FXML private TableView<Part> partTable;
    @FXML private TableView<Product> productTable;
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
            partTable.setItems(partList);
            partTable.getSelectionModel().select(p);
        } catch (Exception e) {
            isInt = false;
        }
        if (!isInt) {
            ObservableList<Part> partList = inv.lookupPart(searchString);
            partTable.setItems(partList);
            if (partList.size() == 1) {
                partTable.getSelectionModel().select(partList.get(0));
            }
        }
        partTable.setPlaceholder(new Text("Part not found"));
    }

    @FXML private void onProductSearch() {
        String searchString = productSearchField.getText();
        boolean isInt = true;
        try {
            int id = Integer.parseInt(searchString);
            Product p = inv.lookupProduct(id);
            ObservableList<Product> productList = FXCollections.observableArrayList();
            productList.add(p);
            productTable.setItems(productList);
            productTable.getSelectionModel().select(p);
        } catch (Exception e) {
            isInt = false;
        }
        if (!isInt) {
            ObservableList<Product> productList = inv.lookupProduct(searchString);
            productTable.setItems(productList);
            if (productList.size() == 1) {
                productTable.getSelectionModel().select(productList.get(0));
            }
        }
        productTable.setPlaceholder(new Text("Part not found"));
    }

    @FXML private void onAddPart() {
        AddPartWindowLoader win = new AddPartWindowLoader(stage);
        win.show();
    }

    @FXML private void onModifyPart() {
        Part p = partTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            ModifyPartWindowLoader win = new ModifyPartWindowLoader(stage, p);
            win.show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a part").showAndWait();
        }
    }

    @FXML private void onDeletePart() {
    }

    @FXML private void onAddProduct() {
        AddProductWindowLoader win = new AddProductWindowLoader(stage);
        win.show();
    }

    @FXML private void onModifyProduct() {
        Product p = productTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            ModifyProductWindowLoader win = new ModifyProductWindowLoader(stage, p);
            win.show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a product").showAndWait();
        }
    }

    @FXML private void onDeleteProduct() {
    }

   @FXML private void onClose() {
        stage.close();
   }

   private void initPartTable() {
       partTable.setItems(inv.getAllParts());
       TableColumn<Part, Integer> partIdColumn = new TableColumn<Part, Integer>("Part ID");
       partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
       TableColumn<Part, String> partNameColumn = new TableColumn<Part, String>("Part Name");
       partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
       TableColumn<Part, Integer> partInvColumn = new TableColumn<Part, Integer>("Inventory");
       partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
       TableColumn<Part, Double> partPriceColumn = new TableColumn<Part, Double>("Price/Cost per Unit");
       partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

       partTable.getColumns().setAll(Arrays.asList(partIdColumn, partNameColumn, partInvColumn, partPriceColumn));

       partIdColumn.prefWidthProperty().bind(partTable.widthProperty().multiply(0.15));
       partNameColumn.prefWidthProperty().bind(partTable.widthProperty().multiply(.29));
       partInvColumn.prefWidthProperty().bind(partTable.widthProperty().multiply(.20));
       partPriceColumn.prefWidthProperty().bind(partTable.widthProperty().multiply(.36));
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
        productTable.setItems(inv.getAllProducts());
        TableColumn<Product, Integer> productIdColumn = new TableColumn<Product, Integer>("Product ID");
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Product, String> productNameColumn = new TableColumn<Product, String>("Product Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product,Integer> productInvColumn = new TableColumn<Product,Integer>("Inventory");
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Product,Double> productPriceColumn = new TableColumn<Product,Double>("Price/Cost per Unit");
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.getColumns().setAll(Arrays.asList(productIdColumn, productNameColumn, productInvColumn, productPriceColumn));

        productIdColumn.prefWidthProperty().bind(productTable.widthProperty().multiply(0.18));
        productNameColumn.prefWidthProperty().bind(productTable.widthProperty().multiply(.29));
        productInvColumn.prefWidthProperty().bind(productTable.widthProperty().multiply(.20));
        productPriceColumn.prefWidthProperty().bind(productTable.widthProperty().multiply(.33));
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