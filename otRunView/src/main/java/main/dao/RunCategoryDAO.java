
package main.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import main.domain.CategoryAttribute;

/**
 * RunCategoryDAO is a data access object for handling database operations in the database table representing relationships between rows in the database tables corresponding to the classes Run & Category
 * 
 * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
 * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String) 
 */
public class RunCategoryDAO {
    
    private String tableName;
    private String runTableName;
    private String categoryTableName;
    
    /**
     * Constructor of initiating a RunCategoryDAO
     *
     * @param   tableName   The table name that the RunDAO should use. 
     * @param   runTableName   The table name to be used when referring to the database table corresponding to the Run class
     * @param   categoryTableName   The table name to be used when referring to the database table corresponding to the Category class
     */ 
    public RunCategoryDAO(String tableName, String runTableName, String categoryTableName) {
        this.tableName = tableName;
        this.runTableName = runTableName;
        this.categoryTableName = categoryTableName;
    }
   
    /**
     * Makes sure there is a database table containing relationships between rows in the database tables corresponding to the classes Run & Category
     *
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String) 
     * 
     * @return  Boolean value for wether or not database table, containing relationships between the classes Run & Category, exists after calling the method
     */ 
    public boolean ensureTableExists() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String sqlStatement = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER AUTO_INCREMENT PRIMARY KEY, run_id INTEGER, category_id INTEGER, FOREIGN KEY (run_id) REFERENCES " + runTableName + "(id), FOREIGN KEY (category_id) REFERENCES " + categoryTableName + "(id));";  
            databaseConnection.prepareStatement(sqlStatement).executeUpdate();
            
            databaseConnection.close();  
            
            return true;
            
        } catch (Exception e) {
            System.out.println("RunCategoryDAO ERROR: table exists, " + e);
            return false;
        }
    }
   
    /**
     * Resets the database table, representing a relationship between a row in the database table representing relationships between rows in the database tables corresponding to the classes Run & Category
     *
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String)
     * 
     * @return  Boolean value for wether or not database table, containing relationships between the classes Run & Category, classes was reset
     */ 
    public boolean resetTable() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            String sqlStatement = "DROP TABLE " + tableName + " IF EXISTS;";
            databaseConnection.prepareStatement(sqlStatement).executeUpdate();
                    
            databaseConnection.close();
            
            ensureTableExists();
            
            return true;
            
        } catch (Exception e) {
            System.out.println("RunCategoryDAO ERROR: Could not reset table");
            return false;
        }         
    }
    
    /**
     * Inserts a new row, representing a relationship between a row in the database tables corresponding to the classes Run & Category, into the database table representing relationships between rows in the database tables corresponding to the classes Run & Category
     *
     * @param   runID   The primary key for a row in the database table corresponding to the Run class
     * @param   categoryID   The primary key for a row in the database table corresponding to the Category class
     * 
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String)
     */
    public void create(int runID, int categoryID) {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String sqStatement = "INSERT INTO " + tableName + " (run_id, category_id) VALUES (?, ?)";
            PreparedStatement statement = databaseConnection.prepareStatement(sqStatement);
            statement.setInt(1, runID);
            statement.setInt(2, categoryID);
            statement.executeUpdate();
            
            statement.close();            
            databaseConnection.close(); 
            
        } catch (Exception e) {
            System.out.println("RunCategoryDAO ERROR: create: " + e);
        }        
        
    }
    
    /**
     * Lists the primary keys, for entries in the database table corresponding to the Category class, that correspond to the given primary key for an entry in the database table corresponding to the Run class
     *
     * @param   runID   The primary key for a row in the database table corresponding to the Run class
     * @param   categoryID   The primary key for a row in the database table corresponding to the Category class
     * 
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String)
     * 
     * @return  A list of integers representing primary keys, for entries in the database table corresponding to the Category class, that correspond to the given primary key for an entry in the database table corresponding to the Run class
     */   
    public List<Integer> listCategoryIDs(int runID) {
        
        List<Integer> categoryIDs = new ArrayList<>();
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            String sqlStatement = "SELECT * FROM " + tableName + " WHERE run_id = ?";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
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
    
    /**
     * Deletes a row, representing a relationship between a row in the database tables corresponding to the classes Run & Category, from the database table representing relationships between rows in the database tables corresponding to the classes Run & Category
     *
     * @param   runID   An integer representing the primary key, corresponding to a Run object, for a row in the database table corresponding to the Run class
     *  
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String)
     * 
     * @return  A boolean value representing wether or not a row with the given primary key was deleted
     */   
    public boolean delete(int runID) {
        
        boolean result = false;
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            String sqlStatement = "DELETE FROM " + tableName + " WHERE run_id = ?";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
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
