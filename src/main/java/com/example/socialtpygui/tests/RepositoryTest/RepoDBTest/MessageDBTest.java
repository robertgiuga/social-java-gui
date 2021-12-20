package com.example.socialtpygui.tests.RepositoryTest.RepoDBTest;


import com.example.socialtpygui.domain.*;
import com.example.socialtpygui.domain.MessageDTO;
import com.example.socialtpygui.domain.ReplyMessage;
import com.example.socialtpygui.repository.db.MessageDb;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Objects;
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
        testGetUserGroups();
        testGetGroup();
        testAddRemoveUserToGroup();
        testAddRemoveGroup();
        testGetGroupMessages();
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
        assert (messageDBTest.size() == 8);
        List<String> list = new ArrayList<String>(); list.add("andr@gamail.com");
        MessageDTO messageDTO = new MessageDTO("gg@gmail.com", list, "Proiect1.pdf", LocalDate.now());
        messageDBTest.save(messageDTO);
        assert (messageDBTest.size() == 9);
        assert (messageDBTest.findOne(messageDTO.getId()) != null);
        messageDBTest.remove(messageDTO.getId());
        assert (messageDBTest.size() == 8);
        assert (messageDBTest.findOne(messageDTO.getId()) == null);
    }

    private static void testGetAllEmailsFromExistingConversation()
    {
        List<String> list = messageDBTest.getAllEmailsFromSendMessage("gg@gmail.com");
        assert (list.size() == 2);
        list = messageDBTest.getAllEmailsFromSendMessage("ds");
        assert (list.size() == 0);
        list = messageDBTest.getAllEmailsFromReceiveEmails("gg@gmail.com");
        assert (list.size() == 2);
        list = messageDBTest.getAllEmailsFromReceiveEmails("gg@gdsmail.com");
        assert (list.size() == 0);
    }

    private static void testGetUserGroups()
    {
        List<GroupDTO> list = messageDBTest.getUserGroups("gg@gmail.com");
        assert (list.size() == 2);
        List<String> nameGroups = new ArrayList<>();
        list.forEach(groupDTO -> {nameGroups.add(groupDTO.getNameGroup());});
        assert (nameGroups.contains("Grupa223"));
        assert (nameGroups.contains("CabanaMunte"));
        List<Integer> numberOfUsers = new ArrayList<>();
        list.forEach(groupDTO -> {numberOfUsers.add(groupDTO.getMembersEmail().size());});
        assert (numberOfUsers.contains(4));
        assert (numberOfUsers.contains(3));
    }

    private static void testGetGroup()
    {
        assert (messageDBTest.getGroup(1).getNameGroup().equals("Grupa223"));
        assert (messageDBTest.getGroup(2).getNameGroup().equals("CabanaMunte"));
    }

    private static void testAddRemoveUserToGroup()
    {
        User user = new User("Snow", "John", "snj@gmail.com", "parola2");
        List<String> list = new ArrayList<>(messageDBTest.getGroup(2).getMembersEmail());
        assert (! list.contains(user.getId()));
        messageDBTest.addUserToGroup(user, 2);
        list.clear();
        list = messageDBTest.getGroup(2).getMembersEmail();
        assert (list.contains(user.getId()));
        messageDBTest.removeUserFromGroup("snj@gmail.com", 2);
        list.clear();
        list = messageDBTest.getGroup(2).getMembersEmail();
        assert (! list.contains(user.getId()));
    }


    private static void testAddRemoveGroup()
    {
        List<User> listMembers = new ArrayList<>();
        listMembers.add(new User("a", "b", "snj@gmail.com", "p")); listMembers.add(new User("a", "b", "gg@gmail.com", "p"));
        assert (messageDBTest.sizeGroup() == 2);
        messageDBTest.addGroup(new Group("Grup224", listMembers));
        assert (messageDBTest.sizeGroup() == 3);
        String sql = "select id from social_group order by id desc limit 1";
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SocialNetworkTest", "postgres", "postgres");
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int id = resultSet.getInt("id");
            messageDBTest.removeGroup(id);
            assert (messageDBTest.sizeGroup() == 2);
        } catch (SQLException e) {
            e.printStackTrace();
        } ;
    }

    private static void testGetGroupMessages()
    {
        List<ReplyMessage> list = messageDBTest.getGroupMessages(1);
        assert (list.size() == 1);
    }
}
