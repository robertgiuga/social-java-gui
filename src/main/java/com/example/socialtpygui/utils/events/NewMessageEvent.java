package com.example.socialtpygui.utils.events;

public class NewMessageEvent implements Event{
    private ChangeEventType eventType;

    public NewMessageEvent(ChangeEventType eventType) {
        this.eventType = eventType;
    }

    public ChangeEventType getEventType() {
        return eventType;
    }

}
