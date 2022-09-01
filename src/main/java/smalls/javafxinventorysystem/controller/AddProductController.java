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
import smalls.javafxinventorysystem.model.Inventory;
import smalls.javafxinventorysystem.model.Part;
import smalls.javafxinventorysystem.model.Product;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class AddProductController implements Initializable {

    private static final String PRODUCT_ID_FIELD_TEXT = "Auto Gen- Disabled";
    private static final String PART_NOT_FOUND_MSG = "Part not found";
    @FXML private Label productWindowLabel;
    @FXML private TextField productIdTextfield;
    @FXML private TextField productNameTextfield;
    @FXML private TextField productPriceTextfield;
    @FXML private TextField productInvTextfield;
    @FXML private TextField productMinTextfield;
    @FXML private TextField productMaxTextfield;
    @FXML private TextField partSearchTextfield;
    @FXML private TableView<Part> partsTable;
    @FXML private TableView<Part>  assocPartsTable;
    @FXML private TableColumn<Part,Integer> partIdCol;
    @FXML private TableColumn<Part,String> partNameCol;
    @FXML private TableColumn<Part,Double> partPriceCol;
    @FXML private TableColumn<Part,Integer> partInvCol;
    @FXML private TableColumn<Part,Integer> assocPartIdCol;
    @FXML private TableColumn<Part,String> assocPartNameCol;
    @FXML private TableColumn<Part,Integer> assocPartInvCol;
    @FXML private TableColumn<Part,Double> assocPartPriceCol;
    @FXML private ObservableList<Part> assocParts;
    private Inventory inv;
    private Stage stage;
    private final String productWindowLabelText;

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

    public AddProductController(String productWindowLabelText) {
        this.productWindowLabelText = productWindowLabelText;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productWindowLabel.setText(productWindowLabelText);
        inv = Inventory.getInstance();
        assocParts = FXCollections.observableArrayList();
        productIdTextfield.setEditable(false);
        productIdTextfield.setText(PRODUCT_ID_FIELD_TEXT);

        setTextFormatters();
        initPartsTable();
        initAssocPartsTable();
    }

    @FXML private void onPartSearch() {
        String searchString = partSearchTextfield.getText();
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
        partsTable.setPlaceholder(new Text(PART_NOT_FOUND_MSG));
    }

    @FXML private void onAddAssocPart() {
        if (partsTable.getSelectionModel().getSelectedItems().size() > 1) {
            assocParts.addAll(partsTable.getSelectionModel().getSelectedItems());
        } else {
            assocParts.add(partsTable.getSelectionModel().getSelectedItem());
        }
    }

    @FXML private void onRemoveAssocPart() {
        if (assocPartsTable.getSelectionModel().getSelectedItems().size() > 1) {
            assocParts.removeAll(assocPartsTable.getSelectionModel().getSelectedItems());
        } else {
            assocParts.remove(assocPartsTable.getSelectionModel().getSelectedItem());
        }
    }

    @FXML private void onSave(ActionEvent e) {
        Product p = null;
        String newProductName;
        double newProductPrice;
        int newProductId, newProductInv, newProductMin, newProductMax;

        if (validateFields()) {
            if (validateInventory()) {
                newProductId = inv.getNextProductId();
                newProductName = productNameTextfield.getText();
                newProductPrice = Double.parseDouble(productPriceTextfield.getText());
                newProductInv = Integer.parseInt(productInvTextfield.getText());
                newProductMin = Integer.parseInt(productMinTextfield.getText());
                newProductMax = Integer.parseInt(productMaxTextfield.getText());

                p = new Product(newProductId, newProductName, newProductPrice,
                        newProductInv, newProductMin, newProductMax);
                if (assocPartsTable.getItems().size() > 0) {
                    for (Part part : assocPartsTable.getItems()) {
                        p.addAssociatedPart(part);
                    }
                }
                inv.addProduct(p);
                clearFields();
                ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
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

    @FXML private void onClose(ActionEvent e){
        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
    }

    private void  initPartsTable() {
        partsTable.setItems(inv.getAllParts());
        partsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void initAssocPartsTable() {
        assocPartsTable.setItems(assocParts);
        assocPartsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void setTextFormatters() {
        productInvTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        productMaxTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        productMinTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        productPriceTextfield.setTextFormatter(new TextFormatter<Double>(doubleFilter));
        productNameTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
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

}
