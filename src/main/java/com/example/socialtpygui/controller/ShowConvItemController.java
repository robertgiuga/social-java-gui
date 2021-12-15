package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.ReplyMessage;
import com.example.socialtpygui.domainEvent.DragMessage;
import com.example.socialtpygui.domainEvent.UserSelected;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ShowConvItemController {
    @FXML
    private Text messageLabel;

    @FXML
    private Label messageToReply;

    @FXML
    private Pane paneShowConvItem;

    private ReplyMessage replyMessage;


    public void setMessage(ReplyMessage replyMessage){
        this.replyMessage =replyMessage;
        if(replyMessage.getOriginal()==null){
            paneShowConvItem.getChildren().remove(messageToReply);
            paneShowConvItem.setPrefHeight(30);
        }
        else{
            messageToReply.setText("replying to: "+replyMessage.getOriginal().getMessage());
        }
        messageLabel.setText(replyMessage.getMessage());
    }

    @FXML
    public void HandlerDragMessage(){
        paneShowConvItem.fireEvent(new DragMessage(DragMessage.MESSAGE_REPLY,  replyMessage));
    }
}
