package com.example.socialtpygui.service.entityservice;

import com.example.socialtpygui.domain.*;
import com.example.socialtpygui.domain.MessageDTO;
import com.example.socialtpygui.domain.ReplyMessage;
import com.example.socialtpygui.repository.db.MessageDb;

import java.util.ArrayList;
import java.util.List;


public class MessageService {
    private final MessageDb messageRepository;

    public MessageService(MessageDb messageDb)
    {
        this.messageRepository = messageDb;
    }


    /**
     * @param id .
     * @return All emails with whom a user has interacted(receive message/send message).
     */
    public List<String> getAllConversation(String id,int pageId)
    {
        /*List<String> listAllEmails = new ArrayList<>(messageRepository.getAllEmailsFromSendMessage(id));

        messageRepository.getAllEmailsFromReceiveEmails(id).forEach(emailReceive->{
            if (!listAllEmails.contains(emailReceive)) listAllEmails.add(emailReceive);
        });*/

        return messageRepository.getAllConversation(id, pageId);
    }


    /**
     * @param id1 .
     * @param id2 .
     * @return a list of replayMessage, it returns all the messages between 2 users
     * if ReplayMessage has currentMessage null that means it is a Message entity
     */
    public List<ReplyMessage> getMessagesBetweenTwoUsers(String id1, String id2, int pageId)
    {
        return messageRepository.findAllMessageBetweenTwoUsers(id1, id2,pageId);
    }

    /**
     * Save a message.
     * @param entity entity must be not null
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     */
    public MessageDTO save(MessageDTO entity) {return messageRepository.save(entity);}

    /**
     * Save a replay message.
     * @param replyMessage entity must be not null
     *      * @return null- if the given entity is saved
     *      *         otherwise returns the entity (id already exists)
     */
    public ReplyMessage saveReplyMessage(ReplyMessage replyMessage) {return messageRepository.saveReplyMessage(replyMessage);}

    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     */
    public MessageDTO findOne(Integer id) {return messageRepository.findOne(id);}

    /**
     * reply with a message to all members from a group
     * @param entity MessageDTO
     * @param idGroup int
     * @return the sent message
     */
    public MessageDTO replyAll(MessageDTO entity, int idGroup) {
        return messageRepository.saveGroupMessage(entity, idGroup);
    }


    public int size() { return messageRepository.size(); }


    /**
     * @param email String
     * @return a list with GroupDTO, only the groups where the user with email "email" is in
     */
    public List<GroupDTO> getUserGroups(String email)
    {
        return messageRepository.getUserGroups(email);
    }

    /**
     * @param id Integer
     * @return a GroupDto which contain the group with id "id"
     */
    public GroupDTO getGroup(int id)
    {
        return messageRepository.getGroup(id);
    }

    /**
     * Add a user to a specify group.
     * @param user User
     * @param groupId Integer
     * @return null, if the user was not added and the user, if the user was added
     */
    public User addUserToGroup(User user, int groupId)
    {
        return messageRepository.addUserToGroup(user, groupId);
    }

    /**
     * Remove a user from a groupe, remove from group_user table.
     * @param email String
     * @param groupId Integer
     */
    public void removeUserFromGroup(String email, int groupId)
    {
        messageRepository.removeUserFromGroup(email, groupId);
    }

    /**
     * Add a group, add in table social_group and in table group_user.
     * @param group Group
     * @return null, if the group was not added and the group, if the group was added
     */
    public Group addGroup(Group group)
    {
        return messageRepository.addGroup(group);
    }

    /**
     * Remove a group, with a specify id. First remove all from message_recipient with group_id = "id"
     * ,then remove all messages was sent to this group, then remove all from group_user and ,finally, remove
     * the group from social_group
     * @param id Integer
     */
    public void removeGroup(int id){
        messageRepository.removeGroup(id);
    }

    /**
     * @return the number of groups
     */
    public int sizeGroup() {return messageRepository.sizeGroup();}

    public MessageDTO remove(Integer id) { return messageRepository.remove(id); }

    /**
     * @param groupId Integer
     * @return a list of replyMessage, it returns all the messages from a group
     * if ReplayMessage has currentMessage null that means it is a Message entity
     */
    public List<ReplyMessage> getGroupMessages(int groupId,int pageId) {return messageRepository.getGroupMessages(groupId,pageId);}

    /**
     * send a reply message to a message from a group with id equals with groupId
     * @param replyMessage ReplyMessage
     * @param groupId int
     */
    public ReplyMessage saveGroupReplyMessage(ReplyMessage replyMessage, int groupId){
        return messageRepository.saveGroupReplyMessage(replyMessage, groupId);
    }

    /**
     * @param email String
     * @param groupId Integer
     * @return true if the user with email "email" is in group with "groupId"
     */
    public boolean userInGroup(String email, int groupId)
    {
        return messageRepository.userInGroup(email, groupId);
    }

    /**
     * @param groupId Integer
     * @return number of users in group with id "groupId"
     */
    public int numberOfUserFromAGroup(int groupId) {return messageRepository.numberOfUserFromAGroup(groupId);}

    /**
     * gets the messages in a group with id bigger than lastMsjID
     * @param groupId the group id
     * @param lastMsjID the message id to get bigger id messages than
     * @return a list of ReplayMessage
     */
    public List<ReplyMessage> getGroupMessagesGreaterThen(Integer groupId, int lastMsjID) {
        return messageRepository.getGroupMessagesGreaterThen(groupId,lastMsjID);
    }
    /**
     * gets the last messages sent by email2 to email1 which have the id bigger than lastMsjId
     * @param email1 the first user
     * @param email2 the second user
     * @param lastMsjId the id which message id has to be bigger than
     * @return a list of ReplayMessages
     */
    public List<ReplyMessage> getConvMessagesGreaterThan(String email1, String email2, int lastMsjId) {
        return messageRepository.getConvMessagesGreaterThan(email1,email2,lastMsjId);
    }

    /**
     * @param email String
     * @return number of new messages(unseen message)
     */
    public int getNumberNewMessage(String email){
        return messageRepository.getNumberNewMessage(email);
    }

    /**
     * Seen new message
     * @param email String
     */
    public void updateSeenMessageToTrue(String email){
        messageRepository.setToSeenNewMessage(email);
    }
}
