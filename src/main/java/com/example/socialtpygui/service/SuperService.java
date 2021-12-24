package com.example.socialtpygui.service;


import com.example.socialtpygui.domain.*;
import com.example.socialtpygui.service.entityservice.*;
import com.example.socialtpygui.service.validators.MessageValidator;
import com.example.socialtpygui.service.validators.NonExistingException;
import com.example.socialtpygui.service.validators.UserValidator;
import com.example.socialtpygui.service.validators.ValidationException;
import com.example.socialtpygui.utils.events.ChangeEventType;
import com.example.socialtpygui.utils.events.Event;
import com.example.socialtpygui.utils.events.ViewItemEvent;
import com.example.socialtpygui.utils.observer.Observable;
import com.example.socialtpygui.utils.observer.Observer;


import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class SuperService implements Observable {
    protected UserValidator userValidator;
    protected UserService userService;
    protected FriendshipService friendshipService;
    protected NetworkService networkService;
    protected MessageService messageService;
    protected MessageValidator messageValidator;
    protected EventService eventService;

    private Observer observer;


    public SuperService(MessageService messageService, NetworkService networkService,
                        FriendshipService friendshipService, UserService userService,
                        UserValidator userValidator,MessageValidator messageValidator,
                        EventService eventService) {
        this.userService = userService;
        this.userValidator=userValidator;
        this.friendshipService = friendshipService;
        this.networkService = networkService;
        this.messageService = messageService;
        this.messageValidator= messageValidator;
        this.eventService = eventService;
    }

    /**
     * Load the friends of the users in memory
     * @param users the list of the users to load friends of
     * @return the list of the users with friends loaded
     */
    private Iterable<User> loadUsersFriends(Iterable<User> users){
        return friendshipService.loadUsersFriends(users);
    }


    /**
     * Add a new user
     * @param newUser the user to add
     * @throws ValidationException when the email already exist
     */
    public void addUser(User newUser){
        userService.addUser(newUser);
    }

    /**
     * Removes an user by id
     * @param id .
     * @throws ValidationException .
     * @throws NonExistingException .
     */
    public void removeUser(String id){
        User toremove = userService.removeUser(id);
        List<TupleOne<String>> removelist= new ArrayList<>();
        friendshipService.friendshipFindAll().forEach(tup->{
            User  removefrom=null;
            if(tup.getId().getRight().equals(id))
                removefrom=userService.findOne(tup.getId().getLeft());
            if(tup.getId().getLeft().equals(id))
                removefrom=userService.findOne(tup.getId().getRight());
            if(removefrom!=null) {
                removefrom.removeFriend(toremove);
                removelist.add(new TupleOne<>(id, removefrom.getId()));
            }
        });
        removelist.forEach(tp-> friendshipService.friendshipRemove(tp));

    }

    /**
     * @return all the users
     */
    public Iterable<User> users(){
        return friendshipService.loadUsersFriends(userService.findAll());

    }

    /**
     * add friends or a friend with 'id' in ids to the user with 'id' id
     * @param id .
     * @param ids .
     * @throws ValidationException .
     */
    public void addFriend(String id, List<String> ids) {
        userValidator.validateEmail(id);
        User toaddto;
        if((toaddto= userService.findOne(id))==null)
            throw new ValidationException("User "+id+" does not exists");
        StringBuilder er= new StringBuilder();
        for (String id1 : ids) {
            try {
                userValidator.validateEmail(id1);
            } catch (ValidationException e) {
                er.append(e.getMessage()).append("\n");
                continue;
            }
            User toadd ;
            if((toadd = userService.findOne(id1))==null) {
                er.append("User ").append(id1).append(" does not exists \n");
                continue;
            }
            if(toaddto.equals(toadd)){
                er.append("Adding yourself as a friend is not permitted!");
                continue;
            }
            if(friendshipService.friendshipSave(new Friendship(toaddto.getId(),toadd.getId(), LocalDate.now())) == null){
                er.append("Friendship with").append(toadd.getId()).append("already exists!");
            }
        }
        if (!er.toString().equals(""))
            throw new NonExistingException(er.toString());

    }

    /**
     * remove a friends or a friend with 'id' in ids of the user with 'id' id
     * @param id .
     * @param ids .
     * @throws ValidationException .
     */
    public void removeFriend(String id, List<String> ids) {
        userValidator.validateEmail(id);
        User toremoveto;
        if((toremoveto= userService.findOne(id))==null)
            throw new ValidationException("User "+ id+ " does not exist!");
        StringBuilder er= new StringBuilder();
        for (String id1 : ids) {
            try {
                userValidator.validateEmail(id1);
            } catch (ValidationException e) {
                er.append(e.getMessage()).append("\n");
                continue;
            }
            User toremove;
            if((toremove = userService.findOne(id1))==null) {
                er.append("User ").append(id1).append(" does not exist!\n");
                continue;
            }
            if(friendshipService.friendshipRemove(new TupleOne<>(toremoveto.getId(),toremove.getId()))==null)
                er.append("The friendship between ").append(id).append(" and ").append(id1).append(" doesc not exist");
        }
        if (!er.toString().equals(""))
            throw new NonExistingException(er.toString());

        //------------
        User toremove=userService.findOne(ids.get(0));
        notifyObservers(new ViewItemEvent(ChangeEventType.DELETE,new UserDTO(toremove)));

    }

    /**
     * @return the nr of communities
     */
    public int nrCommunities() {
        return networkService.nrCommunities();
    }

    /**
     * @return the most social community
     */
    public Iterable<User> socialCommunity() {
        return networkService.socialCommunity();
    }


    /**
     * Gets the friends of a user by his id
     * @param id .
     * @return an iterable of FriendshipDTO always having first user the one that requested
     */
    public Iterable<FriendShipDTO> getFriends(String id) {
        userValidator.validateEmail(id);
        User user1 = userService.findOne(id);
        if(user1==null){
            throw new NonExistingException("User not found!");
        }
        UserDTO finalUserDTO = new UserDTO(user1);
        return friendshipService.getFriends(id).stream().map(s -> {UserDTO user = new UserDTO(userService.findOne(s.getLeft())); return new FriendShipDTO(finalUserDTO, user,s.getRight());}).collect(Collectors.toList());
    }


    /**
     * @param id .
     * @param date .
     * @return the friends of a user in a specific month of a year
     */
    public Iterable<FriendShipDTO> getFriendsSince(String id, YearMonth date)
    {
        userValidator.validateEmail(id);
        User user1 = userService.findOne(id);
        if(user1 == null)
            throw new NonExistingException("User not found!");
        UserDTO finalUserDto = new UserDTO(user1);

        Predicate<LocalDate> predicateY = y->y.getYear() == date.getYear();
        Predicate<LocalDate> predicateM = m->m.getMonth() == date.getMonth();
        Predicate<LocalDate> predicateYM = predicateM.and(predicateY);

        return friendshipService.getFriends(id).stream().filter(p->predicateYM.test(p.getRight()))
                .map(u->{UserDTO user = new UserDTO(userService.findOne(u.getLeft())); return new FriendShipDTO(finalUserDto, user, u.getRight());})
                .collect(Collectors.toList());
    }


    /**
     * @param id .
     * @param password .
     * @return the user if the id and password are correct
     * @throws ValidationException if id or password is incorrect and if the user does not exist
     */
    public User logIn(String id, String password) throws ValidationException {
        return userService.logIn(id, password);
    }

    /**
     * @param id .
     * @return All emails with whom a user has interacted(receive message/send message).
     */
    public List<String> getAllConversation(String id)
    {

        userValidator.validateEmail(id);
        if(userService.findOne(id)==null)
            throw new NonExistingException("User "+id+" does not exist!");
        return messageService.getAllConversation(id);
    }

    /**
     * @param id1 .
     * @param id2 .
     * @return a list of replayMessage, it returns all the messages between 2 users
     * if ReplayMessage has currentMessage null that means it is a Message entity
     */
    public List<ReplyMessage> getMessages(String id1, String id2)
    {
        userValidator.validateEmail(id1);
        userValidator.validateEmail(id2);

        if(userService.findOne(id1)==null)
            throw new NonExistingException("User "+id1+" does not exist!");

        if(userService.findOne(id2)==null)
            throw new NonExistingException("User "+id2+" does not exist!");

        return messageService.getMessages(id1, id2);
    }

    /**
     * validate if the message could be real, if the sender and receiver exists and are not admins
     * validates if the friendship exist between the user to send and the ones to receive
     * @param messageDTO the message to be tested
     */
    private void validateExistingMessageComponents(MessageDTO messageDTO)
    {
        if(userService.findOne(messageDTO.getFrom())==null)
            throw new NonExistingException("User "+ messageDTO.getFrom()+" does not exist!");
        StringBuilder er= new StringBuilder(" ");
        for (String s : messageDTO.getTo()) {

            if (userService.findOne(s) == null)
                er.append("User ").append(s).append(" does not exist!\n");
            boolean sem = false;
            for (Tuple<String, LocalDate> t : friendshipService.getFriends(messageDTO.getFrom()))
                if (t.getLeft().equals(s)) {
                    sem = true;
                    break;
                }

            if (!sem) er.append("User with email ").append(messageDTO.getFrom()).append(" and user with email ").append(s).append(" are not friends!");
        }
        if (!er.toString().equals(" "))
            throw new ValidationException(er.toString());
    }

    /**
     * Send a new message.
     * @throws com.example.socialtpygui.service.validators.ValidationException if the given entity is null.
     */
    public MessageDTO sendMessage(MessageDTO newMessageDTO)
    {
        messageValidator.validate(newMessageDTO);
        validateExistingMessageComponents(newMessageDTO);
        return messageService.save(newMessageDTO);
    }

    /**
     * Send a reply message.
     * @throws com.example.socialtpygui.service.validators.ValidationException if the given entity is null.
     */
    public ReplyMessage replyMessage(ReplyMessageDTO newReplyMessageDTO)
    {
        messageValidator.validate(newReplyMessageDTO.getResponse());
        validateExistingMessageComponents(newReplyMessageDTO.getResponse());
        MessageDTO original;
        if((original=messageService.findOne(Integer.valueOf(newReplyMessageDTO.getOriginalId())))==null)
            throw new ValidationException("ReplayMessage must replay to a valid Message ");

        ReplyMessage replyMessage = new ReplyMessage(newReplyMessageDTO.getResponse(), original);
        return messageService.saveReplyMessage(replyMessage);
    }

    /**
     * add a friend request between user with id1 and user with id2
     * @param id1 .
     * @param id2 .
     * @throws ValidationException when id1 or id2 is not valid
     * @throws NonExistingException when a friendship or a friend request already exist
     */
    public void sendRequest(String id1, String id2){
        validateRequest(id1, id2);

        if(friendshipService.friendshipFindOne(new TupleOne<>(id1, id2)) != null){
            throw new NonExistingException("This friendship already  exist!");
        }


        friendshipService.sendRequest(id1, id2);
    }

    /**
     * Validates if the params are valid for the request, ether if is accept decline or delete
     * Validates if the ids are valid
     * Validates if there are users with that id.
     * @param id1 .
     * @param id2 .
     */
    private void validateRequest(String id1, String id2) {
        String er = "";
        try {
            userValidator.validateEmail(id1);
        }catch (ValidationException e) {
            er += e.getMessage() + "\n";
        }
        try {
            userValidator.validateEmail(id2);
        }catch (ValidationException e){
            er+=e.getMessage() + "\n";
        }
        if(!er.equals("")){
            throw new ValidationException(er);
        }
        if(userService.findOne(id1)==null)
            throw new NonExistingException("User "+id1+" does not exist!");

        if(userService.findOne(id2)==null)
            throw new NonExistingException("User "+id2+" does not exist!");
    }

    /**
     * add a friendship between user with id1 and user with id2 if that does not exist
     * and remove a friend request between user with id1 and user with id2 if that exist
     * @param id1 .
     * @param id2 .
     * @throws ValidationException if id1 or id2 is not valid
     * @throws NonExistingException if a friend request does not exist or a friendship already exist
     */
    public void acceptRequest(String id1, String id2){
        validateRequest(id1, id2);
        friendshipService.acceptRequest(id1, id2);
        notifyObservers(new ViewItemEvent(ChangeEventType.REMOVE,new UserDTO(userService.findOne(id2))));
    }

    /**
     * remove a friend request between user with email id1 and user with id2
     * @param id1 .
     * @param id2 .
     * @throws ValidationException if id1 or id2 is not valid
     * @throws NonExistingException if a friend request does not exist
     */
    public void declineRequest(String id1, String id2){
        validateRequest(id1, id2);
        friendshipService.declineRequest(id1, id2);
        notifyObservers(new ViewItemEvent(ChangeEventType.REMOVE,new UserDTO(userService.findOne(id2))));
    }

    /**
     * get friend request for an user with the id id
     * @param id .
     * @return an Iterable<UserDTO></UserDTO>
     * @throws NonExistingException if user with the id id has not friend requests
     */
    public Iterable<UserDTO> getRequests(String id){
        userValidator.validateEmail(id);
        Iterable<String> emailsRequests = friendshipService.getRequests(id);
        List<UserDTO> users = new ArrayList<>();
        for (String emailRequest : emailsRequests){
            User user = userService.findOne(emailRequest);
            users.add(new UserDTO(user));
        }
        return users;
    }

    /**
     * replay with a message to all the users from a group
     * @param messageDTO the message to be sent in group with id idGroup
     * @param idGroup the id of group where the message will be send in
     */
    public MessageDTO replyAll(MessageDTO messageDTO, int idGroup){
        messageValidator.validate(messageDTO);
        return messageService.replyAll(messageDTO, idGroup);
    }

    /**
     * @param completName .
     * @return Return a list with UserDto, where first_name and last_name contain completName.
     * @throws ValidationException if completName is empty
     */
    public List<UserDTO> getUsersByName(String completName)
    {
        if (completName.length() == 0) throw new ValidationException("Name in searchBar is null!");
        return userService.getUsersByName(completName);
    }

    /**
     * @param email1 .
     * @param email2 .
     * @return null if the friendship doesn t exist, and Date when the friendship was created if it exists
     * @throws ValidationException -> emails are invalid
     */
    public Date friendshipDate(String email1, String email2)
    {
        userValidator.validateEmail(email1);
        userValidator.validateEmail(email2);
        return friendshipService.friendshipDate(email1, email2);
    }

    public Date friendshipRequestDate(String email1, String email2)
    {
        userValidator.validateEmail(email1);
        userValidator.validateEmail(email2);
        return friendshipService.friendshipRequestDate(email1, email2);
    }

    public Friendship friendshipRequestRemove(TupleOne<String> stringTuple)
    {
        userValidator.validateEmail(stringTuple.getLeft());
        userValidator.validateEmail(stringTuple.getRight());
        return friendshipService.friendshipRequestRemove(stringTuple);
    }

    /**
     * get friend request for an user with the id id
     * @param id .
     * @return an Iterable<FriendshipDTO></UserDTO>
     * @throws NonExistingException if user with the id id has not friend requests
     */
    public Iterable<FriendShipDTO> getFriendRequest(String id){
        userValidator.validateEmail(id);
        Iterable<Friendship> friendRequests = friendshipService.getFriendRequests(id);
        List<FriendShipDTO> friendships = new ArrayList<>();
        for (Friendship request : friendRequests){
            UserDTO user1 = new UserDTO(userService.findOne(request.getId().getLeft()));
            UserDTO user2 = new UserDTO(userService.findOne(request.getId().getRight()));
            LocalDate date = request.getDate();
            friendships.add(new FriendShipDTO(user1, user2, date));
        }
        return friendships;
    }

    /**
     * returns a list of a user friends that First or Last name contains "name"
     * @param name
     * @return
     */
    public List<UserDTO> getFriendsByName(String id, String name){
        if(name.length()==0){
            throw new NonExistingException("Cannot contain null values");
        }
        Predicate<UserDTO> contains=
                userDTO -> userDTO.getLastName().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT)) ||
                        userDTO.getFirstName().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT));

        return  friendshipService.getFriends(id).stream()
                .map(s -> new UserDTO(userService.findOne(s.getLeft()))).collect(Collectors.toList())
                .stream().filter(contains).collect(Collectors.toList());
    }

    /**
     * Find one user.
     * @param email searched user's email
     * @return User, the user with email (email)
     */
    public User findOneUser(String email)
    {
        userValidator.validateEmail(email);
        return userService.findOne(email);
    }

    @Override
    public void addObserver(Observer e) {
        observer=e;
    }

    @Override
    public void notifyObservers(Event t) {
        if(observer != null)
            observer.update(t);
    }

    /**
     * @param email String
     * @return a list with GroupDTO, only the groups where the user with email "email" is in
     * @throws NonExistingException, if the user with email "email" does not exist
     */
    public List<GroupDTO> getUserGroups(String email)
    {
        userValidator.validateEmail(email);
        if (userService.findOne(email) == null){throw new NonExistingException("User does not exist!");}
        else{return messageService.getUserGroups(email);}
    }

    /**
     * @param id Integer
     * @return a GroupDto which contain the group with id "id"
     * @throws NonExistingException if the group does not exist
     */
    public GroupDTO getGroup(int id)
    {
        if (messageService.getGroup(id) == null) {throw new NonExistingException("Group with id " + id + " does not exist!");}
        return messageService.getGroup(id);
    }

    /**
     * Add a user to a specify group.
     * @param email String
     * @param groupId Integer
     * @return null, if the user was not added and the user, if the user was added
     * @throws NonExistingException, if the user with email "email" does not exist or the group with groupId doe not exist
     */
    public User addUserToGroup(String email, int groupId)
    {
        userValidator.validateEmail(email);
        User user = userService.findOne(email);
        if (user == null){throw new NonExistingException("User does not exist!");}
        if (messageService.getGroup(groupId) == null) {throw new NonExistingException("Group with id " + groupId + " does not exist!");}
        return messageService.addUserToGroup(user, groupId);
    }

    /**
     * Remove a user from a groupe, remove from group_user table.
     * @param email String
     * @param groupId Intege
     * @throws NonExistingException, if the user with email "email" does not exist or the group with groupId does not exist
     */
    public void removeUserFromGroup(String email, int groupId)
    {
        userValidator.validateEmail(email);
        if (userService.findOne(email) == null){throw new NonExistingException("User does not exist!");}
        if (messageService.getGroup(groupId) == null) {throw new NonExistingException("Group with id " + groupId + " does not exist!");}
        messageService.removeUserFromGroup(email, groupId);
        if (messageService.numberOfUserFromAGroup(groupId) == 0)
        {
            messageService.removeGroup(groupId);
        }
    }

    /**
     * Add a group, add in table social_group and in table group_user.
     * @param groupDTO GroupDTO
     * @return null, if the group was not added and the group, if the group was added
     * @throws ValidationException if the name is null or one email from members list is invalid
     */
    public Group addGroup(GroupDTO groupDTO)
    {
        if (groupDTO.getNameGroup().length() == 0) {throw new ValidationException("Name can not be null");}
        for (String email : groupDTO.getMembersEmail())
        {
            userValidator.validateEmail(email);
        }
        List<User> membersList = new ArrayList<>();
        groupDTO.getMembersEmail().forEach(email->{membersList.add(userService.findOne(email));});
        Group group = new Group(groupDTO.getNameGroup(), membersList);
        return messageService.addGroup(group);
    }

    /**
     * Remove a group, with a specify id. First remove all from message_recipient with group_id = "id"
     * ,then remove all messages was sent to this group, then remove all from group_user and ,finally, remove
     * the group from social_group
     * @param id Integer
     * @throws NonExistingException if the group with id does not exist
     */
    public void removeGroup(int id){
        if (messageService.getGroup(id) == null) {throw new NonExistingException("Group with id " + id + " does not exist!");}
        messageService.removeGroup(id);
    }

    /**
     * @return the number of groups
     */
    public int sizeGroup() {return messageService.sizeGroup();}

    /**
     * @param groupId Integer
     * @return a list of replyMessage, it returns all the messages from a group
     * if ReplayMessage has currentMessage null that means it is a Message entity
     * @throws NonExistingException if the group with groupId does not exist
     */
    public List<ReplyMessage> getGroupMessages(int groupId)
    {
        if (messageService.getGroup(groupId) == null) {throw new NonExistingException("Group with id " + groupId + " does not exist!");}
        return messageService.getGroupMessages(groupId);
    }

    /**
     * send a reply message to a message from a group with id equals with groupId
     * @param replyMessageDTO ReplyMessageDTO
     * @param groupId int
     * @throws NonExistingException if the group with groupId does not exist
     */
    public ReplyMessage replyMessageGroup(ReplyMessageDTO replyMessageDTO, int groupId){
        messageValidator.validate(replyMessageDTO.getResponse());
        MessageDTO original;
        if((original = messageService.findOne(Integer.valueOf(replyMessageDTO.getOriginalId()))) == null)
            throw new ValidationException("ReplyMessage must reply to a valid message");
        if (messageService.getGroup(groupId) == null) {throw new NonExistingException("Group with id " + groupId + " does not exist!");}
        ReplyMessage replyMessage = new ReplyMessage(replyMessageDTO.getResponse(), original);
        return messageService.saveGroupReplyMessage(replyMessage, groupId);
    }

    /**
     * @param email String
     * @param groupId Integer
     * @return true if the user with email "email" is in group with "groupId"
     * @throws NonExistingException if the group with groupId does not exist
     */
    public boolean userInGroup(String email, int groupId)
    {
        userValidator.validateEmail(email);
        if (userService.findOne(email) == null) {throw new NonExistingException("User with id " + email + " does not exist");}
        if (messageService.getGroup(groupId) == null) {throw new NonExistingException("Group with id " + groupId + " does not exist!");}
        return messageService.userInGroup(email, groupId);
    }

    /**
     * @param groupId Integer
     * @return number of users in group with id "groupId"
     * @throws NonExistingException if the group with groupId does not exist
     */
    public int numberOfUserFromAGroup(int groupId)
    {
        if (messageService.getGroup(groupId) == null) {throw new NonExistingException("Group with id " + groupId + " does not exist!");}
        return messageService.numberOfUserFromAGroup(groupId);
    }

    /**
<<<<<<< HEAD
     * Find one event with id "eventId".
     * @param eventId Integer
     * @return null if the event does not exist and the eventDTO if the event exist
     * @throws NonExistingException if the event does not exist
     */
    public EventDTO findOneEvent(Integer eventId) {
        if (eventService.findOne(eventId) == null) {throw new NonExistingException("Event with id " + eventId + " does not exist!");}
        return eventService.findOne(eventId);
    }

    /**
     * Save an event.
     * @param event EventDTO
     * @return event if was saved and null otherwise
     */
    public EventDTO saveEvent(EventDTO event) {
        return eventService.save(event);
    }

    /**
     * Remove a event.
     * @param eventId Integer
     * @return null
     * @throws NonExistingException if the event does not exist
     */
    public EventDTO removeEvent(Integer eventId) {
        if (eventService.findOne(eventId) == null) {throw new NonExistingException("Event with id " + eventId + " does not exist!");}
        return eventService.remove(eventId);
    }

    /**
     * @return the number of events
     */
    public int sizeEvent() {
        return eventService.size();
    }

    /**
     * Add a user(participant) to user_event table.
     * @param user User
     * @param eventId Integer
     * @return user if the user was added and null if the user was not added
     * @throws NonExistingException if the user does not exist or the event does not exist
     */
    public User addParticipants(User user, int eventId) {
        if (eventService.findOne(eventId) == null) {throw new NonExistingException("Event with id " + eventId + " does not exist!");}
        if (userService.findOne(user.getId()) == null){throw new NonExistingException("User does not exist!");}
        return  eventService.addParticipants(user, eventId);
    }

    /**
     * Remove a user(participant) from user_event table
     * @param email String
     * @param eventId Integer
     * @throws NonExistingException if the user does not exist or the event does not exist
     */
    public void removeParticipants(String email, int eventId) {
        if (eventService.findOne(eventId) == null) {
            throw new NonExistingException("Event with id " + eventId + " does not exist!");
        }
        if (userService.findOne(email) == null) {
            throw new NonExistingException("User does not exist!");
        }
        eventService.removeParticipants(email, eventId);
    }

    /**
     * gets the messages in a group with id bigger than lastMsjID
     * @param groupId the group id
     * @param lastMsjID the message id to get bigger id messages than
     * @return a list of ReplayMessage
     */
    public List<ReplyMessage> getGroupMessagesGreaterThen(Integer groupId, int lastMsjID){
        if (messageService.getGroup(groupId) == null) {throw new NonExistingException("Group with id " + groupId + " does not exist!");}
        if(lastMsjID<0)
            throw new ValidationException("there should be no negative id message");
        return messageService.getGroupMessagesGreaterThen(groupId,lastMsjID);
    }

    /**
     * gets the last messages sent by email2 to email1 which have the id bigger than lastMsjId
     * @param email1 the first user
     * @param email2 the second user
     * @param lastMsjId the id which message id has to be bigger than
     * @return a list of ReplayMessages
     */
    public List<ReplyMessage> getConvMessagesGreaterThan(String email1, String email2, int lastMsjId){
        userValidator.validateEmail(email1);
        userValidator.validateEmail(email2);
        if(lastMsjId<0)
            throw new ValidationException("there should be no negative id message");
        return messageService.getConvMessagesGreaterThan(email1,email2,lastMsjId);
    }

}
