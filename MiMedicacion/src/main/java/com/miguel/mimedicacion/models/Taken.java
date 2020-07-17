/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.models;

import java.sql.Timestamp;

/**
 *
 * @author miguel
 */
public class Taken {
    // properties
    private int id;
    private int medication_id;
    private Timestamp taken_at;
    private Medication medication;
    
    // constructors
    public Taken(){}
    
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

    public Timestamp getTaken_at() {
        return taken_at;
    }

    public void setTaken_at(Timestamp taken_at) {
        this.taken_at = taken_at;
    }
    
    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }
}
