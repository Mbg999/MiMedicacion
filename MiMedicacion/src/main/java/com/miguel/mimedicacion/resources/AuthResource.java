/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.resources;

import com.miguel.mimedicacion.dao.mysql.UserDAOImpl;
import com.miguel.mimedicacion.jwtAuth.JwtAuth;
import com.miguel.mimedicacion.models.User;
import com.miguel.mimedicacion.responses.UserResponse;
import com.miguel.mimedicacion.responses.TextResponse;
import java.io.UnsupportedEncodingException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * user auth control routes
 * 
 * @author miguel
 */
@Path("auth") // base name for auth routes
public class AuthResource {
    
    private UserDAOImpl udi;
    
    public AuthResource(){
        this.udi = new UserDAOImpl();
    }
    
    /**
     * Login into the application
     * 
     * If the login goes ok, it returns the JWT Token and the auth user data
     * 
     * @param email String
     * @param password String
     * @return JSON response
     */
    @POST()
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("email") String email, @FormParam("password") String password){
        User user = null;
        
        // DATA VALIDATION
        if(email == null || email.trim().equals("")){
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "The email is required", 400)).build();
        }
        
        if(password == null || password.trim().equals("")){
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "The password is required", 400)).build();
        }
        // /DATA VALIDATION
        
        // CHECK IF THE USER EXISTS
        user = udi.firstByEmail(email.trim().toLowerCase());
        
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).entity(new TextResponse(false, "The user does not exists", 404)).build();
        }
        // /CHECK IF THE USER EXISTS
        
        // CHECK IF THE PASSWORD IS OK
        if(!user.checkPassword(password)){
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "Incorrect password", 400)).build();
        }
        
        // GIVE THE JWT AND USER DATA AS RESPONSE
        user.setPassword(null); // don't give the password to the response
        String token;
        try {
            token = JwtAuth.generateJWT(user);
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new TextResponse(false, "Error generating JWT", 500)).build();
        }
        
        return Response.status(Response.Status.OK).entity(new UserResponse(user, token)).build();
    }
    
    /**
     * Register a new user into the application
     * 
     * @param email String
     * @param password String
     * @param name String
     * @param born_date String, date yyyy-MM-dd
     * @return 
     */
    @POST()
    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@FormParam("email") String email, @FormParam("password") String password,
            @FormParam("name") String name, @FormParam("born_date") String born_date){
        User user = null;
        
        // DATA VALIDATION
        if(email == null || email.trim().equals("")){
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "The email is required", 400)).build();
        }
        
        if(password == null || password.trim().equals("")){
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "The password is required", 400)).build();
        } else if(password.length() < 6){
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "Password must be at least of 6 characters", 400)).build();
        }
        
        if(name == null || name.trim().equals("")){
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "The name is required", 400)).build();
        }
        
        if(born_date == null || born_date.trim().equals("")){
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "The born date is required", 400)).build();
        } else if(!User.isOlderThan18(born_date)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "You must be older than 18", 400)).build();
        }
        // /DATA VALIDATION
        
        // CHECK IF THE EMAIL IS TAKEN
        user = udi.firstByEmail(email);
        
        if(user != null){
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "Taken email", 400)).build();
        }
        // /CHECK IF THE EMAIL IS TAKEN
        
        // REGISTER THE NEW USER
        user = new User(email.trim().toLowerCase(), User.hashPassword(password), name.trim());
        user.setNewBorn_date(born_date);
        
        int id = udi.insert(user);
        
        if(id < 1){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new TextResponse(false, "Failed inserting new user", 500)).build();
        }
        
        // RETURN THE JWT AND USER DATA AS RESPONSE
        user = udi.first(id);
        user.setPassword(null); // don't give the password to the response
        String token;
        try {
            token = JwtAuth.generateJWT(user);
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new TextResponse(false, "Error generating JWT", 500)).build();
        }
        
        return Response.status(Response.Status.CREATED).entity(new UserResponse(user, token)).build();
    }
    
    /**
     * Return the auth user data
     * made for refresh the frontend data
     * 
     * @param token String, bearer token
     * @return JSON response
     */
    @GET()
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response me(@HeaderParam("Authorization") String token){
        User user = null;
        
        // TOKEN VALIDATION
        if(token == null || token.trim().equals("")){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, "Token required", 401)).build();
        }
        
        user = JwtAuth.verifyAuth(token);
        
        if(user == null){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, JwtAuth.UNAUTHORIZED, 401)).build();
        }
        // /TOKEN VALIDATION
        
        return Response.status(Response.Status.OK).entity(new UserResponse(user, null)).build();
    }
}
