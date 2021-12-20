package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.MessageDTO;
import com.example.socialtpygui.domain.ReplyMessage;
import com.example.socialtpygui.domain.ReplyMessageDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.DragMessage;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShowConvController {
    private SuperService service;

    @FXML
    private GridPane gridShowMessages;

    @FXML
    private ScrollPane scrollPaneShowConv;

    @FXML
    private TextField messageText;

    @FXML
    private Button buttonSend;

    @FXML
    private Label messageToReply;

    @FXML
    private AnchorPane anchorPaneShowConvView;

    private User loggedUser;

    private String email;

    private DragMessage dragMessage=null;

    private int groupId;

    /**
     * send a message to user from conversation
     * @throws IOException
     */
    private void sendMessage() throws IOException {
        List<String> to = new ArrayList<>();
        to.add(email);
        //Pane item = null;
        if(dragMessage == null) {
            service.sendMessage(new MessageDTO(loggedUser.getId(), to, messageText.getText(), LocalDate.now()));
            //item = createItem(new ReplyMessage(new Message(loggedUser.getId(), to, messageText.getText(), LocalDate.now()), null));
            //item.getChildren().forEach(node -> {
            //    if (node instanceof Label)
            //        node.setId(String.valueOf(messageText.getText()));
            //});
        }
        else{
            service.replyMessage(new ReplyMessageDTO(new MessageDTO(loggedUser.getId(), to, messageText.getText(), LocalDate.now()), dragMessage.getMessage().getId().toString()));
            //item = createItem(new ReplyMessage(new Message(loggedUser.getId(), to, messageText.getText(), LocalDate.now()), dragMessage.getMessage()));
            //item.getChildren().forEach(node -> {
            //    if (node instanceof Label)
            //        node.setId(String.valueOf(messageText.getText()));
            //});
            messageToReply.setText("");
            dragMessage = null;
        }

        //gridShowMessages.add(item, 1, gridShowMessages.getRowCount());
        messageText.clear();
        gridShowMessages.getChildren().clear();
        this.load(service, loggedUser, email);
    }

    /**
     * create a new view of a Message
     * @return a Pane
     * @throws IOException
     */
    private Pane createItem(ReplyMessage replyMessage) throws IOException{
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("showConv-item.fxml"));
        Pane item = loader.load();
        ShowConvItemController controller = loader.getController();
        controller.setMessage(replyMessage);
        return item;
    }

    /**
     * initialize loggedUser, service and email
     * load into gridPane conversation between two users
     * @param service
     * @param loggedUser
     * @param email
     */
    public void load(SuperService service, User loggedUser, String email){
        anchorPaneShowConvView.addEventFilter(DragMessage.ANY, this::DragMessageHandler);
        this.loggedUser = loggedUser;
        this.service = service;
        this.email = email;
        service.getMessages(loggedUser.getId(), email).forEach(replyMessage->{
            try{
                Pane item = createItem(replyMessage);
                item.getChildren().forEach(node->{
                    if (node instanceof Label)
                        node.setId(String.valueOf(replyMessage.getId()));
                    });
                    if(replyMessage.getFrom().equals(loggedUser.getId())){
                        gridShowMessages.add(item, 1, gridShowMessages.getRowCount());
                    }
                    else{
                        gridShowMessages.add(item, 0, gridShowMessages.getRowCount());
                    }
            }catch (IOException e){
                System.out.println(e.getMessage());
            }

        });
        gridShowMessages.autosize();
    }

    /**
     * initialize loggedUser, service and groupId
     * load into gridPane all conversation from a group
     * @param service SuperService
     * @param loggedUser User
     * @param groupId Integer
     */
    public void loadGroup(SuperService service, User loggedUser, int groupId)
    {
        anchorPaneShowConvView.addEventFilter(DragMessage.ANY, this::DragMessageHandler);
        this.loggedUser = loggedUser;
        this.service = service;
        this.groupId = groupId;
        service.getGroupMessages(groupId).forEach(replyMessage -> {
            try{
                Pane item = createItem(replyMessage);
                item.getChildren().forEach(node->{
                    if (node instanceof Label)
                        node.setId(String.valueOf(replyMessage.getId()));
                });
                if(replyMessage.getFrom().equals(loggedUser.getId())){
                    gridShowMessages.add(item, 1, gridShowMessages.getRowCount());
                }
                else{
                    gridShowMessages.add(item, 0, gridShowMessages.getRowCount());
                }
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        });
        gridShowMessages.autosize();
    }


    /**
     * handle press ok 'ENTER' key sending a Message with text from textField
     * @throws IOException
     */
    @FXML
    private void pressedSendButton() throws IOException {
        if(!messageText.getText().equals("")){
                sendMessage();
            }
    }

    /**
     * handle press Send Button sending a message in current conversation
     * @param event
     * @throws IOException
     */
    @FXML
    private void handlerKeyPressed(KeyEvent event) throws IOException {
        if(!messageText.getText().equals(""))
            if(event.getCode().equals(KeyCode.ENTER)) {
                sendMessage();
            }
    }

    /**
     * set text in label messageToReply as message text from dragged message from conversation
     * @param m
     */
    private void DragMessageHandler(DragMessage m) {
        if(m != null) {
            messageToReply.setText("replying to: "+m.getMessage().getMessage());
            this.dragMessage = m;
        }
    }
}
