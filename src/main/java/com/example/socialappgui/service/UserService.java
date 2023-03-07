package com.example.socialappgui.service;

import com.example.socialappgui.domain.User;
import com.example.socialappgui.events.ChangeEventType;
import com.example.socialappgui.events.EntityChangeEvent;
import com.example.socialappgui.observer.Observable;
import com.example.socialappgui.observer.Observer;
import com.example.socialappgui.repository.UserDBRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * service used to give instructions to the user repository
 */
public class UserService{
    private UserDBRepo userRepo;


    /**
     * the class constructor
     * @param userRepo - the user repository that will be called in methods
     */
    public UserService(UserDBRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * method that will create a new 'User' object and attempt to save it in the repository
     * @param name - the name of the user
     * @return - 1, if the save failed
     *         - 0, if the save was successful
     */
    public int save(String name)
    {
        User user = new User(name);
        if(userRepo.save(user)!=null)
            return 1;

        EntityChangeEvent<User> event = new EntityChangeEvent<User>(ChangeEventType.ADD, user);
        return 0;
    }

    /**
     * method that will attempt to delete the user with the given id from the repository
     * @param id - id of the desired user
     * @return - 1, if the deletion failed
     *         - 0, if it was successful
     */
    public int delete(Long id)
    {
        User user = userRepo.delete(id);
        if(user==null)
            return 1;
        return 0;
    }

    /**
     * method that will attempt to modify the name of the user with the given id
     * @param id - id of the desired user
     * @param newName - the desired new username
     * @return - 1, if the operation failed
     *         - 0, if it was successful
     */
    public int modify(Long id,String newName)
    {
        User user = new User(newName);
        user.setID(id);
        if(userRepo.update(user)!=null)
            return 1;
        return 0;
    }

    public Iterable<User> getUserList()
    {
        return userRepo.findAll();
    }


}