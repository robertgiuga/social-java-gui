package com.example.socialtpygui.utils.events;

import com.example.socialtpygui.domain.UserDTO;

public class ViewItemEvent implements Event {
    private ChangeEventType type;
    private UserDTO userDTO;

    public ViewItemEvent(ChangeEventType type, UserDTO userDTO) {
        this.type = type;
        this.userDTO = userDTO;
    }

    public ChangeEventType getType() {
        return type;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }
}
