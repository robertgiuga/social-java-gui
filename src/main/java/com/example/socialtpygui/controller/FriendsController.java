package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.FriendShipDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.domainEvent.UserSelected;
import com.example.socialtpygui.service.SuperService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class FriendsController {

    @FXML
    private GridPane gridPane;


    private SuperService service;

    private User loggedUser;

    private ObservableList<Node> friends;

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
        friendItemController.setFriendsController(this);
        return item;
    }


    /**
     * loads all the friends of the logged user in the gridPane
     */
    public void load(SuperService service, User loggedUser) {
        this.service= service;
        this.loggedUser=loggedUser;

     //   gridPane.getParent().addEventFilter(UserSelected.USER_DELETE,this::handlerForSelectedFriend);
        service.getFriends(loggedUser.getId()).forEach(friendShipDTO -> {
            try {
                Pane item = createItem(friendShipDTO);
                item.getChildren().forEach(node -> {if(node instanceof Button) node.setId(String.valueOf(gridPane.getRowCount()));});
                gridPane.addRow(gridPane.getRowCount(),item);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        friends=gridPane.getChildren();
    }

    /**
     * deletes an friend item view from the gridPane by the row
     * @param row . the row at is the friend
     */
    public void deleteItemFromGridPane(String row){
        gridPane.getChildren().remove(friends.get(Integer.parseInt(row)));
    }

    /*private void handlerForSelectedFriend(UserSelected e) {
        if (e.getEventType().equals(UserSelected.USER_DELETE)){
            System.out.println(e.getSelectedUserId());
        }
        if (e.getEventType().equals(UserSelected.USER_SELECTED)){
            System.out.println(e.getSelectedUserId());
        }
    }*/
}
