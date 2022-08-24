package smalls.javafxinventorysystem.controller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import smalls.javafxinventorysystem.model.Part;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    @FXML private Label productWindowLabel;
    @FXML private TextField productIdTextfield;
    @FXML private TextField productNameTextfield;
    @FXML private TextField productPriceTextfield;
    @FXML private TextField productMinTextfield;
    @FXML private TextField productMaxTextfield;
    @FXML private TextField partSearchTextfield;
    @FXML private Button partSearchBtn;
    @FXML private Button addAssocPartBtn;
    @FXML private Button removeAssocPartBtn;
    @FXML private Button closeBtn;
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
    private String productWindowLabelText;

    public AddProductController(String productWindowLabelText) {
        this.productWindowLabelText = productWindowLabelText;
    }

    @FXML private void onPartSearch() {
    }

    @FXML private void onAddAssocPart() {
    }

    @FXML private void onRemoveAssocPart() {
    }

    @FXML private void onClose(){
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productWindowLabel.setText(productWindowLabelText);
    }
}



//public class AddProductController implements Initializable {
//    private static final String PART_NOT_FOUND_MSG = "Part not found";
//    private final String PRODUCT_ID_TEXTFIELD_TEXT = "Auto Gen- Disabled";
//    @FXML
//    private Label productFormLabel;
//    private String formLabelText;
//    @FXML
//    private TextField productIdTextfield;
//    @FXML
//    private TextField productNameTextfield;
//    @FXML
//    private TextField productInvTextfield;
//    @FXML
//    private TextField productPriceTextfield;
//    @FXML
//    private TextField productMaxTextfield;
//    @FXML
//    private TextField productMinTextfield;
//    @FXML
//    private TextField productSearchTextfield;
//    @FXML
//    private Button partSearchBtn;
//    @FXML
//    private Button addAssocPartBtn;
//    @FXML
//    private Button removeAssocPartBtn;
//    @FXML
//    private Button productSaveBtn;
//    @FXML
//    private Button productCancelBtn;
//    @FXML
//    private TableView partsInvTable;
//    @FXML
//    private TableColumn partIdCol;
//    @FXML
//    private TableColumn partNameCol;
//    @FXML
//    private TableColumn partInvCol;
//    @FXML
//    private TableColumn partPriceCol;
//    @FXML
//    private TableView assocPartsTable;
//    @FXML
//    private TableColumn assocPartIdCol;
//    @FXML
//    private TableColumn assocPartNameCol;
//    @FXML
//    private TableColumn assocPartInvCol;
//    @FXML
//    private TableColumn assocPartPriceCol;
//    private Inventory inv;
//    private ObservableList<Part> associatedParts;
//
//    //functional interfaces for restricting text input to valid characters
//    private UnaryOperator<TextFormatter.Change> integerFilter = change -> {
//        String newText = change.getControlNewText();
//        if (newText.matches("([1-9][0-9]*)?")) {
//            return change;
//        }
//        return null;
//    };
//
//    private UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
//        String newText = change.getControlNewText();
//        if (newText.matches("[\\d]*(\\.((\\d{0,2})?))?")) {
//            return change;
//        }
//        return null;
//    };
//
//    private UnaryOperator<TextFormatter.Change> stringFilter = change -> {
//        String newText = change.getControlNewText();
//        if (newText.matches("([a-zA-Z\\-]*)")) {
//            return change;
//        }
//        return null;
//    };
//
//    public AddProductController(String formLabelText) {
//        this.formLabelText = formLabelText;
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        inv = Inventory.getInstance();
//        associatedParts = FXCollections.observableArrayList();
//        productIdTextfield.setEditable(false);
//        productIdTextfield.setText(PRODUCT_ID_TEXTFIELD_TEXT);
//        productFormLabel.setText(formLabelText);
//
//        productInvTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
//        productMaxTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
//        productMinTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
//        productPriceTextfield.setTextFormatter(new TextFormatter<Double>(doubleFilter));
//        productNameTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
//
//        partsInvTable.setItems(inv.getAllParts());
//        assocPartsTable.setItems(associatedParts);
//        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
//        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
//        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
//        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
//        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//        assocPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
//        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
//    }
//
//    public void onPartSearch() {
//        String searchString = productSearchTextfield.getText();
//        try {
//            Integer id = Integer.parseInt(searchString);
//            Part p = inv.lookupPart(id);
//            ObservableList<Part> partList = FXCollections.observableArrayList();
//            partList.add(p);
//            partsInvTable.setItems(partList);
//        } catch (Exception e) {
//            ObservableList<Part> partList = inv.lookupPart(searchString);
//            partsInvTable.setItems(partList);
//        }
//        partsInvTable.setPlaceholder(new Text(PART_NOT_FOUND_MSG));
//    }
//
//    public void onAddAssocPart() {
//        associatedParts.add((Part) partsInvTable.getSelectionModel().getSelectedItem());
//    }
//
//    public void onRemoveAssocPart() {
//        associatedParts.remove(assocPartsTable.getSelectionModel().getSelectedItem());
//    }
//
//    public void onProductSave(ActionEvent e) {
//        if ((!(productNameTextfield.getText() == null || productNameTextfield.getText().length() == 0 ||
//                productPriceTextfield.getText() == null || productPriceTextfield.getText().length() == 0) ||
//                productInvTextfield.getText() == null || productInvTextfield.getText().length() == 0 ||
//                productMaxTextfield.getText() == null || productMaxTextfield.getText().length() == 0  ||
//                productMinTextfield.getText() == null || productMinTextfield.getText().length() == 0) &&
//                (Integer.valueOf(productMinTextfield.getText()) <= Integer.valueOf(productInvTextfield.getText()) &&
//                        (Integer.valueOf(productInvTextfield.getText()) <= Integer.valueOf(productMaxTextfield.getText())))) {
//            try {
//                Product p = new Product(inv.getNextId(), productNameTextfield.getText(), Double.parseDouble(productPriceTextfield.getText()),
//                        Integer.parseInt(productInvTextfield.getText()), Integer.parseInt(productMinTextfield.getText()),
//                        Integer.parseInt(productMaxTextfield.getText()));
//                //add associated parts
//                if (assocPartsTable.getItems() != null) {
//                    for (Object part : assocPartsTable.getItems()) {
//                        p.addAssociatedPart((Part) part);
//                    }
//                }
//                inv.addProduct(p);
//                clearFields();
//                ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
//            } catch (NumberFormatException ex) {
//                System.out.println(ex.getMessage()); //TODO alert?
//            }
//        }
//    }
//
//    public void onProductCancel(ActionEvent e) {
//        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
//    }
//
//    private void clearFields() {
//        productNameTextfield.clear();
//        productPriceTextfield.clear();
//        productInvTextfield.clear();
//        productMinTextfield.clear();
//        productMaxTextfield.clear();
//    }
//}

