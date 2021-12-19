package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.UserSelected;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Arrays;

public class FriendItemController {

    @FXML
    private Button deleteBtn;
    @FXML
    private Label nameLbl;
    @FXML
    private Label dateLbl;

    private SuperService service;
    private User loggedUser;

    /**
     * handler for delete button
     * it delete the friendship and then notify the friendsController to delete the item from the view
     */
    @FXML
    private void handlerDeleteButton(){
        service.removeFriend(loggedUser.getId(), Arrays.asList(nameLbl.getId()));
    }

    /**
     * sets the name to be displayed
     * @param name .
     */
    public void setName(String name){
        nameLbl.setText(name);
    }

    /**
     * sets the date since they are friends to be displayed
     * @param date .
     */
    public void setDate(String date){
        dateLbl.setText("friends since: "+ date);
    }

    /**
     * sets the email of the user displayed
     * @param email .
     */
    public void setEmail(String email){ nameLbl.setId(email);}


    /**
     * sets the service
     * @param service .
     */
    public void setService(SuperService service) {
        this.service = service;
    }

    /**
     * sets the user currently logged
     * @param loggedUser .
     */
    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
}
