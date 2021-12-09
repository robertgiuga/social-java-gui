package com.example.socialtpygui.tests;


import com.example.socialtpygui.tests.RepositoryTest.RepoDBTest.FriendshipRequestDBTest;
import com.example.socialtpygui.tests.RepositoryTest.RepositoryTests;
import com.example.socialtpygui.tests.DomainTests.DomainTests;


public class Tests {

    public static void RunALL() {
        RepositoryTests.runTests();
        DomainTests.runTest();
        //ServiceTests.runTests();
        FriendshipRequestDBTest.runTests();
    }
}
