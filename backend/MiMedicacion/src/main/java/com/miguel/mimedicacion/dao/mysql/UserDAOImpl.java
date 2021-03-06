/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.dao.mysql;

import com.miguel.mimedicacion.config.DB;
import com.miguel.mimedicacion.dao.UserDAO;
import com.miguel.mimedicacion.models.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO implementation, all the DB interactions for the user table
 * 
 * @author miguel
 */
public class UserDAOImpl implements UserDAO {

    // all the used SQL sentences
    private static final String FIRST = "SELECT * FROM users WHERE id = ?";
    private static final String FIRSTBYEMAIL = "SELECT * FROM users WHERE email like ?";
    private static final String INSERT = "INSERT INTO users(email, password, name, born_date) values(?,?,?,?)";
    private static final String UPDATE = "UPDATE users "+
            "SET password = ?, name = ?, born_date = ?, mins_before = ? "+
            "WHERE id = ?";
    private static final String UPDATE_NOT_PASSWORD = "UPDATE users "+
            "SET name = ?, born_date = ?, mins_before = ? "+
            "WHERE id = ?";
    private static final String UPDATEPICTURE = "UPDATE users SET picture = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM users WHERE id = ?";
    
    // DB
    private DB db;
    
    /**
     * Initialization of the DB
     */
    public UserDAOImpl(){
        try {
            db = new DB();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns the first user found by email, or null
     * 
     * @param email String
     * @return User | null
     */
    @Override
    public User firstByEmail(String email) {
        ResultSet rs = null;
        User user = null;
        try {
            rs = db.preparedStatement(FIRSTBYEMAIL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, new Object[]{email});
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setBorn_date(rs.getDate("born_date"));
                user.setPicture(rs.getString("picture"));
                user.setMins_before(rs.getInt("mins_before"));
                user.setCreated_at(rs.getTimestamp("created_at"));
                user.setUpdated_at(rs.getTimestamp("updated_at"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return user;
    }

    /**
     * Returns the first user found by id, or null
     * 
     * @param id Integer
     * @return User | null
     */
    @Override
    public User first(Integer id) {
        ResultSet rs = null;
        User user = null;
        try {
            rs = db.preparedStatement(FIRST, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, new Object[]{id});
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setBorn_date(rs.getDate("born_date"));
                user.setPicture(rs.getString("picture"));
                user.setMins_before(rs.getInt("mins_before"));
                user.setCreated_at(rs.getTimestamp("created_at"));
                user.setUpdated_at(rs.getTimestamp("updated_at"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return user;
    }

    /**
     * Insert a new user and return the generated ID
     * 
     * @param o User
     * @return int generated ID
     */
    @Override
    public int insert(User o) {
        return db.insert(INSERT, new Object[]{
            o.getEmail(),
            o.getPassword(),
            o.getName(),
            o.getBorn_date()
        });
    }

    /**
     * Update a user and return if the user was updated or not
     * 
     * @param o User
     * @return boolean
     */
    @Override
    public boolean update(User o) {
        if(o.getPassword() != null){
            return db.update(UPDATE, new Object[]{
                o.getPassword(),
                o.getName(),
                o.getBorn_date(),
                o.getMins_before(),
                o.getId()
            }) > 0;
        }
        return db.update(UPDATE_NOT_PASSWORD, new Object[]{
                o.getName(),
                o.getBorn_date(),
                o.getMins_before(),
                o.getId()
            }) > 0;
    }
    
    /**
     * Update a user picture and return if was or not updated
     * 
     * @param o User
     * @return boolean
     */
    @Override
    public boolean updatePicture(User o) {
        return db.update(UPDATEPICTURE, new Object[]{
            o.getPicture(),
            o.getId()
        }) > 0;
    }

    /**
     * Delete a user and return if the user was deleted or not
     * 
     * @param o User
     * @return boolean
     */
    @Override
    public boolean delete(User o) {
        return db.delete(DELETE, new Object[]{o.getId()}) > 0;
    }
}
