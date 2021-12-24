package com.example.socialtpygui.domain;

import java.time.LocalDate;

public class Event extends Entity<Integer>{
    private String description;
    private LocalDate date;

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }
}
