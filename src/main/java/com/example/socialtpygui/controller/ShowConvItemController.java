package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.ReplyMessage;
import com.example.socialtpygui.domainEvent.DragMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ShowConvItemController {
    @FXML
    private Text messageLabel;

    @FXML
    private Label messageToReply;

    @FXML
    private Pane paneShowConvItem;
    @FXML
    private Label fromLbl;

    private ReplyMessage replyMessage;

    public void setFrom(String from){
        fromLbl.setVisible(true);
        fromLbl.setText("from: "+from);
    }

    public void setMessage(ReplyMessage replyMessage){
        fromLbl.setVisible(false);
        this.replyMessage =replyMessage;
        if(replyMessage.getOriginal()==null){
            paneShowConvItem.getChildren().remove(messageToReply);
            paneShowConvItem.setPrefHeight(30);
        }
        else{
            messageToReply.setText("replied to: "+replyMessage.getOriginal().getMessage());
        }
        messageLabel.setText(replyMessage.getMessage());
    }

    @FXML
    public void HandlerDragMessage(){
        paneShowConvItem.fireEvent(new DragMessage(DragMessage.MESSAGE_REPLY,  replyMessage));
    }
}
