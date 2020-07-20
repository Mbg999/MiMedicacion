/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    private boolean finished;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Taken last_taken;
    
    // constructors
    public Medication(){}
    
    public Medication(int user_id, String name,
            String description, int hours_interval){
        this.user_id = user_id;
        this.name = name;
        this.description = description;
        this.hours_interval = hours_interval;
    }
    
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

    public LocalDateTime getCreated_at() {
        return created_at.toLocalDateTime();
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    
    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at.toLocalDateTime();
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Taken getLast_taken() {
        return last_taken;
    }

    public void setLast_taken(Taken last_taken) {
        this.last_taken = last_taken;
    }
    
    
}
