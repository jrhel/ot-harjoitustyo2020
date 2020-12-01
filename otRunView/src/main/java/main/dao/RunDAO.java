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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import main.domain.Run;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author J
 */
public class RunDAO {

    public int create(Run newRun) {
        
        int primaryKey = -1;
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            PreparedStatement statement = databaseConnection.prepareStatement("INSERT INTO Run (date, distance, duration, speedKmH, cadence, gpx) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, newRun.getDate().toString());  
            statement.setDouble(2, newRun.getDistanceKm());  
            statement.setString(3, newRun.getDuration().toString());  
            statement.setDouble(4, newRun.getAvgSpeedKmH());  
            statement.setInt(5, newRun.getAvgCadence());  
            statement.setString(6, newRun.getGpxFilePath());  
            statement.executeUpdate();
            
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                primaryKey = resultSet.getInt(1);
            }
            
            closeResources(resultSet, statement, databaseConnection);
            
        } catch (Exception e) {
            System.out.println("RunDAO ERROR: create");
        }
        return primaryKey;
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
                run = new Run(id, distance, date, duration, avgSpeedKmH, cadence, gpxFilePath);                
            }            
            
            closeResources(resultSet, statement, databaseConnection);

        } catch (Exception e) {
            System.out.println("CatDAO ERROR: read, id: " + primaryKey);
        }
        
        return run;
    }

    public void ensureTableExists() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            String createTable = "CREATE TABLE IF NOT EXISTS Run (id INTEGER AUTO_INCREMENT PRIMARY KEY, date DATE, distance FLOAT, duration TIME, speedKmH FLOAT, cadence INTEGER, gpx TEXT);";  
            databaseConnection.prepareStatement(createTable).executeUpdate();
            
            databaseConnection.close();            
            
        } catch (Exception e) {
            System.out.println("RunDAO ERROR: table exists");
        }
    }

    public void resetTable() {
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            databaseConnection.prepareStatement("DROP TABLE Run IF EXISTS;").executeUpdate();            
            
            databaseConnection.close();
            
        } catch (Exception e) {
            System.out.println("RunDAO ERROR: reset table");
        }        
        
        ensureTableExists();
    }

    public List<Run> list() {
        List<Run> allRuns = new ArrayList<>();
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            PreparedStatement statement = databaseConnection.prepareStatement("SELECT * FROM Run ORDER BY date DESC");
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDate date = new LocalDate(resultSet.getDate("date"));
                double distance = resultSet.getDouble("distance");
                String duration = resultSet.getString("duration");
                double avgSpeedKmH = resultSet.getDouble("speedKmH");
                int cadence = resultSet.getInt("cadence");
                String gpxFilePath = resultSet.getString("gpx");
                Run run = new Run(id, distance, date, duration, avgSpeedKmH, cadence, gpxFilePath);       
                allRuns.add(run);
            }            
            
            closeResources(resultSet, statement, databaseConnection);
            
        } catch (Exception e) {
            System.out.println("RunDAO ERROR: listing runs");
        } 
        
        return allRuns;
    }
    
    public boolean delete(int id) {
        boolean result = false;
        
        try (Connection databaseConnection = DriverManager.getConnection("jdbc:h2:./runView", "sa", "")) {
            
            PreparedStatement statement = databaseConnection.prepareStatement("DELETE FROM Run WHERE id = ?");
            statement.setInt(1, id);
            statement.execute();
            
            statement.close();
            databaseConnection.close();
            
            result = true;
            
        } catch (Exception e) {
            System.out.println("RunDAO ERROR: delete failed: " + e);
        }
        
        return result;
    }
    
    private static void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) throws Exception {
        resultSet.close();
        statement.close();
        connection.close();
    }
}
