package com.example.socialtpygui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LogInController {

    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField passswordTxt;
    @FXML
    private Button loginBtn;

    @FXML
    private void handlerLogIn(){
        usernameTxt.setText("buton apasat");
    }

}
