package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.service.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


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

    @FXML
    private Button exitBtnMW;

    @FXML
    private Button minimizeBtnMW;

    @FXML
    private Button extindBtnMW;


    private SuperService service;

    private User loggedUser;

    public void load(SuperService service, User loggedUser){
        this.service=service;
        this.loggedUser=loggedUser;
    }


    @FXML
    private void handlerFriendsButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(LogInApplication.class.getResource("friends-view.fxml"));
        AnchorPane panel= loader.load();

        FriendsController friendsController= loader.getController();
        friendsController.load(service,loggedUser);

        Pane view = new Pane(panel);

        borderPane.setCenter(view);
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
        MessageController messageController=loader.getController();
        messageController.load(service,loggedUser);
        borderPane.setCenter(panel);
    }

    public void handlerExitBtnMW(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    public void handlerMinimizeBtnMW(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).setIconified(true);
    }

    public void handlerExtindButtonMW(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).setFullScreen(true);
    }
}
