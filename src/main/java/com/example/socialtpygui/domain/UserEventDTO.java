package com.example.socialtpygui.domain;

public class UserEventDTO extends Entity<Integer>{
    private int EventId;
    private String notifyTime;

    public UserEventDTO(int eventId, String notifyTime) {
        this.setId(eventId);
        this.notifyTime = notifyTime;
    }

    public String getNotifyTime() {
        return notifyTime;
    }
}
