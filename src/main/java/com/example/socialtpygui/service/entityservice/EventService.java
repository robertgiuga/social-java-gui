package com.example.socialtpygui.service.entityservice;

import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.repository.db.EventDb;

public class EventService {
    EventDb repositoryEvent;


    public EventService(EventDb repositoryEvent) {
        this.repositoryEvent = repositoryEvent;
    }

    /**
     * Find one event with id "eventId".
     * @param eventId Integer
     * @return null if the event does not exist and the eventDTO if the event exist
     */
    public EventDTO findOne(Integer eventId) {
        return repositoryEvent.findOne(eventId);
    }

    /**
     * Save an event.
     * @param event EventDTO
     * @return event if was saved and null otherwise
     */
    public EventDTO save(EventDTO event) {
        return repositoryEvent.save(event);
    }

    /**
     * Remove a event.
     * @param eventId Integer
     * @return null
     */
    public EventDTO remove(Integer eventId) {
        return repositoryEvent.remove(eventId);
    }

    /**
     * @return the number of events
     */
    public int size() {
        return repositoryEvent.size();
    }

    /**
     * Add a user(participant) to user_event table.
     * @param user User
     * @param eventId Integer
     * @return user if the user was added and null if the user was not added
     */
    public User addParticipants(User user, int eventId) {
        return  repositoryEvent.addParticipants(user, eventId);
    }

    /**
     * Remove a user(participant) from user_event table
     * @param email String
     * @param eventId Integer
     */
    public void removeParticipants(String email, int eventId) {
        repositoryEvent.removeParticipants(email, eventId);
    }

    /**
     * @return all events.
     */
    public Iterable<EventDTO> findAll() {return repositoryEvent.findAll();}
}
