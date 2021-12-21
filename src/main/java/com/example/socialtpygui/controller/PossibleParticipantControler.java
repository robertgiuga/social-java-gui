package com.example.socialtpygui.controller;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class PossibleParticipantControler {
    public Label nameLbl;
    public CheckBox check;

    /**
     * sets the name to be displayed
     * @param name
     */
    public void setName(String name){
        nameLbl.setText(name);
    }

    /**
     * sets the user displayed id in the checkbox.id field
     * @param id
     */
    public void setId(String id) {
        check.setId(id);
    }
}
