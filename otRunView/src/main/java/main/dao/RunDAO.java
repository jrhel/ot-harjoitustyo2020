
package main.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import main.domain.Run;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * RunDAO is a data access object for handling database operations relating to the Run class.
 * 
 * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
 */
public class RunDAO implements DAO<Run, Integer> {    
    private String tableName;
    
    /**
     * Constructor of initiating a RunyDAO.
     *
     * @param   tableName   The table name that the RunDAO should use. 
     */ 
    public RunDAO(String tableName) {        
        this.tableName = tableName;
    }
    
    /**
     * Makes sure there is a database table corresponding to the Run class.
     * 
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     *
     * @return  Boolean value for wether or not database table corresponding to the Run class exists after calling the method
     */ 
    public boolean ensureTableExists() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {                 
            String sqlStatement = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER AUTO_INCREMENT PRIMARY KEY, date DATE, distance FLOAT, duration TIME, speedKmH FLOAT, cadence INTEGER, gpx TEXT);";  
            databaseConnection.prepareStatement(sqlStatement).executeUpdate();
            
            databaseConnection.close();   
            
            return true;
            
        } catch (Exception e) {            
            return false;            
        }
    }
    
    /**
     * Resets the database table corresponding to the Run class, so that the table exists but no longer contains any data.
     * 
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     *
     * @return  Boolean value for wether or not database table corresponding to the Run class was reset
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
     * Inserts a new row into the database table corresponding to the Run class.
     *
     * @param   newRun   A Run object containing the data to be inserted into the database table
     *
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     *
     * @return  The generated primary key of the inserted row
     */
    public Integer create(Run newRun) {        
        int id = -1;
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {            
            String sqlStatement = "INSERT INTO " + tableName + " (date, distance, duration, speedKmH, cadence, gpx) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);            
            setInsertStatement(newRun, statement); 
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
     * Handles the injection of values into a SQL insert statement wrapped in a PreparedStatement object.
     *
     * @param   newRun   A Run object containing the data to be inserted into the database table
     * @param   statement   The PreparedStatement object wrapping the SQL insert statement
     * 
     * @throws  Exception   SQLException
     */
    public void setInsertStatement(Run newRun, PreparedStatement statement) throws Exception {        
        statement.setString(1, newRun.getDate().toString());  
        statement.setDouble(2, newRun.getDistanceKm());  
        statement.setString(3, newRun.getDuration().toString());  
        statement.setDouble(4, newRun.getAvgSpeedKmH());  
        statement.setInt(5, newRun.getAvgCadence());  
        statement.setString(6, newRun.getGpxFilePath());
    }
    
    /**
     * Reads a row, corresponding to a Run object, from the database table corresponding to the Run class.
     *
     * @param   id   An Integer corresponding to the primary key of the row to be read
     *
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     *
     * @return  A Run object
     */   
    public Run read(Integer id) {            
        Run run = new Run();
                
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String sqlStatement = "SELECT * FROM " + tableName + " WHERE id = ?";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {                
                run = makeRunObject(resultSet);     
            }          
            
            closeResources(resultSet, statement, databaseConnection);

        } catch (Exception e) {
            
        }        
        return run;
    }
    
    /**
     * Lists all the data in the database table corresponding to the Run class, in the form of Run objects.
     *
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     *
     * @return  A list of Run objects
     */   
    public List<Run> list() {        
        List<Run> allRuns = new ArrayList<>();
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {            
            String sqlStatement = "SELECT * FROM " + tableName + " ORDER BY date DESC";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Run run = makeRunObject(resultSet);     
                allRuns.add(run);
            }            
            
            closeResources(resultSet, statement, databaseConnection);
            
        } catch (Exception e) {
            
        }         
        return allRuns;
    }
    
    /**
     * Deletes a row, corresponding to a Run object, from the database table corresponding to the Run class.
     *
     * @param   id   An integer corresponding to the primary key of the row to be deleted
     *
     * @return  Boolean value representing wether or not a row with the given primary key was deleted
     */   
    public boolean delete(Integer id) {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {            
            String sqlStatement = "DELETE FROM " + tableName + " WHERE id = ?";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlStatement);
            statement.setInt(1, id);
            statement.execute();
            
            statement.close();
            databaseConnection.close();
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Creates a Run object and sets values for it from the ReusltSet passed passed to the method as a parameter.
     *
     * @param   resultSet   A resultSet to get values from
     * 
     * @see     main.domain.Run#Run(double, org.joda.time.LocalDate, org.joda.time.LocalTime, int, java.lang.String, java.util.List) 
     *
     * @return  A Run object
     */ 
    private Run makeRunObject(ResultSet resultSet) throws Exception {        
        Run run = new Run();
        
        Integer id = resultSet.getInt("id");
        LocalDate date = new LocalDate(resultSet.getDate("date"));
        double distance = resultSet.getDouble("distance");
        String duration = resultSet.getString("duration");
        double avgSpeedKmH = resultSet.getDouble("speedKmH");
        int cadence = resultSet.getInt("cadence");
        String gpxFilePath = resultSet.getString("gpx");
        run = new Run(id, distance, date, duration, avgSpeedKmH, cadence, gpxFilePath);
        
        return run;
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
