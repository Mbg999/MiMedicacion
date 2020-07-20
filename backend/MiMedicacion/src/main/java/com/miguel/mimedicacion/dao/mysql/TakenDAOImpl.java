/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.dao.mysql;

import com.miguel.mimedicacion.config.DB;
import com.miguel.mimedicacion.dao.TakenDAO;
import com.miguel.mimedicacion.models.Taken;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO implementation, all the DB interactions for the taken table
 * 
 * @author miguel
 */
public class TakenDAOImpl implements TakenDAO {

    // all the used SQL sentences
    private static final String FIRST = "SELECT * FROM taken WHERE id = ?";
    private static final String LAST_OF_A_MEDICATION = "SELECT * FROM taken WHERE medication_id = ? ORDER BY taken_at DESC LIMIT 1";
    private static final String ALL = "SELECT * FROM taken";
    private static final String ALL_OF_A_MEDICATION = "SELECT * FROM taken WHERE medication_id = ? ORDER BY taken_at DESC";
    private static final String INSERT = "INSERT INTO taken(medication_id) values(?)";
    private static final String DELETE = "DELETE FROM taken WHERE id = ?";
    
    // DB
    private DB db;
    
    public TakenDAOImpl(){
        try {
            db = new DB();
        } catch (SQLException ex) {
            Logger.getLogger(MedicationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns the first taken found by id, or null
     * 
     * @param id Integer
     * @return Taken | null
     */
    @Override
    public Taken first(Integer id) {
        Taken taken = null;
        ResultSet rs = null;
        
        try {
            rs = db.preparedStatement(FIRST, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY,
                    new Object[]{id});
            
            if(rs.next()){
                taken = new Taken();
                taken.setId(rs.getInt("id"));
                taken.setMedication_id(rs.getInt("medication_id"));
                taken.setTaken_at(rs.getTimestamp("taken_at"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TakenDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MedicationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return taken;
    }
    
   @Override
    public Taken lastOfAMedication(int id) {
        Taken taken = null;
        ResultSet rs = null;
        
        try {
            rs = db.preparedStatement(LAST_OF_A_MEDICATION, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY,
                    new Object[]{id});
            
            if(rs.next()){
                taken = new Taken();
                taken.setId(rs.getInt("id"));
                taken.setMedication_id(rs.getInt("medication_id"));
                taken.setTaken_at(rs.getTimestamp("taken_at"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TakenDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MedicationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return taken;
    }

    /**
     * Returns all the taken
     * 
     * @return List&lt;Taken&gt;
     */
    @Override
    public List<Taken> all() {
        ResultSet rs = null;
        Taken taken;
        List<Taken> takens = new ArrayList();
        
        try {
            rs = db.statement(ALL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            while(rs.next()){
                taken = new Taken();
                taken.setId(rs.getInt("id"));
                taken.setMedication_id(rs.getInt("medication_id"));
                taken.setTaken_at(rs.getTimestamp("taken_at"));
                takens.add(taken);
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
        
        return takens;
    }
    
    /**
     * Returns all the taken of a medication
     * 
     * @param id int
     * @return List&lt;Taken&gt;
     */
    @Override
    public List<Taken> allOfAMedication(int id) {
        ResultSet rs = null;
        Taken taken;
        List<Taken> takens = new ArrayList();
        
        try {
            rs = db.preparedStatement(ALL_OF_A_MEDICATION, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY, new Object[]{id});
            
            while(rs.next()){
                taken = new Taken();
                taken.setId(rs.getInt("id"));
                taken.setMedication_id(rs.getInt("medication_id"));
                taken.setTaken_at(rs.getTimestamp("taken_at"));
                takens.add(taken);
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
        
        return takens;
    }

    /**
     * Insert a new taken and return the generated ID
     * 
     * @param o Taken
     * @return int
     */
    @Override
    public int insert(Taken o) {
        return this.db.insert(INSERT, new Object[]{
            o.getMedication_id()
        });
    }

    /**
     * Taken can't be updated
     * 
     * @param o Taken
     * @return boolean 
     */
    @Override
    public boolean update(Taken o) {
        throw new UnsupportedOperationException("Taken can't be updated");
    }

    /**
     * Delete a taken and return if the taken was deleted or not
     * 
     * @param o Taken
     * @return boolean
     */
    @Override
    public boolean delete(Taken o) {
        return this.db.delete(DELETE, new Object[]{o.getId()}) > 0;
    }
    
}
