package com.example.socialtpygui.service.validators;

import com.example.socialtpygui.domain.Message;

public class MessageValidator implements Validator<Message> {

    UserValidator userValidator;
    public MessageValidator(UserValidator userValidator){
        this.userValidator= userValidator;
    }

    @Override
    public void validate(Message entity) throws ValidationException {
        userValidator.validateEmail(entity.getFrom());
        entity.getTo().forEach(userValidator::validateEmail);
        if(!(entity.getMessage().length()>0))
            throw new ValidationException("Message must not be null!");
    }
}
