package com.example.socialtpygui.tests.DomainTests;

import com.example.socialtpygui.domain.Post;

import java.time.LocalDate;

public class PostTests {

    private PostTests(){};

    /**
     * run the particularly test of the class
     */
    public static void runTests(){
        testGet();
    }

    /**
     * test if the get and set methods are working well
     */
    private static void testGet(){
        Post post = new Post("descriere", "email", LocalDate.parse("2021-12-12"), 0);
        assert post.getDate().toString().equals("2021-12-12");
        assert post.getDescription().equals("descriere");
        assert post.getEmailUser().equals("email");
        assert post.getNrLikes()==0;
    }
}
