package com.example.socialappgui.validator;

import com.example.socialappgui.domain.User;

import java.util.Objects;

/**
 * validator that will be used to validate users
 */
public class UserValidator implements Validator<User> {

    @Override
    public void validate(User entity) throws ValidationException
    {
        if(Objects.equals(entity.getName(), ""))
            throw new ValidationException("User must have a name!");
    }
}
