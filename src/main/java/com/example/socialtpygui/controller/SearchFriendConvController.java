package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SearchFriendConvController {

    public ScrollPane scrollPaneSearchFriendConvView;
    @FXML
    private GridPane gridPane;

    private SuperService service;
    private UserDTO loggedUser;
    private int pageId;
    private String name;

    /**
     * create a new view of a Friend
     * @param user the user of the view is creates
     * @return the view
     * @throws IOException .
     */
    private Pane createItem(UserDTO user) throws IOException {
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("searchFriendConv-item-view.fxml"));
        Pane item = loader.load();
        SearchFriendConvItemController controller= loader.getController();
        controller.setName(user.getFirstName()+" "+user.getLastName());

        return item;
    }

    /**
     * loads the gridPane with friends that names matches the "name"
     * @param service the SuperService
     * @param loggedUser  the user currently logged in
     * @param name the names of the Friends to search for
     */
    public void load(SuperService service, UserDTO loggedUser,String name){
        this.service=service;
        this.loggedUser=loggedUser;
        this.name=name;
        nextPage();
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

    /**
     * load new data in UI by pageId
     */
    private void nextPage(){
        service.getFriendsByName(loggedUser.getId(),name,pageId++).forEach(userDTO ->  {
            try {
                Pane item = createItem(userDTO);
                item.getChildren().forEach(node -> {
                    if (node instanceof Label) node.setId(String.valueOf(userDTO.getId()));
                });
                gridPane.addRow(gridPane.getRowCount(), item);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * loads new data in UI when scroll bar hit a value
     * @param scrollEvent
     */
    public void handlerScroll(ScrollEvent scrollEvent) {
        if(scrollPaneSearchFriendConvView.getVvalue()>0.45&&scrollPaneSearchFriendConvView.getVvalue()<0.55){
            nextPage();
        }
    }
}
