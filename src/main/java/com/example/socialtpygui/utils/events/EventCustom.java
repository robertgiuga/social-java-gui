package com.example.socialtpygui.utils.events;

public class EventCustom implements Event {
    private ChangeEventType type;

    public ChangeEventType getType() {
        return type;
    }

    public EventCustom(ChangeEventType type) {
        this.type = type;
    }
}
