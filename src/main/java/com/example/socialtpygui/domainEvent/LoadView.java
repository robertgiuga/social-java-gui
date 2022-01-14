package com.example.socialtpygui.domainEvent;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class LoadView extends Event {
    private static final long serialVersionUID = 97924780751776547L;
    private String validationCode;

    public static final EventType<LoadView> VIEW = new EventType<>("VIEW");
    public static final EventType<LoadView> ANY = VIEW;
    public static final EventType<LoadView> SIGN_UP_NEXT = new EventType<>(LoadView.ANY,"SIGN_UP_NEXT");
    public static final EventType<LoadView> FINAL_SIGN_UP = new EventType<>(LoadView.ANY,"FINAL_SIGN_UP");
    public static final EventType<LoadView> LOAD_EVENTS = new EventType<>(LoadView.ANY,"LOAD_EVENTS");

    public LoadView(EventType<? extends Event> eventType, String validationCode) {
        super(eventType);
        this.validationCode = validationCode;
    }

    public LoadView(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public LoadView(Object source, EventTarget target, EventType<? extends Event> eventType, String validationCode) {
        super(source, target, eventType);
        this.validationCode = validationCode;
    }

    public String getValidationCode() {
        return validationCode;
    }
}
