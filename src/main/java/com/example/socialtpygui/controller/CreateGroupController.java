package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.Group;
import com.example.socialtpygui.domain.GroupDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.domainEvent.ItemSelected;
import com.example.socialtpygui.service.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateGroupController {

    public TextField nameGroupLbl;
    public GridPane friendsPane;
    private SuperService service;
    private UserDTO loggedUser;


    /**
     * adds in the gridPane the friends of the user, creating a possibleParticipant view
     * @param service
     * @param loggedUser
     */
    public void load(SuperService service, UserDTO loggedUser){
        this.service=service;
        this.loggedUser=loggedUser;
        service.getFriends(loggedUser.getId()).forEach(friendShipDTO -> {
            UserDTO friend= friendShipDTO.getUser2();
            FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("posibleParticipant-viewItem.fxml"));
            try {
                Pane item= loader.load();
                PossibleParticipantControler controller =loader.getController();
                controller.setName(friend.getFirstName()+" "+ friend.getLastName());
                controller.setId(friend.getId());
                friendsPane.addRow(friendsPane.getRowCount(),item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * create a group with the data from the view
     * puttings as the participants the loggedUser and the one selected
     * @param mouseEvent
     */
    public void handelerCreateGroupBtn(javafx.scene.input.MouseEvent mouseEvent) {
        if(nameGroupLbl.getText().length()>0){
            List<String> participants = new ArrayList<>();
            participants.add(loggedUser.getId());
            friendsPane.getChildren().forEach(node -> {
                Pane item =(Pane)node;
                item.getChildren().forEach(node1 ->{
                    if(node1 instanceof CheckBox)
                        if(((CheckBox) node1).isSelected()){
                            participants.add(node1.getId());
                        }
                });
            });
            GroupDTO groupDTO= new GroupDTO(-1,nameGroupLbl.getText(),participants);
            Group newgroup =service.addGroup(groupDTO);
            friendsPane.fireEvent(new ItemSelected(ItemSelected.GROUP_LOAD_MSJ,String.valueOf(newgroup.getId())));
        }
    }
}
