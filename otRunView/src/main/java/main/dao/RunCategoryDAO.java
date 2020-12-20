
package main.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import main.domain.CategoryAttribute;

/**
 * RunCategoryDAO is a data access object for handling database operations in the database table representing relationships between rows in the database tables corresponding to the classes Run and Category. 
 * 
 * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
 * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String) 
 */
public class RunCategoryDAO implements DAO<Integer, Integer> {    
    private String tableName;
    private String runTableName;
    private String categoryTableName;
    
    /**
     * Constructor of initiating a RunCategoryDAO.
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
     * Makes sure there is a database table containing relationships between rows in the database tables corresponding to the classes Run and Category.
     *
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String) 
     * 
     * @return  Boolean value for wether or not database table, containing relationships between the classes Run and Category, exists after calling the method
     */ 
    public boolean ensureTableExists() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {            
            String sqlStatement = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER AUTO_INCREMENT PRIMARY KEY, run_id INTEGER, category_id INTEGER, FOREIGN KEY (run_id) REFERENCES " + runTableName + "(id), FOREIGN KEY (category_id) REFERENCES " + categoryTableName + "(id));";  
            databaseConnection.prepareStatement(sqlStatement).executeUpdate();
            
            databaseConnection.close();  
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
   
    /**
     * Resets the database table, representing a relationship between a row in the database table representing relationships between rows in the database tables corresponding to the classes Run and Category.
     *
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String)
     * 
     * @return  Boolean value for wether or not database table, containing relationships between the classes Run and Category, classes was reset
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
     * Inserts a new row, representing a relationship between a row in the database tables corresponding to the classes Run and Category, into the database table representing relationships between rows in the database tables corresponding to the classes Run and Category.
     *
     * @param   runId   The primary key for a row in the database table corresponding to the Run class
     * @param   categoryId   The primary key for a row in the database table corresponding to the Category class
     * 
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String)
     */
    public void create(Integer runId, Integer categoryId) {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {            
            String sqStatement = "INSERT INTO " + tableName + " (run_id, category_id) VALUES (?, ?)";
            PreparedStatement statement = databaseConnection.prepareStatement(sqStatement);
            statement.setInt(1, runId);
            statement.setInt(2, categoryId);
            statement.executeUpdate();
            
            statement.close();            
            databaseConnection.close(); 
            
        } catch (Exception e) {
            
        }                
    }
    
    /**
     * Lists the primary keys, for entries in the database table corresponding to the Category class, which correspond to the given primary keys for entries in the database table corresponding to the Run class.
     *
     * @param   runId   The primary key for a row in the database table corresponding to the Run class
     * 
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String)
     * 
     * @return  A list of Integers representing primary keys, for entries in the database table corresponding to the Category class, that correspond to the given primary key for an entry in the database table corresponding to the Run class
     */   
    public List<Integer> listCategoryIds(Integer runId) {        
        List<Integer> categoryIds = new ArrayList<>();
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            String sqlStatement = "SELECT * FROM " + tableName + " WHERE run_id = ?";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
            statement.setInt(1, runId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Integer categoryId = resultSet.getInt("category_id");
                categoryIds.add(categoryId);
            }

            closeResources(resultSet, statement, databaseConnection);
            
        } catch (Exception e) {
            
        }                
        return categoryIds;
    }
    
    /**
     * Lists the primary keys, for entries in the database table corresponding to the Run class, which correspond to the given primary keys for entries in the database table corresponding to the Category class.
     *
     * @param   categoryIds   A list of primary keys for rows in the database table corresponding to the Category class
     * 
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String)
     * 
     * @return  A list of Integers representing primary keys, for entries in the database table corresponding to the Category class, that correspond to the given primary key for an entry in the database table corresponding to the Run class
     */ 
    public List<Integer> listRunIds(List<Integer> categoryIds) {
        List<Integer> runIds = new ArrayList<>();
        
        String range = getRange(categoryIds);
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            String sqlStatement = "SELECT * FROM " + tableName + " WHERE category_id  IN (" + range + ")";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Integer runId = resultSet.getInt("run_id");
                if (!runIds.contains(runId)) {
                    runIds.add(runId);
                }
            }
            
            closeResources(resultSet, statement, databaseConnection);            
            
        } catch (Exception e) {
            
        }               
        return runIds;
    }
    
    /**
     * Deletes a row, representing a relationship between a row in the database tables corresponding to the classes Run and Category, from the database table representing relationships between rows in the database tables corresponding to the classes Run and Category.
     *
     * @param   runId   An Integer representing the primary key, corresponding to a Run object, for a row in the database table corresponding to the Run class
     *  
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     * @see     main.domain.Category#Category(java.lang.Integer, java.lang.String, java.lang.String)
     * 
     * @return  Boolean value representing wether or not a row with the given primary key was deleted
     */   
    public boolean delete(Integer runId) {        
        boolean result = false;
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            String sqlStatement = "DELETE FROM " + tableName + " WHERE run_id = ?";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
            statement.setInt(1, runId);
            statement.execute();
            
            statement.close();
            databaseConnection.close();
            
            result = true;
            
        } catch (Exception e) {
            
        }        
        return result;
    }
    
    /**
     * Converts a list of Integers to a string that can be injected into an SQL statement as a range.
     *
     * @param   categoryIds The list of Integers to be converted to a string
     * 
     * @return  A string that can be injected into an SQL statement as a range.
     */ 
    private String getRange(List<Integer> categoryIds) {
        String range = "";
        for (Integer i: categoryIds) {
            range = range + "'" + i + "',";
        }
        range = range.substring(0, range.length() - 1);
        
        return range;
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
