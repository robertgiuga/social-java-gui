package com.example.socialtpygui.domainEvent;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class UserSelected extends Event {

    private static final long serialVersionUID= 6481283618293L;
    private String selectedUserId;

    public static final EventType<UserSelected> USER= new EventType<>(Event.ANY,"ANY");
    public static final EventType<UserSelected> ANY= USER;
    public static final EventType<UserSelected> USER_LOAD_MSJ= new EventType<>(UserSelected.ANY,"USER_LOAD_MSJ");
    public static final EventType<UserSelected> USER_SELECTED= new EventType<>(UserSelected.ANY,"USER_SELECTED");

    public UserSelected(EventType<? extends Event> eventType,String userId) {
        super(eventType);
        selectedUserId =userId;
    }

    public UserSelected(Object source, EventTarget target, EventType<? extends Event> eventType, String selectedUserId) {
        super(source, target, eventType);
        this.selectedUserId = selectedUserId;
    }

    public String getSelectedUserId() {
        return selectedUserId;
    }


}
