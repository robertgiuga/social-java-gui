package com.example.socialtpygui.domain;

import java.util.List;

public class Group extends Entity<Integer>{
    private String nameGroup;
    private List<User> membersList;

    public Group(String nameGroup, List<User> membersList)
    {
        this.membersList = membersList;
        this.nameGroup = nameGroup;
    }

    /**
     * @return name of the Group
     */
    public String getNameGroup() {
        return nameGroup;
    }

    /**
     * @return list of users, users which is in this group
     */
    public List<User> getMembersList() {
        return membersList;
    }

    /**
     * Set the name of the group.
     * @param nameGroup String
     */
    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    /**
     * Set the list of members.
     * @param membersList List<User>
     */
    public void setMembersList(List<User> membersList) {
        this.membersList = membersList;
    }
}

