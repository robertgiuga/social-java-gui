package com.example.socialtpygui.tests.DomainTests;

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
        EntityTests.runTests();
    }

}
