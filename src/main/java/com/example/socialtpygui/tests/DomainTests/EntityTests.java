package com.example.socialtpygui.tests.DomainTests;


import com.example.socialtpygui.domain.Entity;

public class EntityTests {

    private EntityTests(){};

    /**
     * run the particularly test of the class
     */
    public static void runTests(){
        testGetSet();
        testEquals();
        testToString();
    }

    /**
     * Tests the getters and setters of Entity
     */
    private static void testGetSet(){
        Entity<Integer> entity= new Entity<>();
        entity.setId(1);
        assert entity.getId()==1;
        entity.setId(2);
        assert entity.getId()==2;
    }

    /**
     * tests the equal propriety
     */
    private static void testEquals(){
        Entity<Integer> entity= new Entity<>();
        entity.setId(1);
        Entity<Integer> entity2= new Entity<>();
        entity2.setId(1);
        Entity<Integer> entity3= new Entity<>();
        entity2.setId(2);

        assert entity.equals(entity);
        assert entity.equals(entity);
        assert entity2.equals(entity2);
        assert !entity.equals(entity3);
    }

    /**
     * tests the return statement of toString method to corresponds with what should be
     */
    private static void testToString(){
        Entity<Integer> entity= new Entity<>();
        entity.setId(1);

        assert entity.toString().equals("Entity{" + "id=1"+ '}');
    }
}
