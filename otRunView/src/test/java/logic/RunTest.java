/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.List;
import main.domain.Category;
import main.domain.Run;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author J
 */
public class RunTest {
    
    @Test public void canCalculateAvgSpeed() {
        double distanceKm = 21.0;
        LocalTime time = new LocalTime(1, 40, 40);
        LocalDate date = new LocalDate(2018-11-3);
        int avgCadence = 154;
        String gpx = "";
        List<Category> categories = new ArrayList<>();        
        Run run =  new Run(distanceKm, date, time, avgCadence, gpx, categories);
        double hours = ((40.0 / 3600) + (40.0 / 60) + 1);
        double expectedResult = distanceKm / hours;
        assertEquals(expectedResult, run.calculateAvgSpeedKmH(distanceKm, time), 0.01);
    }    
    
    @Test
    public void durationCanBeConvertedFromStringToLocalTimeObject() {
        String duration = "0:23:54";
        String [] timeUnits = duration.split(":");
        int hours = Integer.valueOf(timeUnits[0]);
        int minutes = Integer.valueOf(timeUnits[1]);
        int seconds = Integer.valueOf(timeUnits[2]);
        LocalTime timeObject = new LocalTime(hours, minutes, seconds);
        
        assertEquals("00:23:54.000", timeObject.toString());
    }
}
