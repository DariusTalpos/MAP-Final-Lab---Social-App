package com.example.socialappgui.domain;

import java.util.*;

/**
 * User class that extends the Entity class, with the ID being 'Long' type
 */
public class User extends Entity<Long>{
    private String name;
    private List<Long> friends;

    /**
     * constructor for the class
     * @param name - the name the user will have
     */
    public User(String name) {
        this.name = name;
        this.friends = new ArrayList<Long>();
    }

    /**
     * getter for the name
     * @return - returns the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * setter for the name
     * @param name - the name that will be set to the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * adds a friend's id into the 'friends' list
     * @param id - the id of the friend
     */
    public void addFriend(Long id)
    {
        friends.add(id);
    }

    /**
     * deletes a friend's id from the 'friends' list
     * @param id - the id of the friend
     */
    public void deleteFriend(Long id)
    {
        friends.remove(id);
    }

    /**
     * getter for the 'friends' list
     * @return - returns the list of friends
     */
    public List<Long> getFriends() {
        return friends;
    }

    /**
     * turns the user into a string
     * @return - the details of the user written in the form of a stirng
     */
    @Override
    public String toString() {
        return "Id:"+getID()+"; User: "+ name;
    }

    /**
     * verifies if the user and the 'o' Object are the same
     * @param o - the object that will be compared to the user
     * @return - true, if 'o' and the user are the same
     *         - false, otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return Objects.equals(name, that.name);
    }

    /**
     * determines the hashcode of the user
     * @return - the hascode determined by the name and friends of the user
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, friends);
    }
}

