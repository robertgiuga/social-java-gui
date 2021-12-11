package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SearchFriendConvController {

    @FXML
    private GridPane gridPane;

    private SuperService service;
    private User loggedUser;

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
    public void load(SuperService service, User loggedUser,String name){
        this.service=service;
        this.loggedUser=loggedUser;

        service.getFriendsByName(loggedUser.getId(),name).forEach(userDTO ->  {
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


}
