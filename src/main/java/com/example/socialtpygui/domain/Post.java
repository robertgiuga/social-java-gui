package com.example.socialtpygui.domain;

import java.time.LocalDate;

public class Post extends  Entity<Integer>{
    private final String description;
    private final String emailUser;
    private final LocalDate date;

    public Post(String description, String emailUser, LocalDate date) {
        this.description = description;
        this.emailUser = emailUser;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public LocalDate getDate() {
        return date;
    }
}
