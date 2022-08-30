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

public class ModifyPartController implements Initializable {
    private final String IN_HOUSE_LABEL = "Machine ID";
    private final String OUTSOURCED_LABEL = "Company Name";
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
    private Part part;
    private int partIndex;
    private InvalidationListener partNameChangeListener;
    private InvalidationListener partPriceChangeListener;
    private InvalidationListener partInvChangeListener;
    private InvalidationListener partMinChangeListener;
    private InvalidationListener partMaxChangeListener;
    private boolean partNameModified;
    private boolean partPriceModified;
    private boolean partInvModified;
    private boolean partMinModified;
    private boolean partMaxModified;

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
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(inHouseRadioBtn, outsourcedRadioBtn);
        inHouseRadioBtn.setSelected(true);
        partIdTextfield.setEditable(false);
        partIdTextfield.setText(String.valueOf(part.getId()));
        partIndex = inv.getAllParts().indexOf(part);
        setTextFormatters();
        autoFillForm(part);
        initModifiedPropertyBooleans();
        initInvalidationListeners();
        addInvalidationListeners();
    }//END of initialize

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
        if (validateFields()) {
            if (validateInventory()) {
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
                inv.updatePart(partIndex, part);
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

    @FXML private void onPartCancel(ActionEvent e) {
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

    public boolean validateFields() {
        return !(partNameTextfield.getText().equals("") || partNameTextfield.getText().length() == 0 ||
                partPriceTextfield.getText().equals("") || partPriceTextfield.getText().length() == 0 ||
                partInvTextfield.getText().equals("") || partInvTextfield.getText().length() == 0 ||
                partMaxTextfield.getText().equals("") || partMaxTextfield.getText().length() == 0 ||
                partMinTextfield.getText().equals("") || partMinTextfield.getText().length() == 0);
    }

    public boolean validateInventory() {
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

    public void initModifiedPropertyBooleans() {
        partNameModified = false;
        partPriceModified = false;
        partInvModified = false;
        partMinModified = false;
        partMaxModified = false;
    }

    public void setTextFormatters() {
        partInvTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partMaxTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partMinTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
        partPriceTextfield.setTextFormatter(new TextFormatter<Double>(doubleFilter));
        partNameTextfield.setTextFormatter(new TextFormatter<String>(stringFilter));
        dynamicPartTextfield.setTextFormatter(new TextFormatter<Integer>(integerFilter));
    }

    public void initInvalidationListeners() {
        partNameChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                partNameModified = true;
            }
        };
        partPriceChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                partPriceModified = true;
            }
        };
        partInvChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                partInvModified = true;
            }
        };
        partMinChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                partMinModified = true;
            }
        };
        partMaxChangeListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                partMaxModified = true;
            }
        };
    }

    public void addInvalidationListeners() {
        partNameTextfield.textProperty().addListener(partNameChangeListener);
        partPriceTextfield.textProperty().addListener(partPriceChangeListener);
        partInvTextfield.textProperty().addListener(partInvChangeListener);
        partMinTextfield.textProperty().addListener(partMinChangeListener);
        partMaxTextfield.textProperty().addListener(partMaxChangeListener);
    }

}



