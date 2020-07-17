/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.models;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author miguel
 */
public class Medication {
    // properties
    private int id;
    private int user_id;
    private String name;
    private String description;
    private int hours_interval;
    private Timestamp created_at;
    private Timestamp updated_at;
    private User user;
    private List<Taken> taken;
    
    // constructors
    public Medication(){}
    
    public Medication(int id, int user_id, String name,
            String description, int hours_interval,
            Timestamp created_at, Timestamp updated_at){
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.description = description;
        this.hours_interval = hours_interval;
        this.created_at = created_at;
        this.updated_at = updated_at; 
   }
    
    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHours_interval() {
        return hours_interval;
    }

    public void setHours_interval(int hours_interval) {
        this.hours_interval = hours_interval;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Taken> getTaken() {
        return taken;
    }

    public void setTaken(List<Taken> taken) {
        this.taken = taken;
    }
}
