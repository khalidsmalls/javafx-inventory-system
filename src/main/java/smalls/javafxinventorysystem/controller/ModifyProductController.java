package smalls.javafxinventorysystem.controller;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.model.Inventory;
import smalls.javafxinventorysystem.model.Part;
import smalls.javafxinventorysystem.model.Product;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class ModifyProductController implements Initializable {
    private final Product product;
    private final String windowLabelText;
    private final String PART_NOT_FOUND_MSG = "Part not found";
    @FXML private Label productWindowLabel;
    @FXML private TextField productIdTextfield;
    @FXML private TextField productNameTextfield;
    @FXML private TextField productPriceTextfield;
    @FXML private TextField productInvTextfield;
    @FXML private TextField productMinTextfield;
    @FXML private TextField productMaxTextfield;
    @FXML private TextField partSearchTextfield;
    @FXML private TableView<Part> partsTable;
    @FXML private TableView<Part> assocPartsTable;
    @FXML private TableColumn<Part,Integer> partIdCol;
    @FXML private TableColumn<Part,Integer> partNameCol;
    @FXML private TableColumn<Part,Integer> partInvCol;
    @FXML private TableColumn<Part,Double> partPriceCol;
    @FXML private TableColumn<Part,Integer> assocPartIdCol;
    @FXML private TableColumn<Part,Integer> assocPartNameCol;
    @FXML private TableColumn<Part,Integer> assocPartInvCol;
    @FXML private TableColumn<Part,Double> assocPartPriceCol;
    private NumberFormat currencyFormat;
    private Inventory inv;
    private int productIndex;
    private boolean productNameModified;
    private boolean productPriceModified;
    private boolean productInvModified;
    private boolean productMinModified;
    private boolean productMaxModified;

    //functional interfaces for restricting text input to valid characters
    private final UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("([1-9][0-9]*)?")) {
            return change;
        }
        return null;
    };

    private final UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("[\\d]*(\\.((\\d{0,2})?))?")) {
            return change;
        }
        return null;
    };

    private final UnaryOperator<TextFormatter.Change> stringFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("([a-zA-Z\\-]*)")) {
            return change;
        }
        return null;
    };

    public ModifyProductController(Product product, String windowLabelText) {
        this.product = product;
        this.windowLabelText = windowLabelText;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productWindowLabel.setText(windowLabelText);
        inv = Inventory.getInstance();
        productIdTextfield.setEditable(false);
        productIdTextfield.setText(String.valueOf(product.getId()));
        productIndex = inv.getAllProducts().indexOf(product);
        currencyFormat = NumberFormat.getCurrencyInstance();

        setTextFormatters();
        populateFields();
        initPartsTable();
        initAssocPartsTable();
        initInvalidationListeners();
    }

    @FXML private void onPartSearch() {
        partsTable.setPlaceholder(new Text(PART_NOT_FOUND_MSG));
        String searchString = partSearchTextfield.getText();
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
    }

    @FXML private void onAddAssocPart() {
        if (partsTable.getSelectionModel().getSelectedItems().size() > 1) {
            product.addAssociatedParts(partsTable.getSelectionModel().getSelectedItems());
        } else {
            product.addAssociatedPart(partsTable.getSelectionModel().getSelectedItem());
        }
    }

    @FXML private void onRemoveAssocPart() {
        if (assocPartsTable.getSelectionModel().getSelectedItems().size() > 1) {
            //user selected multiple associated parts
            //building confirmDelete message
            StringBuilder parts = new StringBuilder("Are you sure you want to remove ");
            for (int i = 0; i < assocPartsTable.getSelectionModel().getSelectedItems().size() - 1; ++i) {
                parts.append(assocPartsTable.getSelectionModel().getSelectedItems().get(i).getName()).append(", ");
            }
            parts.append("and ")
                    //getting the last part
                    .append(assocPartsTable
                    .getSelectionModel()
                    .getSelectedItems()
                    .get(assocPartsTable.getSelectionModel().getSelectedItems().size() - 1).getName())
                    .append("?");

            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION, parts.toString());
            Optional<ButtonType> result = confirmDelete.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                product.deleteAssociatedParts(assocPartsTable.getSelectionModel().getSelectedItems());
            }
        } else if (assocPartsTable.getSelectionModel().getSelectedItems().size() == 1) {
            //single selection
            Part p = assocPartsTable.getSelectionModel().getSelectedItem();
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove " + p.getName() + "?");
            Optional<ButtonType> result = confirmDelete.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                product.deleteAssociatedParts(assocPartsTable.getSelectionModel().getSelectedItems());
            }
        } else {
            //user did not select an assoc part to remove
            new Alert(Alert.AlertType.ERROR, "Please select one or more parts to remove").showAndWait();
        }
    }//END of onRemoveAssocPart

    @FXML private void onSave(ActionEvent event) {
        if (validateFields()) {
            if(validateInventory()) {
                if (productNameModified) {
                    product.setName(productNameTextfield.getText());
                }
                if (productPriceModified) {
                    product.setPrice(Double.parseDouble(productPriceTextfield.getText()));
                }
                if (productInvModified) {
                    product.setStock(Integer.parseInt(productInvTextfield.getText()));
                }
                if (productMinModified) {
                    product.setMin(Integer.parseInt(productMinTextfield.getText()));
                }
                if (productMaxModified) {
                    product.setMax(Integer.parseInt(productMaxTextfield.getText()));
                }
                inv.updateProduct(productIndex, product);
                clearFields();
                ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();

            } else {
                //validate inventory returned false
                new Alert(Alert.AlertType.ERROR, "Inventory must be greater than or equal to Minimum and " +
                        "less than or equal to Maximum").showAndWait();
            }
        } else {
            //validate fields returned false
            new Alert(Alert.AlertType.ERROR, "Please enter a value for all fields").showAndWait();
        }
    }

    @FXML private void onClose(ActionEvent e) {
        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
    }

    private void initPartsTable() {
        partsTable.setItems(inv.getAllParts());
        partsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //set currency format on price cell
        partPriceCol.setCellFactory(cell -> new TableCell<Part, Double>() {
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

    }

    private void initAssocPartsTable() {
        assocPartsTable.setItems(product.getAllAssociatedParts());
        assocPartsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //set currency format on price cell
        assocPartPriceCol.setCellFactory(cell -> new TableCell<Part, Double>() {
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
    }

    private void setTextFormatters() {
        productInvTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        productMaxTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        productMinTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        productPriceTextfield.setTextFormatter(new TextFormatter<Double>(doubleFilter));
        productNameTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
    }

    private void populateFields() {
        productIdTextfield.setText(String.valueOf(product.getId()));
        productNameTextfield.setText(product.getName());
        productInvTextfield.setText(String.valueOf(product.getStock()));
        productPriceTextfield.setText(String.valueOf(product.getPrice()));
        productMaxTextfield.setText(String.valueOf(product.getMax()));
        productMinTextfield.setText(String.valueOf(product.getMin()));
    }

    private boolean validateFields() {
        return !(productNameTextfield.getText().equals("") || productNameTextfield.getText().length() == 0 ||
                productPriceTextfield.getText().equals("") || productPriceTextfield.getText().length() == 0 ||
                productInvTextfield.getText().equals("") || productInvTextfield.getText().length() == 0 ||
                productMaxTextfield.getText().equals("") || productMaxTextfield.getText().length() == 0 ||
                productMinTextfield.getText().equals("") || productMinTextfield.getText().length() == 0);
    }

    private boolean validateInventory() {
        return Integer.parseInt(productMinTextfield.getText()) <= Integer.parseInt(productInvTextfield.getText()) &&
                (Integer.parseInt(productInvTextfield.getText()) <= Integer.parseInt(productMaxTextfield.getText()));
    }

    private void clearFields() {
        productNameTextfield.clear();
        productPriceTextfield.clear();
        productInvTextfield.clear();
        productMinTextfield.clear();
        productMaxTextfield.clear();
    }

    public void initInvalidationListeners() {
        productNameModified = false;
        productPriceModified = false;
        productInvModified = false;
        productMinModified = false;
        productMaxModified = false;
        InvalidationListener productNameChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                productNameModified = true;
            }
        };
        InvalidationListener productPriceChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                productPriceModified = true;
            }
        };
        InvalidationListener productInvChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                productInvModified = true;
            }
        };
        InvalidationListener productMinChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                productMinModified = true;
            }
        };
        InvalidationListener productMaxChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                productMaxModified = true;
            }
        };

        productNameTextfield.textProperty().addListener(productNameChangeListener);
        productPriceTextfield.textProperty().addListener(productPriceChangeListener);
        productInvTextfield.textProperty().addListener(productInvChangeListener);
        productMinTextfield.textProperty().addListener(productMinChangeListener);
        productMaxTextfield.textProperty().addListener(productMaxChangeListener);
    }

}
