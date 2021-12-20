package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.GroupDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.ItemSelected;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class GroupSettingsController {
    @FXML
    private Button backBtn;
    @FXML
    private GridPane gridMembers;

    private int groupId;
    private SuperService service;

    @FXML
    private void handlerBackButton(){
        backBtn.fireEvent(new ItemSelected(ItemSelected.GROUP_LOAD_MSJ,String.valueOf(groupId)));
    }

    private Pane createItem(String name,String id) throws IOException {
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("member-viewItem.fxml"));
        Pane item = loader.load();
        MemberController controller=loader.getController();
        controller.setName(name);
        controller.setId(id);
        return item;
    }

    /**
     *  loads the participants of the group
     * @param groupId
     */
    public void load(SuperService service, int groupId){
        this.groupId = groupId;
        this.service=service;

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
}
