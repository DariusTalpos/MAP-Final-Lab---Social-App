package com.example.socialappgui.validator;

import com.example.socialappgui.domain.Friendship;

import java.util.Objects;

/**
 * validator that will be used to validate friendships
 */
public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException
    {
        if(entity.getIdUser1()<=0 || entity.getIdUser2()<=0)
            throw new ValidationException("User ID must be above 0!");
        if(Objects.equals(entity.getIdUser1(), entity.getIdUser2()))
            throw new ValidationException("A user cannot become a friend with themselves.");
        if(Objects.equals(entity.getDescription(), ""))
            throw new ValidationException("Status cannot be null");
    }
}
