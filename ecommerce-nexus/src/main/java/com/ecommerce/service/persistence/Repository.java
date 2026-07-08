package com.ecommerce.service.persistence;

import java.util.List;

/**
 * 
 * Repository
 * @param <T>
 * @param <ID>
 */
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    void save(T entity);
}