package smalls.javafxinventorysystem.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class AddPartController implements Initializable {
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

    public AddPartController() {
    }

    public AddPartController(String partWindowLabelText) {
        this.partWindowLabelText = partWindowLabelText;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inv = Inventory.getInstance();
        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(inHouseRadioBtn, outsourcedRadioBtn);
        inHouseRadioBtn.setSelected(true);
        dynamicPartLabel.setText(IN_HOUSE_LABEL);
        dynamicPartTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partIdTextfield.setEditable(false);
        partIdTextfield.setText(PART_ID_TEXTFIELD_TEXT);
        partWindowLabel.setText(partWindowLabelText);

        setTextFormatters();
    }

    @FXML private void onInHouseRadioClick() {
        dynamicPartLabel.setText(IN_HOUSE_LABEL);
        dynamicPartTextfield.clear();
        dynamicPartTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
    }

    @FXML private void onOutsourcedRadioClick() {
        dynamicPartLabel.setText(OUTSOURCED_LABEL);
        dynamicPartTextfield.clear();
        dynamicPartTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
    }

    @FXML private void onPartSave(ActionEvent event) {
        Part p = null;
        String newPartName;
        double newPartPrice;
        int newPartId, newPartInv, newPartMin, newPartMax;

        if (validateFields()) {
            if (validateInventory()) {
                newPartId = inv.getNextId();
                newPartName = partNameTextfield.getText();
                newPartPrice = Double.parseDouble(partPriceTextfield.getText());
                newPartInv = Integer.parseInt(partInvTextfield.getText());
                newPartMin = Integer.parseInt(partMinTextfield.getText());
                newPartMax = Integer.parseInt(partMaxTextfield.getText());

                if (toggleGroup.getSelectedToggle() == inHouseRadioBtn) {
                    int newPartMachineId = Integer.parseInt(dynamicPartTextfield.getText());
                    p = new InHouse(
                            newPartId, newPartName, newPartPrice, newPartInv,
                            newPartMin, newPartMax, newPartMachineId
                    );
                } else {
                    String newPartCompanyName = dynamicPartTextfield.getText();
                    p = new Outsourced(
                            newPartId, newPartName, newPartPrice, newPartInv,
                            newPartMin, newPartMax, newPartCompanyName
                    );
                }

                inv.addPart(p);
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
    }//END of onPartSave

    @FXML private void onPartCancel(ActionEvent e) {
        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
    }

    private boolean validateFields() {
        return !(partNameTextfield.getText().equals("") || partNameTextfield.getText().length() == 0 ||
                partPriceTextfield.getText().equals("") || partPriceTextfield.getText().length() == 0 ||
                partInvTextfield.getText().equals("") || partInvTextfield.getText().length() == 0 ||
                partMaxTextfield.getText().equals("") || partMaxTextfield.getText().length() == 0 ||
                partMinTextfield.getText().equals("") || partMinTextfield.getText().length() == 0);
    }

    private boolean validateInventory() {
        return Integer.parseInt(partMinTextfield.getText()) <= Integer.parseInt(partInvTextfield.getText()) &&
                (Integer.parseInt(partInvTextfield.getText()) <= Integer.parseInt(partMaxTextfield.getText()));
    }

    private void clearFields() {
        partNameTextfield.clear();
        partPriceTextfield.clear();
        partInvTextfield.clear();
        partMinTextfield.clear();
        partMaxTextfield.clear();
        dynamicPartTextfield.clear();
    }

    private void setTextFormatters() {
        partInvTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partMaxTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partMinTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partPriceTextfield.setTextFormatter(new TextFormatter<Double>(doubleFilter));
        partNameTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
    }

}//END of class


