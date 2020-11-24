/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.domain;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.*;

/**
 *
 * @author J
 */
public class Run {
    
    private double distanceKm;
    private LocalDate date;
    private LocalTime duration;
    private double avgSpeedKmH;
    private int avgCadence;
    private String gpxFilePath;
    private List<Category> categories;

    public Run(double distanceKm, LocalDate date, String duration, double avgSpeedKmH, int avgCadence, String gpxFilePath) {
        this.distanceKm = distanceKm;
        this.date = date;
        this.duration = durationConversion(duration);
        this.avgSpeedKmH = avgSpeedKmH;
        this.avgCadence = avgCadence;
        this.gpxFilePath = gpxFilePath;
        this.categories = categories;
        categories = new ArrayList<>();
    }   

    public Run() {
    }
    
    public Run(double distanceKm, LocalDate date, LocalTime duration, int avgCadence, String gpxFilePath, List<Category> categories) {
        this.distanceKm = distanceKm;
        this.date = date;
        this.duration = duration;
        this.avgCadence = avgCadence;
        this.gpxFilePath = gpxFilePath;
        this.categories = categories;
        this.avgSpeedKmH = calculateAvgSpeedKmH(distanceKm, duration);
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public double getAvgSpeedKmH() {
        return avgSpeedKmH;
    }

    public int getAvgCadence() {
        return avgCadence;
    }

    public String getGpxFilePath() {
        return gpxFilePath;
    }

    public List<Category> getCategories() {
        return categories;
    }
    
    public double calculateAvgSpeedKmH(double distanceKm, LocalTime time) {
        double hourSeconds = time.getSecondOfMinute() / 3600.00;
        double hourMinutes = time.getMinuteOfHour() / 60.00;
        double hourTime = 1.00 * (time.getHourOfDay() + hourMinutes + hourSeconds);
        
        return distanceKm / hourTime;
    }
    
    public LocalTime durationConversion(String duration) {
        String [] timeUnits = duration.split(":");
        int hours = Integer.valueOf(timeUnits[0]);
        int minutes = Integer.valueOf(timeUnits[1]);
        int seconds = Integer.valueOf(timeUnits[2]);
        
        LocalTime timeObject = new LocalTime(hours, minutes, seconds);
        return timeObject;
    }

    @Override
    public String toString() {
        String result = "";
        result = result + "\nDate: " + date;
        result = result + "\nDistance (Km): " + distanceKm;
        result = result + "\nTime: " + duration;
        result = result + "\nAverage speed (Km/h): " + avgSpeedKmH;
        result = result + "\nAverage cadence: " + avgCadence;
        result = result + "\n.gpx-file location: " + gpxFilePath;
        return result;
    }
    
    
}