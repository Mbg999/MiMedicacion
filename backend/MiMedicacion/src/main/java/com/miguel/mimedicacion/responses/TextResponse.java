/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.responses;

/**
 * POJO class for a JSON response with a message, ok status and status code
 * 
 * @author miguel
 */
public class TextResponse {
    private boolean ok;
    private String message;
    private int status;
    private Object errors;
    
    public TextResponse(){}
    
    public TextResponse(boolean ok, String message, int status){
        this.ok = ok;
        this.message = message;
        this.status = status;
    }
    
    public TextResponse(boolean ok, String message, int status, Object errors){
        this.ok = ok;
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }
}
