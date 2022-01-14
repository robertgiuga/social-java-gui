package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class SearchController{

    public ScrollPane scrollPaneSearchView;
    List<UserDTO> users;
    FXMLLoader fxmlLoader;
    UserDTO loggedUser;
    SuperService service;
    @FXML
    GridPane gridPane;

    private int row =0;
    private int pageId=0;
    private String name;

    /**
     * Load in gridpane all the item, dinamic
     * @param completeNameSearch
     */
    public void load(String completeNameSearch)
    {
        this.name=completeNameSearch;
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
     * loads new data by the pageId
     */
    private void nextPage(){
        service.getUsersByName(name,pageId++).forEach(userDTO -> {
            try {
                createItem(userDTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void createItem(UserDTO user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("search-item.fxml"));
        Pane item = fxmlLoader.load();
        SearchItemController searchItemController = fxmlLoader.getController();
        searchItemController.setData(user);
        searchItemController.setLoggedUser(this.loggedUser);
        searchItemController.setService(this.service);
        if ((service.friendshipDate(this.loggedUser.getId(), user.getId()) != null) || (user.getId().equals(this.loggedUser.getId()))){
            searchItemController.hideAddBtn();
            searchItemController.hideCancelBtn();
            searchItemController.hideCancelLabel();
            searchItemController.hideSendLabel();
        }

        if (service.friendshipRequestDate(this.loggedUser.getId(), user.getId()) != null)
        {searchItemController.hideAddBtn(); searchItemController.hideSendLabel();}
        else
        {searchItemController.hideCancelBtn(); searchItemController.hideCancelLabel();}
        gridPane.addRow(row++, item);
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

    /**
     * loads new data in UI when scroll bar hit a value
     * @param scrollEvent
     */
    public void handlerScroll(ScrollEvent scrollEvent) {
        if(scrollPaneSearchView.getVvalue()>0.45&&scrollPaneSearchView.getVvalue()<0.55){
            nextPage();
        }
    }
}
