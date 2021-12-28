package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.ItemSelected;
import com.example.socialtpygui.service.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class MemberController {

    @FXML
    private Label nameLbl;
    @FXML
    private ImageView removeBtn;

    private int groupId;
    private String id;
    private SuperService service;

    /**
     * sets the name witch is gonna be displayed
     * @param name
     */
    public void setName(String name){
        nameLbl.setText(name);
    }

    /**
     * sets the id of the user witch is displayed
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    @FXML
    private void handlerRemoveBtn() {
        service.removeUserFromGroup(id, groupId);
        removeBtn.fireEvent(new ItemSelected(ItemSelected.REMOVE_MEMBER, id));
    }

    public void setService(SuperService service) {
        this.service = service;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
