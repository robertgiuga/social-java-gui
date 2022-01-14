package com.example.socialtpygui.domain;

import java.time.LocalDate;

public class Post extends  Entity<Integer>{
    private final String description;
    private final String emailUser;
    private final LocalDate date;
    private int nrLikes;

    public Post(String description, String emailUser, LocalDate date, int nrLikes) {
        this.description = description;
        this.emailUser = emailUser;
        this.date = date;
        this.nrLikes = nrLikes;
    }

    public int getNrLikes() {
        return nrLikes;
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
