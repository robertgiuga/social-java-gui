package com.example.socialtpygui.controller;

import com.example.socialtpygui.RunApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {

    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField passswordTxt;
    @FXML
    private Button logInBtn;

    @FXML
    private void handlerLogIn() throws IOException {
        //verify if ok login

        FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("mainWindow.fxml"));
        Stage manWindowStage= new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 377, 210);
        manWindowStage.setScene(scene);
        manWindowStage.initModality(Modality.NONE);
        manWindowStage.show();

        Stage logInStage =(Stage) logInBtn.getScene().getWindow();
        logInStage.close();
    }

}
