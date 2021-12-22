package com.example.socialtpygui.controller;

import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SignUpConfirmEmailWindowController {
    @FXML
    private TextField textFieldVerificationCodeSignUp;
    @FXML
    private Button  btnSignUpConfirmEmail;

    SuperService service;

    public void handlerBtnSignUpConfirmEmail(MouseEvent mouseEvent) {
    }

    public void setService(SuperService service) {this.service = service;}
}
