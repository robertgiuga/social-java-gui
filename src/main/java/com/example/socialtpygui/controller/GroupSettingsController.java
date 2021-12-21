package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.GroupDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.domainEvent.ItemSelected;
import com.example.socialtpygui.domainEvent.LoadConvList;
import com.example.socialtpygui.service.SuperService;
import com.example.socialtpygui.service.validators.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupSettingsController {
    @FXML
    private Button backBtn, leaveBtn;
    @FXML
    private GridPane gridMembers;
    @FXML
    private GridPane gridPaneGroupSettingSB;
    @FXML
    private TextField searchBarGroupSetting;
    @FXML
    private Label groupNameLabel;
    @FXML
    private AnchorPane settingsPane;

    User loggerUser;
    private int groupId;
    private SuperService service;
    List<UserDTO> participants;
    List<UserDTO> possibleParticipants;

    /**
     * handle the back button witch is fireing a ItemSelected Event for reloading the conv
     */
    @FXML
    private void handlerBackButton() {
        backBtn.fireEvent(new ItemSelected(ItemSelected.GROUP_LOAD_MSJ, String.valueOf(groupId)));
    }

    /**
     * create a member-viewItem with the given name and sets the id also
     *
     * @param name .
     * @param id   .
     * @return the Pane with the created view
     * @throws IOException .
     */
    private Pane createItem(String name, String id) throws IOException {
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("member-viewItem.fxml"));
        Pane item = loader.load();
        MemberController controller = loader.getController();
        controller.setGroupId(groupId);
        controller.setService(service);
        controller.setName(name);
        controller.setId(id);
        return item;
    }

    /**
     * loads the participants of the group and add users in participants list.
     * @param groupId
     */
    public void load(SuperService service, int groupId, User loggedUser) {
        catchEvent();
        groupNameLabel.setText(service.getGroup(groupId).getNameGroup());
        this.groupId = groupId;
        this.service = service;
        this.loggerUser = loggedUser;
        this.participants = new ArrayList<>();

        GroupDTO currentGroup = service.getGroup(groupId);
        currentGroup.getMembersEmail().forEach(s -> {
            if (! s.equals(this.loggerUser.getId())) {
                User user = service.findOneUser(s);
                try {
                    UserDTO userDTO = new UserDTO(user);
                    participants.add(userDTO);
                    gridMembers.addRow(gridMembers.getRowCount(), createItem(user.getFirstName() + " " + user.getLastName(), user.getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Leave from a group and fire LoadConvList event for load all conversation
     * @param actionEvent ActionEvent
     */
    public void handlerLeaveGroup(ActionEvent actionEvent) {
        service.removeUserFromGroup(this.loggerUser.getId(), groupId);
        leaveBtn.fireEvent(new LoadConvList(LoadConvList.LOAD_CONV));
    }

    /**
     * Load all users which is friend and are not in te group and add users in possibleParticipants list.
     * @param completeNameSearch String
     */
    public void loadUsersWithNameMatchSearchBarTxt(String completeNameSearch) {
        int row = 0;
        possibleParticipants = new ArrayList<>();
            gridPaneGroupSettingSB.getChildren().clear();
        try {
            for (UserDTO user : service.getUsersByName(completeNameSearch)) {
                if (!service.userInGroup(user.getId(), groupId)) {
                    FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("possibleMember-item.fxml"));
                    Pane item = fxmlLoader.load();
                    PossibleMemberItemController possibleMemberController = fxmlLoader.getController();
                    possibleParticipants.add(user);
                    possibleMemberController.setData(user);
                    possibleMemberController.setService(this.service);
                    possibleMemberController.setGroupId(this.groupId);
                    gridPaneGroupSettingSB.addRow(row++, item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * handler for enter pressed, when enter is pressed, load all friends which is not in the group.
     * @param keyEvent KeyEvent
     */
    public void handlerLoadPossibleMember(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            try {
                loadUsersWithNameMatchSearchBarTxt(searchBarGroupSetting.getText());
            } catch (ValidationException ignored) {
            }
        }
    }

    /**
     * handle the custom ItemSelected Event
     * @param e ItemSelected
     */
    public void handlerForSelectedMembers(ItemSelected e) {
        if (e.getEventType().equals(ItemSelected.REMOVE_MEMBER)) {
            gridMembers.getChildren().remove(participants.indexOf(new UserDTO(service.findOneUser(e.getSelectedItemId()))));
            participants.remove(new UserDTO(service.findOneUser(e.getSelectedItemId())));
        } else if (e.getEventType().equals(ItemSelected.ADD_MEMBER)) {
            UserDTO userDTO = new UserDTO(service.findOneUser(e.getSelectedItemId()));
            gridPaneGroupSettingSB.getChildren().remove(possibleParticipants.indexOf(userDTO));
            try {
                gridMembers.addRow(gridMembers.getRowCount(), createItem(userDTO.getFirstName() + " " + userDTO.getLastName(), userDTO.getId()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            participants.add(new UserDTO(service.findOneUser(e.getSelectedItemId())));
            possibleParticipants.remove(new UserDTO(service.findOneUser(e.getSelectedItemId())));
        }
    }

    /**
     * Catch fire events
     */
    public void catchEvent() {
        settingsPane.addEventFilter(ItemSelected.REMOVE_MEMBER, this::handlerForSelectedMembers);
        settingsPane.addEventFilter(ItemSelected.ADD_MEMBER, this::handlerForSelectedMembers);
    }

}
