package com.example.socialtpygui.domainEvent;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class LoadConvList extends Event{

        private static final long serialVersionUID= 64812824686182293L;
        private String selectedItemId;

    public static final EventType<LoadConvList> LOAD= new EventType<>("LOAD");
    public static final EventType<LoadConvList> ANY= LOAD;
    public static final EventType<LoadConvList> LOAD_CONV= new EventType<>(LoadConvList.ANY,"LOAD_CONV");

        public LoadConvList(EventType<? extends Event> eventType) {
            super(eventType);
        }
}
