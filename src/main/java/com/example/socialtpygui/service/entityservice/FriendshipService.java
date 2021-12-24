package com.example.socialtpygui.service.entityservice;


import com.example.socialtpygui.domain.*;
import com.example.socialtpygui.repository.db.FriendshipDb;
import com.example.socialtpygui.repository.db.FriendshipRequestDb;
import com.example.socialtpygui.service.validators.NonExistingException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class FriendshipService {
    private final FriendshipDb repositoryFriendship;
    private final FriendshipRequestDb repositoryFriendshipRequest;

    public FriendshipService(FriendshipRequestDb friendshipRequestDb, FriendshipDb friendshipDb) {
        this.repositoryFriendship = friendshipDb;
        this.repositoryFriendshipRequest = friendshipRequestDb;
    }

    /**
     * @param id -the id of the entity to be returned
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     */
    public Friendship friendshipFindOne(TupleOne<String> id) {
        return repositoryFriendship.findOne(id);
    }

    /**
     * @return all entites.
     */
    public Iterable<Friendship> friendshipFindAll() {
        return repositoryFriendship.findAll();
    }

    /**
     * @return the size of the current elements in the repository
     */
    public int friendshipSize() {
        return repositoryFriendship.size();
    }

    /**
     * Save a friendship.
     *
     * @param friendship .
     * @return null- if the given entity is saved
     * * otherwise returns the entity (id already exists)
     */
    public Friendship friendshipSave(Friendship friendship) {
        return repositoryFriendship.save(friendship);
    }

    /**
     * Remove a friendship.
     *
     * @param id .
     * @return the removed entity or null if there is no entity with the given id
     */
    public Friendship friendshipRemove(TupleOne<String> id) {
        return repositoryFriendship.remove(id);
    }

    /**
     * Load the friends of the users in memory
     *
     * @param users the list of the users to load friends of
     * @return the list of the users with friends loaded
     */
    public Iterable<User> loadUsersFriends(Iterable<User> users) {
        Map<String, User> users1 = new HashMap<>();
        users.forEach(user -> users1.put(user.getId(), user));
        repositoryFriendship.findAll().forEach(frnds -> {
            users1.get(frnds.getId().getLeft()).addFriend(users1.get(frnds.getId().getRight()));
            users1.get(frnds.getId().getRight()).addFriend(users1.get(frnds.getId().getLeft()));
        });
        return users1.values();
    }

    /**
     * @param id the id of the user to search for his friends for
     * @return the list of the id of his friends
     */
    public List<Tuple<String, LocalDate>> getFriends(String id) {
        return repositoryFriendship.getFriends(id);
    }

    /**
     * Save a friendship to friendshipRequest.
     *
     * @param entity entity must be not null
     *               * @return null- if the given entity is saved
     *               * otherwise returns the entity (id already exists)
     */
    public Friendship friendshipRequestSave(Friendship entity) {
        return repositoryFriendshipRequest.save(entity);
    }

    /**
     * Remove a friendship from friendshipRequest.
     *
     * @param stringTuple id must be not null
     * @return the removed entity or null if there is no entity with the given id
     */
    public Friendship friendshipRequestRemove(TupleOne<String> stringTuple) {
        return repositoryFriendshipRequest.remove(stringTuple);
    }

    /**
     * @return the size of the current elements in the repository
     */
    public int friendshipRequestSize() {
        return repositoryFriendshipRequest.size();
    }

    /**
     * @param stringTupleOne -the id of the entity to be returned
     *                       id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     */
    public Friendship friendshipRequestFindOne(TupleOne<String> stringTupleOne) {
        return repositoryFriendshipRequest.findOne(stringTupleOne);
    }

    /**
     * @return all entities
     */
    public Iterable<Friendship> friendshipRequestFindAll() {
        return repositoryFriendshipRequest.findAll();
    }

    /**
     * add a friend request between user with id1 and user with id2
     *
     * @param id1 .
     * @param id2 .
     * @throws NonExistingException when a friendship or a friend request already exist
     */
    public void sendRequest(String id1, String id2) {
        if (repositoryFriendshipRequest.save(new Friendship(id1, id2, LocalDate.now())) == null) {
            throw new NonExistingException("This friend request already exist!");
        }
    }

    /**
     * remove a friend request between user with email id1 and user with id2
     *
     * @param id1 .
     * @param id2 .
     * @throws NonExistingException if a friend request does not exist
     */
    public void declineRequest(String id1, String id2) {
        Friendship friendship = repositoryFriendshipRequest.remove(new TupleOne<>(id1, id2));
        if (friendship == null) {
            throw new NonExistingException("You can't decline a friend request which doesn't exist!");
        }
    }

    /**
     * add a friendship between user with id1 and user with id2 if that does not exist
     * and remove a friend request between user with id1 and user with id2 if that exist
     *
     * @param id1 .
     * @param id2 .
     * @throws NonExistingException if a friend request does not exist or a friendship already exist
     */
    public void acceptRequest(String id1, String id2) {
        Friendship friendship = repositoryFriendshipRequest.remove(new TupleOne<>(id1, id2));

        if (friendship == null) {
            throw new NonExistingException("You can't accept a friend request which doesn't exist!");
        }

        Friendship friendship1 = new Friendship(friendship.getId().getLeft(), friendship.getId().getRight(), LocalDate.now());

        if (repositoryFriendship.save(friendship1) == null) {
            throw new NonExistingException("This friendship already exist!");
        }
    }

    /**
     * get id request for an user with the id
     * @param id .
     * @return an Iterable<String>
     * @throws NonExistingException if user with the id id has not friend requests
     */
    public Iterable<String> getRequests(String id) {
        Iterable<String> emailsRequests = repositoryFriendshipRequest.getRequests(id);
        long size = StreamSupport.stream(emailsRequests.spliterator(), false).count();
        if (size == 0) {
            throw new NonExistingException("You don't have friend requests!");
        }
        return emailsRequests;
    }

    /**
     * @param email1
     * @param email2
     * @return null if the friendship doesn t exist, and Date when the friendship was created if it exists
     */
    public Date friendshipDate(String email1, String email2)
    {
        return repositoryFriendship.friendshipDate(email1, email2);
    }

    /**
     * @param email1
     * @param email2
     * @return null if the friendship request doesn t exist, and Date when the friendship request was created if it exists
     */
    public Date friendshipRequestDate(String email1, String email2)
    {
        return repositoryFriendshipRequest.friendshipRequestDate(email1, email2);
    }

    /**
     * get id request for an user with the id
     * @param email .
     * @return an Iterable<Friendship>
     * @throws NonExistingException if user with the id id has not friend requests
     */
    public Iterable<Friendship> getFriendRequests(String email){
        return repositoryFriendshipRequest.getFriendRequest(email);
    }

}

