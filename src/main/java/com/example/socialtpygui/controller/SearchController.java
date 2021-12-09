package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.List;

public class SearchController{

    List<UserDTO> users;

    FXMLLoader fxmlLoader;

    SuperService service;

    @FXML
    GridPane gridPane;

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
                searchItemController.hideAddBtn();

                gridPane.addRow(row++, item);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setService(SuperService service) {
        this.service = service;
    }


}
