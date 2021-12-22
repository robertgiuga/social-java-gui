package com.example.socialtpygui.controller;
import com.example.socialtpygui.domainEvent.LoadView;
import com.example.socialtpygui.utils.RandomString;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;





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
            System.out.println(verificationConde);
            btnSignUpNext.fireEvent(new LoadView(LoadView.SIGN_UP_NEXT, verificationConde));
        }
    }

    public String getTextFieldSignUpEmail() {
        return textFieldSignUpEmail.getText();
    }

    public String getTextFieldSignUpFN() {
        return textFieldSignUpFN.getText();
    }

    public String getTextFieldSignUpLN() {
        return textFieldSignUpLN.getText();
    }

    public String getTextFieldPassword(){return passwordFieldSignUp.getText();}


}
