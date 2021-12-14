package com.example.socialtpygui.tests.DomainTests;


import com.example.socialtpygui.domain.FriendShipDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;

import java.time.LocalDate;

public class FriendshipDTOTests {

    private FriendshipDTOTests(){}

    public static void runTest(){

    }

    private static void testGetSet(){
        LocalDate date = LocalDate.now();
        User user= new User("a","b","c","d");
        User user1= new User("d","e","f","g");
        UserDTO userDTO= new UserDTO(user);
        UserDTO userDTO1= new UserDTO(user1);
        FriendShipDTO friendShipDTO= new FriendShipDTO(userDTO,userDTO1,date);

        assert friendShipDTO.getUser1().getId().equals(user.getId());
        assert friendShipDTO.getUser2().getId().equals(user1.getId());
        assert friendShipDTO.getDate().equals(date);
    }
}
