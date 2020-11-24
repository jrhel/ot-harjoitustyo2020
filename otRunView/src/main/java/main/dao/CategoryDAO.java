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
import main.domain.Category;

/**
 *
 * @author J
 */
public class CategoryDAO {
    
    // create
    public boolean create(Category newCategory) {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            PreparedStatement statement = databaseConnection.prepareStatement("INSERT INTO Category (name, parent) VALUES (?, ?)");
            statement.setString(1, newCategory.getName());
            statement.setString(2, newCategory.getParentName());
            statement.executeUpdate();
            
            statement.close();            
            databaseConnection.close(); 
            
            return true;
            
        } catch (Exception e) {
            System.out.println("CatDAO ERROR: create");
            return false;
        }
    }
    
    // read
    
    public Category read(int primaryKey) {
        
        Category category = new Category(-1, "", null, "");
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {

            PreparedStatement statement = databaseConnection.prepareStatement("SELECT * FROM Category WHERE id = ?");
            statement.setInt(1, primaryKey);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));   
                category.setParentName(resultSet.getString("parent"));
            }

        } catch (Exception e) {
            System.out.println("CatDAO ERROR: read");
        }    
        
        return category;
    }
    
    // delete
    
    // list
    
    public void ensureTableExists() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String createTable = "CREATE TABLE IF NOT EXISTS Category (id INTEGER AUTO_INCREMENT PRIMARY KEY, name TEXT, parent TEXT);";  
            databaseConnection.prepareStatement(createTable).executeUpdate();
            
            databaseConnection.close();            
            
        } catch (Exception e) {
            System.out.println("CatDAO ERROR: table exists");
        }
    }
    
    public void resetTable() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            databaseConnection.prepareStatement("DROP TABLE Category IF EXISTS;").executeUpdate();            
            
            databaseConnection.close();
            
        } catch (Exception e) {
            System.out.println("CatDAO ERROR: reset table");
        }        
        
        ensureTableExists();
    }
    
    public Integer getPrimaryKey(String name) {
        
        Integer id = -1;
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            PreparedStatement statement = databaseConnection.prepareStatement("SELECT id FROM Category WHERE name = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            
            statement.close();
            resultSet.close();
            databaseConnection.close();        
        
        } catch (Exception e) {
            System.out.println("Could not obtain CategoryPrimaryKey for " + name + ": " + e);
        }
        
        return id;
    }
    
}
