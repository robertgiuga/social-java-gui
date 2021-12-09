package com.example.socialtpygui.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FriendShipDTO {
    private final UserDTO user1;
    private final UserDTO user2;
    private final LocalDate date;

    public FriendShipDTO(UserDTO user1, UserDTO user2, LocalDate date) {
        this.user1 = user1;
        this.user2 = user2;
        this.date = date;
    }

    /**
     *
     * @return the first user from friendship
     */
    public UserDTO getUser1(){
        return user1;
    }

    /**
     *
     * @return the second user from friendship
     */
    public  UserDTO getUser2(){
        return user2;
    }

    /**
     *
     * @return the date when a friendship was created
     */
    public LocalDate getDate(){
        return date;
    }
}
