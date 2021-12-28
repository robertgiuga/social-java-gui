package com.example.socialtpygui.tests.RepositoryTest;


import com.example.socialtpygui.tests.RepositoryTest.RepoDBTest.*;

public class RepositoryTests {

    public static void runTests(){
        UserDBTest.runTests();
        FriendshipDBTest.runTests();
        MessageDBTest.runTests();
        EventDBTest.runTests();
        PostDBTest.runTests();
    }
}
