/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.resources;

import com.miguel.mimedicacion.dao.mysql.MedicationDAOImpl;
import com.miguel.mimedicacion.jwtAuth.JwtAuth;
import com.miguel.mimedicacion.models.Medication;
import com.miguel.mimedicacion.models.User;
import com.miguel.mimedicacion.responses.DataResponse;
import com.miguel.mimedicacion.responses.TextResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * All the medication routes
 * 
 * @author miguel
 */
@Path("medications") // base name for medication routes
public class MedicationResource {
    private final MedicationDAOImpl mdi;
    
    public MedicationResource(){
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
        Medication med = this.mdi.first(id);
        
        if(med == null){
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new TextResponse(false, "Medication not found", 404))
                .build();
        }
        
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(med))
                .build();
    }
    
    /**
     * Get all the medications
     * 
     * @return JSON response
     */
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(){
        List<Medication> meds = mdi.all();
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(meds))
                .build();
    }
    
    /**
     * Get all the medications of the auth user
     * 
     * @param token String, bearer token
     * @return JSON response
     */
    @GET
    @Path("all/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allOfAuthUser(@HeaderParam("Authorization") String token){
        User user;
        List<Medication> meds;
        
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
        
        meds = mdi.allOfAUser(user.getId());
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(meds))
                .build();
    }
    
    /**
     * Get all the medications of a user
     * 
     * @param id int
     * @return JSON response
     */
    @GET
    @Path("all/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allOfAUser(@PathParam("id") int id){
        List<Medication> meds = mdi.allOfAUser(id);
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(meds))
                .build();
    }
    
    /**
     * Create a new medication for the auth user
     * 
     * returns the created medication
     * 
     * @param token String, bearer token
     * @param name String
     * @param description String
     * @param hours_interval int
     * @return JSON response
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response store(@HeaderParam("Authorization") String token, @FormParam("name") String name,
            @FormParam("description") String description, @FormParam("hours_interval") int hours_interval){
        User user;
        Medication med;
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
        errors = this.validateMedication(true, name, description, hours_interval);
        
        if(errors.size() > 0){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, "Invalid medication data", 400, errors)).build();
        }
        // /DATA VALIDATION
        
        // STORING THE NEW MEDICATION
        med = new Medication(user.getId(), name.trim(), description.trim(), hours_interval);
        
        int id = mdi.insert(med);
        
        if(id < 1){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new TextResponse(false, "Failed inserting new medication", 500)).build();
        }
        // /STORING THE NEW MEDICATION
        
        // RETRIEVING THE NEW MEDICATION AND RESPONSE
        med = mdi.first(id);
        
        return Response.status(Response.Status.CREATED).entity(new DataResponse(med)).build();
    }
    
    /**
     * Update a medication from the auth user
     * 
     * returns the updated medication
     * 
     * @param token String, bearer token
     * @param id int, medication id
     * @param name String
     * @param description String
     * @param hours_interval int
     * @param finished boolean
     * @return JSON response
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@HeaderParam("Authorization") String token, @PathParam("id") int id,
            @FormParam("name") String name, @FormParam("description") String description,
            @FormParam("hours_interval") int hours_interval, @FormParam("finished") Boolean finished){
        
        User user;
        Medication med;
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
        errors = this.validateMedication(name, description, hours_interval);
        
        if(errors.size() > 0){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, "Invalid medication data", 400, errors)).build();
        }
        // /DATA VALIDATION
        
        // RETRIEVE THE UPDATING MEDICATION
        med = mdi.first(id);
        
        if(med == null){
            return Response.status(Response.Status.NOT_FOUND).entity(new TextResponse(false, "The medication does not exists", 404)).build();
        }
        
        if(med.getUser_id() != user.getId()){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, "You are not authorized for update this medication", 401)).build();
        }
        // /RETRIEVE THE UPDATING MEDICATION
        
        // UPDATE THE MEDICATION, RETRIEVE UPDATED MEDICATION FOR AUTOGENERATED FIELDS AND RESPONSE WITH THE UPDATED MEDICATION DATA
        // JDBC will update the object if at least one field is updated, so i don't need to control if the fields are updated or not
        if(name != null) med.setName(name.trim());
        if(description != null) med.setDescription(description.trim());
        if(hours_interval > 0) med.setHours_interval(hours_interval);
        if(finished != null) med.setFinished(finished);
        
        if(!mdi.update(med)){
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TextResponse(false, "Error updating", 500))
                .build();
        }
        
        med = mdi.first(id);
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(med))
                .build();
    }
    
    /**
     * Delete the a medication
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
        
        // RETRIEVE THE MEDICATION TO DELETE
        med = mdi.first(id);
        
        if(med == null){
            return Response.status(Response.Status.NOT_FOUND).entity(new TextResponse(false, "The medication does not exists", 404)).build();
        }
        
        if(med.getUser_id() != user.getId()){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, "You are not authorized for delete this medication", 401)).build();
        }
        // /RETRIEVE THE MEDICATION TO DELETE
        
        // DELETE + RESPONSE
        if(!mdi.delete(med)){
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TextResponse(false, "Error deleting", 500))
                .build();
        }
        
        return Response
                .status(Response.Status.OK)
                .entity(new TextResponse(true, "Medication deleted", 200))
                .build();
    }
    
    /**
     * Validates a medication fields for a new medication
     * 
     * @param required boolean
     * @param name String
     * @param description String
     * @param hours_interval int
     * @return Map&lt;String, String&gt; errors
     */
    private Map<String, String> validateMedication(boolean required, String name, String description, int hours_interval){
        Map<String, String> errors = new HashMap();
        
        if(name == null || name.trim().equals("")){
            errors.put("name", "The name is required");
        } else if(name.length() > 200){
            errors.put("name", "The maximum name length is 200");
        }
        
        if(description == null || description.trim().equals("")){
            errors.put("description", "The description is required");
        } else if(description.length() > 1000){
            errors.put("description", "The maximum description length is 1000");
        }
        
        if(hours_interval < 1){
            errors.put("hours_interval", "The hours interval is required");
        } else if(hours_interval > 255){
            errors.put("hours_interval", "The maximum hours interval value is 255");
        }
        
        return errors;
    }
    
    /**
     * Validates a medication fields for update a medication
     * 
     * @param name String
     * @param description String
     * @param hours_interval int
     * @return Map&lt;String, String&gt; errors
     */
    private Map<String, String> validateMedication(String name, String description, int hours_interval){
        Map<String, String> errors = new HashMap();
        
        if(name != null && name.length() > 200){
            errors.put("name", "The maximum name length is 200");
        }
        
        if(description != null && description.length() > 1000){
            errors.put("description", "The maximum description length is 1000");
        }
        
        if(hours_interval > 0 && hours_interval > 255){
            errors.put("hours_interval", "The maximum hours interval value is 255");
        }
        
        return errors;
    }
}
