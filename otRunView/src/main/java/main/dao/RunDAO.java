/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import main.domain.Run;

/**
 *
 * @author J
 */
public class RunDAO implements DAO<Run, Integer> {

    @Override
    public void create(Run newRun) {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            // date DATE, distance FLOAT, duration TIME, speedKmH FLOAT, cadence INTEGER, gpx TEXT)
            PreparedStatement statement = databaseConnection.prepareStatement("INSERT INTO RUN (date, distance, duration, speedKmH, cadence, gpx) VALUES (?, ?, ?, ?, ?, ?)");
            /*
            statement.setString(1, newRun);  
            statement.setInt(2, newRun);  
            statement.setString(3, newRun);  
            statement.setInt(4, newRun);  
            statement.setString(5, newRun);  
            statement.setInt(6, newRun);  
            statement.executeUpdate();
*/
            statement.close();
            
            databaseConnection.close(); 
            
        } catch (Exception e) {
            System.out.println("DAO ERROR: create");
        }
    }

    @Override
    public void delete(Integer key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ensureTableExists() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String createTable = "CREATE TABLE IF NOT EXISTS Run (id INTEGER AUTO_INCREMENT PRIMARY KEY, date DATE, distance FLOAT, duration TIME, speedKmH FLOAT, cadence INTEGER, gpx TEXT);";  
            databaseConnection.prepareStatement(createTable).executeUpdate();
            
            databaseConnection.close();            
            
        } catch (Exception e) {
            System.out.println("RunDAO ERROR: table exists");
        }
    }

    @Override
    public void resetTable() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            databaseConnection.prepareStatement("DROP TABLE Run IF EXISTS;").executeUpdate();            
            
            databaseConnection.close();
            
        } catch (Exception e) {
            System.out.println("RunDAO ERROR: reset table");
        }        
        
        ensureTableExists();
    }

    
}
