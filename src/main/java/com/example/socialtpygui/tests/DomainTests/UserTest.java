package com.example.socialtpygui.tests.DomainTests;


import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.service.validators.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class UserTest {

    private UserTest(){};

    /**
     * run the particularly test of the class
     */
    public static void runTests(){
        testSetGet();
        testToString();
        testAddRemoveFriend();
    }

    /**
     * test if the get and set methods are working well
     */
    private static void testSetGet(){
        User user= new User("a","b","c","d");

        assert user.getFirstName().equals("a");
        assert user.getLastName().equals("b");
        assert user.getId().equals("c");
        assert user.getPassword().equals("d");
        user.setId("c1");
        user.setPassword("d1");
        assert user.getId().equals("c1");
        assert user.getPassword().equals("d1");

    }
    /**
     * tests the return statement of toString method to corresponds with what should be
     */
    private static void testToString()
    {
        User user= new User("a","b","c","d");
        List<String> el= new ArrayList<>();
        user.friendList.forEach(e->el.add(e.getId().toString()));
        assert user.toString().equals("Utilizator{" + "firstName='" + "a" + '\'' +
                ", lastName='" + "b" + '\'' +
                ", password='" + "d" + '\''+
                ", friends='"+ el+'\''+
                '}');

    }

    /**
     * test if the add and remove friends methods does what needed
     */
    private static void  testAddRemoveFriend(){
        User user= new User("a","b","c","d1");
        User user1= new User("a1","b","c1","d1");
        User user2= new User("a2","b","c2","d1");
        user.addFriend(user1);
        assert user.friendList.get(0)==user1;

        user.removeFriend(user1);
        assert user.friendList.size()==0;

        try{
            user.addFriend(user);
        }catch (ValidationException e){
            assert true;
        }
        user.addFriend(user1);
        try{
            user.addFriend(user1);
        }catch (ValidationException e){
            assert true;
        }
        try{
            user.addFriend(null);
        }catch (ValidationException e){
            assert true;
        }

    }
}
