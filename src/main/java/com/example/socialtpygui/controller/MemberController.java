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

    /**
     * sets the name witch is gonna be displayed
     * @param name
     */
    public void setName(String name){
        nameLbl.setText(name);
    }

    /**
     * sets the id of the user witch is displayed
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    @FXML
    private void handlerRemoveBtn() {
        System.out.println(id);
    }
}
