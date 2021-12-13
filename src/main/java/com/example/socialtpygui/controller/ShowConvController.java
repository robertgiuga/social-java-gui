package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.Message;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private ScrollPane scrollShowMessages;

    @FXML
    private TextField messageText;

    @FXML
    private Button buttonSend;

    private User loggedUser;

    private String email;

    /**
     * create a new view of a Message
     * @param message
     * @return a Pane
     * @throws IOException
     */
    private Pane createItem(String message) throws IOException{
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("showConv-item.fxml"));
        Pane item = loader.load();
        ShowConvItemController controller = loader.getController();
        controller.setMessage(message);
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
        this.loggedUser = loggedUser;
        this.service = service;
        this.email = email;
        service.getMessages(loggedUser.getId(), email).forEach(replyMessage->{
            try{
                Pane item = createItem(replyMessage.getMessage());
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
     * @param event
     * @throws IOException
     */
    @FXML
    private void handlerKeyPressed(KeyEvent event) throws IOException {
        if(!messageText.getText().equals(""))
            if(event.getCode().equals(KeyCode.ENTER)) {
                List<String> to = new ArrayList<>();
                to.add(email);
                service.sendMessage(new Message(loggedUser.getId(), to, messageText.getText(), LocalDate.now()));
                Pane item = createItem(messageText.getText());
                item.getChildren().forEach(node->{
                    if (node instanceof Label)
                        node.setId(String.valueOf(messageText.getText()));
                });
                gridShowMessages.add(item, 1, gridShowMessages.getRowCount());
                messageText.clear();
            }
    }
}
