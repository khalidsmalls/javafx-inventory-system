package smalls.javafxinventorysystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import smalls.javafxinventorysystem.model.InHouse;
import smalls.javafxinventorysystem.model.Inventory;
import smalls.javafxinventorysystem.model.Outsourced;
import smalls.javafxinventorysystem.model.Part;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class ModifyPartController implements Initializable {

    private static final String PART_ID_TEXTFIELD_TEXT = "Auto Gen- Disabled";
    private static final String IN_HOUSE_LABEL = "Machine ID";
    private static final String OUTSOURCED_LABEL = "Company Name";
    @FXML private Label partWindowLabel;
    @FXML private Label dynamicPartLabel;
    @FXML private RadioButton inHouseRadioBtn;
    @FXML private RadioButton outsourcedRadioBtn;
    @FXML private TextField partIdTextfield;
    @FXML private TextField partNameTextfield;
    @FXML private TextField partInvTextfield;
    @FXML private TextField partPriceTextfield;
    @FXML private TextField partMaxTextfield;
    @FXML private TextField partMinTextfield;
    @FXML private TextField dynamicPartTextfield;
    private Inventory inv;
    private String partWindowLabelText;
    private ToggleGroup toggleGroup;
    private Part part;
    private int partIndex;
    private int partId;

    /**
     * the following functional interfaces restrict form input to valid
     * characters using regular expressions
     */
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
        if (newText.matches("([a-zA-Z\\- ']*)")) {
            return change;
        }
        return null;
    };


    public ModifyPartController() {
    }

    public ModifyPartController(Part part, String windowLabelText) {
        this.part = part;
        this.partWindowLabelText = windowLabelText;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partWindowLabel.setText(partWindowLabelText);
        inv = Inventory.getInstance();
        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(inHouseRadioBtn, outsourcedRadioBtn);
        inHouseRadioBtn.setSelected(true);
        partIdTextfield.setEditable(false);
        partIdTextfield.setText(String.valueOf(part.getId()));

        partInvTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partMaxTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partMinTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partPriceTextfield.setTextFormatter(new TextFormatter<Double>(doubleFilter));
        partNameTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
        dynamicPartTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));

        autoFillForm(part);
        partIndex = inv.getAllParts().indexOf(part);
        partId = part.getId();
    }

    public void onInHouseRadioClick() {
        dynamicPartLabel.setText(IN_HOUSE_LABEL);
        dynamicPartTextfield.clear();
        dynamicPartTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
    }

    public void onOutsourcedRadioClick() {
        dynamicPartLabel.setText(OUTSOURCED_LABEL);
        dynamicPartTextfield.clear();
        dynamicPartTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
    }

    public void onPartSave() {

    }

    public void onPartCancel(ActionEvent e) {
        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
    }

    private void autoFillForm(Part p) {
        partNameTextfield.setText(p.getName());
        partIdTextfield.setText(String.valueOf(p.getId()));
        partInvTextfield.setText(String.valueOf(p.getStock()));
        partPriceTextfield.setText(String.valueOf(p.getPrice()));
        partMaxTextfield.setText(String.valueOf(p.getMax()));
        partMinTextfield.setText(String.valueOf(p.getMin()));
        if (p instanceof InHouse) {
            dynamicPartTextfield.setText(String.valueOf(((InHouse) p).getMachineId()));
            dynamicPartLabel.setText(IN_HOUSE_LABEL);
            inHouseRadioBtn.setSelected(true);
        } else {
            dynamicPartTextfield.setText(((Outsourced) p).getCompanyName());
            dynamicPartLabel.setText(OUTSOURCED_LABEL);
            outsourcedRadioBtn.setSelected(true);
        }
    }

    private void clearFields() {
        partNameTextfield.clear();
        partPriceTextfield.clear();
        partInvTextfield.clear();
        partMinTextfield.clear();
        partMaxTextfield.clear();
        dynamicPartTextfield.clear();
    }
}

