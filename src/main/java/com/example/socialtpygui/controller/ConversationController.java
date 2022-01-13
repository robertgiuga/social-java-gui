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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ConversationController {
    public ScrollPane scrollPaneConversationView;
    @FXML
    GridPane gridPane;

    SuperService service;
    UserDTO loggedUser;
    private int pageId=0;

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
    public void setLoggedUser(UserDTO loggedUser){this.loggedUser = loggedUser;}


    /**
     * loads the gridPane with friends and groups with active conversation.
     */
    public void load()
    {
        nextPage();
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

    private void nextPage(){
        this.service.getAllConversation(loggedUser.getId(),pageId++).forEach(email->{
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
    }

    public void handlerScroll(ScrollEvent scrollEvent) {
        if(scrollPaneConversationView.getVvalue()>0.45&&scrollPaneConversationView.getVvalue()<0.55){
            nextPage();
        }
    }
}
