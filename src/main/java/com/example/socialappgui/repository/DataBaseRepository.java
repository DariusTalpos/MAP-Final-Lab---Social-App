package com.example.socialappgui.repository;

import com.example.socialappgui.domain.Entity;
import com.example.socialappgui.validator.Validator;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Objects;

/**
 * generic repository used to handle the elements stored in a database
 * @param <ID> - a generic data type that will be used to identify specific objects
 * @param <E>  - a generic data type that will represent the entity type
 */
public class DataBaseRepository <ID, E extends Entity<ID>> implements GenericRepo<ID,E>
{
    Validator<E> validator;
    String url;
    String username;
    String password;

    /**
     * the class constructor
     * @param validator - object of 'Validator' class that will be used to validate data of type 'E'
     * @param url - the url that leads to the database
     * @param username - the username of the account used to access the database
     * @param password  - the password of the account used to access the database
     */
    public DataBaseRepository(Validator<E> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * overridden method that checks if the entity is valid before saving it
     * @param entity - the entity that will be verified
     * @return - throws IllegalArgumentException if the entity is null
     *         - null, otherwise
     */
    @Override
    public E save(E entity) {
        if (entity==null)
        {
            throw new IllegalArgumentException("Entity must not be null");
        }
        validator.validate(entity);
        HashSet<E> entities = (HashSet<E>) findAll();
        for(E e: entities)
        {
            if(Objects.equals(entity,e))
                throw new IllegalArgumentException("Entity already exists");
        }
        return null;
    }

    /**
     * overridden method that checks if the entity is valid before updating it
     * @param entity - the entity that will be verified
     * @return - throws IllegalArgumentException if the entity is null
     *         - null, otherwise
     */
    @Override
    public E update(E entity) {
        if (entity==null)
        {
            throw new IllegalArgumentException("Entity must not be null");
        }
        validator.validate(entity);
        return null;
    }

    /**
     * overridden method that checks if the given id is valid before deleting its owner
     * @param id - the id that will be verified
     * @return - throws IllegalArgumentException if the id is null
     *         - null, otherwise
     */
    @Override
    public E delete(ID id) {
        if (id==null)
        {
            throw new IllegalArgumentException("ID must not be null");
        }
        return null;
    }

    /**
     * overridden method that checks if the given id is valid before searching for its owner
     * @param id - the id that will be verified
     * @return - throws IllegalArgumentException if the id is null
     *         - null, otherwise
     */
    @Override
    public E findOne(ID id) {
        if (id==null)
        {
            throw new IllegalArgumentException("ID must not be null");
        }
        return null;
    }

    /**
     * overridden method that... does nothing?
     * @return - null, always
     */
    @Override
    public Iterable<E> findAll() {
        return null;
    }

    protected E extractData(ResultSet resultSet)
    {
        return null;
    }
}
