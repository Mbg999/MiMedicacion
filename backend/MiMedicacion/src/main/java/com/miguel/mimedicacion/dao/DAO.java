/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.dao;

import java.util.List;

/**
 * Blueprint for all DAO interfaces
 * 
 * @author miguel
 * @param <T> Class name
 * @param <K> ID type, generic types must be a type object
 */
public interface DAO<T,K> {
    // T -> class name, o -> object var name
    T first(K id);
    List<T> all();
    int insert(T o);
    boolean update(T o);
    boolean delete(T o);
}
