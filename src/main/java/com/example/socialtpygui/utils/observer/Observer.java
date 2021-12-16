package com.example.socialtpygui.utils.observer;


import com.example.socialtpygui.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}