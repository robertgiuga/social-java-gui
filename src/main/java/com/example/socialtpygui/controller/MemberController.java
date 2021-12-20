package com.example.socialtpygui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MemberController {

    @FXML
    private Label nameLbl;
    @FXML
    private Button removeBtn;

    private String id;

    public void setName(String name){
        nameLbl.setText(name);
    }

    public void setId(String id) {
        this.id = id;
    }

    @FXML
    private void handlerRemoveBtn() {
        System.out.println(id);
    }
}
