package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.time.LocalDate;

public class RequestItemController {
    @FXML
    private ImageView acceptBtn;

    @FXML
    private ImageView declineBtn;

    @FXML
    private Label requestLabel;

    @FXML
    private Label dateLabel;

    private RequestController requestController;
    private SuperService service;
    private User loggedUser;

    @FXML
    private void handlerDeclineButton(){
        service.declineRequest(loggedUser.getId(), requestLabel.getId());
    }

    @FXML
    private void handlerAcceptButton(){
        service.acceptRequest(loggedUser.getId(), requestLabel.getId());
    }

    public void setName(String name){
        requestLabel.setText(name);
    }

    public void setEmail(String email){
        requestLabel.setId(email);
    }

    public void setDate(LocalDate date){
        dateLabel.setText(String.valueOf(date));
    }

    public void setService(SuperService service){
        this.service = service;
    }

    public void setLoggedUser(User loggedUser){
        this.loggedUser = loggedUser;
    }
}
