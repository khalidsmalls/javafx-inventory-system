package smalls.javafxinventorysystem.controller;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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

/**
 * Responsible for funcionality that allows end-user
 * to modify an existing <code>Part</code>.
 *
 * @author Khalid Smalls
 */
public class ModifyPartController implements Initializable {
    private final String IN_HOUSE_LABEL = "Machine ID";
    private final String OUTSOURCED_LABEL = "Company Name";
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
    private final Part part;
    private int partIndex;
    private boolean partNameModified;
    private boolean partPriceModified;
    private boolean partInvModified;
    private boolean partMinModified;
    private boolean partMaxModified;
    private boolean dynamicFieldModified;
    private ToggleGroup toggleGroup;

//       the following functional interfaces restrict form input to valid
//       characters using regular expressions
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

    /**
     * class constructor.
     * <p>
     * gets the <code>Part</code> to be modified and <code>partWindowLabelText</code>
     * from the caller.
     *
     * @param part the part to be modified
     * @param windowLabelText the main window label text to set
     */
    public ModifyPartController(Part part, String windowLabelText) {
        this.part = part;
        this.partWindowLabelText = windowLabelText;
    }

    /**
     * allows for customization of nodes after the
     * scene graph is constructed, but before the scene is displayed.
     * <p>
     * initializes radio toggles, gets
     * inventory instance, part index,
     * initializes part id textfield as non-editable,
     * sets text formatters on textfields,
     * populates textFields with part data,
     * sets invalidation listeners on textFields
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partWindowLabel.setText(partWindowLabelText);
        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(inHouseRadioBtn, outsourcedRadioBtn);
        partIdTextfield.setEditable(false);
        partIdTextfield.setText(String.valueOf(part.getId()));
        partIndex = Inventory.getAllParts().indexOf(part);
        if (part instanceof InHouse) {
            inHouseRadioBtn.setSelected(true);
            dynamicLabel.setText(IN_HOUSE_LABEL);
        }
        if (part instanceof Outsourced) {
            outsourcedRadioBtn.setSelected(true);
            dynamicLabel.setText(OUTSOURCED_LABEL);
        }
        setTextFormatters();
        autoFillForm(part);
        initInvalidationListeners();
    }//END of initialize

    /**
     * specifies an <code>InHouse</code> part.
     * <p>
     * sets <code>dynamicLabel</code> text to "machine id" and sets
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
     * saves a modified part or creates a new part if the user selects
     * the other part type, and saves the part to the same index.
     * <p>
     * First, validates fields are populated and that min stock
     * entered by user is less than or equal to inventory and inventory
     * is less than or equal to max stock using helper methods
     * <code>validateFields</code> and <code>validateInventory</code>.
     * <p>
     * If user changes the part type with a radio toggle,
     * <code>createNewPart</code> is called using the same part_id
     * for the created part so that it may be re-inserted into the list
     * at the same index. Otherwise, <code>modifyPart</code> helper method
     * is called to modify the part through setters.
     *
     * @param event allows access to the stage, so that it may be closed
     *              after part is saved
     */
    @FXML private void onPartSave(ActionEvent event) {
        if (validateFields()) {
            if (validateInventory()) {
                if ((part instanceof InHouse && outsourcedRadioBtn.isSelected()) ||
                        (part instanceof Outsourced && inHouseRadioBtn.isSelected())) {
                    createNewPart(event);
                } else {
                    modifyPart(event);
                }
//                clearFields();
//                ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
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
     * onPartSave helper method,
     * modifies part through setters.
     */
    private void modifyPart(ActionEvent event) {
        if (partNameModified) {
            part.setName(partNameTextfield.getText());
        }
        if (partPriceModified) {
            part.setPrice(Double.parseDouble(partPriceTextfield.getText()));
        }
        if (partInvModified) {
            part.setStock(Integer.parseInt(partInvTextfield.getText()));
        }
        if (partMinModified) {
            part.setMin(Integer.parseInt(partMinTextfield.getText()));
        }
        if (partMaxModified) {
            part.setMax(Integer.parseInt(partMaxTextfield.getText()));
        }
        if (dynamicFieldModified) {
            if (part instanceof InHouse) {
                try {
                    ((InHouse) part).setMachineId(Integer.parseInt(dynamicPartTextfield.getText()));
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Please enter a machine id").showAndWait();
                    return;
                }
            }
            if (part instanceof Outsourced) {
                if (dynamicPartTextfield.getText().equals("")) {
                    new Alert(Alert.AlertType.ERROR, "Please enter a company name").showAndWait();
                    return;
                }
                ((Outsourced) part).setCompanyName(dynamicPartTextfield.getText());
            }
        }
        Inventory.updatePart(partIndex, part);
        clearFields();
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    /**
     *   onPartSave helper method.
     *   <p>
     *   user changed part type from in-house to outsourced
     *   or outsourced to in-house, so a new part is
     *   created.
     */
    private void createNewPart(ActionEvent event) {
        Part updatedPart = null;
        int newPartId = part.getId();
        String newPartName = partNameTextfield.getText();
        double newPartPrice = Double.parseDouble(partPriceTextfield.getText());
        int newPartInv = Integer.parseInt(partInvTextfield.getText());
        int newPartMin = Integer.parseInt(partMinTextfield.getText());
        int newPartMax = Integer.parseInt(partMaxTextfield.getText());

        if (toggleGroup.getSelectedToggle() == inHouseRadioBtn) {
            int newPartMachineId = 0;
            try {
                newPartMachineId = Integer.parseInt(dynamicPartTextfield.getText());
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Please enter a machine id").showAndWait();
                return;
            }
            updatedPart = new InHouse(
                    newPartId, newPartName, newPartPrice, newPartInv,
                    newPartMin, newPartMax, newPartMachineId
            );
        }
        if (toggleGroup.getSelectedToggle() == outsourcedRadioBtn) {
            String newPartCompanyName = dynamicPartTextfield.getText();
            if (newPartCompanyName.equals("")) {
                new Alert(Alert.AlertType.ERROR, "Please enter a company name").showAndWait();
                return;
            }
            updatedPart = new Outsourced(
                    newPartId, newPartName, newPartPrice, newPartInv,
                    newPartMin, newPartMax, newPartCompanyName
            );
        }
        if (updatedPart != null) {
            Inventory.updatePart(partIndex, updatedPart);
            clearFields();
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }
    }//END of create new part

    /**
     * closes window.
     *
     * @param event event object - allows access to the stage
     *          so that it may be closed
     */
    @FXML private void onPartCancel(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    /**
     *  helper method used to populate textFields on
     *  initialization with part data to be modified.
     *
     * @param p the part whose attributes will populate
     *          the form fields
     */
    private void autoFillForm(Part p) {
        partNameTextfield.setText(p.getName());
        partIdTextfield.setText(String.valueOf(p.getId()));
        partInvTextfield.setText(String.valueOf(p.getStock()));
        partPriceTextfield.setText(String.valueOf(p.getPrice()));
        partMaxTextfield.setText(String.valueOf(p.getMax()));
        partMinTextfield.setText(String.valueOf(p.getMin()));
        if (p instanceof InHouse) {
            dynamicPartTextfield.setText(String.valueOf(((InHouse) p).getMachineId()));
            dynamicLabel.setText(IN_HOUSE_LABEL);
            inHouseRadioBtn.setSelected(true);
        }
        if (p instanceof Outsourced) {
            dynamicPartTextfield.setText(((Outsourced) p).getCompanyName());
            dynamicLabel.setText(OUTSOURCED_LABEL);
            outsourcedRadioBtn.setSelected(true);
        }
    }

    /**
     * validates all fields except dynamic textfield are populated.
     *
     * @return <code>true</code> if fields are populated. <code>false</code> if not.
     */
    public boolean validateFields() {
        return !(partNameTextfield.getText().equals("") || partNameTextfield.getText().length() == 0 ||
                partPriceTextfield.getText().equals("") || partPriceTextfield.getText().length() == 0 ||
                partInvTextfield.getText().equals("") || partInvTextfield.getText().length() == 0 ||
                partMaxTextfield.getText().equals("") || partMaxTextfield.getText().length() == 0 ||
                partMinTextfield.getText().equals("") || partMinTextfield.getText().length() == 0);
    }

    /**
     * validates user-entered min stock is less that or equal to inventory and inventory is
     *  less than or equal to max stock entered by user.
     *
     * @return <code>true</code> if min is less than or equal to inventory and inventory is
     * less than or equal to max. <code>false</code> if not.
     */
    public boolean validateInventory() {
        return Integer.parseInt(partMinTextfield.getText()) <= Integer.parseInt(partInvTextfield.getText()) &&
                (Integer.parseInt(partInvTextfield.getText()) <= Integer.parseInt(partMaxTextfield.getText()));
    }

    /**
     * helper method clears all textFields
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
     * helper method sets regex filters on textFields to restrict user input to valid
     * data
     */
    public void setTextFormatters() {
        partInvTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partMaxTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partMinTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partPriceTextfield.setTextFormatter(new TextFormatter<Double>(doubleFilter));
        partNameTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
        if (part instanceof InHouse) {
            dynamicPartTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        }
        if (part instanceof Outsourced) {
            dynamicPartTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
        }

    }

    /**
     * helper method initializes invalidation listeners and adds them to textFields.
     * this helper method is called during initialization, however, the
     * invalidation listeners are of interest to the modifyPart helper
     * method called by onPartSave and used to flag the fields that the user
     * has modified.
     */
    public void initInvalidationListeners() {
        partNameModified = false;
        partPriceModified = false;
        partInvModified = false;
        partMinModified = false;
        partMaxModified = false;
        dynamicFieldModified = false;
        InvalidationListener partNameChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                partNameModified = true;
            }
        };
        InvalidationListener partPriceChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                partPriceModified = true;
            }
        };
        InvalidationListener partInvChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                partInvModified = true;
            }
        };
        InvalidationListener partMinChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                partMinModified = true;
            }
        };
        InvalidationListener partMaxChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                partMaxModified = true;
            }
        };
        InvalidationListener dynamicFieldChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                dynamicFieldModified = true;
            }
        };

        partNameTextfield.textProperty().addListener(partNameChangeListener);
        partPriceTextfield.textProperty().addListener(partPriceChangeListener);
        partInvTextfield.textProperty().addListener(partInvChangeListener);
        partMinTextfield.textProperty().addListener(partMinChangeListener);
        partMaxTextfield.textProperty().addListener(partMaxChangeListener);
        dynamicPartTextfield.textProperty().addListener(dynamicFieldChangeListener);
    }

}



