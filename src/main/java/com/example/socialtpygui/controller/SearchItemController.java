package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.TupleOne;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SearchItemController {

    @FXML
    private Label nameLbl;

    @FXML
    private Button addBtn;

    @FXML
    private  Button cancelBtn;

    private UserDTO userDTO;

    private SuperService service;

    private User loggedUser;


    public void setData(UserDTO userDTO)
    {
        this.userDTO = userDTO;
        nameLbl.setText(userDTO.getFirstName() + " " + userDTO.getLastName());
    }

    public void hideAddBtn()
    {
        addBtn.setVisible(false);
    }

    public void hideCancelBtn()
    {
        cancelBtn.setVisible(false);
    }

    public void setService(SuperService service) {
        this.service = service;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }



    public void handlerAddBtn(ActionEvent actionEvent) {
        if (! addBtn.isPressed())
        {
            service.sendRequest(this.loggedUser.getId(), this.userDTO.getId());
            addBtn.setVisible(false);
            cancelBtn.setVisible(true);
        }
    }

    public void handlerCloseBtn(ActionEvent actionEvent) {
        if (! cancelBtn.isPressed()){
            service.friendshipRequestRemove(new TupleOne<>(this.loggedUser.getId(), this.userDTO.getId()));
            addBtn.setVisible(true);
            cancelBtn.setVisible(false);
        }
    }
}
