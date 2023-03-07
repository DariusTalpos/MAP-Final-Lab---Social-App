package com.example.socialappgui.repository;

import com.example.socialappgui.domain.Entity;

/**
 * a generic repository interface that will be used as a basis
 * @param <ID> - a generic data type that will be used to identify specific objects
 * @param <E>  - a generic data type that will represent the entity type
 */
public interface GenericRepo<ID, E extends Entity<ID>> {
    E save(E entity);
    E update(E entity);
    E delete(ID id);
    E findOne(ID id);
    Iterable<E> findAll();

}
