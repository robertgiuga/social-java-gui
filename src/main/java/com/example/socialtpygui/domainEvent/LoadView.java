package com.example.socialtpygui.domainEvent;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class LoadView extends Event {
    private static final long serialVersionUID = 97924780751776547L;

    public static final EventType<LoadView> VIEW = new EventType<>("VIEW");
    public static final EventType<LoadView> ANY = VIEW;
    public static final EventType<LoadView> SIGN_UP_NEXT = new EventType<>(LoadView.ANY,"SIGN_UP_NEXT");
    public static final EventType<LoadView> FINAL_SIGN_UP = new EventType<>(LoadView.ANY,"FINAL_SIGN_UP");

    public LoadView(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public LoadView(Object source, EventTarget target, EventType<? extends Event> eventType) {
        super(source, target, eventType);
    }
}
