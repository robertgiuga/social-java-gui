package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.*;
import com.example.socialtpygui.domainEvent.DragMessage;
import com.example.socialtpygui.service.SuperService;
import com.example.socialtpygui.utils.events.ChangeEventType;
import com.example.socialtpygui.utils.events.EventCustom;
import com.example.socialtpygui.utils.observer.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ShowConvController implements Observer<EventCustom> {
    private SuperService service;

    @FXML
    private GridPane gridShowMessages;

    @FXML
    private ScrollPane scrollPaneShowConv;

    @FXML
    private TextField messageText;

    @FXML
    private ImageView buttonSend;

    @FXML
    private Label messageToReply;

    @FXML
    private AnchorPane anchorPaneShowConvView;
    @FXML
    private ImageView settingsBtn;


    private UserDTO loggedUser;

    private String email2User;

    private DragMessage dragMessage=null;

    private Integer groupId = null;
    private int lastGroupMsjId;
    private int lastConvMsjId;
    private int pageId=0;

    /**
     * send a message witch is create by the logged user in the GUI
     * and adds the new message in the GUI
     * @throws IOException .
     */
    private void sendMessage() throws IOException {
        scrollPaneShowConv.setVvalue(1.0f);
        Pane item;

        if(groupId == null) {
            item=sendConvMessage();
        }
        else{
            item= sendGroupMessage();
        }
        insertRows(1);
        gridShowMessages.add(item, 0, 0);
        messageToReply.setText("");
        messageText.clear();
        gridShowMessages.autosize();
        scrollPaneShowConv.autosize();
        scrollPaneShowConv.setVvalue(1.0f);
    }

    /**
     * insert rows in GridShowMessage
     * @param count
     */
    private void insertRows(int count) {
        for (Node child : gridShowMessages.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(child);
            GridPane.setRowIndex(child, rowIndex == null ? count : count + rowIndex);
        }
    }

    private boolean nextPagePrivateConv(){
        List<ReplyMessage> messages=service.getMessagesBetween2Users(loggedUser.getId(), email2User,pageId++);
        if(messages.size()>0) {
            messages.forEach(replyMessage -> {
                try {
                    Pane item = createItem(replyMessage);
                    lastConvMsjId = replyMessage.getId();
                    item.getChildren().forEach(node -> {
                        if (node instanceof Label)
                            node.setId(String.valueOf(replyMessage.getId()));
                    });
                    if (replyMessage.getFrom().equals(loggedUser.getId())) {

                        gridShowMessages.add(item, 0, gridShowMessages.getRowCount());
                    } else {
                        gridShowMessages.add(item, 1, gridShowMessages.getRowCount());
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

            });
            gridShowMessages.autosize();
            return true;
        }
        return false;
    }

    /**
     * send a private message to the User witch is in the conv displayed (email2User)
     * @return the pane with the new message
     * @throws IOException .
     */
    private Pane sendConvMessage() throws IOException {
        Pane item;
        ReplyMessage message;
        if (dragMessage == null) {
            MessageDTO messageDTO = service.sendMessage(new MessageDTO(loggedUser.getId(), List.of(email2User), messageText.getText(), LocalDate.now()));
            message= new ReplyMessage(messageDTO,null);
            lastConvMsjId =messageDTO.getId();
        }
        else {
            message = service.replyMessage(new ReplyMessageDTO(new MessageDTO(loggedUser.getId(), List.of(email2User), messageText.getText(), LocalDate.now()), dragMessage.getMessage().getId().toString()));
            dragMessage = null;
            lastConvMsjId = message.getId();
        }
        item = createItem(message);
        item.getChildren().forEach(node -> {
            if (node instanceof Label)
                node.setId(String.valueOf(messageText.getText()));
        });
        return item;
    }

    /**
     * sends a message to the group witch is displayed(groupID)
     * @return the Pane with the new message
     * @throws IOException .
     */
    private Pane sendGroupMessage() throws IOException {
        Pane item;
        ReplyMessage message;
        if(dragMessage == null) {
            MessageDTO messageDTO = service.replyAll(new MessageDTO(loggedUser.getId(), null, messageText.getText(), LocalDate.now()), groupId);
            message= new ReplyMessage(messageDTO,null);
            lastGroupMsjId =messageDTO.getId();
        }
        else{
            message = service.replyMessageGroup(new ReplyMessageDTO(new MessageDTO(loggedUser.getId(), null, messageText.getText(), LocalDate.now()), dragMessage.getMessage().getId().toString()), groupId);
            lastGroupMsjId = message.getId();
            dragMessage=null;
        }
        item = createGroupItem(message);
        item.getChildren().forEach(node -> {
            if (node instanceof Label)
                node.setId(String.valueOf(messageText.getText()));
        });
        return item;
    }

    /**
     * create a new view of a Message
     * @return a Pane
     * @throws IOException .
     */
    private Pane createItem(ReplyMessage replyMessage) throws IOException{
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("showConv-item.fxml"));
        Pane item = loader.load();
        ShowConvItemController controller = loader.getController();
        controller.setMessage(replyMessage);
        return item;
    }

    /**
     * create a group item
     * @param replyMessage the message witch is going to be displayed
     * @return the Pane with the item
     * @throws IOException .
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
     * @param service .
     * @param loggedUser .
     * @param email .
     */
    public void load(SuperService service, UserDTO loggedUser, String email){
        service.addObserver(this);
        settingsBtn.setVisible(false);
        anchorPaneShowConvView.addEventFilter(DragMessage.ANY, this::DragMessageHandler);
        this.loggedUser = loggedUser;
        this.service = service;
        this.email2User = email;
        this.groupId = null;
        scrollPaneShowConv.setVvalue(1.0f);
        nextPagePrivateConv();
        gridShowMessages.autosize();
    }

    /**
     * initialize loggedUser, service and groupId
     * load into gridPane all conversation from a group
     * @param service SuperService
     * @param loggedUser User
     * @param groupId Integer
     */
    public void loadGroup(SuperService service, UserDTO loggedUser, int groupId)
    {
        service.addObserver(this);
        anchorPaneShowConvView.addEventFilter(DragMessage.ANY, this::DragMessageHandler);
        this.loggedUser = loggedUser;
        this.service = service;
        this.groupId = groupId;
        nextGroupPage();
    }

    private boolean nextGroupPage(){
        List<ReplyMessage> messages=service.getGroupMessages(groupId,pageId++);
        if(messages.size()>0) {
            messages.forEach(replyMessage -> {
                try {
                    Pane item = createGroupItem(replyMessage);
                    lastGroupMsjId = replyMessage.getId();
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
            gridShowMessages.autosize();
            return true;
        }
        return false;
    }

    /**
     * handle press ok 'ENTER' key sending a Message with text from textField
     * @throws IOException .
     */
    @FXML
    private void pressedSendButton() throws IOException {
        if(!messageText.getText().equals("")){
            sendMessage();
        }
    }

    /**
     * handle press Send Button sending a message in current conversation
     * @param event .
     * @throws IOException .
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
     * @param m .
     */
    private void DragMessageHandler(DragMessage m) {
        if(m != null) {
            messageToReply.setText("replying to: "+m.getMessage().getMessage());
            this.dragMessage = m;
        }
    }

    /**
     * create a groupSettings view
     * @throws IOException .
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
    public void update(EventCustom eventCustom) {
        scrollPaneShowConv.setVvalue(1.0f);
        insertRows(1);
        System.out.println("update");
        if(eventCustom.getType().equals(ChangeEventType.NEW_MSJ)) {
            service.getConvMessagesGreaterThan(loggedUser.getId(), email2User,lastConvMsjId ).forEach(replyMessage -> {
                try {
                    Pane item = createItem(replyMessage);
                    item.getChildren().forEach(node -> {
                        if (node instanceof Label)
                            node.setId(String.valueOf(replyMessage.getId()));
                    });
                    if (replyMessage.getFrom().equals(loggedUser.getId())) {
                        gridShowMessages.add(item, 0, 0);
                    } else {
                        gridShowMessages.add(item, 1, 0);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
        else if(groupId!=null&&eventCustom.getType().equals(ChangeEventType.NEW_GROUP_MSJ)){
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
                        gridShowMessages.add(item, 0, 0);
                    }
                    else{
                        gridShowMessages.add(item, 1, 0);
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

    public void handlerScroll(ScrollEvent scrollEvent) {
        System.out.println(scrollPaneShowConv.getVvalue());
        if(scrollPaneShowConv.getVvalue()>0.45&&scrollPaneShowConv.getVvalue()<0.55){


            double val= scrollPaneShowConv.getVvalue();

            if(groupId==null) {
                if (nextPagePrivateConv()) {
                    //todo set scrollbar
                    int elm = gridShowMessages.getChildren().size();
                    System.out.println(elm);
                    val += 1.0 / elm * 7.0;
                    scrollPaneShowConv.setVvalue(val);
                }
            }
            else if(nextGroupPage()){
                //todo group msj
                int elm = gridShowMessages.getChildren().size();
                System.out.println(elm);
                val += 1.0 / elm * 7.0;
                scrollPaneShowConv.setVvalue(val);
            }
        }
    }
}
