package com.example.socialtpygui.domainEvent;

import com.example.socialtpygui.domain.Message;
import com.example.socialtpygui.domain.ReplyMessage;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class DragMessage extends Event {
    private static final long serialVersionUID = 97924780751776547L;
    private Message message;

    public static final EventType<DragMessage> MESSAGE = new EventType<>("MESSAGE");
    public static final EventType<DragMessage> ANY = MESSAGE;
    public static final EventType<DragMessage> MESSAGE_REPLY = new EventType<>(DragMessage.ANY,"MESSAGE_REPLY");
    public static final EventType<DragMessage> MESSAGE_SELECTED = new EventType<>(DragMessage.ANY,"MESSAGE_SELECTED");

    public DragMessage(EventType<? extends Event> eventType, Message message){
        super(eventType);
        this.message = message;
    }


    public Message getMessage() {
        return message;
    }
}
