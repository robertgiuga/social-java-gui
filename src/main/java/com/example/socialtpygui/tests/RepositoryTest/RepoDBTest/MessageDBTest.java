package com.example.socialtpygui.tests.RepositoryTest.RepoDBTest;


import com.example.socialtpygui.domain.MessageDTO;
import com.example.socialtpygui.domain.ReplyMessage;
import com.example.socialtpygui.repository.db.MessageDb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class MessageDBTest {

    private static MessageDb messageDBTest = new MessageDb("jdbc:postgresql://localhost:5432/SocialNetworkTest", "postgres", "postgres");

    private MessageDBTest(){}

    public static void runTests()
    {
        testFindOne();
        testFindAllMessageBetweenTwoUsers();
        testRemoveAndSaveSize();
        testGetAllEmailsFromExistingConversation();
    }

    private static void testFindOne()
    {
        assert (messageDBTest.findOne(1).getFrom().equals("jon1@yahoo.com"));
        assert (messageDBTest.findOne(1).getMessage().equals("Ce faci?"));
        assert (messageDBTest.findOne(1).getTo().get(0).equals("gg@gmail.com"));
        assert (messageDBTest.findOne(1).getTo().get(1).equals("andr@gamail.com"));
        assert (messageDBTest.findOne(1).getId() == 1);
        assert (messageDBTest.findOne(-21) == null);
    }

    private static void testFindAllMessageBetweenTwoUsers()
    {
        Iterable<ReplyMessage> list = messageDBTest.findAllMessageBetweenTwoUsers("aand@hotmail.com","snj@gmail.com");
        long size = StreamSupport.stream(list.spliterator(), false).count();
        assert(size == 3);
        assert(messageDBTest.findOne(4).getMessage().equals("Ce faceti baietii?"));
        assert (messageDBTest.findOne(6).getMessage().equals("Merg la magazin, tu?"));
        assert (messageDBTest.findOne(7).getMessage().equals("Merg si eu la magazin, ne intalnim."));
    }

    private static void testRemoveAndSaveSize()
    {
        assert (messageDBTest.size() == 7);
        List<String> list = new ArrayList<String>(); list.add("andr@gamail.com");
        MessageDTO messageDTO = new MessageDTO("gg@gmail.com", list, "Proiect1.pdf", LocalDate.now());
        messageDBTest.save(messageDTO);
        assert (messageDBTest.size() == 8);
        assert (messageDBTest.findOne(messageDTO.getId()) != null);
        messageDBTest.remove(messageDTO.getId());
        assert (messageDBTest.size() == 7);
        assert (messageDBTest.findOne(messageDTO.getId()) == null);
    }

    private static void testGetAllEmailsFromExistingConversation()
    {
        List<String> list = messageDBTest.getAllEmailsFromSendMessage("gg@gmail.com");
        assert (list.size() == 2);
        assert (list.get(0).equals("jon1@yahoo.com"));
        assert (list.get(1).equals("snj@gmail.com"));
        list = messageDBTest.getAllEmailsFromSendMessage("ds");
        assert (list.size() == 0);
        list = messageDBTest.getAllEmailsFromReceiveEmails("gg@gmail.com");
        assert (list.size() == 2);
        assert (list.get(1).equals("aand@hotmail.com"));
        assert (list.get(0).equals("jon1@yahoo.com"));
        list = messageDBTest.getAllEmailsFromReceiveEmails("gg@gdsmail.com");
        assert (list.size() == 0);
    }
}
