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
    private int id;
    private double distanceKm;
    private LocalDate date;
    private LocalTime duration;
    private double avgSpeedKmH;
    private int avgCadence;
    private String gpxFilePath;
    private List<Category> categories;

    public Run(int id, double distanceKm, LocalDate date, String duration, double avgSpeedKmH, int avgCadence, String gpxFilePath) {
        
        this.id = id;
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
    
    public Run(double distanceKm, LocalDate date, LocalTime duration, int steps, String gpxFilePath, List<Category> categories) {        
        this.distanceKm = distanceKm;
        this.date = date;
        this.duration = duration;
        this.avgCadence = calculateAvgCadence(steps, this.duration);
        this.gpxFilePath = gpxFilePath;
        this.categories = categories;
        this.avgSpeedKmH = calculateAvgSpeedKmH(distanceKm, duration);
    }

    public int getId() {
        return id;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public LocalDate getDate() {
        return date;
    }
    
    public String getDateAsText() {
        int day = date.getDayOfMonth();
        String month = date.monthOfYear().getAsShortText();
        int year = date.getYear();
        String date = month + " " + day + " " + year;
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
    
    public String printCategories() {
        String categoryNames = "";
        for (Category category: categories) {
            categoryNames = categoryNames + ", " + category.getName();
        }
        return categoryNames;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public void setAvgCadence(int avgCadence) {
        this.avgCadence = avgCadence;
    }

    public void setAvgSpeedKmH(double avgSpeedKmH) {
        this.avgSpeedKmH = avgSpeedKmH;
    }

    public void setGpxFilePath(String gpxFilePath) {
        this.gpxFilePath = gpxFilePath;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    
    public double calculateAvgSpeedKmH(double distanceKm, LocalTime time) {
        double hourSeconds = time.getSecondOfMinute() / 3600.00;
        double hourMinutes = time.getMinuteOfHour() / 60.00;
        double hourTime = 1.00 * (time.getHourOfDay() + hourMinutes + hourSeconds);
        
        return distanceKm / hourTime;
    }
    
    public int calculateAvgCadence(int steps, LocalTime time) {
        double minuteTime = (time.getHourOfDay() * 60) + time.getMinuteOfHour() + time.getSecondOfMinute() / 60;
        int cadence = (int) (steps / minuteTime);
        
        return cadence;
    }
    
    public LocalTime durationConversion(String duration) {
        String [] timeUnits = duration.split(":");
        int hours = Integer.valueOf(timeUnits[0]);
        int minutes = Integer.valueOf(timeUnits[1]);
        int seconds = Integer.valueOf(timeUnits[2]);  
        
        LocalTime timeObject = new LocalTime(hours, minutes, seconds);
        
        return timeObject;
    }
}