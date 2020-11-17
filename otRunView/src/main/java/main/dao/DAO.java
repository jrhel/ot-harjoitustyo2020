/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.dao;

import java.sql.*;
import java.util.*;

/**
 * This class is a Data Access Object interface, to be implemented by each Data Access Object
 */
public interface DAO<T, K> {
    
    /**
     * This method creates an entry into the database.
     *
     * @param   object   The object, of class "T", to be entered into the database.
     */  
    void create(T object);
    
    /**
     * This method deletes a database entry with the primary key "K".
     *
     * @param   key   The key of the entry which is to be deleted.
     */  
    void delete(K key);
    
    /**
     * This method ensures that the database table exists
     * 
     * 
     */  
    void ensureTableExists();
    
    void resetTable();
}