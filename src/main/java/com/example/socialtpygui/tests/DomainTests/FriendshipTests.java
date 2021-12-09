package com.example.socialtpygui.tests.DomainTests;

import com.example.socialtpygui.domain.Friendship;
import com.example.socialtpygui.domain.TupleOne;

import java.time.LocalDate;

public class FriendshipTests {

    private FriendshipTests(){};

    /**
     * run the particularly test of the class
     */
    public static void runTests(){
        testSetGet();
    }

    /**
     * test if the get and set methods are working well
     */
    private static void testSetGet(){
        LocalDate date = LocalDate.now();
        Friendship friendship= new Friendship("a","b", date);

        assert friendship.getId().getRight().equals("b");
        assert friendship.getId().getLeft().equals("a");
        assert friendship.getDate().equals(date);

        friendship.setId(new TupleOne<>("c","d"));

        assert friendship.getId().getRight().equals("d");
        assert friendship.getId().getLeft().equals("c");

    }
}
