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
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;


/**
 *  Responsible for functionality that adds an
 *  <code>InHouse</code> or <code>Outsourced</code>
 *  part to the <code>allParts</code> <code>ObservableList</code>.
 *
 * @author Khalid Smalls
 */
public class AddPartController implements Initializable {
    private static final String PART_ID_TEXTFIELD_TEXT = "Auto Gen- Disabled";
    private static final String IN_HOUSE_LABEL = "Machine ID";
    private static final String OUTSOURCED_LABEL = "Company Name";
    @FXML private Label partWindowLabel;
    @FXML private Label dynamicLabel;
    @FXML private RadioButton inHouseRadioBtn;
    @FXML private RadioButton outsourcedRadioBtn;
    @FXML private TextField partIdTextfield;
    @FXML private TextField partNameTextfield;
    @FXML private TextField partInvTextfield;
    @FXML private TextField partPriceTextfield;
    @FXML private TextField partMaxTextfield;
    @FXML private TextField partMinTextfield;
    @FXML private TextField dynamicPartTextfield;
    private final String partWindowLabelText;
    private ToggleGroup toggleGroup;


//       the following functional interfaces restrict form input to valid
//       characters using regular expressions
    private final UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.length() > 15) {
            return null;

        }
        if (newText.matches("([1-9][0-9]*)?")) {
            return change;
        }
        return null;
    };

    private final UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.length() > 15) {
            return null;
        }
        if (newText.matches("[\\d]*(\\.((\\d{0,2})?))?")) {
            return change;
        }
        return null;
    };

    private final UnaryOperator<TextFormatter.Change> stringFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.length() > 35) {
            return null;
        }
        if (newText.matches("([a-zA-Z\\- ']*)")) {
            return change;
        }
        return null;
    };

    /**
     * class constructor.
     * <p>
     * Gets window label text from caller.
     *
     * @param partWindowLabelText the text to set the main window label to
     */
    public AddPartController(String partWindowLabelText) {
        this.partWindowLabelText = partWindowLabelText;
    }

    /**
     * allows for customization of  nodes after
     * the scene graph is constructed, but before
     * the scene is displayed.
     * <p>
     * initializes radio toggle buttons,
     * sets selected toggle to in-house.
     * sets dynamic label to machine-id.
     * sets part textField non-editable,
     * sets main window label, and sets
     * text formatters on textFields.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(inHouseRadioBtn, outsourcedRadioBtn);
        inHouseRadioBtn.setSelected(true);
        dynamicLabel.setText(IN_HOUSE_LABEL);
        dynamicPartTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partIdTextfield.setEditable(false);
        partIdTextfield.setText(PART_ID_TEXTFIELD_TEXT);
        partWindowLabel.setText(partWindowLabelText);

        setTextFormatters();
    }

    /**
     * specifies an <code>InHouse</code> part.
     * <p>
     * sets dynamic label text to "machine id" and sets
     * integer filter on corresponding textField. Also
     * clears the textField if it expects a company name.
     */
    @FXML private void onInHouseRadioClick() {
        dynamicLabel.setText(IN_HOUSE_LABEL);
        dynamicPartTextfield.clear();
        dynamicPartTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
    }

    /**
     * specifies an <code>outsourced</code> part.
     * <p>
     * sets dynamic label text to "company name" and sets
     * string filter on corresponding textField. Also
     * clears the textField if it expects a machine id.
     */
    @FXML private void onOutsourcedRadioClick() {
        dynamicLabel.setText(OUTSOURCED_LABEL);
        dynamicPartTextfield.clear();
        dynamicPartTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
    }

    /**
     * attempts to add a <code>Part</code> to the
     * <code>allParts</code> <code>ObservableList</code>.
     * <p>
     * validates all fields are populated with relevant data and
     * that user-entered min stock is less than or equal to inventory
     * and inventory is less than or equal to max stock before
     * creating an <code>InHouse</code> or <code>Outsourced</code>
     * part and adding it to <code>allParts</code>.
     *
     * @param event  object that allows access to the stage, so that
     *               it may be closed after the part is added to inventory
     */
    @FXML private void onPartSave(ActionEvent event) {
        Part p = null;
        String newPartName;
        double newPartPrice;
        int newPartId, newPartInv, newPartMin, newPartMax;

        if (validateFields()) {
            if (validateInventory()) {
                newPartId = Inventory.getNextPartId();
                newPartName = partNameTextfield.getText();
                newPartPrice = Double.parseDouble(partPriceTextfield.getText());
                newPartInv = Integer.parseInt(partInvTextfield.getText());
                newPartMin = Integer.parseInt(partMinTextfield.getText());
                newPartMax = Integer.parseInt(partMaxTextfield.getText());

                if (toggleGroup.getSelectedToggle() == inHouseRadioBtn) {
                    int newPartMachineId;
                    try {
                        newPartMachineId = Integer.parseInt(dynamicPartTextfield.getText());
                    } catch (NumberFormatException e) {
                        new Alert(Alert.AlertType.ERROR, "Please enter a machine id").showAndWait();
                        return;
                    }
                    p = new InHouse(
                            newPartId, newPartName, newPartPrice, newPartInv,
                            newPartMin, newPartMax, newPartMachineId
                    );
                    Inventory.addPart(p);
                }
                if (toggleGroup.getSelectedToggle() == outsourcedRadioBtn) {
                    String newPartCompanyName = dynamicPartTextfield.getText();
                    if (newPartCompanyName.equals("")) {
                        new Alert(Alert.AlertType.ERROR, "Please enter a company name").showAndWait();
                        return;
                    }
                    p = new Outsourced(
                            newPartId, newPartName, newPartPrice, newPartInv,
                            newPartMin, newPartMax, newPartCompanyName
                    );
                    Inventory.addPart(p);
                }
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

    /**
     * closes window without saving part.
     *
     * @param event the object fired on click which allows access to
     *          the window object so that it may be closed
     */
    @FXML private void onPartCancel(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    /**
     * helper method validates all fields are populated when user
     * clicks save button.
     *
     * @return <code>true</code> if all fields are populated, <code>false</code> if not
     */
    private boolean validateFields() {
        return !(partNameTextfield.getText().equals("") || partNameTextfield.getText().length() == 0 ||
                partPriceTextfield.getText().equals("") || partPriceTextfield.getText().length() == 0 ||
                partInvTextfield.getText().equals("") || partInvTextfield.getText().length() == 0 ||
                partMaxTextfield.getText().equals("") || partMaxTextfield.getText().length() == 0 ||
                partMinTextfield.getText().equals("") || partMinTextfield.getText().length() == 0);
    }

    /**
     * helper method validates the user entered an inventory value that is greater than or
     * equal to min stock and less than or equal to max stock.
     * <p>
     * @return <code>true</code> if min is less than or equal to inventory and inventory is less than
     *          or equal to max, <code>false</code> otherwise.
     */
    private boolean validateInventory() {
        return Integer.parseInt(partMinTextfield.getText()) <= Integer.parseInt(partInvTextfield.getText()) &&
                (Integer.parseInt(partInvTextfield.getText()) <= Integer.parseInt(partMaxTextfield.getText()));
    }

    /**
     * helper method clears all textFields.
     */
    private void clearFields() {
        partNameTextfield.clear();
        partPriceTextfield.clear();
        partInvTextfield.clear();
        partMinTextfield.clear();
        partMaxTextfield.clear();
        dynamicPartTextfield.clear();
    }

    /**
     *  helper method sets text formatters on textFields to ensure valid user input.
     */
    private void setTextFormatters() {
        partInvTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partMaxTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partMinTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partPriceTextfield.setTextFormatter(new TextFormatter<Double>(doubleFilter));
        partNameTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
    }

}//END of class


