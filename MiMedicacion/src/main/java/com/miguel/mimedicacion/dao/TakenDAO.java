/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.dao;

import com.miguel.mimedicacion.models.Taken;
import java.util.List;

/**
 *
 * @author miguel
 */
public interface TakenDAO extends DAO<Taken, Integer> {
    List<Taken> allOfAMedication(int id);
}
