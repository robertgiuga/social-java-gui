package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.FriendShipDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import com.example.socialtpygui.service.validators.NonExistingException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class RequestController {
    User loggedUser;
    SuperService service;

    @FXML
    private GridPane gridPane;

    private ObservableList<Node> requests;

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setService(SuperService service) {
        this.service = service;
    }

    private Pane createItem(FriendShipDTO request) throws IOException{
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("request-item.fxml"));
        Pane item = loader.load();
        RequestItemController requestItemController = loader.getController();
        requestItemController.setName(request.getUser2().getFirstName()+" "+request.getUser2().getLastName());
        requestItemController.setDate(request.getDate());
        requestItemController.setLoggedUser(loggedUser);
        requestItemController.setService(service);
        requestItemController.setEmail(request.getUser2().getId());
        requestItemController.setRequestController(this);
        return item;
    }

    public void load(){
        try {
            service.getFriendRequest(loggedUser.getId()).forEach(friendShipDTO-> {
                try {
                    Pane item = createItem(friendShipDTO);
                    item.getChildren().forEach(node -> {
                        if (node instanceof Button) node.setId(String.valueOf(gridPane.getRowCount()));
                    });
                    gridPane.addRow(gridPane.getRowCount(), item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }catch (NonExistingException e){
            System.out.println(e.getMessage());
        }
        requests = gridPane.getChildren();
    }

    public void deleteItemFromGridPane(String row){
        gridPane.getChildren().remove(requests.get(Integer.parseInt(row)));
    }
}
