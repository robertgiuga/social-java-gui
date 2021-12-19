package com.example.socialtpygui.controller;

import com.example.socialtpygui.domainEvent.ItemSelected;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class SearchFriendConvItemController {

    @FXML
    private Label nameLbl;
    @FXML
    private AnchorPane anchorPane;

    /**
     * handle the clicks
     * raise a new UserSelect Event with the id of the user which is displayed
     */
    @FXML
    private void handlerMouseClicked(){
        anchorPane.fireEvent(new ItemSelected(ItemSelected.USER_SELECTED, nameLbl.getId()));
    }

    /**
     * sets the label witch is displayed with name
     * @param name the name of the user to be displayed
     */
    public void setName(String name){
        nameLbl.setText(name);
    }

}
