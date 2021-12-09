package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.service.SuperService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Arrays;

public class FriendItemController {

    @FXML
    private Button deleteBtn;
    @FXML
    private Label nameLbl;

    private FriendsController friendsController;
    private SuperService service;
    private User loggedUser;

    /**
     * handler for delete button
     * it delete the friendship and then notify the friendsController to delete the item from the view
     */
    @FXML
    private void handlerDeleteButton(){
        service.removeFriend(loggedUser.getId(), Arrays.asList(nameLbl.getId()));
        friendsController.deleteItemFromGridPane(deleteBtn.getId());
    }

    /**
     * sets the name to be displayed
     * @param name .
     */
    public void setName(String name){
        nameLbl.setText(name);
    }

    /**
     * sets the email of the user displayed
     * @param email .
     */
    public void setEmail(String email){ nameLbl.setId(email);}

    /**
     * sets the friendsController, being as parent for notify of deleting an item
     * @param friendsController .
     */
    public void setFriendsController(FriendsController friendsController) {
        this.friendsController = friendsController;
    }

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
