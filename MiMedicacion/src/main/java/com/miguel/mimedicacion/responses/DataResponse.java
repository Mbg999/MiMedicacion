/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.responses;

/**
 * POJO class for a JSON response with data and ok status
 * 
 * @author miguel
 */
public class DataResponse {
    private boolean ok;
    private Object data;
    
    public DataResponse(){}
    
    public DataResponse(Object data){
        this.ok = true;
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
