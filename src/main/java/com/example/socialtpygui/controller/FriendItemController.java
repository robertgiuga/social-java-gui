package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.Arrays;

public class FriendItemController {

    @FXML
    private ImageView deleteBtn;
    @FXML
    private Label nameLbl;
    @FXML
    private Label dateLbl;

    private SuperService service;
    private UserDTO loggedUser;

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
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }
}
