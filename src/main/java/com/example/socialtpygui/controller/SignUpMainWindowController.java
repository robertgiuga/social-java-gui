package com.example.socialtpygui.controller;
import com.example.socialtpygui.domainEvent.LoadView;
import com.example.socialtpygui.utils.HashStringSHA_256;
import com.example.socialtpygui.utils.JavaMailUtil;
import com.example.socialtpygui.utils.RandomString;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SignUpMainWindowController {

    @FXML
    public TextField textFieldSignUpFN;
    @FXML
    public TextField textFieldSignUpLN;
    @FXML
    public TextField textFieldSignUpEmail;
    @FXML
    public Button btnSignUpNext;
    @FXML
    public PasswordField passwordFieldSignUp;
    @FXML
    public PasswordField passwordFieldSignUpConfirm;

    String verificationConde = RandomString.getAlphaNumericString(10);

    /**
     * Handler for btnSignUpNext, if the text fields are not empty, then send a mail with verificationCode
     * and fire LoadView Event for load window for validation code
     * @param mouseEvent
     * @throws Exception
     */
    public void handlerBtnSignUpNext(MouseEvent mouseEvent) throws Exception {
        if ((textFieldSignUpLN.getText().length() == 0) || (textFieldSignUpFN.getText().length() == 0) || (textFieldSignUpEmail.getText().length() == 0)
        || (passwordFieldSignUp.getText().length() == 0) || (passwordFieldSignUpConfirm.getText().length() == 0))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Text fields can not be empty!!");
            alert.show();
        }
        else if (! passwordFieldSignUpConfirm.getText().equals(passwordFieldSignUp.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Passwords did not match!");
            alert.show();
        }
        else {
            JavaMailUtil.sendMail(textFieldSignUpEmail.getText(), verificationConde);
            btnSignUpNext.fireEvent(new LoadView(LoadView.SIGN_UP_NEXT, verificationConde));
        }
    }

    /**
     * @return text from textFieldSignUpEmail
     */
    public String getTextFieldSignUpEmail() {
        return textFieldSignUpEmail.getText();
    }

    /**
     * @return text from textFieldSignUpFN
     */
    public String getTextFieldSignUpFN() {
        return textFieldSignUpFN.getText();
    }

    /**
     * @return text from textFieldSignUpLN
     */
    public String getTextFieldSignUpLN() {
        return textFieldSignUpLN.getText();
    }

    /**
     * @return text from textFieldPassword
     */
    public String getTextFieldPassword()
    {

        try {
            return HashStringSHA_256.hashString(passwordFieldSignUp.getText());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return passwordFieldSignUp.getText();
    }


}
