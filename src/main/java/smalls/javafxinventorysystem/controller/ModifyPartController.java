package smalls.javafxinventorysystem.controller;

import smalls.javafxinventorysystem.model.Part;

public class ModifyPartController {

    private Part p;
    private String formLabelText;

    public ModifyPartController() {
    }

    public ModifyPartController(Part p, String formLabelText) {
        this.p = p;
        this.formLabelText = formLabelText;
    }


}
