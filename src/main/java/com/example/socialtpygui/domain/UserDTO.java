package com.example.socialtpygui.domain;

public class UserDTO {
    String id;
    String firstName;
    String lastName;

    public UserDTO(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    /**
     *
     * @return the id to a user
     */
    public String getId(){
        return id;
    }

    /**
     *
     * @return the firstName to a user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return  the lastName to a user
     */
    public String getLastName(){
        return lastName;
    }

    @Override
    public String toString(){
        return  id + " : " + firstName + ", " + lastName;
    }
}
