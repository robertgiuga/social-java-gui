package com.example.socialtpygui.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ShowConvItemController {
    @FXML
    private Text messageLabel;

    /**
     * set text from textField to value of parameter 'message'
     * @param message String representing the text of a Message
     */
    public void setMessage(String message){
        messageLabel.setText(message);
    }
}
