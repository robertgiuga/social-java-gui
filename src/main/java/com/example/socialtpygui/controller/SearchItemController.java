package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.TupleOne;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.w3c.dom.events.MouseEvent;

public class SearchItemController {

    @FXML
    private Label nameLbl, cancelRequestLabel, sendRequestLabel;

    @FXML
    private ImageView addBtn;

    @FXML
    private  ImageView cancelBtn;

    private UserDTO userDTO;
    private SuperService service;
    private UserDTO loggedUser;

    /**
     * Set the name label.
     * @param userDTO
     */
    public void setData(UserDTO userDTO)
    {
        this.userDTO = userDTO;
        nameLbl.setText(userDTO.getFirstName() + " " + userDTO.getLastName());
    }

    /**
     * Hide addBtn
     */
    public void hideAddBtn()
    {
        addBtn.setVisible(false);
    }

    /**
     * Hide cancelBtn
     */
    public void hideCancelBtn()
    {
        cancelBtn.setVisible(false);
    }

    /**
     * Hide cancel label.
     */
    public void hideCancelLabel() {cancelRequestLabel.setVisible(false);}

    /**
     * Hide send label.
     */
    public void hideSendLabel() {sendRequestLabel.setVisible(false);}

    /**
     * Set service
     * @param service
     */
    public void setService(SuperService service) {
        this.service = service;
    }

    /**
     * Set the user which is logged
     * @param loggedUser
     */
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * Send a request when the button is pushed and hide addBtn button.
     * @param mouseEvent
     */
    public void handlerAddBtn(javafx.scene.input.MouseEvent mouseEvent) {
        service.sendRequest(this.loggedUser.getId(), this.userDTO.getId());
        addBtn.setVisible(false);
        cancelBtn.setVisible(true);
        cancelRequestLabel.setVisible(true);
        sendRequestLabel.setVisible(false);
    }

    /**
     * Cancel a request when the button is pushed and hide cancelBtn button.
     * @param mouseEvent
     */
    public void handlerCancelBtn(javafx.scene.input.MouseEvent mouseEvent) {
        service.friendshipRequestRemove(new TupleOne<>(this.loggedUser.getId(), this.userDTO.getId()));
        addBtn.setVisible(true);
        cancelBtn.setVisible(false);
        cancelRequestLabel.setVisible(false);
        sendRequestLabel.setVisible(true);
    }
}
