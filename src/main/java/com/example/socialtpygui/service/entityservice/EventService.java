package com.example.socialtpygui.service.entityservice;

import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.domain.UserEventDTO;
import com.example.socialtpygui.repository.db.EventDb;

import java.time.LocalDate;
import java.util.List;
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
    public UserDTO addParticipants(UserDTO user, int eventId, String notification) {
        return  repositoryEvent.addParticipants(user, eventId, notification);
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

    /**
     * @param eventId Integer
     * @return number of participants from a group with id "groupId"
     */
    public int numberOfParticipantsFromAnEvent(int eventId){return repositoryEvent.numberOfParticipantsFromAnEvent(eventId);}

    /**
     * Verify if a user is enrolled in an event with id "groupId"
     * @param email String
     * @param eventId Integer
     * @return true, if the user is enrolled, false otherwise
     */
    public boolean isUserEnrolledInAnEvent(String email, int eventId){return repositoryEvent.getUserEnrollment(email, eventId);}

    /**
     * Verify if a use is notified by an event with id "eventId"
     * @param email String
     * @param eventId Integer
     * @return true, if the user is notified, false otherwise
     */
    public String timeNotifiedFromEvent(String email, int eventId) {return repositoryEvent.getTimeNotifiedFromEvent(email, eventId); }

    /**
     * Modify notification to an event with id "eventId"
     * @param eventId Integer
     * @param email String
     * @param notification String
     */
    public void updateNotificationEvent(int eventId, String email, String notification){repositoryEvent.updateNotificationTimeEvent(eventId, email, notification);}

    /**
     * @param date LocalDate
     * @return number of events in a specify date
     */
    public int getTodayEvents(LocalDate date) {
        return repositoryEvent.getTodayEvents(date);
    }

    /**
     * gets the enrolled events of a user
     * @param id the user id
     * @return a list of UserEventDTO
     */
    public List<UserEventDTO> getUserIdsEvents(String id) {
        return repositoryEvent.getUserIdsEvents(id);
    }
}
