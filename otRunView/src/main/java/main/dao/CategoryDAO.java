
package main.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import main.domain.Category;

/**
 * CategoryDAO is a data access object for handling database operations relating to the Category class
 * 
 * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String) 
 */
public class CategoryDAO {
    
    private String tableName;
    
    /**
     * Constructor of initiating a CategoryDAO
     *
     * @param   tableName   The table name that the CategoryDAO should use. 
     */  
    public CategoryDAO(String tableName) {
        this.tableName = tableName;
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
    
    /**
     * Makes sure there is a database table corresponding to the "Category" class
     * 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String) 
     *
     * @return  Boolean value for wether or not database table corresponding to the Category class exists after calling the method
     */ 
    public boolean ensureTableExists() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String sqlStatement = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER AUTO_INCREMENT PRIMARY KEY, name TEXT, parent TEXT);";
            databaseConnection.prepareStatement(sqlStatement).executeUpdate();
            
            databaseConnection.close();                        
            
            return true;
            
        } catch (Exception e) {
            System.out.println("CatDAO ERROR: table exists");
            return false;
        }
    }
    
    /**
     * Resets the database table corresponding to the "Category" class, so that the table exists but no longer contains any data
     * 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String) 
     *
     * @return  Boolean value for wether or not database table corresponding to the Category class was reset
     */ 
    public boolean resetTable() {
        
        boolean result = true;
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            String sqlStatement = "DROP TABLE " + tableName + " IF EXISTS;";
            databaseConnection.prepareStatement(sqlStatement).executeUpdate();            
            
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
    
    /**
     * Inserts a new row, corresponding to a Category object, into the database table corresponding to the Category class
     *
     * @param   newCategory   A Category object containing the data to be inserted into the database table
     *
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String) 
     *
     * @return  Boolean value for wether or not data was successfully inserted into the database table corresponding to the Category class
     */
    public boolean create(Category newCategory) {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String sqlStatement = "INSERT INTO " + tableName + " (name, parent) VALUES (?, ?)";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
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
    
    /**
     * Reads a row, corresponding to a Category object, from the database table corresponding to the Category class
     *
     * @param   primaryKey   An integer corresponding to the primary key of the row to be read
     *
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String) 
     *
     * @return  A Category object
     */    
    public Category read(int primaryKey) {
        
        Category category = new Category(-1, "", null, "");
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String sqlStatement = "SELECT * FROM " + tableName + " WHERE id = ?";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
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
    
    /**
     * Obtains the primary key for a row, corresponding to a Category object from the database table corresponding to the Category class, with the name given as a parameter to the method 
     *
     * @param   name   A string corresponding to the desired primary key
     *
     * @return  The primary key for the row containing the string given as a parameter to the method. If no row contains the given string, the method returns -1
     */ 
    public Integer getPrimaryKey(String name) {
        
        Integer id = -1;
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String sqlStatement = "SELECT id FROM " + tableName + " WHERE name = ?";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
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
    
    /**
     * Lists all the data in the database table corresponding to the Category class, in the form of Category objects
     *
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String) 
     *
     * @return  A list of Category objects
     */   
    public List<Category> list() {
        
        List<Category> categories = new ArrayList<>();
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String sqlStatement = "SELECT * FROM " + tableName;
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
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
