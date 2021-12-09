package com.example.socialtpygui.tests.DomainTests;

import com.example.socialtpygui.domain.Message;
import com.example.socialtpygui.domain.User;

public class DomainTests {

    /**
     * run all domain tests
     */
    public static void runTest(){
        EntityTests.runTests();
        UserTest.runTests();
        FriendshipTests.runTests();
        TupleTests.runTest();
        TupleOneTests.runTest();
        MessageTests.runTest();
        ReplyMessageTests.runTest();
        UserDTOTests.runTests();
        FriendshipDTOTests.runTest();
    }

}
