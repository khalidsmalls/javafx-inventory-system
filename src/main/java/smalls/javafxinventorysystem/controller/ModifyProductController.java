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


/**
 * responsible for the functionality that allows an end-user
 * to modify an existing <code>Product</code>.
 *
 * @author Khalid Smalls
 */
public class ModifyProductController implements Initializable {
    private final Product product;
    private final String productWindowLabelText;
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
    private int productIndex;
    private boolean productNameModified;
    private boolean productPriceModified;
    private boolean productInvModified;
    private boolean productMinModified;
    private boolean productMaxModified;
    private NumberFormat currencyFormat;

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

    /**
     * class constructor.
     * <p>
     *  gets <code>Product</code> to be modified and
     *  <code>productWindowLabelText</code> from caller.
     *
     * @param product the product to be modified
     * @param productWindowLabelText the main window label text to set
     */
    public ModifyProductController(Product product, String productWindowLabelText) {
        this.product = product;
        this.productWindowLabelText = productWindowLabelText;
    }

    /**
     * allows for customization of nodes after the scene graph is constructed,
     * but before the scene is displayed.
     * <p>
     * initializes product id textField as non-editable,
     * sets window label text, gets product index, populates
     * textFields with product data, initializes tableviews and
     * sets invalidation listeners on textFields.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productWindowLabel.setText(productWindowLabelText);
        productIdTextfield.setEditable(false);
        productIdTextfield.setText(String.valueOf(product.getId()));
        productIndex = Inventory.getAllProducts().indexOf(product);
        currencyFormat = NumberFormat.getCurrencyInstance();

        setTextFormatters();
        populateFields();
        initTableViews();
        initInvalidationListeners();
    }

    /**
     * searches for a <code>Part</code> by id number or name.
     * <p>
     * first, attempts to lookup part by id. If this is successful the part is
     * added as the sole member of an observable list created and set to the
     * parts table. if unsuccessful lookup by name is attempted. If a part or
     * parts are found that match the search string, they are added to an
     * observable list created and set to the parts table. Otherwise,
     * a "part not found" placeholder is displayed.
     */
    @FXML private void onPartSearch() {
        String searchString = partSearchTextfield.getText();
        boolean isInt = true;
        try {
            int id = Integer.parseInt(searchString);
            Part p = Inventory.lookupPart(id);
            ObservableList<Part> partList = FXCollections.observableArrayList();
            if (p != null) {
                partList.add(p);
                partsTable.getSelectionModel().select(p);
            }
            partsTable.setItems(partList);
        } catch (NumberFormatException e) {
            isInt = false;
        }
        if (!isInt) {
            ObservableList<Part> partList = Inventory.lookupPart(searchString);
            partsTable.setItems(partList);
            if (partList.size() == 1) {
                partsTable.getSelectionModel().select(partList.get(0));
            }
        }
        partsTable.setPlaceholder(new Text(PART_NOT_FOUND_MSG));
    }

    /**
     * adds a part to <code>assocPartsTable</code> <code>Tableview</code>.
     * <p>
     * The part is not added to this
     * product's associated parts list until <code>onSave</code> method is successfully executed.
     */
    @FXML private void onAddAssocPart() {
        if (partsTable.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "Please select a part").showAndWait();
            return;
        }
        if (partsTable.getSelectionModel().getSelectedItems().size() > 1) {
            product.addAssociatedParts(partsTable.getSelectionModel().getSelectedItems());
        } else {
            product.addAssociatedPart(partsTable.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * removes one or more associated parts from the <code>assocPartsTable</code>
     * <code>TableView</code>.
     * <p>
     * Prompts the user for confirmation before removing parts,
     * however, the updated parts are not saved until the <code>onSave</code> method is successfully
     * executed.
     */
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

    /**
     * saves a modified product.
     * <p>
     * modifies fields with setters,
     * always saves product with same <code>id</code>
     * at same index in <code>allProducts</code> list.
     *
     * @param event the object that allows access to the stage so that
     *              it may be closed after the product is saved
     */
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
                Inventory.updateProduct(productIndex, product);
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

    /**
     * closes window.
     *
     * @param event the object used to access the stage so that it may
     *          be closed
     */
    @FXML private void onClose(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    /**
     * helper method initializes both tableviews.
     */
    private void initTableViews() {
        partsTable.setItems(Inventory.getAllParts());
        partsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocPartsTable.setItems(product.getAllAssociatedParts());
        assocPartsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

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

    /**
     * helper method sets text formatters on textfields to ensure valid input.
     */
    private void setTextFormatters() {
        productInvTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        productMaxTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        productMinTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        productPriceTextfield.setTextFormatter(new TextFormatter<Double>(doubleFilter));
        productNameTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
    }

    /**
     * helper method populates textFields with product information on initialization.
     */
    private void populateFields() {
        productIdTextfield.setText(String.valueOf(product.getId()));
        productNameTextfield.setText(product.getName());
        productInvTextfield.setText(String.valueOf(product.getStock()));
        productPriceTextfield.setText(String.valueOf(product.getPrice()));
        productMaxTextfield.setText(String.valueOf(product.getMax()));
        productMinTextfield.setText(String.valueOf(product.getMin()));
    }

    /**
     *  helper method validates textFields are populated with data.
     *
     * @return <code>true</code> if all textFields are populated with data,
     *         <code>false</code> if not.
     */
    private boolean validateFields() {
        return !(productNameTextfield.getText().equals("") || productNameTextfield.getText().length() == 0 ||
                productPriceTextfield.getText().equals("") || productPriceTextfield.getText().length() == 0 ||
                productInvTextfield.getText().equals("") || productInvTextfield.getText().length() == 0 ||
                productMaxTextfield.getText().equals("") || productMaxTextfield.getText().length() == 0 ||
                productMinTextfield.getText().equals("") || productMinTextfield.getText().length() == 0);
    }

    /**
     * helper method validates user entered min stock is less than or equal to inventory and inventory is
     *         less than or equal to max stock.
     * <p>
     * @return <code>true</code> if min is less than or equal to inventory and inventory is less
     *         than or equal to max. <code>false</code> otherwise.
     */
    private boolean validateInventory() {
        return Integer.parseInt(productMinTextfield.getText()) <= Integer.parseInt(productInvTextfield.getText()) &&
                (Integer.parseInt(productInvTextfield.getText()) <= Integer.parseInt(productMaxTextfield.getText()));
    }

    /**
     * helper method for clearing all textFields.
     */
    private void clearFields() {
        productNameTextfield.clear();
        productPriceTextfield.clear();
        productInvTextfield.clear();
        productMinTextfield.clear();
        productMaxTextfield.clear();
    }

    /**
     * initializes invalidation listeners and adds them to textFields.
     * <p>
     * this helper method is called during initialization, however, the
     * invalidation listeners are of interest to the onSave method and
     * used to flag the fields that the user has modified.
     */
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
