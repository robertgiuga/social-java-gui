package com.example.socialtpygui.service.entityservice;

import com.example.socialtpygui.domain.Message;
import com.example.socialtpygui.domain.ReplyMessage;
import com.example.socialtpygui.domain.ReplyMessageDTO;
import com.example.socialtpygui.repository.db.MessageDb;
import com.example.socialtpygui.service.validators.NonExistingException;
import com.example.socialtpygui.service.validators.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


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
    public List<String> getAllConversation(String id)
    {
        List<String> listAllEmails = new ArrayList<>(messageRepository.getAllEmailsFromSendMessage(id));
        messageRepository.getAllEmailsFromReceiveEmails(id).forEach(emailReceive->{
            if (!listAllEmails.contains(emailReceive)) listAllEmails.add(emailReceive);
        });
        return listAllEmails;
    }


    /**
     * @param id1 .
     * @param id2 .
     * @return a list of replayMessage, it returns all the messages between 2 users
     * if ReplayMessage has currentMessage null that means it is a Message entity
     */
    public List<ReplyMessage> getMessages(String id1, String id2)
    {
        return messageRepository.findAllMessageBetweenTwoUsers(id1, id2);
    }

    /**
     * Save a message.
     * @param entity entity must be not null
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     */
    public Message save(Message entity) {return messageRepository.save(entity);}

    /**
     * Save a replay message.
     * @param replyMessage entity must be not null
     *      * @return null- if the given entity is saved
     *      *         otherwise returns the entity (id already exists)
     */
    public Message saveReplyMessage(ReplyMessage replyMessage) {return messageRepository.saveReplyMessage(replyMessage);}

    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     */
    public Message findOne(Integer id) {return messageRepository.findOne(id);}

    /**
     * replay with a message to all the users that the original message has been sent to
     *
     * @param replyMessageDTO the message to be sent. The 'to' list in the object it will be null because
     *                     the upright layers cannot know who to send to
     */
    public void replayAll(ReplyMessageDTO replyMessageDTO)
    {
        Message original = findOne(Integer.valueOf(replyMessageDTO.getOriginalId()));
        if (original == null) throw new NonExistingException("Original message doesn't exist!");
        Predicate<String> notSender =o -> !o.equals(replyMessageDTO.getResponse().getFrom());
        List<String> to = original.getTo().stream().filter(notSender).collect(Collectors.toList());
        to.add(original.getFrom());
        replyMessageDTO.getResponse().setTo(to);
        ReplyMessage replyMessage = new ReplyMessage(replyMessageDTO.getResponse(), original);
        messageRepository.saveReplyMessage(replyMessage);
    }

    public int size() { return messageRepository.size(); }

    public Message remove(Integer id) { return messageRepository.remove(id); }
}
