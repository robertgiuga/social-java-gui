package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.LocalDate;

public class RequestItemController {
    @FXML
    private Button acceptBtn;

    @FXML
    private Button declineBtn;

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
        requestController.deleteItemFromGridPane(declineBtn.getId());
    }

    @FXML
    private void handlerAcceptButton(){
        service.acceptRequest(loggedUser.getId(), requestLabel.getId());
        requestController.deleteItemFromGridPane(acceptBtn.getId());
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

    public void setRequestController(RequestController requestController){
        this.requestController = requestController;
    }

    public void setService(SuperService service){
        this.service = service;
    }

    public void setLoggedUser(User loggedUser){
        this.loggedUser = loggedUser;
    }
}
