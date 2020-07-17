/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.resources;


import com.miguel.mimedicacion.dao.mysql.UserDAOImpl;
import com.miguel.mimedicacion.jwtAuth.JwtAuth;
import com.miguel.mimedicacion.models.User;
import com.miguel.mimedicacion.responses.DataResponse;
import com.miguel.mimedicacion.responses.TextResponse;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * All the user routes
 * 
 * @author miguel
 */
@Path("users") // base name for user routes
public class UserResource {
    
    private UserDAOImpl udi;
    
    public UserResource(){
        this.udi = new UserDAOImpl();
    }
    
    /**
     * Get all the users
     * 
     * @return JSON response
     */
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(){
        List<User> users = udi.all();
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(users))
                .build();
    }
    
    /**
     * Find by id
     * @param id int
     * @return JSON response
     */
    @GET
    @Path("find/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") int id){
        User user = this.udi.first(id);
        
        if(user == null){
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new TextResponse(false, "User not found", 404))
                .build();
        }
        
        user.setPassword(null); // don't give the password to the response
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(user))
                .build();
    }
    
    /**
     * Find by email
     * 
     * @param email String
     * @return JSON response
     */
    @GET
    @Path("findByEmail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByEmail(@PathParam("email") String email){
        User user = this.udi.firstByEmail(email);
        
        if(user == null){
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new TextResponse(false, "User not found", 404))
                .build();
        }
        
        user.setPassword(null); // don't give the password to the response
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(user))
                .build();
    }
    
    /**
     * Update the auth user
     * 
     * @param token String, the jwt bearer token
     * @param password String
     * @param name String
     * @param born_date String
     * @param picture String
     * @param mins_before String
     * @return JSON Response
     */
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@HeaderParam("Authorization") String token, @FormParam("password") String password,
            @FormParam("name") String name, @FormParam("born_date") String born_date,
            @FormParam("picture") String picture, @FormParam("mins_before") int mins_before){
        User user = null;
        
        // TOKEN VALIDATION
        if(token == null || token.trim().equals("")){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, "Token required", 401)).build();
        }
        
        user = JwtAuth.verifyAuth(token);
        
        if(user == null){
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new TextResponse(false, JwtAuth.UNAUTHORIZED, 401))
                .build();
        }
        // /TOKEN VALIDATION
        
        // DATA VALIDATION
        if(password != null && !password.trim().equals("")){
            user.setPassword(User.hashPassword(password));
        }
        
        if(name != null && !name.trim().equals("")){
            user.setName(name.trim());
        }
        
        if(born_date != null && !born_date.trim().equals("")){
            if(!User.isOlderThan18(born_date)){
                return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "You must be older than 18", 400)).build();
            }
            user.setNewBorn_date(born_date);
        }
        
        if(picture != null && !picture.trim().equals("")){
            user.setPicture(picture);
        }
        
        if(mins_before > 0){
            user.setMins_before(mins_before);
        }
        // /DATA VALIDATION
        
        // UPDATING USER AND RESPONSE WITH THE NEW USER DATA
        if(!udi.update(user)){
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TextResponse(false, "Error updating", 500))
                .build();
        }
        
        user.setPassword(null); // don't give the password to the response
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(user))
                .build();
    }
    
    /**
     * Delete the auth user
     * @param token String, jwt bearer token
     * @return JSON response
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@HeaderParam("Authorization") String token){
        User user = null;
        
        // TOKEN VALIDATION
        if(token == null || token.trim().equals("")){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, "Token required", 401)).build();
        }
        
        user = JwtAuth.verifyAuth(token);
        
        if(user == null){
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new TextResponse(false, JwtAuth.UNAUTHORIZED, 401))
                .build();
        }
        // /TOKEN VALIDATION
        
        // DELETE + RESPONSE
        if(!udi.delete(user)){
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TextResponse(false, "Error deleting", 500))
                .build();
        }
        
        return Response
                .status(Response.Status.OK)
                .entity(new TextResponse(true, "User deleted", 200))
                .build();
    }
}
