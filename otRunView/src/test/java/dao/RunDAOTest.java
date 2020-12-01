/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import main.dao.RunDAO;
import main.domain.Category;
import main.domain.CategoryAttribute;
import main.domain.Run;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author J
 */
public class RunDAOTest {
    
    @Test
    public void runCanBeSavedToDB() {
        RunDAO runDao = new RunDAO();
        runDao.resetTable();
        
        LocalDate date = new LocalDate(2016, 2, 29);
        LocalTime duration = new LocalTime(0, 55, 34, 23);
        List<Category> categories = new ArrayList<>();
        CategoryAttribute catAtt = new CategoryAttribute(1, "56km", 1);
        List<CategoryAttribute> catAtts = new ArrayList<>();
        catAtts.add(catAtt);
        Category category = new Category(1, "category", catAtts, "");
        categories.add(category);
        
        Run testRun = new Run(5.5, date, duration, 173, "", categories);
        assertEquals(1, runDao.create(testRun));
    }
    
    public void runCanBeReadFromDB() {
        RunDAO runDao = new RunDAO();
        runDao.resetTable();
        
        LocalDate date = new LocalDate(2020, 10, 15);
        LocalTime duration = new LocalTime(1, 57, 33, 10);
        List<Category> categories = new ArrayList<>();
        CategoryAttribute catAtt = new CategoryAttribute(1, "56km", 1);
        List<CategoryAttribute> catAtts = new ArrayList<>();
        catAtts.add(catAtt);
        Category category = new Category(1, "category", catAtts, "");
        categories.add(category);        
        Run testRun = new Run(10.7, date, duration, 166, ".gpx", categories);
        Run readRun = runDao.read(1);
        boolean readSuccess = true;
        if (!((readRun.getAvgCadence() == 166) && (readRun.getDate().equals(date)) && (readRun.getDistanceKm() == 10.7) && (readRun.getGpxFilePath().equals(".gpx")) && (readRun.getDuration().equals(duration)))) {
            readSuccess = false;
        }        
        assertTrue(readSuccess);
    }
}
