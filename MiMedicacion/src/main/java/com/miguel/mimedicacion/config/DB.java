/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author miguel
 */
public class DB {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String DSN = "jdbc:mysql://localhost:3306/"; // Data Source Name
    private static final String DBNAME = "mimedicacion";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private Connection conn;
    
    public DB() throws SQLException {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DSN + DBNAME, USERNAME, PASSWORD);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns a PreparedStatement instance,
     * with a query and a scroll and read ResultSet type
     * 
     * Scroll type:
     *  - ResultSet.TYPE_FORWARD_ONLY: scroll forward only
     *  - ResultSet.TYPE_SCROLL_SENSITIVE: sensitive to db changes
     *  - ResultSet.TYPE_SCROLL_INSENSITIVE: insensitive to db changes
     * 
     * Read type:
     *  - ResultSet.CONCUR_READ_ONLY: ResultSet can not be updated
     *  - ResultSet.CONCUR_UPDATABLE: ResultSet can be updated
     * 
     * @param query String
     * @param scroll_type int
     * @param read_type int
     * @return PreparedStatement
     * @throws SQLException 
     */
    public PreparedStatement getPreparedStatement(String query) throws SQLException{
        return this.conn.prepareStatement(query);
    }
    
    /**
     * Returns a PreparedStatement instance,
     * with a query and a scroll and read ResultSet type
     * 
     * Scroll type:
     *  - ResultSet.TYPE_FORWARD_ONLY: scroll forward only
     *  - ResultSet.TYPE_SCROLL_SENSITIVE: sensitive to db changes
     *  - ResultSet.TYPE_SCROLL_INSENSITIVE: insensitive to db changes
     * 
     * Read type:
     *  - ResultSet.CONCUR_READ_ONLY: ResultSet can not be updated
     *  - ResultSet.CONCUR_UPDATABLE: ResultSet can be updated
     * 
     * @param query String
     * @param scroll_type int
     * @param read_type int
     * @return PreparedStatement
     * @throws SQLException 
     */
    public PreparedStatement getPreparedStatement(String query, int scroll_type, int read_type) throws SQLException{
        return this.conn.prepareStatement(query, scroll_type, read_type);
    }
    
    /**
     * Returns a ResultSet instance result of a raw query,
     * with scroll and read ResultSet type
     * 
     * Scroll type:
     *  - ResultSet.TYPE_FORWARD_ONLY: scroll forward only
     *  - ResultSet.TYPE_SCROLL_SENSITIVE: sensitive to db changes
     *  - ResultSet.TYPE_SCROLL_INSENSITIVE: insensitive to db changes
     * 
     * Read type:
     *  - ResultSet.CONCUR_READ_ONLY: ResultSet can not be updated
     *  - ResultSet.CONCUR_UPDATABLE: ResultSet can be updated
     * 
     * @param query String
     * @param scroll_type int
     * @param read_type int
     * @return ResultSet
     * @throws SQLException 
     */
    public ResultSet statement(String query, int scroll_type, int read_type) throws SQLException{
        return conn.createStatement(scroll_type, read_type).executeQuery(query);
    }
    
    /**
     * Returns a ResultSet instance result of a prepared statement query,
     * with scroll and read ResultSet type
     * 
     * Params are passed in a type Object array
     * 
     * Scroll type:
     *  - ResultSet.TYPE_FORWARD_ONLY: scroll forward only
     *  - ResultSet.TYPE_SCROLL_SENSITIVE: sensitive to db changes
     *  - ResultSet.TYPE_SCROLL_INSENSITIVE: insensitive to db changes
     * 
     * Read type:
     *  - ResultSet.CONCUR_READ_ONLY: ResultSet can not be updated
     *  - ResultSet.CONCUR_UPDATABLE: ResultSet can be updated
     * 
     * @param query String
     * @param scroll_type int
     * @param read_type int
     * @param bindings Object[]
     * @return ResultSet
     * @throws SQLException 
     */
    public ResultSet preparedStatement(String query, int scroll_type, int read_type, Object[] bindings) throws SQLException{
        PreparedStatement ps = conn.prepareStatement(query, scroll_type, read_type);
        this.addBindings(ps, bindings);
        return ps.executeQuery();
    }
    
    /**
     * Returns the generated ID: 0 = failed inserting, greater than 0 = inserted
     * 
     * Params are passed in a type Object array
     * 
     * @param query String
     * @param bindings Object[]
     * @return boolean 
     */
    public int insert(String query, Object[] bindings){
        try {
            PreparedStatement ps = conn.prepareStatement(query, RETURN_GENERATED_KEYS);
            this.addBindings(ps, bindings);
        
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    /**
     * Returns an int who indicates the number of updated rows
     * 
     * Params are passed in a type Object array
     * 
     * @param query String
     * @param bindings Object[]
     * @return int
     */
    public int update(String query, Object[] bindings){
        try {
            return this.executeUpdate(query, bindings);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    /**
     * Returns an int who indicates the number of deleted rows
     * 
     * Params are passed in a type Object array
     * 
     * @param query String
     * @param bindings Object[]
     * @return int
     */
    public int delete(String query, Object[] bindings){
        try {
            return this.executeUpdate(query, bindings);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    /**
     * Add bindings to PreparedStatement instance
     * 
     * implicit pointers use
     * 
     * @param ps PreparedStatement
     * @param bindings Object[]
     * @throws SQLException 
     */
    private void addBindings(PreparedStatement ps, Object[] bindings) throws SQLException{
        for(int i = 0; i < bindings.length; i++){
            ps.setObject((i+1), bindings[i]); // preparedStatement params are indexed starting from 1
        }
    }
    
    /**
     * Update and delete has diferent names, but uses the same code
     * 
     * @param query String
     * @param bindings Object[]
     * @return int
     * @throws SQLException 
     */
    private int executeUpdate(String query, Object[] bindings) throws SQLException{
        PreparedStatement ps = conn.prepareStatement(query);
        this.addBindings(ps, bindings);
        return ps.executeUpdate();
    }
}
