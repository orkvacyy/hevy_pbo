package com.hevy.hevy.repository;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T> {

    void save(T entity);

    Optional<T> findById(Long id);
    List<T> findAll();
    
    void update(T entity);

    void delete(Long id);
}