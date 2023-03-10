package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.FriendShipDTO;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import com.example.socialtpygui.service.validators.NonExistingException;
import com.example.socialtpygui.utils.events.ChangeEventType;
import com.example.socialtpygui.utils.events.EventCustom;
import com.example.socialtpygui.utils.observer.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestController implements Observer<EventCustom> {
    UserDTO loggedUser;
    SuperService service;

    @FXML
    private GridPane gridPane;

    private List<UserDTO> requests;

    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setService(SuperService service) {
        service.addObserver(this);
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
        return item;
    }

    public void load(){
        try {
            requests=new ArrayList<>();
            service.getFriendRequest(loggedUser.getId()).forEach(friendShipDTO-> {
                try {
                    Pane item = createItem(friendShipDTO);
                    gridPane.addRow(gridPane.getRowCount(), item);
                    requests.add(friendShipDTO.getUser2());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }catch (NonExistingException e){
            System.out.println(e.getMessage());
        }
        if (gridPane.getRowCount() == 0){
            try {
                FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("nothingFound-view.fxml"));
                Pane item = loader.load();
                gridPane.addRow(gridPane.getRowCount(), item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void update(EventCustom viewItemEvent) {
        if(viewItemEvent.getType().equals(ChangeEventType.REMOVE)) {
            gridPane.getChildren().remove(requests.indexOf(viewItemEvent.getUserDTO()));
            requests.remove(viewItemEvent.getUserDTO());
        }
    }
}
