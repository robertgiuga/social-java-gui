package com.example.socialtpygui.service.validators;

import com.example.socialtpygui.domain.MessageDTO;

public class MessageValidator implements Validator<MessageDTO> {

    UserValidator userValidator;
    public MessageValidator(UserValidator userValidator){
        this.userValidator= userValidator;
    }

    @Override
    public void validate(MessageDTO entity) throws ValidationException {
        userValidator.validateEmail(entity.getFrom());
        entity.getTo().forEach(userValidator::validateEmail);
        if(!(entity.getMessage().length()>0))
            throw new ValidationException("Message must not be null!");
    }
}
