/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.dao;

import com.miguel.mimedicacion.models.User;

/**
 *
 * @author miguel
 */
public interface UserDAO extends DAO<User, Integer>{
    User firstByEmail(String email);
}
