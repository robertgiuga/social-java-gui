package com.example.socialtpygui.domainEvent;

import com.example.socialtpygui.domain.MessageDTO;
import javafx.event.Event;
import javafx.event.EventType;

public class DragMessage extends Event {
    private static final long serialVersionUID = 97924780751776547L;
    private MessageDTO messageDTO;

    public static final EventType<DragMessage> MESSAGE = new EventType<>("MESSAGE");
    public static final EventType<DragMessage> ANY = MESSAGE;
    public static final EventType<DragMessage> MESSAGE_REPLY = new EventType<>(DragMessage.ANY,"MESSAGE_REPLY");
    public static final EventType<DragMessage> MESSAGE_SELECTED = new EventType<>(DragMessage.ANY,"MESSAGE_SELECTED");

    /**
     * Constructor for Event DragMessage
     * @param eventType
     * @param messageDTO
     */
    public DragMessage(EventType<? extends Event> eventType, MessageDTO messageDTO){
        super(eventType);
        this.messageDTO = messageDTO;
    }

    /**
     * @return field message
     */
    public MessageDTO getMessage() {
        return messageDTO;
    }
}
