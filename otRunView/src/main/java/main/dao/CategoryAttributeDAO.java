
package main.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import main.domain.Category;
import main.domain.CategoryAttribute;

/**
 * CategoryAttributeDAO is a data access object for handling database operations relating to the CategoryAttribute class.
 * 
 * @see     main.domain.CategoryAttribute#CategoryAttribute(int, java.lang.String, int) 
 */
public class CategoryAttributeDAO implements DAO<CategoryAttribute, Integer> {    
    private String tableName;
    private String categoryTableName;
    
    /**
     * Constructor initiating a CategoryAttributeDAO.
     *
     * @param   tableName   The table name that the CategoryDAO should use. 
     * @param   categoryTableName   The table name to be used when referring to the database table corresponding to the Category class
     * 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String) 
     */ 
    public CategoryAttributeDAO(String tableName, String categoryTableName) {        
        this.tableName = tableName;
        this.categoryTableName = categoryTableName;
    }
    
    /**
     * Makes sure there is a database table corresponding to the CategoryAttribute class.
     *
     * @see     main.domain.CategoryAttribute#CategoryAttribute(int, java.lang.String, int) 
     * 
     * @return  Boolean value for wether or not database table corresponding to the CategoryAttribute class exists after calling the method
     */ 
    public boolean ensureTableExists() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {            
            String sqlStatement = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER AUTO_INCREMENT PRIMARY KEY, attribute TEXT, category_id INTEGER, FOREIGN KEY (category_id) REFERENCES " + categoryTableName + "(id));";  
            databaseConnection.prepareStatement(sqlStatement).executeUpdate();
            
            databaseConnection.close();   
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Resets the database table corresponding to the CategoryAttribute class, so that the table exists but no longer contains any data.
     *  
     * @see     main.domain.CategoryAttribute#CategoryAttribute(int, java.lang.String, int) 
     * 
     * @return  Boolean value for wether or not database table corresponding to the CategoryAttribute class was reset
     */ 
    public boolean resetTable() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            String sqlStatement = "DROP TABLE " + tableName + " IF EXISTS;";
            databaseConnection.prepareStatement(sqlStatement).executeUpdate();
            
            databaseConnection.close();        
            
            return ensureTableExists();
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Inserts a new row into the database table corresponding to the CategoryAttribute class.
     *
     * @param   attribute   A CategoryAttribute object containing the data to be inserted into the database table
     *
     * @see     main.domain.CategoryAttribute#CategoryAttribute(int, java.lang.String, int) 
     * 
     * @return  The generated primary key of the inserted row
     */
    public Integer create(CategoryAttribute attribute) {        
        Integer id = -1;
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {            
            String sqlStatement = "INSERT INTO " + tableName + " (attribute, category_id) VALUES (?, ?)";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
            statement.setString(1, attribute.getAttribute());
            statement.setInt(2, attribute.getCategoryId());
            statement.executeUpdate();
            
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            
            closeResources(resultSet, statement, databaseConnection); 
            
        } catch (Exception e) {
            
        }                
        return id;
    }
    
    /**
     * Reads a row, corresponding to a CategoryAttribute object, from the database table corresponding to the CategoryAttrbiute class.
     *
     * @param   id   An Integer corresponding to the primary key of the row to be read
     *
     * @see     main.domain.CategoryAttribute#CategoryAttribute(int, java.lang.String, int) 
     *
     * @return  A CategoryAttribute object
     */    
    public CategoryAttribute read(Integer id) {        
        CategoryAttribute attribute = new CategoryAttribute(id, "", -1);
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {            
            String sqlStatement = "SELECT * FROM " + tableName + " WHERE id = ?";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                attribute.setAttribute(resultSet.getString("attribute"));
                attribute.setCategoryId(resultSet.getInt("category_id"));
            }
            
            closeResources(resultSet, statement, databaseConnection);

        } catch (Exception e) {
            
        }            
        return attribute;
    }
    
    /**
     * Lists all the data in the database table corresponding to the CategoryAttribute class, in the form of CategoryAttribute objects.
     *
     * @param   categoryId   An Integer corresponding a foreign key, referring to a row int the database table corresponding to the Category class, found in the rows, in the database table corresponding to the CategoryAttribute class, to be included in the list
     * 
     * @see     main.domain.CategoryAttribute#CategoryAttribute(int, java.lang.String, int) 
     *
     * @return  A list of CategoryAttribute objects
     */
    public List<CategoryAttribute> list(Integer categoryId) {        
        List<CategoryAttribute> attributes = new ArrayList<>();       
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {            
            String sqlStatement = "SELECT * FROM "  + tableName;
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String attribute = resultSet.getString("attribute");
                Integer category = resultSet.getInt("category_id");
                
                CategoryAttribute newAttribute = new CategoryAttribute(id, attribute, category);
                if (newAttribute.getCategoryId() == categoryId)  {
                    attributes.add(newAttribute);
                }
            } 
            
            closeResources(resultSet, statement, databaseConnection);
            
        } catch (Exception e) {
            
        }        
        return attributes;
    }
    
    /**
     * Handles closure of several objects often used together in database operations, namely a ResultSet, a PreparedStatement, and a Connection.
     *
     * @param   resultSet   The ResultSet to be closed
     * @param   preparedStatement    The PreparedStatement to be closed
     * @param   connection    The Connection to be closed
     */ 
    private static void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) throws Exception {        
        resultSet.close();
        statement.close();
        connection.close();
    }
}
