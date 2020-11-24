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
import java.util.List;
import main.domain.Run;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author J
 */
public class RunDAO implements DAO<Run, Integer> {

    @Override
    public void create(Run newRun) {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            // date DATE, distance FLOAT, duration TIME, speedKmH FLOAT, cadence INTEGER, gpx TEXT)
            PreparedStatement statement = databaseConnection.prepareStatement("INSERT INTO Run (date, distance, duration, speedKmH, cadence, gpx) VALUES (?, ?, ?, ?, ?, ?)");
            
            statement.setString(1, newRun.getDate().toString());  
            statement.setDouble(2, newRun.getDistanceKm());  
            statement.setString(3, newRun.getDuration().toString());  
            statement.setDouble(4, newRun.getAvgSpeedKmH());  
            statement.setInt(5, newRun.getAvgCadence());  
            statement.setString(6, newRun.getGpxFilePath());  
            statement.executeUpdate();

            statement.close();
            
            databaseConnection.close(); 
            
        } catch (Exception e) {
            System.out.println("RunDAO ERROR: create");
        }
    }
    
    public Run read(int primaryKey) {        
        Run run = new Run();
                
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            PreparedStatement statement = databaseConnection.prepareStatement("SELECT * FROM Run WHERE id = ?");
            statement.setInt(1, primaryKey);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDate date = new LocalDate(resultSet.getDate("date"));
                double distance = resultSet.getDouble("distance");
                String duration = resultSet.getString("duration");
                double avgSpeedKmH = resultSet.getDouble("speedKmH");
                int cadence = resultSet.getInt("cadence");
                String gpxFilePath = resultSet.getString("gpx");
                run = new Run(distance, date, duration, avgSpeedKmH, cadence, gpxFilePath);                
            }            
            resultSet.close();
            statement.close();
            databaseConnection.close();

        } catch (Exception e) {
            System.out.println("CatDAO ERROR: read, id: " + primaryKey);
        }
        return run;
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
