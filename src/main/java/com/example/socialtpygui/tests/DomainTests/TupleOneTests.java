package com.example.socialtpygui.tests.DomainTests;


import com.example.socialtpygui.domain.Tuple;
import com.example.socialtpygui.domain.TupleOne;

public class TupleOneTests {

    private TupleOneTests(){};

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
        TupleOne<Integer> tuple= new TupleOne<>(1,2);

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
        TupleOne<Integer> tuple= new TupleOne<>(1,2);

        assert tuple.toString().equals("" + 1 + "," + 2);
    }

    /**
     * tests the equal propriety
     */
    private static void testEquals(){
        TupleOne<Integer> tuple= new TupleOne<>(1,2);
        TupleOne<Integer> tuple2= new TupleOne<>(1,2);
        TupleOne<Integer> tuple3= new TupleOne<>(2,1);

        assert tuple.equals(tuple2);
        assert tuple.equals(tuple3);
    }
}
