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
import smalls.javafxinventorysystem.view.AddPartWindow;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML private TextField partSearchField;
    @FXML private TextField productSearchField;
    @FXML private Button partSearchBtn;
    @FXML private Button productSearchBtn;
    @FXML private Button addPartBtn;
    @FXML private Button modifyPartBtn;
    @FXML private Button deletePartBtn;
    @FXML private Button addProductBtn;
    @FXML private Button modifyProductBtn;
    @FXML private Button deleteProductBtn;
    @FXML private Button closeBtn;
    @FXML private TableView<Part> partTable;
    @FXML private TableView<Product> productTable;
    private Inventory inv;

    private NumberFormat currencyFormat;
    private Stage stage;

    public MainWindowController() {
        inv = Inventory.getInstance();
        stage = new Stage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partTable.setItems(inv.getAllParts());
        productTable.setItems(inv.getAllProducts());
        currencyFormat = NumberFormat.getCurrencyInstance();

        initPartTable();
        initProductTable();
    }

    public void onPartSearch() {
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

    public void onProductSearch() {
    }

    public void onAddPart() {
        AddPartWindow win = new AddPartWindow(stage);
        win.show(inv);
    }

    public void onModifyPart() {
    }

    public void onDeletePart() {
    }

    public void onAddProduct() {
    }

    public void onModifyProduct() {
    }

    public void onDeleteProduct() {
    }

   public void onClose() {
   }

   public void initPartTable() {
       TableColumn<Part, Integer> partIdColumn = new TableColumn<Part, Integer>("Part ID");
       partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
       TableColumn<Part, String> partNameColumn = new TableColumn<Part, String>("Part Name");
       partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
       TableColumn<Part, Integer> partInvColumn = new TableColumn<Part, Integer>("Inventory");
       partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
       TableColumn<Part, Double> partPriceColumn = new TableColumn<Part, Double>("Price/Cost per Unit");
       partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

       partTable.getColumns().setAll(partIdColumn, partNameColumn, partInvColumn, partPriceColumn);

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

    public void initProductTable() {
        TableColumn<Product, Integer> productIdColumn = new TableColumn<Product, Integer>("Product ID");
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Product, String> productNameColumn = new TableColumn<Product, String>("Product Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product,Integer> productInvColumn = new TableColumn<Product,Integer>("Inventory");
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Product,Double> productPriceColumn = new TableColumn<Product,Double>("Price/Cost per Unit");
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.getColumns().setAll(productIdColumn, productNameColumn, productInvColumn, productPriceColumn);

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