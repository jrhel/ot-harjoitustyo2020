/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.domain;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalTime;

/**
 *
 * @author J
 */
public class AggregatedRunData {    
    private List<Run> runs;
    private int totalRuns;
    private double combinedDistanceKm;
    private double avgDistanceKm;
    private LocalTime combinedDuration;
    private LocalTime avgDuration;
    private double avgSpeedKmH;
    private int avgCadence;
    private Run pb;
    
    public AggregatedRunData(List<Run> runs) {
        this.runs = runs;
        this.totalRuns = runs.size();
        this.combinedDistanceKm = 0.0;
        this.avgDistanceKm = 0.0;
        this.combinedDuration = new LocalTime(0, 0, 0);
        this.avgDuration = new LocalTime(0, 0, 0);
        this.avgSpeedKmH = 0.0;
        this.avgCadence = 0;  
        this.pb = new Run();
        initiate();
    }
    
    private void initiate() {   
        double combinedAvgSpeedKmH = 0.0;
        double combinedCadence = 0.0;
        pb.setAvgSpeedKmH(0.0);
        for (Run run: runs) {
            combinedDistanceKm = combinedDistanceKm + run.getDistanceKm();
            combinedAvgSpeedKmH = combinedAvgSpeedKmH + run.getAvgSpeedKmH();
            growCombinedLocalTime(run);
            combinedCadence = combinedCadence + run.getAvgCadence();
            if (run.getAvgSpeedKmH() > pb.getAvgSpeedKmH()) {
                pb = run;
            }
        }    
        this.avgDistanceKm = combinedDistanceKm / this.totalRuns;
        this.avgSpeedKmH = combinedAvgSpeedKmH / this.totalRuns;
        this.avgCadence = (int) combinedCadence / this.totalRuns;
        this.avgDuration = setAvgTime();
    }
    
    private void growCombinedLocalTime(Run run) {
        combinedDuration = combinedDuration.plusHours(run.getDuration().getHourOfDay());
        combinedDuration = combinedDuration.plusMinutes(run.getDuration().getMinuteOfHour());
        combinedDuration = combinedDuration.plusSeconds(run.getDuration().getSecondOfMinute());
    }
    
    public LocalTime setAvgTime() {
        int hours = combinedDuration.getHourOfDay();
        int avgHours = hours / totalRuns;
        int excessHours = hours % totalRuns;
        int minutes = combinedDuration.getMinuteOfHour() + (excessHours * 60);
        int avgMinutes = minutes / totalRuns;
        int excessMinutes = minutes % totalRuns;
        int seconds = combinedDuration.getSecondOfMinute() + (excessMinutes * 60);
        int avgSeconds = seconds / totalRuns;
        int excessSeconds = seconds % totalRuns;
        int milliseconds = combinedDuration.getMillisOfSecond() + (excessSeconds * 1000);
        int avgMilliseconds = milliseconds / totalRuns;
        
        LocalTime avgTime = new LocalTime(avgHours, avgMinutes, avgSeconds, avgMilliseconds);
        
        return avgTime;
    }

    public int getAvgCadence() {
        return avgCadence;
    }

    public double getAvgDistanceKm() {
        return avgDistanceKm;
    }

    public LocalTime getAvgDuration() {
        return avgDuration;
    }

    public double getAvgSpeedKmH() {
        return avgSpeedKmH;
    }  

    public double getCombinedDistanceKm() {
        return combinedDistanceKm;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public List<Run> getRuns() {
        return runs;
    } 

    public Run getPb() {
        return pb;
    }

    public void setCombinedDistanceKm(double combinedDistanceKm) {
        this.combinedDistanceKm = combinedDistanceKm;
    }

    public void setCombinedDuration(LocalTime combinedDuration) {
        this.combinedDuration = combinedDuration;
    }

    public LocalTime getCombinedDuration() {
        return combinedDuration;
    }
    
    
}
