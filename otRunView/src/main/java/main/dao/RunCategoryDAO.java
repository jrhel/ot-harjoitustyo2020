/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import main.domain.CategoryAttribute;

/**
 *
 * @author J
 */
public class RunCategoryDAO {
    
    public void ensureTableExists() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String createTable = "CREATE TABLE IF NOT EXISTS RunCategory (id INTEGER AUTO_INCREMENT PRIMARY KEY, run_id INTEGER, category_id INTEGER, FOREIGN KEY (run_id) REFERENCES Run(id), FOREIGN KEY (category_id) REFERENCES Category(id));";  
            databaseConnection.prepareStatement(createTable).executeUpdate();
            
            databaseConnection.close();            
            
        } catch (Exception e) {
            System.out.println("RunCategoryDAO ERROR: table exists, " + e);
        }
    }
    
    public void resetTable() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            databaseConnection.prepareStatement("DROP TABLE RunCategory IF EXISTS;").executeUpdate();
                    
            databaseConnection.close();
            
        } catch (Exception e) {
            System.out.println("RunCategoryDAO ERROR: Could not reset table");
        }        
        
        ensureTableExists();
    }
    
    public void create(int runID, int categoryID) {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            PreparedStatement statement = databaseConnection.prepareStatement("INSERT INTO RunCategory (run_id, category_id) VALUES (?, ?)");
            statement.setInt(1, runID);
            statement.setInt(2, categoryID);
            statement.executeUpdate();
            
            statement.close();            
            databaseConnection.close(); 
            
        } catch (Exception e) {
            System.out.println("RunCategoryDAO ERROR: create: " + e);
        }        
        
    }
    
    public List<Integer> listCategoryIDs(int runID) {
        
        List<Integer> categoryIDs = new ArrayList<>();
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {            
            PreparedStatement statement = databaseConnection.prepareStatement("SELECT * FROM RunCategory WHERE run_id = ?");
            statement.setInt(1, runID);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Integer categoryID = resultSet.getInt("category_id");
                categoryIDs.add(categoryID);
            }

            resultSet.close();
            statement.close();
            databaseConnection.close();
            
        } catch (Exception e) {
            System.out.println("RunCategoryDAO ERROR: listCategoryIDs" + e);
        }        
        
        return categoryIDs;
    }
    
    public boolean delete(int runID) {
        
        boolean result = false;
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {            
            PreparedStatement statement = databaseConnection.prepareStatement("DELETE FROM RunCategory WHERE run_id = ?");
            statement.setInt(1, runID);
            statement.execute();
            
            statement.close();
            databaseConnection.close();
            
            result = true;
            
        } catch (Exception e) {
            System.out.println("RunCategoryERROR: could not delete runCategories: " + e);
        }
        
        return result;
    }
}
