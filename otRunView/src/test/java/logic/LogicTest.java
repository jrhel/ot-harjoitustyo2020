/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.List;
import main.domain.Category;
import main.domain.Logic;
import main.domain.Run;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author J
 */
public class LogicTest {
    
    @Test
    public void parentCategoryCanBeSaved() {        
        Logic logic = new Logic();
        logic.resetDatabase();
        
        List<String> attributes = new ArrayList<>();
        attributes.add("5k");
        attributes.add("trail");
        logic.saveCategory("category", attributes, "");
        
        assertEquals("id: 1, name: category, parent: , attributes:\n5k\ntrail\n", logic.readCategory("category"));
        
    }
    
    @Test
    public void childCategoryCanBeSaved() {
        
        Logic logic = new Logic();
        logic.resetDatabase();
        
        List<String> parentAttributes = new ArrayList<>();
        logic.saveCategory("parent", parentAttributes, "");
        List<String> attributes = new ArrayList<>();
        attributes.add("sunny");
        logic.saveCategory("child", attributes, "parent");
        assertEquals("id: 2, name: child, parent: parent, attributes:\nsunny\n", logic.readCategory("child"));
    }
    
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
}
