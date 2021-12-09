package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SearchItemController {

    @FXML
    private Label nameLbl;

    @FXML
    private Button addBtn;

    private UserDTO userDTO;

    public void setData(UserDTO userDTO)
    {
        this.userDTO = userDTO;
        nameLbl.setText(userDTO.getFirstName() + " " + userDTO.getLastName());
    }

    public void hideAddBtn()
    {
        addBtn.setVisible(false);
    }
}
