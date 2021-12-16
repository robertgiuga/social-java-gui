package com.example.socialtpygui.utils.events;

public class ViewItemEvent implements Event {
    private ChangeEventType type;
    private int index;

    public ViewItemEvent(ChangeEventType type, int index) {
        this.type = type;
        this.index = index;
    }

    public ChangeEventType getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }
}
