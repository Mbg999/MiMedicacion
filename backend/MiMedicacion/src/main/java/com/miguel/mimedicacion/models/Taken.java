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
public class Taken {
    // properties
    private int id;
    private int medication_id;
    private Timestamp taken_at;
    
    // constructors
    public Taken(){}
    
    public Taken(int medication_id){
        this.medication_id = medication_id;
    }
    
    public Taken(int medication_id, Timestamp taken_at){
        this.medication_id = medication_id;
        this.taken_at = taken_at;
   }
    
    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getMedication_id() {
        return medication_id;
    }

    public void setMedication_id(int medication_id) {
        this.medication_id = medication_id;
    }

    public LocalDateTime getTaken_at() {
        return taken_at.toLocalDateTime();
    }

    public void setTaken_at(Timestamp taken_at) {
        this.taken_at = taken_at;
    }
}
