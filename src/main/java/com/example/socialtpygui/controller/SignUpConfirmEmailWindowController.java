package com.example.socialtpygui.controller;

import com.example.socialtpygui.domainEvent.LoadView;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SignUpConfirmEmailWindowController {
    @FXML
    private TextField textFieldVerificationCodeSignUp;
    @FXML
    private Button  btnSignUpConfirmEmail;

    SuperService service;
    String verificationCode;

    /**
     * Handler for btnSignUpConfirmEmail, if the text from textFieldVerificationCodeSignUp is equal
     * with verificationCode fire LoadEvent Event for SignUp
     * @param mouseEvent MouseEvent
     */
    public void handlerBtnSignUpConfirmEmail(MouseEvent mouseEvent) {
        if (textFieldVerificationCodeSignUp.getText().equals(verificationCode))
        {
           btnSignUpConfirmEmail.fireEvent(new LoadView(LoadView.FINAL_SIGN_UP, null));
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Verification Code is invalid! Try again.");
            alert.show();
        }
    }

    /**
     * Set service
     * @param service SuperService
     */
    public void setService(SuperService service) {this.service = service;}

    /**
     * Set verification code
     * @param verificationCode String
     */
    public void setVerificationCode(String verificationCode) {this.verificationCode = verificationCode;}
}
