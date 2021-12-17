package com.example.socialtpygui.tests.DomainTests;


import com.example.socialtpygui.domain.MessageDTO;
import com.example.socialtpygui.domain.ReplyMessage;
import com.example.socialtpygui.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReplyMessageTests {

    private ReplyMessageTests(){}

    public static void runTest()
    {
        testGetSet();
    }

    private static void testGetSet()
    {

        User user = new User("Gulea", "Cristian","gulea@gmail.com","a");
        User user1 = new User("Paul", "Marian","marian@gmail.com","a1");
        User user2 = new User("George", "Mihai","mihai@gmail.com","a2");
        List<String> list = new ArrayList<>(); list.add("marian@gmail.com"); list.add("mihai@gmail.com");
        List<String> list1 = new ArrayList<>(); list.add("gulea@gmail.com"); list.add("marian@gmail.com");
        LocalDate date = LocalDate.of(2020, 1, 8);
        MessageDTO messageDTO =  new MessageDTO( "mihai@gmail.com", list1, "Atentie!", date);
        messageDTO.setId(43);
        ReplyMessage replyMessage = new ReplyMessage("mihai@gmail.com", list, "Reminder!", date, messageDTO);
        assert (replyMessage.getOriginal().equals(messageDTO));

    }

}
