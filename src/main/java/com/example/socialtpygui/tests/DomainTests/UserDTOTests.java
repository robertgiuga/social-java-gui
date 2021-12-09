package com.example.socialtpygui.tests.DomainTests;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;

public class UserDTOTests {

    private UserDTOTests(){}

    public static void runTests(){
        testGetSet();
        testToString();
    }

    private static void testGetSet(){
        User user= new User("a","b","c","d",false);

        UserDTO userDTO= new UserDTO(user);

        assert userDTO.getFirstName().equals("a");
        assert userDTO.getId().equals("c");
        assert userDTO.getLastName().equals("b");


    }

    private static void testToString(){
        User user= new User("a","b","c","d",false);

        UserDTO userDTO= new UserDTO(user);

        assert userDTO.toString().equals("c" + " : " + "a" + ", " + "b");

    }
}
