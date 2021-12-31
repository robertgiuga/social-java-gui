package com.example.socialtpygui.domainEvent;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class LikeEvent extends Event{

        private static final long serialVersionUID = 9792478075172137L;
        private int idPost;

        public static final EventType<LikeEvent> VIEW = new EventType<>("LIKE");
        public static final EventType<LikeEvent> ANY = VIEW;
        public static final EventType<LikeEvent> LIKE_POST = new EventType<>(LikeEvent.ANY,"LIKE_POST");
        public static final EventType<LikeEvent> UNLIKE_POST = new EventType<>(LikeEvent.ANY,"UNLIKE_POST");

        public LikeEvent(EventType<? extends javafx.event.Event> eventType, int idPost) {
            super(eventType);
            this.idPost = idPost;
        }

        public LikeEvent(Object source, EventTarget target, EventType<? extends javafx.event.Event> eventType, int idPost) {
            super(source, target, eventType);
            this.idPost = idPost;
        }

        public int getIdPost() {
                return idPost;
        }
}
