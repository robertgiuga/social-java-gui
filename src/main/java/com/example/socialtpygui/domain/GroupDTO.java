package com.example.socialtpygui.domain;

import java.util.List;

public class GroupDTO {
    private int id;
    private String nameGroup;
    private List<String> membersEmail;

    public GroupDTO(Group group)
    {
        this.id = group.getId();
        this.nameGroup = group.getNameGroup();
        group.getMembersList().forEach(user->{this.membersEmail.add(user.getId());});
    }

    /**
     * @return id of this group
     */
    public int getId() {
        return id;
    }

    /**
     * @return name of the group
     */
    public String getNameGroup() {
        return nameGroup;
    }

    /**
     * @return list with members emails
     */
    public List<String> getMembersEmail() {
        return membersEmail;
    }
}
