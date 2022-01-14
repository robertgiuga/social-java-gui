package com.example.socialtpygui.utils.events;

import com.example.socialtpygui.domain.UserDTO;

public class EventCustom implements Event {
    private ChangeEventType type;
    private UserDTO userDTO;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public ChangeEventType getType() {
        return type;
    }

    public EventCustom(ChangeEventType type) {
        this.type = type;
    }
}
