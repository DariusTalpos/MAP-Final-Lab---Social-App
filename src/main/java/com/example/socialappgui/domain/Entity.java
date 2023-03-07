package com.example.socialappgui.domain;

import java.io.Serializable;

/**
 * generic,serializable class that will be used as a template for the 'User' and 'Friendship' class
 * @param <ID> - a generic data type that will be used to identify specific objects
 */
public class Entity<ID> implements Serializable {
    private static final long serialVersionUID = 420691984L;
    private ID id;

    /**
     * getter for the ID
     * @return - returns the ID of the entity
     */
    public ID getID()
    {
        return id;
    }
    /**
     * setter for the id
     * @param id - the id that will be set to the entity
     */
    public void setID(ID id)
    {
        this.id = id;
    }

}