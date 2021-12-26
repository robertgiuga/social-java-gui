package com.example.socialtpygui.controller;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class FriendReportController {
    public Label nameLbl;
    public RadioButton radioBtn;

    public void setName(String s) {
        nameLbl.setText(s);
    }

    public void setToggleGroup(ToggleGroup toggleGroup) {
        radioBtn.setToggleGroup(toggleGroup);
    }

    public void setId(String id) {
        radioBtn.setId(id);
    }
}
