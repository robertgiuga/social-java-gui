package com.example.socialtpygui.domain;

public class PageDTO {
    UserDTO userDTO;
    int numberOfNewRequests;
    int numberOfNewMessages;
    int numberOfTodayEvents;

    public PageDTO(UserDTO userDTO, int numberOfNewRequests, int numberOfNewMessages, int numberOfTodayEvents) {
        this.userDTO = userDTO;
        this.numberOfNewRequests = numberOfNewRequests;
        this.numberOfNewMessages = numberOfNewMessages;
        this.numberOfTodayEvents = numberOfTodayEvents;
    }


    public UserDTO getUserDTO() {
        return userDTO;
    }

    public int getNumberOfNewRequests() {
        return numberOfNewRequests;
    }

    public int getNumberOfNewMessages() {
        return numberOfNewMessages;
    }

    public int getNumberOfTodayEvents() {
        return numberOfTodayEvents;
    }
}
