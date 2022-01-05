package com.example.socialtpygui.utils.events;

import com.example.socialtpygui.domain.UserDTO;

public class ViewItemEvent extends EventCustom {
    private UserDTO userDTO;

    public ViewItemEvent(ChangeEventType type, UserDTO userDTO) {
        super(type);
        this.userDTO = userDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }
}
