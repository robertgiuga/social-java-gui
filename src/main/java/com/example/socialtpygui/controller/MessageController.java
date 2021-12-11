package com.example.socialtpygui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MessageController {

    @FXML
    private TextField searchFriendBar;

    @FXML
    private void handlerSearchBarKeyPressed(KeyEvent e){
        if(e.getCode().equals(KeyCode.ENTER)){
            searchFriendBar.setText("aiurea");
        }
    }
}
