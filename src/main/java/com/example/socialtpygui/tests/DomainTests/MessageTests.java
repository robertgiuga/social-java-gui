package com.example.socialtpygui.tests.DomainTests;

import com.example.socialtpygui.domain.Message;
import com.example.socialtpygui.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MessageTests {

    private MessageTests(){}

    public static void runTest(){
        testSetGet();
    }

    private static void testSetGet()
    {

        User user = new User("Gulea", "Cristian","gulea@gmail.com","a",false);
        User user1 = new User("Paul", "Marian","marian@gmail.com","a2",false);
        User user2 = new User("George", "Mihai","mihai@gmail.com","a3",false);
        List<String> list = new ArrayList<>(); list.add("marian@gmail.com"); list.add("mihai@gmail.com");
        List<String> list1 = new ArrayList<>(); list1.add("marian@gmail.com"); list1.add("mihai@gmail.com"); list1.add("gulea@gmail.com");
        LocalDate date = LocalDate.of(2020, 1, 8);
        LocalDate date1 = LocalDate.of(2001, 1, 8);
        Message message = new Message( "gulea@gmail.com", list, "Reminder! Tema Laborator!", date);
        message.setId(123);
        assert (message.getMessage().equals("Reminder! Tema Laborator!"));
        assert (message.getFrom().equals("gulea@gmail.com"));
        assert (message.getId() == 123);
        assert (message.getTo().get(0).equals("marian@gmail.com"));
        assert (message.getTo().get(1).equals("mihai@gmail.com"));
        assert (message.getData().equals(date));
        message.setId(1234);
        message.setData(date1);
        message.setTo(list1);
        message.setMessage("REMINDER! Tema Laborator!");
        message.setFrom("gg@gmail.com");
        assert (message.getId() == 1234);
        assert (message.getMessage().equals("REMINDER! Tema Laborator!"));
        assert (message.getFrom().equals("gg@gmail.com"));
        assert (message.getTo().get(0).equals("marian@gmail.com"));
        assert (message.getTo().get(1).equals("mihai@gmail.com"));
        assert (message.getTo().get(2).equals("gulea@gmail.com"));
        assert (message.getData().equals(date1));


    }
}
