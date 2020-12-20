/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayList;
import java.util.List;
import main.dao.CategoryDAO;
import main.dao.RunDAO;
import main.domain.Category;
import main.domain.CategoryAttribute;
import main.domain.Logic;
import main.domain.Run;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author J
 */
public class LogicTest {
    
    @Test
    public void parentCategoryCanBeSaved() {        
        Logic logic = new Logic("TestRun", "TestCategory", "TestRunCategory", "TestCategoryAttribute");
        logic.resetDatabase();
        
        List<String> attributes = new ArrayList<>();
        attributes.add("5k");
        attributes.add("trail");
        logic.saveCategory("category", attributes, "");
        
        Category readCategory = logic.readCategory(1);
        String readAttributes = ", attributes:";
        for (CategoryAttribute attribute: readCategory.getAttributes()) {
            readAttributes = readAttributes + "\n" + attribute.getAttribute();
        }
        assertEquals("id: 1, name: category, parent: , attributes:\n5k\ntrail", "id: " + readCategory.getId() + ", name: " + readCategory.getName() + ", parent: " + readCategory.getParentName() + readAttributes);        
    }
    
    @Test
    public void childCategoryCanBeSaved() {        
        Logic logic = new Logic("TestRun", "TestCategory", "TestRunCategory", "TestCategoryAttribute");
        logic.resetDatabase();
        
        List<String> parentAttributes = new ArrayList<>();
        logic.saveCategory("parent", parentAttributes, "");
        List<String> attributes = new ArrayList<>();
        attributes.add("sunny");
        logic.saveCategory("child", attributes, "parent");
        
        Category readCategory = logic.readCategory(2);
        String readAttributes = ", attributes:";
        for (CategoryAttribute attribute: readCategory.getAttributes()) {
            readAttributes = readAttributes + "\n" + attribute.getAttribute();
        }
        
        assertEquals("id: 2, name: child, parent: parent, attributes:\nsunny", "id: " + readCategory.getId() + ", name: " + readCategory.getName() + ", parent: " + readCategory.getParentName() + readAttributes);
    }
    
    @Test
    public void categoryCanBeRead() {
        Logic logic = new Logic("TestRun", "TestCategory", "TestRunCategory", "TestCategoryAttribute");
        logic.resetDatabase();
        
        CategoryDAO catDao = new CategoryDAO("TestCategory");
        Category category = new Category(1, "child", "parent");
        catDao.create(category);
        
        Category readCategory = logic.readCategory(1);
        
        assertEquals("id: " + category.getId() + ", name: " + category.getName() + ", parent: " + category.getParentName(), "id: " + readCategory.getId() + ", name: " + readCategory.getName() + ", parent: " + readCategory.getParentName());
    }
    
    @Test
    public void runCanBeSaved() {
        Logic logic = new Logic("TestRun", "TestCategory", "TestRunCategory", "TestCategoryAttribute");
        logic.resetDatabase();
        double distanceKm = 21.0;
        String date = "2019-11-5";
        LocalDate localDate = new LocalDate(2019, 11, 5);
        String duration = "1:58:00";
        LocalTime localTime = new LocalTime(1, 58, 0);
        int steps = 19278;
        String gpxFilePath = "-";
        String category = "half marathon";
        List<String> attributes = new ArrayList<>();
        logic.saveCategory(category, attributes, "");
        List<String> categories = new ArrayList<>();
        categories.add(category);
        logic.saveRun(distanceKm, date, duration, steps, gpxFilePath, categories);
        RunDAO runDao = new RunDAO("TestRun");
        Run readRun = runDao.read(1);
        int cadence = 19278 / 118;
        boolean readSuccess = true;
        if (!((readRun.getAvgCadence() == cadence) && (readRun.getDate().equals(localDate)) && (readRun.getDistanceKm() == 21.0) && (readRun.getGpxFilePath().equals("-")) && (readRun.getDuration().equals(localTime)))) {
            readSuccess = false;
        }        
        assertTrue(readSuccess);
    }
    
    @Test
    public void runCanBeRead() {
        Logic logic = new Logic("TestRun", "TestCategory", "TestRunCategory", "TestCategoryAttribute");
        logic.resetDatabase();
        double distanceKm = 21.0;
        String date = "2018-12-10";
        LocalDate localDate = new LocalDate(2018, 12, 10);
        String duration = "01:30:00";
        LocalTime localTime = new LocalTime(1, 30, 0);
        int steps = 13300;
        String gpxFilePath = "-";
        String category = "long run";
        List<String> attributes = new ArrayList<>();
        logic.saveCategory(category, attributes, "");
        List<String> categories = new ArrayList<>();
        categories.add(category);
        int savedRunId = logic.saveRun(distanceKm, date, duration, steps, gpxFilePath, categories);
        Run readRun = logic.readRun(savedRunId);
        String readRunString = readRun.getDateAsText() + ", " + readRun.getDistanceKm() + " Km, " + readRun.getDuration() + ", long run";
        assertEquals("Dec 10 2018" + ", " + distanceKm + " Km, " + duration + ".000, long run", readRunString);
    }
    
    @Test
    public void databaseExists() {
        Logic logic = new Logic("TestRun", "TestCategory", "TestRunCategory", "TestCategoryAttribute");
        assertTrue(logic.ensureDataBaseExists());
    }
    
    @Test
    public void databaseCanBeReset() {
        Logic logic = new Logic("TestRun", "TestCategory", "TestRunCategory", "TestCategoryAttribute");
        assertTrue(logic.resetDatabase());
    }
    
    @Test
    public void localDateObjectCanBeObtained() {
        Logic logic =  new Logic("TestRun", "TestCategory", "TestRunCategory", "TestCategoryAttribute");
        logic.resetDatabase();
        String date = "2020-11-23";
        assertEquals(date, logic.getLocalDateObject(date).toString());
    }
    
    @Test
    public void localTimeObjectCanBeObtained() {
        Logic logic =  new Logic("TestRun", "TestCategory", "TestRunCategory", "TestCategoryAttribute");
        logic.resetDatabase();
        String time = "12:59:23";
        assertEquals(time + ".000", logic.getLocalTimeObject(time).toString());
    }
    
    
}
