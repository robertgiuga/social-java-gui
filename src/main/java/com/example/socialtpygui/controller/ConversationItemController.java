package com.example.socialtpygui.controller;

import com.example.socialtpygui.domainEvent.ItemSelected;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ConversationItemController {
    @FXML
    private AnchorPane anchorPaneConv;

    @FXML
    private Label nameLabel;

    private boolean isGroup=false;

    /**
     * Set the name of the label.
     * @param name name of the label
     */
    public void setNameLabel(String name)
    {
        nameLabel.setText(name);
    }

    /**
     * handle the clicks
     * raise a new UserSelect Event with the id of the user which is displayed
     */
    public void handlerMouseClick(MouseEvent mouseEvent) {
        if (isGroup)
            anchorPaneConv.fireEvent(new ItemSelected(ItemSelected.GROUP_LOAD_MSJ, nameLabel.getId()));
        else
        anchorPaneConv.fireEvent(new ItemSelected(ItemSelected.USER_LOAD_MSJ, nameLabel.getId()));
    }

    public void setIsGroup(boolean group) {
        isGroup = group;
    }
}