//public class ModifyPartController implements Initializable {
//    private final String inHouseDynamicLabelText = "Machine ID";
//    private final String outsourcedDynamicLabelText = "Company Name";
//    private final Part part;
//    @FXML
//    private RadioButton inHouseRadioBtn;
//
//    @FXML
//    private RadioButton outsourcedRadioBtn;
//
//    @FXML
//    private TextField partIdTextfield;
//
//    @FXML
//    private TextField partNameTextfield;
//
//    @FXML
//    private Button modifyPartSaveBtn;
//
//    @FXML
//    private Button modifyPartCancelBtn;
//
//    @FXML
//    private TextField partInvTextfield;
//
//    @FXML
//    private TextField partPriceTextfield;
//
//    @FXML
//    private TextField partMaxTextfield;
//
//    @FXML
//    private TextField partMinTextfield;
//
//    @FXML
//    private TextField dynamicTextfield;
//
//    @FXML
//    private Label dynamicLabel;
//    private String formLabelText;
//    @FXML
//    private Label formLabel;
//
//    private String errorMessage;
//
//    private ToggleGroup toggleGroup;
//
//    private Inventory inv;
//    private int partIndex;
//    private int partId;
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
//    public ModifyPartController(Part p, String formLabelText) {
//        this.part = p;
//        this.formLabelText = formLabelText;
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        inv = Inventory.getInstance();
//        toggleGroup = new ToggleGroup();
//        toggleGroup.getToggles().addAll(inHouseRadioBtn, outsourcedRadioBtn);
//        partIdTextfield.setEditable(false);
//        partIdTextfield.setText(String.valueOf(part.getId()));
//        formLabel.setText(formLabelText);
//
//        partInvTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
//        partMaxTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
//        partMinTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
//        partPriceTextfield.setTextFormatter(new TextFormatter<Double>(doubleFilter));
//        partNameTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
//        autoFillForm(part);
//        partIndex = inv.getAllParts().indexOf(part);
//        partId = part.getId();
//    }
//
//    public void onInHouseRadioClick() {
//        dynamicLabel.setText(inHouseDynamicLabelText);
//    }
//
//    public void onOutsourcedRadioBtnClick() {
//        dynamicLabel.setText(outsourcedDynamicLabelText);
//    }
//
//    public void onSave(ActionEvent e) {
//        try {
//            if ((!(partNameTextfield.getText() == null || partNameTextfield.getText().length() == 0 ||
//                    partPriceTextfield.getText() == null || partPriceTextfield.getText().length() == 0) ||
//                    partInvTextfield.getText() == null || partInvTextfield.getText().length() == 0 ||
//                    partMaxTextfield.getText() == null || partMaxTextfield.getText().length() == 0  ||
//                    partMinTextfield.getText() == null || partMinTextfield.getText().length() == 0) &&
//                    (Integer.valueOf(partMinTextfield.getText()) <= Integer.valueOf(partInvTextfield.getText()) &&
//                            (Integer.valueOf(partInvTextfield.getText()) <= Integer.valueOf(partMaxTextfield.getText())))) {
//
//                if ((toggleGroup.getSelectedToggle() == inHouseRadioBtn)) {
//                    InHouse newPart = new InHouse(partId, partNameTextfield.getText(), Double.parseDouble(partPriceTextfield.getText()),
//                            Integer.parseInt(partInvTextfield.getText()), Integer.parseInt(partMinTextfield.getText()),
//                            Integer.parseInt(partMaxTextfield.getText()), Integer.parseInt(dynamicTextfield.getText()));
//                    inv.updatePart(partIndex, newPart);
//                } else {
//                    Outsourced newPart = new Outsourced(partId, partNameTextfield.getText(), Double.parseDouble(partPriceTextfield.getText()),
//                            Integer.parseInt(partInvTextfield.getText()), Integer.parseInt(partMinTextfield.getText()),
//                            Integer.parseInt(partMaxTextfield.getText()), dynamicTextfield.getText());
//                    inv.updatePart(partIndex, newPart);
//                }
//                clearFields();
//                ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
//            }
//        } catch (NumberFormatException ex) {
//            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
//        }
//    }
//
//    public void onCancel(ActionEvent e) {
//        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
//    }
//
//
//    public void autoFillForm(Part p) {
//        partNameTextfield.setText(p.getName());
//        partIdTextfield.setText(String.valueOf(p.getId()));
//        partInvTextfield.setText(String.valueOf(p.getStock()));
//        partPriceTextfield.setText(String.valueOf(p.getPrice()));
//        partMaxTextfield.setText(String.valueOf(p.getMax()));
//        partMinTextfield.setText(String.valueOf(p.getMin()));
//        if (p instanceof InHouse) {
//            dynamicTextfield.setText(String.valueOf(((InHouse) p).getMachineId()));
//            dynamicLabel.setText(inHouseDynamicLabelText);
//            inHouseRadioBtn.setSelected(true);
//        } else {
//            dynamicTextfield.setText(((Outsourced) p).getCompanyName());
//            dynamicLabel.setText(outsourcedDynamicLabelText);
//            outsourcedRadioBtn.setSelected(true);
//        }
//    }
//
//    private void clearFields() {
//        partNameTextfield.clear();
//        partPriceTextfield.clear();
//        partInvTextfield.clear();
//        partMinTextfield.clear();
//        partMaxTextfield.clear();
//        dynamicTextfield.clear();
//    }
//}
