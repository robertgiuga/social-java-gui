package com.example.socialtpygui.repository.memory;

import com.example.socialtpygui.domain.Entity;
import com.example.socialtpygui.service.validators.ValidationException;
import com.example.socialtpygui.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository<ID,E extends Entity<ID>> implements Repository<ID,E> {

    protected Map<ID, E> entities;


    public InMemoryRepository() {
        entities= new HashMap<>();
    }

    @Override
    public E findOne(ID id) {
        if(id==null)
            throw new IllegalArgumentException("id must not be null");
        return entities.get(id);
    }

    @Override
    public List<E> findAll(int pageSize) {
        return entities.values().stream().toList();
    }

    @Override
    public E save(E entity) {
        if (entity==null)
            throw new ValidationException("Entity must not be null");

        if(entities.get(entity.getId())!=null)
            return entity;

        entities.put(entity.getId(),entity);
        return null;
    }

    public E remove(ID id){

        return entities.remove(id);
    }

    @Override
    public int size() {
        return entities.size();
    }


}
