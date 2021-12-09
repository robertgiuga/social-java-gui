package com.example.socialtpygui.service.validators;

import com.example.socialtpygui.domain.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator<User>{
    /**
     *
     * @param entity
     * validate the user
     * validate if is not null by any componet
     * @throws ValidationException when first name last name and password is null or when is only ""
     */
    @Override
    public void validate(User entity) throws ValidationException {
        validateEmail(entity.getId());
        if(!validateString(entity.getFirstName()))
            throw new ValidationException("First name cannot be null!");
        if(!validateString(entity.getLastName()))
            throw new ValidationException("Last name cannot be null!");
        if(!validateString(entity.getPassword()))
            throw new ValidationException("Password cannot be null!");
    }

    /**
     *
     * @param param .
     * @return true if string is not null or empty
     */
    boolean validateString(String param){
        return !param.equals("");
    }

    /**
     * validate an email if it is not null or empty and has the right form
     * @param param .
     */
    public void validateEmail(String param){
        if(param==null)
            throw new ValidationException("Email must not be null!");
        if(!validateString(param))
            throw new ValidationException("Email must not be empty!");
        Pattern pat = Pattern.compile("([a-z]+)([0-9]+)?[@][a-z]+[.]((com)|(ro))$");
        Matcher matcher =pat.matcher(param);
        if(!matcher.find())
            throw new ValidationException("Email "+ param+" is invalid!");
    }
}
