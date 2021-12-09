package com.example.socialtpygui.tests.DomainTests;


import com.example.socialtpygui.domain.Tuple;

public class TupleTests {

    private TupleTests(){}

    /**
     * run the particularly test of the class
     */
    public static void runTest(){
        testGetSet();
        testToString();
        testEquals();
    }

    /**
     * test if the get and set methods are working well
     */
    private static void testGetSet(){
        Tuple<Integer,Integer> tuple= new Tuple<>(1,2);

        assert tuple.getRight()==2;
        assert tuple.getLeft()==1;
        tuple.setLeft(3);
        tuple.setRight(4);
        assert tuple.getRight()==4;
        assert tuple.getLeft()==3;
    }

    /**
     * tests the return statement of toString method to corresponds with what should be
     */
    private static void testToString(){
        Tuple<Integer,Integer> tuple= new Tuple<>(1,2);

        assert tuple.toString().equals("" + 1 + "," + 2);
    }

    /**
     * tests the equal propriety
     */
    private static void testEquals(){
        Tuple<Integer,Integer> tuple= new Tuple<>(1,2);
        Tuple<Integer,Integer> tuple2= new Tuple<>(1,2);
        Tuple<Integer,Integer> tuple3= new Tuple<>(2,1);

        assert tuple.equals(tuple2);
        assert !tuple.equals(tuple3);
    }
}
