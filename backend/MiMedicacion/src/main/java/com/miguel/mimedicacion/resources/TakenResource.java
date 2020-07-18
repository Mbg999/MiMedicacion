/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.resources;

import com.miguel.mimedicacion.dao.mysql.MedicationDAOImpl;
import com.miguel.mimedicacion.dao.mysql.TakenDAOImpl;
import com.miguel.mimedicacion.jwtAuth.JwtAuth;
import com.miguel.mimedicacion.models.Medication;
import com.miguel.mimedicacion.models.Taken;
import com.miguel.mimedicacion.models.User;
import com.miguel.mimedicacion.responses.DataResponse;
import com.miguel.mimedicacion.responses.TextResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * All the taken routes
 * 
 * @author miguel
 */
@Path("taken") // base name for taken routes
public class TakenResource {
    private final TakenDAOImpl tdi;
    private final MedicationDAOImpl mdi;
    
    public TakenResource(){
        this.tdi = new TakenDAOImpl();
        this.mdi = new MedicationDAOImpl();
    }
    
    /**
     * Find by id
     * 
     * @param id int
     * @return JSON response
     */
    @GET
    @Path("find/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") int id){
        Taken taken = this.tdi.first(id);
        
        if(taken == null){
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new TextResponse(false, "Taken not found", 404))
                .build();
        }
        
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(taken))
                .build();
    }
    
    /**
     * Get all the taken rows
     * 
     * @return JSON response
     */
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(){
        List<Taken> taken = tdi.all();
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(taken))
                .build();
    }
    
    /**
     * Get all the taken rows from a medication
     * 
     * @param token String, bearer token
     * @param id int
     * @return JSON response
     */
    @GET
    @Path("all/medication/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allOfAMedication(@HeaderParam("Authorization") String token,
            @PathParam("id") int id){
        User user;
        List<Taken> takens;
        Medication med;
        
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
        
        // VALIDATING THE MEDICATION
        med = mdi.first(id);
        
        if(med == null){
            return Response.status(Response.Status.NOT_FOUND).entity(new TextResponse(false, "Medication not found", 404)).build();
        }
        
        if(med.getUser_id() != user.getId()){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, "You are not authorized to get this data", 401)).build();
        }
        // /VALIDATING THE MEDICATION
        
        takens = tdi.allOfAMedication(id);
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(takens))
                .build();
    }
    
    /**
     * Create a new taken for the auth user
     * 
     * returns the created medication
     * 
     * @param token String, bearer token
     * @param medication_id int
     * @return JSON response
     */
    @POST
    @Path("{medication_id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response store(@HeaderParam("Authorization") String token,
            @PathParam("medication_id") int medication_id){
        
        User user;
        Medication med;
        Taken taken;
        Map<String, String> errors;
        
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
        errors = this.validateMedication(medication_id);
        
        if(errors.size() > 0){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, "Invalid medication data", 400, errors)).build();
        }
        // /DATA VALIDATION
        
        // VALIDATING THE MEDICATION
        med = mdi.first(medication_id);
        
        if(med == null){
            return Response.status(Response.Status.NOT_FOUND).entity(new TextResponse(false, "Medication not found", 404)).build();
        }
        
        if(med.getUser_id() != user.getId()){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, "You are not authorized to get this data", 401)).build();
        }
        // /VALIDATING THE MEDICATION
        
        // STORING THE NEW TAKEN
        taken = new Taken(medication_id);
        
        int id = tdi.insert(taken);
        
        if(id < 1){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new TextResponse(false, "Failed inserting new taken medication", 500)).build();
        }
        // /STORING THE NEW TAKEN
        
        // RETRIEVING THE NEW TAKEN AND RESPONSE
        taken = tdi.first(id);
        
        return Response.status(Response.Status.CREATED).entity(new DataResponse(taken)).build();
    }
    
    /**
     * Delete the a taken
     * 
     * @param token String, jwt bearer token
     * @param id int
     * @return JSON response
     */
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") int id){
        
        User user;
        Medication med;
        Taken taken;
        
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
        
        // RETRIEVE THE TAKEN TO DELETE
        taken = tdi.first(id);
        
        if(taken == null){
            return Response.status(Response.Status.NOT_FOUND).entity(new TextResponse(false, "The taken does not exists", 404)).build();
        }
        // /RETRIEVE THE TAKEN TO DELETE
        
        // VALIDATING THE USER TO DELETE THIS
        med = mdi.first(taken.getMedication_id());
        
        if(med == null){
            return Response.status(Response.Status.NOT_FOUND).entity(new TextResponse(false, "Medication not found", 404)).build();
        }
        
        if(med.getUser_id() != user.getId()){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, "You are not authorized to get this data", 401)).build();
        }
        // /VALIDATING THE USER TO DELETE THIS
        
        
        
        // DELETE + RESPONSE
        if(!tdi.delete(taken)){
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TextResponse(false, "Error deleting", 500))
                .build();
        }
        
        return Response
                .status(Response.Status.OK)
                .entity(new TextResponse(true, "Taken deleted", 200))
                .build();
    }
    
    /**
     * Validates a medication fields for update a medication
     * 
     * @param medication_id int
     * @return Map&lt;String, String&gt; errors
     */
    private Map<String, String> validateMedication(int medication_id){
        Map<String, String> errors = new HashMap();
        
        if(medication_id < 1){
            errors.put("medication_id", "The medication id is required");
        }
        
        return errors;
    }
}
