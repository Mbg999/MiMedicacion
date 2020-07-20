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
import com.miguel.mimedicacion.utils.Upload;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.DateTimeException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * All the user routes
 * 
 * @author miguel
 */
@Path("users") // base name for user routes
public class UserResource {
    
    private final UserDAOImpl udi;
    
    public UserResource(){
        this.udi = new UserDAOImpl();
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
     * Update the auth user
     * 
     * @param token String, the jwt bearer token
     * @param password String
     * @param name String
     * @param born_date String
     * @param mins_before String
     * @return JSON Response
     */
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@HeaderParam("Authorization") String token, @FormParam("password") String password,
            @FormParam("name") String name, @FormParam("born_date") String born_date,
            @FormParam("mins_before") Integer mins_before){
        
        User user;
        Map<String, String> errors = new HashMap();
        
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
            if(password.length() < 6){
                errors.put("password", "Password must be at least of 6 characters");
            } else {
                user.setPassword(User.hashPassword(password));
            }
        }
        
        if(name != null && !name.trim().equals("")){
            if(name.length() > 200){
                errors.put("name", "The maximum name length is 200");
            } else {
                user.setName(name.trim());
            }
        }
        
        if(born_date != null && !born_date.trim().equals("")){
            try {
                if(!User.isOlderThan18(born_date)){
                    errors.put("born_date", "You must be older than 18");
                }
                user.setNewBorn_date(born_date);
            } catch(DateTimeException ex){
                errors.put("born_date", ex.getMessage());
            }
            
        }
        
        if(mins_before != null && mins_before > -1){
            if(mins_before > 255){
            errors.put("mins_before", "The maximum mins before value is 255");
            } else {
                user.setMins_before(mins_before);
            }
        }
        
        if(errors.size() > 0){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new TextResponse(false, "Invalid user data", 400, errors)).build();
        }
        // /DATA VALIDATION
        
        // UPDATING USER, RETRIEVE UPDATED USER FOR AUTOGENERATED FIELDS AND RESPONSE WITH THE NEW USER DATA
        if(!udi.update(user)){
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TextResponse(false, "Error updating", 500))
                .build();
        }
        
        user = udi.first(user.getId());
        
        user.setPassword(null); // don't give the password to the response
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(user))
                .build();
    }
    
    /**
     * Returns a user picture, this should be an application web server work
     * 
     * only auth users can get pictures
     * 
     * @param picture String
     * @param token String, bearer token
     * @return image/png | image/jpg | image/jpeg
     */
    @GET
    @Path("picture/{picture}/{authorization}")
    @Produces({"image/png", "image/jpg", "image/jpeg"})
    public Response getFullImage(@PathParam("picture") String picture,
            @PathParam("authorization") String token) {
        
        User user;
        File file;
        BufferedImage image = null;
        ByteArrayOutputStream baos;
        String ext = null;
        
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
        file = new File(Upload.FILES_PATH + Upload.USER_PICTURES_PATH + picture);
        baos = new ByteArrayOutputStream();
        
        try {
            image = ImageIO.read(file);
            ext = file.getName().substring(file.getName().lastIndexOf(".")+1).toLowerCase();
            ImageIO.write(image, ext, baos);
        } catch (IOException ex) {
            try {
                file = new File(Upload.FILES_PATH + Upload.USER_PICTURES_PATH + "nopicture.png");
                image = ImageIO.read(file);
                ext = "png";
                ImageIO.write(image, ext, baos);
            } catch (IOException ex1) {
                Logger.getLogger(UserResource.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
        byte[] imageData = baos.toByteArray();
        // uncomment line below to send non-streamed
        // return Response.ok(imageData).build();
        // uncomment line below to send streamed
         return Response.ok(new ByteArrayInputStream(imageData)).build();
    }
    
    /**
     * Upload a new picture for the auth user
     * 
     * the old one will be deleted
     * 
     * @param token String, bearer token
     * @param file InputStream
     * @param fileDetails FormDataContentDisposition
     * @return JSON response
     */
    @POST
    @Path("picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadPicture(@HeaderParam("Authorization") String token,
            @FormDataParam("picture") InputStream file,
            @FormDataParam("picture") FormDataContentDisposition fileDetails){
        
        User user;
        Map<String, String> result;
        String ext; // file extension
        
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
        
        // VALIDATE FILE
        try {
            ext = fileDetails.getFileName().substring(fileDetails.getFileName().lastIndexOf(".")).toLowerCase();
        } catch (StringIndexOutOfBoundsException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "Invalid file name", 400)).build();
        }
        
        if(!Upload.isAValidPictureExtension(ext)){
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "Not a valid file extension", 400)).build();
        }
        // /VALIDATE FILE
        
        // DELETE OLD PICTURE
        if(user.getPicture() != null && !Upload.deleteUserPicture(user.getPicture())){
            return Response.status(Response.Status.BAD_REQUEST).entity(new TextResponse(false, "Error updating picture, try again", 500)).build();
        }
        // /DELETE OLD PICTURE
        
        // UPLOAD NEW PICTURE
        result = Upload.userPicture(file, ext);
        
        if(result.get("error") != null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new TextResponse(false, result.get("error"), 500)).build();
        }
        // /UPLOAD NEW PICTURE
        
        // UPDATE USER PICTURE
        user.setPicture(result.get("picture"));
        
        if(!udi.updatePicture(user)){
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TextResponse(false, "Error updating", 500))
                .build();
        }
        // /UPDATE USER PICTURE
        
        // RETRIEVE AUTOGENERATED FIELDS AND RESPONSE
        user = udi.first(user.getId());
        user.setPassword(null); // don't give the password to the response
        
        return Response
                .status(Response.Status.OK)
                .entity(new DataResponse(user))
                .build();
    }
    
    /**
     * Delete the auth user
     * 
     * @param token String, jwt bearer token
     * @return JSON response
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@HeaderParam("Authorization") String token){
        User user;
        
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
        
        // DELETE USER PICTURE FROM STORAGE
        if(user.getPicture() != null){
            File file = new File(Upload.FILES_PATH + Upload.USER_PICTURES_PATH + user.getPicture());
            file.delete();
        }
        // /DELETE USER PICTURE FROM STORAGE
        
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
