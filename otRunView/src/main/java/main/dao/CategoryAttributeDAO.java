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
import java.util.Map;
import main.domain.CategoryAttribute;

/**
 *
 * @author J
 */
public class CategoryAttributeDAO {
    
    public void ensureTableExists() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String createTable = "CREATE TABLE IF NOT EXISTS CategoryAttribute (id INTEGER AUTO_INCREMENT PRIMARY KEY, attribute TEXT, category_id INTEGER, FOREIGN KEY (category_id) REFERENCES Category(id));";  
            databaseConnection.prepareStatement(createTable).executeUpdate();
            
            databaseConnection.close();            
            
        } catch (Exception e) {
            System.out.println("AttributeDAO ERROR: table exists");
        }
    }
    
    public void resetTable() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            databaseConnection.prepareStatement("DROP TABLE CategoryAttribute IF EXISTS;").executeUpdate();
                    
            databaseConnection.close();
            
        } catch (Exception e) {
            System.out.println("AttributeDAO ERROR: Could not reset table");
        }        
        
        ensureTableExists();
    }
    
    public void create(CategoryAttribute attribute) {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            PreparedStatement statement = databaseConnection.prepareStatement("INSERT INTO CategoryAttribute (attribute, category_id) VALUES (?, ?)");
            statement.setString(1, attribute.getAttribute());
            statement.setInt(2, attribute.getCategory_id());
            statement.executeUpdate();
            
            statement.close();            
            databaseConnection.close(); 
            
        } catch (Exception e) {
            System.out.println("AttributeDAO ERROR: create, " + attribute.toString() + ", " + e);
        }        
                
    }
    
    public CategoryAttribute read(int id) {
        
        CategoryAttribute attribute = new CategoryAttribute(id, "", -1);
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {

            PreparedStatement statement = databaseConnection.prepareStatement("SELECT * FROM CategoryAttribute WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                attribute.setAttribute(resultSet.getString("attribute"));
                attribute.setCategory_id(resultSet.getInt("category_id"));
            }
            
            resultSet.close();
            statement.close();
            databaseConnection.close();

        } catch (Exception e) {
            System.out.println("CatDAO ERROR: read, id: " + id);
        }    
        
        return attribute;
    }
    
    public List<CategoryAttribute> list(int categoryID) {        
        List<CategoryAttribute> attributes = new ArrayList<>();       
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {            
            PreparedStatement statement = databaseConnection.prepareStatement("SELECT * FROM CategoryAttribute");
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String attribute = resultSet.getString("attribute");
                Integer category = resultSet.getInt("category_id");
                
                CategoryAttribute newAttribute = new CategoryAttribute(id, attribute, category);
                if (newAttribute.getCategory_id() == categoryID)  {
                    attributes.add(newAttribute);
                }
            } 
            
            closeResources(resultSet, statement, databaseConnection);
            
        } catch (Exception e) {
            System.out.println("AttributeDAO ERROR: list" + e);
        }        
        return attributes;
    }
    
    private static void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) throws Exception {
        resultSet.close();
        statement.close();
        connection.close();
    }
}
