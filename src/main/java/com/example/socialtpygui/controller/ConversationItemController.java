package com.example.socialtpygui.controller;

import com.example.socialtpygui.domainEvent.UserSelected;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.w3c.dom.events.Event;

public class ConversationItemController {
    @FXML
    private AnchorPane anchorPaneConv;

    @FXML
    private Label nameLabel;

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
        anchorPaneConv.fireEvent(new UserSelected(UserSelected.USER_LOAD_MSJ, nameLabel.getId()));
    }
}
