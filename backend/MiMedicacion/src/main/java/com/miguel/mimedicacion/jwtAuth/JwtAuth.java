/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.jwtAuth;

import com.miguel.mimedicacion.dao.mysql.UserDAOImpl;
import com.miguel.mimedicacion.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JWT Token control
 * 
 * @author miguel
 */
public class JwtAuth {
    public static final String UNAUTHORIZED = "Unauthorized user"; // default unauthorized message
    private static final String SECRET = User.hashPassword("MiMedication secret"); // signature secret
    
    /**
     * Verify if a token is valid and return the auth user data
     * 
     * @param token String, the bearer token
     * @return User | null
     */
    public static User verifyAuth(String token){
        User user = null;
        Jws<Claims> jwt;
        UserDAOImpl udi;
        
        try {
            jwt = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes("UTF-8")))
                .build()
                .parseClaimsJws(token);
            
            udi = new UserDAOImpl();
            user = udi.firstByEmail(jwt.getBody().getSubject());
            if(user != null) user.setPassword(null); // don't give the password to the response
        } catch(JwtException | UnsupportedEncodingException ex){
            Logger.getLogger(JwtAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
    
    /**
     * Generates a JWT token for a user
     * @param user User
     * @return String, the generated token
     * @throws UnsupportedEncodingException exception
     */
    public static String generateJWT(User user) throws UnsupportedEncodingException{
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(Date.from(ZonedDateTime.now().plusMonths(1).toInstant())) // 1 month to exp
                .setId(String.valueOf(user.getId()))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes("UTF-8")))
                .compact();
    }
}
