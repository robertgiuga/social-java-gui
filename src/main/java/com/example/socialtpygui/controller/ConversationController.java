package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.GroupDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ConversationController {
    @FXML
    GridPane gridPane;

    SuperService service;
    User loggedUser;

    /**
     * Create a conversationItem
     * @param user the user of the item(view)
     * @return conversationItem(view)
     * * @throws IOException
     */
    private Pane createItem(UserDTO user) throws IOException {
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("conversation-item-view.fxml"));
        Pane item = loader.load();
        ConversationItemController controller= loader.getController();
        controller.setNameLabel(user.getFirstName()+" "+user.getLastName());

        return item;
    }

    /**
     * creates a conversationItem that is a group (the isGroup boolean identifier from itemController is set to true)
     * @param group the group of the item
     * @return  conversationItem
     * @throws IOException .
     */
    private Pane createGroupIem(GroupDTO group) throws IOException {
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("conversation-item-view.fxml"));
        Pane item = loader.load();
        ConversationItemController controller= loader.getController();
        controller.setNameLabel(group.getNameGroup());
        controller.setIsGroup(true);
        return item;
    }

    /**
     * Set the service.
     * @param service
     */
    public void setService(SuperService service) {this.service = service;}

    /**
     * Set the logged user.
     * @param loggedUser
     */
    public void setLoggedUser(User loggedUser){this.loggedUser = loggedUser;}


    /**
     * loads the gridPane with friends with active conversation.
     */
    public void load()
    {
        this.service.getAllConversation(loggedUser.getId()).forEach(email->{
            try {
                UserDTO userDTO = new UserDTO(this.service.findOneUser(email));
                Pane pane = createItem(userDTO);
                pane.getChildren().forEach(node-> {
                    if (node instanceof Label) node.setId(String.valueOf(userDTO.getId()));
                });
                gridPane.addRow(gridPane.getRowCount(), pane);
            } catch (IOException e) {
                e.printStackTrace();
            }});
        this.service.getUserGroups(loggedUser.getId()).forEach(groupDTO -> {
            try {
                Pane item= createGroupIem(groupDTO);
                item.getChildren().forEach(node-> {if (node instanceof Label) node.setId(String.valueOf(groupDTO.getId()));});
                gridPane.addRow(gridPane.getRowCount(),item);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}
