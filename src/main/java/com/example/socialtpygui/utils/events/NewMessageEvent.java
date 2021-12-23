package com.example.socialtpygui.utils.events;

public class NewMessageEvent implements Event{
    private ChangeEventType eventType;
    private int nrOfMsj;

    public NewMessageEvent(ChangeEventType eventType, int nrOfMsj) {
        this.eventType = eventType;
        this.nrOfMsj = nrOfMsj;
    }

    public ChangeEventType getEventType() {
        return eventType;
    }

    public int getNrOfMsj() {
        return nrOfMsj;
    }
}
