package com.example.socialtpygui.controller;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.domainEvent.ItemSelected;
import com.example.socialtpygui.service.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class PossibleMemberItemController {

    @FXML
    private Label labelPossibleMember;

    @FXML
    private ImageView btnPossibleMember;

    SuperService service;
    private UserDTO userDTO;
    int groupId;




    /**
     * Set service
     * @param service SuperService
     */
    public void setService(SuperService service) {
        this.service = service;
    }

    /**
     * Set userDTO and set text to labelPossibleMember.
     * @param userDTO UserDTO
     */
    public void setData(UserDTO userDTO)
    {
        this.userDTO = userDTO;
        labelPossibleMember.setText(userDTO.getFirstName() + " " + userDTO.getLastName());
    }

    /**
     * Set groupId
     * @param groupId Integer
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * Handler for add a member in a group with id "groupId"
     * @param mouseEvent MouseEvent
     */
    public void handlerAddMemberBtn(MouseEvent mouseEvent) {
        service.addUserToGroup(userDTO.getId(), groupId);
        btnPossibleMember.fireEvent(new ItemSelected(ItemSelected.ADD_MEMBER, userDTO.getId()));
    }
}
