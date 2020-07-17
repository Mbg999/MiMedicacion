/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.responses;

import com.miguel.mimedicacion.models.User;

/**
 * POJO class for a JSON response with JWT token, auth user and ok status
 * 
 * @author miguel
 */
public class UserResponse {
    private boolean ok;
    private User user;
    private String token;
    
    public UserResponse(){}
    
    public UserResponse(User user, String token){
        this.ok = true;
        this.user = user;
        this.token = token;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
