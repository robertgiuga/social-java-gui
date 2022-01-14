package com.example.socialtpygui.domainEvent;


import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class EventCursor extends Event {
    private static final long serialVersionUID = 97924780751776547L;

    public static final EventType<EventCursor> VIEW = new EventType<>("CURSOR");
    public static final EventType<EventCursor> ANY = VIEW;
    public static final EventType<EventCursor> NEXT_EVENT = new EventType<>(EventCursor.ANY,"NEXT_EVENT");
    public static final EventType<EventCursor> PREVIOUS_EVENT = new EventType<>(EventCursor.ANY,"PREVIOUS_EVENT");

    public EventCursor(EventType<? extends javafx.event.Event> eventType) {
        super(eventType);
    }

    public EventCursor(Object source, EventTarget target, EventType<? extends javafx.event.Event> eventType) {
        super(source, target, eventType);
    }
}
