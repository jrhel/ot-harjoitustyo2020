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
import main.domain.Category;

/**
 *
 * @author J
 */
public class CategoryDAO {
    
    
    private static void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) throws Exception {
        resultSet.close();
        statement.close();
        connection.close();
    }
    
    public boolean ensureTableExists() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String createTable = "CREATE TABLE IF NOT EXISTS Category (id INTEGER AUTO_INCREMENT PRIMARY KEY, name TEXT, parent TEXT);";  
            databaseConnection.prepareStatement(createTable).executeUpdate();
            
            databaseConnection.close();            
            
            
            return true;
            
        } catch (Exception e) {
            System.out.println("CatDAO ERROR: table exists");
            return false;
        }
    }
    
    public boolean resetTable() {
        
        boolean result = true;
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            databaseConnection.prepareStatement("DROP TABLE Category IF EXISTS;").executeUpdate();            
            
            databaseConnection.close();
            
        } catch (Exception e) {
            System.out.println("CatDAO ERROR: reset table");
            return false;
        }        
        
        if (ensureTableExists()) {
            return true;
        } else {
            return false;
        }
        
    }
    
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
            
            closeResources(resultSet, statement, databaseConnection); 

        } catch (Exception e) {
            System.out.println("CatDAO ERROR: read");
        }    
        
        return category;
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
            
            closeResources(resultSet, statement, databaseConnection);        
        
        } catch (Exception e) {
            System.out.println("Could not obtain CategoryPrimaryKey for " + name + ": " + e);
        }
        
        return id;
    }
    
    public List<Category> list() {
        
        List<Category> categories = new ArrayList<>();
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            PreparedStatement statement = databaseConnection.prepareStatement("SELECT * FROM Category");
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String parent = resultSet.getString("parent");
                Category category = new Category(id, name, parent);
                categories.add(category);
            }
            
            closeResources(resultSet, statement, databaseConnection);
            
        } catch (Exception e) {
            System.out.println("CatDAO ERROR: list, " + e);
        }
        
        return categories;
    }
    
}
