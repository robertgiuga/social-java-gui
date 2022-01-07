package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class SearchController{

    List<UserDTO> users;
    FXMLLoader fxmlLoader;
    UserDTO loggedUser;
    SuperService service;
    @FXML
    GridPane gridPane;

    /**
     * Load in gridpane all the item, dinamic
     * @param completeNameSearch
     */
    public void load(String completeNameSearch)
    {
        int row = 0;
        this.users = service.getUsersByName(completeNameSearch);
        try
        {
            for (UserDTO user : users) {
                FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("search-item.fxml"));
                Pane item = fxmlLoader.load();
                SearchItemController searchItemController = fxmlLoader.getController();
                searchItemController.setData(user);
                searchItemController.setLoggedUser(this.loggedUser);
                searchItemController.setService(this.service);
                if ((service.friendshipDate(this.loggedUser.getId(), user.getId()) != null) || (user.getId().equals(this.loggedUser.getId()))){
                    searchItemController.hideAddBtn();
                    searchItemController.hideCancelBtn();
                }

                if (service.friendshipRequestDate(this.loggedUser.getId(), user.getId()) != null)
                {searchItemController.hideAddBtn();}
                else
                {searchItemController.hideCancelBtn();}
                gridPane.addRow(row++, item);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Set service
     * @param service
     */
    public void setService(SuperService service) {
        this.service = service;
    }

    /**
     * Set logged user
     * @param loggedUser
     */
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }


}
