/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.dao;

import java.sql.*;
import java.util.*;

/**
 * This class is a Data Access Object interface, to be implemented by each Data Access Object.
 */
public interface DAO<T, K> {
    
    /**
     * This method ensures that the database table exists.
     * 
     * @return  Boolean value for wether or not database table corresponding to the class "T" exists after calling the method
     */  
    boolean ensureTableExists();
    
    /**
     * This method resets the database table exists, so that it contains no data but is ready to be used.
     * 
     * @return  Boolean value for wether or not database table corresponding to the class "T" was reset
     */ 
    boolean resetTable(); 
}