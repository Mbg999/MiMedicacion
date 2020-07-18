/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class User {
    // properties
    private int id;
    private String email;
    private String password;
    private String name;
    private Date born_date;
    private String picture;
    private int mins_before;
    private Timestamp created_at;
    private Timestamp updated_at;

    // constructors
    public User(){}
    
    public User(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
   }
    
    public User(int id, String email, String password, String name,
            Date born_date, String picture, int mins_before,
            Timestamp created_at, Timestamp updated_at){
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.born_date = born_date;
        this.picture = picture;
        this.mins_before = mins_before;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

    /**
     *
     * Simple md5 hex encription algorithm
     * 
     * https://stackoverflow.com/a/14201817
     * @param password 
     * @return  String
     */
    public static String hashPassword(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] passBytes = password.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Check if the user password and the param one is the same
     * @param password
     * @return 
     */
    public boolean checkPassword(String password){
        return this.password.equals(User.hashPassword(password));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBorn_date() {
        return born_date.toLocalDate();
    }

    public void setBorn_date(Date born_date) {
        this.born_date = born_date;
    }
    
    /**
     * Made for set a String as java.mysql.Date to born_date
     * @param born_date String
     */
    public void setNewBorn_date(String born_date){
        String d = born_date.replace("/", "-");
        d = d.replace("-","");
        try {
            java.util.Date parsed = new SimpleDateFormat("yyyyMMdd").parse(d);
            this.born_date = new Date(parsed.getTime());
        } catch (ParseException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Check if the user is older than 18 years old
     * 
     * @param born_date
     * @return 
     */
    public static boolean isOlderThan18(String born_date) throws DateTimeException{
        String[] d = born_date.replace("/", "-").split("-");
        LocalDate bd = LocalDate.of(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));
        LocalDate today = LocalDate.now();
        
        return bd.isBefore(today.minusYears(18));
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getMins_before() {
        return mins_before;
    }

    public void setMins_before(int mins_before) {
        this.mins_before = mins_before;
    }

    public LocalDateTime getCreated_at() {
        return created_at.toLocalDateTime();
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
}
