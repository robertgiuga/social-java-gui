package com.example.socialtpygui.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Friendship extends Entity<TupleOne<String>> {

    LocalDate date;

    public Friendship(String id1 ,String id2,LocalDate date) {
        this.setId(new TupleOne<>(id1,id2));
        this.date= date;

    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDate getDate() {
        return date;
    }
}
