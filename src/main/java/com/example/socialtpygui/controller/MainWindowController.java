package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


import java.io.IOException;

public class MainWindowController {

    @FXML
    private Button friendsBtn;

    @FXML
    private TextField searchbar;

    @FXML
    private Button requestsBtn;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button messageBtn;

    private SuperService service;

    private User loggedUser;

    @FXML
    private void handlerFriendsButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(LogInApplication.class.getResource("friends-view.fxml"));
        AnchorPane panel= loader.load();

        FriendsController friendsController= loader.getController();
        friendsController.setService(service);
        friendsController.setLoggedUser(loggedUser);
        friendsController.load();

        Pane view = new Pane(panel);

        borderPane.setCenter(view);
    }

    public void setService(SuperService service) {
        this.service = service;
    }


    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    @FXML
    private void handlerKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
        {
            FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("search-view.fxml"));
            Pane view = fxmlLoader.load();
            SearchController searchController = fxmlLoader.getController();
            searchController.setService(service);
            searchController.setLoggedUser(this.loggedUser);
            searchController.load(searchbar.getText());
            borderPane.setCenter(view);
        }
    }

    @FXML
    private void handlerRequestsButton() throws IOException{
        FXMLLoader loader= new FXMLLoader(LogInApplication.class.getResource("request-view.fxml"));
        AnchorPane panel= loader.load();

        RequestController requestController= loader.getController();
        requestController.setService(service);
        requestController.setLoggedUser(loggedUser);
        requestController.load();

        Pane view = new Pane(panel);

        borderPane.setCenter(view);
    }

    @FXML
    private void handlerMessageBtn() throws IOException {
        FXMLLoader loader= new FXMLLoader(LogInApplication.class.getResource("messageWindow-view.fxml"));
        Pane panel= loader.load();

        borderPane.setCenter(panel);
    }
}
