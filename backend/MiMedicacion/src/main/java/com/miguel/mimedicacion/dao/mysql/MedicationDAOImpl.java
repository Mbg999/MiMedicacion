/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.dao.mysql;

import com.miguel.mimedicacion.config.DB;
import com.miguel.mimedicacion.dao.MedicationDAO;
import com.miguel.mimedicacion.models.Medication;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO implementation, all the DB interactions for the medication table
 * 
 * @author miguel
 */
public class MedicationDAOImpl implements MedicationDAO {
    
    // all the used SQL sentences
    private static final String FIRST = "SELECT * FROM medications WHERE id = ?";
    private static final String ALL_OF_A_USER = "SELECT * FROM medications WHERE user_id = ? ORDER BY created_at DESC, finished ASC";
    private static final String INSERT = "INSERT INTO medications(user_id, name, description, hours_interval) values(?,?,?,?)";
    private static final String UPDATE = "UPDATE medications "+
            "SET name = ?, description = ?, hours_interval = ?, finished = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM medications WHERE id = ?";
    
    // DB
    private DB db;
    private TakenDAOImpl tdi;
    
    public MedicationDAOImpl(){
        try {
            db = new DB();
            tdi = new TakenDAOImpl();
        } catch (SQLException ex) {
            Logger.getLogger(MedicationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the first medication found by id, or null
     * 
     * @param id Integer
     * @return Medication | null
     */
    @Override
    public Medication first(Integer id) {
        ResultSet rs = null;
        Medication med = null;
        
        try {
            rs = db.preparedStatement(FIRST, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY,
                    new Object[]{id});
            
            if(rs.next()){
                med = new Medication();
                med.setId(rs.getInt("id"));
                med.setUser_id(rs.getInt("user_id"));
                med.setName(rs.getString("name"));
                med.setDescription(rs.getString("description"));
                med.setHours_interval(rs.getInt("hours_interval"));
                med.setFinished(rs.getBoolean("finished"));
                med.setCreated_at(rs.getTimestamp("created_at"));
                med.setUpdated_at(rs.getTimestamp("updated_at"));
                med.setLast_taken(this.tdi.lastOfAMedication(id));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MedicationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MedicationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return med;
    }

    /**
     * Returns all the medications of a user
     * 
     * @param id int
     * @return List&lt;Medication&gt;
     */
    @Override
    public List<Medication> allOfAUser(int id) {
        ResultSet rs = null;
        Medication med;
        List<Medication> meds = new ArrayList();
        
        try {
            rs = db.preparedStatement(ALL_OF_A_USER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY,
                    new Object[]{id});
            
            while(rs.next()){
                med = new Medication();
                med.setId(rs.getInt("id"));
                med.setUser_id(rs.getInt("user_id"));
                med.setName(rs.getString("name"));
                med.setDescription(rs.getString("description"));
                med.setHours_interval(rs.getInt("hours_interval"));
                med.setFinished(rs.getBoolean("finished"));
                med.setCreated_at(rs.getTimestamp("created_at"));
                med.setUpdated_at(rs.getTimestamp("updated_at"));
                med.setLast_taken(this.tdi.lastOfAMedication(med.getId()));
                meds.add(med);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MedicationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MedicationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return meds;
    }

    /**
     * Insert a new medication and return the generated ID
     * 
     * @param o Medication
     * @return int
     */
    @Override
    public int insert(Medication o) {
        return this.db.insert(INSERT, new Object[]{
            o.getUser_id(),
            o.getName(),
            o.getDescription(),
            o.getHours_interval()
        });
    }

    /**
     * Update a medication and return if the medication was updated or not
     * 
     * @param o Medication
     * @return boolean
     */
    @Override
    public boolean update(Medication o) {
        return this.db.update(UPDATE, new Object[]{
            o.getName(),
            o.getDescription(),
            o.getHours_interval(),
            o.isFinished(),
            o.getId()
        }) > 0;
    }

    /**
     * Delete a medication and return if the medication was deleted or not
     * 
     * @param o Medication
     * @return boolean
     */
    @Override
    public boolean delete(Medication o) {
        return this.db.delete(DELETE, new Object[]{
            o.getId()
        }) > 0;
    }
}
