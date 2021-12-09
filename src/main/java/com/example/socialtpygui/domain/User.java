package com.example.socialtpygui.domain;

import com.example.socialtpygui.service.validators.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class User extends Entity<String>{
    private String firstName, lastName, password;
    private boolean admin= false;
    public List<User> friendList;

    public User(String firstName, String lastName, String email,String password, boolean admin) {
        this.firstName = firstName;
        this.lastName = lastName;
        setId(email);
        friendList= new ArrayList<>();
        this.password= password;
        this.admin= admin;
    }

    public User(String firstName, String lastName, String email,String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        setId(email);
        friendList= new ArrayList<>();
        this.password= password;
        this.admin= false;
    }
    @Override
    public String toString() {
        List<String> el= new ArrayList<>();
        friendList.forEach(e->el.add(e.getId().toString()));
        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\''+
                ", friends='"+ el+'\''+
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void addFriend(User friend){
        if(friend==null)
            throw new ValidationException("");
        if(friend.equals(this))
            throw new ValidationException("");
        if(friendList.contains(friend))
            throw new ValidationException("");
        friendList.add(friend);
    }

    public void removeFriend(User exFriend){friendList.remove(exFriend);}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
