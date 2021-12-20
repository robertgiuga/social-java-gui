package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.GroupDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.ItemSelected;
import com.example.socialtpygui.domainEvent.LoadConvList;
import com.example.socialtpygui.service.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

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

    User loggerUser;
    private int groupId;
    private SuperService service;

    /**
     * handle the back button witch is fireing a ItemSelected Event for reloading the conv
     */
    @FXML
    private void handlerBackButton(){
        backBtn.fireEvent(new ItemSelected(ItemSelected.GROUP_LOAD_MSJ,String.valueOf(groupId)));
    }

    /**
     * create a member-viewItem with the given name and sets the id also
     * @param name .
     * @param id .
     * @return the Pane with the created view
     * @throws IOException .
     */
    private Pane createItem(String name,String id) throws IOException {
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("member-viewItem.fxml"));
        Pane item = loader.load();
        MemberController controller=loader.getController();
        controller.setGroupId(groupId);
        controller.setService(service);
        controller.setName(name);
        controller.setId(id);
        return item;
    }

    /**
     *  loads the participants of the group
     * @param groupId
     */
    public void load(SuperService service, int groupId, User loggedUser){
        groupNameLabel.setText(service.getGroup(groupId).getNameGroup());
        this.groupId = groupId;
        this.service=service;
        this.loggerUser = loggedUser;

        GroupDTO currentGroup= service.getGroup(groupId);
        currentGroup.getMembersEmail().forEach(s -> {
            User user =service.findOneUser(s);
            try {
                gridMembers.addRow(gridMembers.getRowCount(),createItem(user.getFirstName()+" "+user.getLastName(),user.getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void handlerLeaveGroup(ActionEvent actionEvent) {
        service.removeUserFromGroup(this.loggerUser.getId(), groupId);
        leaveBtn.fireEvent(new LoadConvList(LoadConvList.LOAD_CONV));
    }


}
