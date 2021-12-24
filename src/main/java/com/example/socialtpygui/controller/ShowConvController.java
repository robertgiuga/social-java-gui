package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.MessageDTO;
import com.example.socialtpygui.domain.ReplyMessage;
import com.example.socialtpygui.domain.ReplyMessageDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.DragMessage;
import com.example.socialtpygui.service.SuperService;
import com.example.socialtpygui.utils.events.ChangeEventType;
import com.example.socialtpygui.utils.events.NewMessageEvent;
import com.example.socialtpygui.utils.observer.Observer;
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

public class ShowConvController implements Observer<NewMessageEvent> {
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
    @FXML
    private Button settingsBtn;


    private User loggedUser;

    private String email2User;

    private DragMessage dragMessage=null;

    private Integer groupId = null;
    private int lastGroupMsjId;
    private int lastConvMsjId;

    /**
     * send a message to user from conversation
     * @throws IOException
     */
    private void sendMessage() throws IOException {
        scrollPaneShowConv.setVvalue(1.0f);
        List<String> to = new ArrayList<>();
        to.add(email2User);
        Pane item = null;
        if(groupId == null) {
            if (dragMessage == null) {
                MessageDTO messageDTO = service.sendMessage(new MessageDTO(loggedUser.getId(), to, messageText.getText(), LocalDate.now()));
                item = createItem(new ReplyMessage(messageDTO, null));
                item.getChildren().forEach(node -> {
                    if (node instanceof Label)
                        node.setId(String.valueOf(messageText.getText()));
                });
                lastConvMsjId =messageDTO.getId();
            }
            else {
                ReplyMessage replyMessage = service.replyMessage(new ReplyMessageDTO(new MessageDTO(loggedUser.getId(), to, messageText.getText(), LocalDate.now()), dragMessage.getMessage().getId().toString()));
                item = createItem(replyMessage);
                item.getChildren().forEach(node -> {
                    if (node instanceof Label)
                        node.setId(String.valueOf(messageText.getText()));
                });
                dragMessage = null;
                lastConvMsjId = replyMessage.getId();
            }
        }
        else{
            if(dragMessage == null) {
                MessageDTO messageDTO = service.replyAll(new MessageDTO(loggedUser.getId(), null, messageText.getText(), LocalDate.now()), groupId);
                item = createGroupItem(new ReplyMessage(messageDTO, null));
                item.getChildren().forEach(node -> {
                    if (node instanceof Label)
                        node.setId(String.valueOf(messageText.getText()));
                });
                lastGroupMsjId =messageDTO.getId();
            }
            else{
                ReplyMessage replyMessage = service.replyMessageGroup(new ReplyMessageDTO(new MessageDTO(loggedUser.getId(), null, messageText.getText(), LocalDate.now()), dragMessage.getMessage().getId().toString()), groupId);
                item = createGroupItem(replyMessage);
                item.getChildren().forEach(node -> {
                    if (node instanceof Label)
                        node.setId(String.valueOf(messageText.getText()));
                });
                lastGroupMsjId = replyMessage.getId();
            }
        }

        gridShowMessages.add(item, 1, gridShowMessages.getRowCount());
        messageToReply.setText("");
        messageText.clear();
        gridShowMessages.autosize();
        scrollPaneShowConv.autosize();
        scrollPaneShowConv.setVvalue(1.0f);
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
     *
     * @param replyMessage
     * @return
     * @throws IOException
     */
    private Pane createGroupItem(ReplyMessage replyMessage) throws IOException {
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("showConv-item.fxml"));
        Pane item = loader.load();
        ShowConvItemController controller = loader.getController();
        controller.setMessage(replyMessage);
        User user =service.findOneUser(replyMessage.getFrom());
        controller.setFrom(user.getFirstName()+" "+user.getLastName());
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
        service.addObserver(this);
        settingsBtn.setVisible(false);
        anchorPaneShowConvView.addEventFilter(DragMessage.ANY, this::DragMessageHandler);
        this.loggedUser = loggedUser;
        this.service = service;
        this.email2User = email;
        this.groupId = null;
        scrollPaneShowConv.setVvalue(1.0f);
        service.getMessages(loggedUser.getId(), email).forEach(replyMessage->{
            try{
                Pane item = createItem(replyMessage);
                lastConvMsjId= replyMessage.getId();
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
        service.addObserver(this);
        anchorPaneShowConvView.addEventFilter(DragMessage.ANY, this::DragMessageHandler);
        this.loggedUser = loggedUser;
        this.service = service;
        this.groupId = groupId;
        service.getGroupMessages(groupId).forEach(replyMessage -> {
            try{
                Pane item = createGroupItem(replyMessage);
                lastGroupMsjId =replyMessage.getId();
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

    /**
     * create a groupSettings view
     * @throws IOException
     */
    @FXML
    private void handlerSettignsButton() throws IOException {
        buttonSend.setDisable(true);
        messageText.setDisable(true);
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("groupSettings-view.fxml"));
        Pane item = loader.load();
        GroupSettingsController controller=loader.getController();
        controller.load(service,groupId,this.loggedUser);
        scrollPaneShowConv.setContent(item);
    }

    @Override
    public void update(NewMessageEvent newMessageEvent) {
        scrollPaneShowConv.setVvalue(1.0f);
        System.out.println("update");
        if(newMessageEvent.getEventType().equals(ChangeEventType.NEW_MSJ)) {
            //TODO
            // get msj from a certain id ahead
            service.getConvMessagesGreaterThan(loggedUser.getId(), email2User,lastConvMsjId ).forEach(replyMessage -> {
                try {
                    Pane item = createItem(replyMessage);
                    item.getChildren().forEach(node -> {
                        if (node instanceof Label)
                            node.setId(String.valueOf(replyMessage.getId()));
                    });
                    if (replyMessage.getFrom().equals(loggedUser.getId())) {
                        gridShowMessages.add(item, 1, gridShowMessages.getRowCount());
                    } else {
                        gridShowMessages.add(item, 0, gridShowMessages.getRowCount());
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
        else if(groupId!=null&&newMessageEvent.getEventType().equals(ChangeEventType.NEW_GROUP_MSJ)){
            //TODO
           // System.out.println(groupId);
            System.out.println("reload msj");
            service.getGroupMessagesGreaterThen(groupId, lastGroupMsjId).forEach(replyMessage -> {
                try{
                    Pane item = createGroupItem(replyMessage);
                    lastGroupMsjId =replyMessage.getId();
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
        }
        gridShowMessages.autosize();
        scrollPaneShowConv.autosize();
        scrollPaneShowConv.setVvalue(1.0f);

    }
}
