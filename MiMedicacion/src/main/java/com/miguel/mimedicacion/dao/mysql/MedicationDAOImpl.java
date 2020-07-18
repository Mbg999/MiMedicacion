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


public class MedicationDAOImpl implements MedicationDAO {
    
    // all the used SQL sentences
    private static final String FIRST = "SELECT * FROM medications WHERE id = ?";
    private static final String ALL = "SELECT * FROM medications";
    private static final String ALLFROMAUSER = "SELECT * FROM medications WHERE user_id = ?";
    private static final String INSERT = "INSERT INTO medications(user_id, name, description, hours_interval) values(?,?,?,?)";
    private static final String UPDATE = "UPDATE medications "+
            "SET name = ?, description = ?, hours_interval = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM medications WHERE id = ?";
    
    // DB
    private DB db;
    
    public MedicationDAOImpl(){
        try {
            db = new DB();
        } catch (SQLException ex) {
            Logger.getLogger(MedicationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


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
                med.setCreated_at(rs.getTimestamp("created_at"));
                med.setUpdated_at(rs.getTimestamp("updated_at"));
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

    @Override
    public List<Medication> all() {
        ResultSet rs = null;
        Medication med = null;
        List<Medication> meds = new ArrayList();
        
        try {
            rs = db.statement(ALL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            while(rs.next()){
                med = new Medication();
                med.setId(rs.getInt("id"));
                med.setUser_id(rs.getInt("user_id"));
                med.setName(rs.getString("name"));
                med.setDescription(rs.getString("description"));
                med.setHours_interval(rs.getInt("hours_interval"));
                med.setCreated_at(rs.getTimestamp("created_at"));
                med.setUpdated_at(rs.getTimestamp("updated_at"));
                meds.add(med);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MedicationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return meds;
    }
    
    @Override
    public List<Medication> allFromAUser(int id) {
        ResultSet rs = null;
        Medication med = null;
        List<Medication> meds = new ArrayList();
        
        try {
            rs = db.preparedStatement(ALLFROMAUSER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY,
                    new Object[]{id});
            
            while(rs.next()){
                med = new Medication();
                med.setId(rs.getInt("id"));
                med.setUser_id(rs.getInt("user_id"));
                med.setName(rs.getString("name"));
                med.setDescription(rs.getString("description"));
                med.setHours_interval(rs.getInt("hours_interval"));
                med.setCreated_at(rs.getTimestamp("created_at"));
                med.setUpdated_at(rs.getTimestamp("updated_at"));
                meds.add(med);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MedicationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return meds;
    }

    @Override
    public int insert(Medication o) {
        return this.db.insert(INSERT, new Object[]{
            o.getUser_id(),
            o.getName(),
            o.getDescription(),
            o.getHours_interval()
        });
    }

    @Override
    public boolean update(Medication o) {
        return this.db.update(UPDATE, new Object[]{
            o.getName(),
            o.getDescription(),
            o.getHours_interval(),
            o.getId()
        }) > 0;
    }

    @Override
    public boolean delete(Medication o) {
        return this.db.delete(DELETE, new Object[]{
            o.getId()
        }) > 0;
    }
}
