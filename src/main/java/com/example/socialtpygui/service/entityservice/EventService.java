package com.example.socialtpygui.service.entityservice;

import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.repository.db.EventDb;

public class EventService {
    EventDb repositoryEvent;


    public EventService(EventDb repositoryEvent) {
        this.repositoryEvent = repositoryEvent;
    }

    public EventDTO findOne(Integer eventId) {
        return repositoryEvent.findOne(eventId);
    }

    public EventDTO save(EventDTO event) {
        return repositoryEvent.save(event);
    }

    public EventDTO remove(Integer eventId) {
        return repositoryEvent.remove(eventId);
    }

    public int size() {
        return repositoryEvent.size();
    }

    public User addParticipants(User user, int eventId) {
        return  repositoryEvent.addParticipants(user, eventId);
    }

    public void removeParticipants(String email, int eventId) {
        repositoryEvent.removeParticipants(email, eventId);
    }
}
