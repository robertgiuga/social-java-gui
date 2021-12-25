package com.example.socialtpygui.domain;

import java.time.LocalDate;
import java.util.List;

public class EventDTO extends Entity<Integer>{
    private String description;
    private LocalDate date;
    private String location;
    private List<UserDTO> participants;
    private String name;
    private String notification;

    public String getName() {
        return name;
    }

    public EventDTO(String description, LocalDate date, String location, List<UserDTO> participants, String name) {
        this.description = description;
        this.date = date;
        this.location = location;
        this.participants = participants;
        this.name = name;
        this.notification = null;
    }

    public EventDTO(String description, LocalDate date, String location, List<UserDTO> participants, String name, String notification) {
        this.description = description;
        this.date = date;
        this.location = location;
        this.participants = participants;
        this.name = name;
        this.notification = notification;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public List<UserDTO> getParticipants() {
        return participants;
    }

    public String getNotification() {return this.notification; }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
