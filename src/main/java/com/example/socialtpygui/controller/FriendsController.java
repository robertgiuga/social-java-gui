package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.FriendShipDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import com.example.socialtpygui.utils.events.ViewItemEvent;
import com.example.socialtpygui.utils.observer.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FriendsController implements Observer<ViewItemEvent> {

    @FXML
    private GridPane gridPane;

    private SuperService service;
    private User loggedUser;
    private List<UserDTO> friends;

    /**
     * create a custom item of a friend to be displayed
     * @return the Pane item to be displayed
     * @throws IOException .
     */
    private Pane createItem(FriendShipDTO friendShipDTO) throws IOException {
        UserDTO user = friendShipDTO.getUser2();
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("friendItem.fxml"));
        Pane item = loader.load();
        FriendItemController friendItemController= loader.getController();
        friendItemController.setName(user.getFirstName()+" "+user.getLastName());
        friendItemController.setDate(friendShipDTO.getDate().toString());
        friendItemController.setLoggedUser(loggedUser);
        friendItemController.setService(service);
        friendItemController.setEmail(user.getId());
        return item;
    }


    /**
     * loads all the friends of the logged user in the gridPane
     */
    public void load(SuperService service, User loggedUser) {
        this.service= service;
        this.loggedUser=loggedUser;
        service.addObserver(this);

        loadFriends();
    }

    private void loadFriends(){
        friends=new ArrayList<>();
        service.getFriends(loggedUser.getId()).forEach(friendShipDTO -> {
            try {
                Pane item= createItem(friendShipDTO);
                gridPane.addRow(gridPane.getRowCount(),item);
                friends.add(friendShipDTO.getUser2());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void update(ViewItemEvent viewItemEvent) {
        gridPane.getChildren().remove(friends.indexOf(viewItemEvent.getUserDTO()));
        friends.remove(viewItemEvent.getUserDTO());
    }
}
