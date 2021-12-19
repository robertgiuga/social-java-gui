package com.example.socialtpygui.domainEvent;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class ItemSelected extends Event {

    private static final long serialVersionUID= 6481283618293L;
    private String selectedItemId;

    public static final EventType<ItemSelected> USER= new EventType<>("ITEM");
    public static final EventType<ItemSelected> ANY= USER;
    public static final EventType<ItemSelected> USER_LOAD_MSJ= new EventType<>(ItemSelected.ANY,"USER_LOAD_MSJ");
    public static final EventType<ItemSelected> USER_SELECTED= new EventType<>(ItemSelected.ANY,"USER_SELECTED");
    public static final EventType<ItemSelected> GROUP_LOAD_MSJ= new EventType<>(ItemSelected.ANY,"GROUP_LOAD_MSJ");

    public ItemSelected(EventType<? extends Event> eventType, String userId) {
        super(eventType);
        selectedItemId =userId;
    }

    public ItemSelected(Object source, EventTarget target, EventType<? extends Event> eventType, String selectedUserId) {
        super(source, target, eventType);
        this.selectedItemId = selectedUserId;
    }

    public String getSelectedItemId() {
        return selectedItemId;
    }


}
