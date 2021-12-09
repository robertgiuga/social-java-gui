package com.example.socialtpygui.repository.file;

import com.example.socialtpygui.domain.User;

import java.util.List;

public class UserFile extends AbstractFileRepository<String, User> {

    public UserFile(String fileName) {
        super(fileName);
    }

    @Override
    protected User extractEntity(List<String> atributes) {
        User user= new User(atributes.get(1),atributes.get(2), atributes.get(0),atributes.get(3),Boolean.getBoolean(atributes.get(4)));
        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId()+","+entity.getFirstName()+","+entity.getLastName();
    }
}
