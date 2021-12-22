package com.example.socialtpygui.controller;
import com.example.socialtpygui.domainEvent.LoadView;
import javafx.fxml.FXML;
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


    public void handlerBtnSignUpNext(MouseEvent mouseEvent) throws Exception {
        btnSignUpNext.fireEvent(new LoadView(LoadView.SIGN_UP_NEXT));
    }

}
