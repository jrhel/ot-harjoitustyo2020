/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import main.dao.CategoryAttributeDAO;
import main.dao.CategoryDAO;
import main.dao.RunDAO;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author J
 */
public class Logic {
    
    private RunDAO runDao;
    private CategoryDAO catDao;
    private CategoryAttributeDAO catAttributeDao;

    public Logic() {
        this.runDao = new RunDAO();
        this.catDao = new CategoryDAO();
        this.catAttributeDao = new CategoryAttributeDAO();
        this.ensureDataBaseExists();
    }
    
    public void saveCategory(String category, List<String> attributes, String parent) {
        Category newCategory = new Category(category);
        newCategory.setParentName(parent);
        catDao.create(newCategory);
        int categoryPK = catDao.getPrimaryKey(category);
        for (String attribute: attributes) {
            CategoryAttribute newAttribute = new CategoryAttribute(-1, attribute, categoryPK);
            catAttributeDao.create(newAttribute);
        }
        
    }
    
    public String readCategory(String name) {
        int primaryKey = catDao.getPrimaryKey(name);
        Category category = catDao.read(primaryKey);
        category.setAttributes(catAttributeDao.list(primaryKey));
        category.setParentName(catDao.read(catDao.getPrimaryKey(category.getParentName())).getName());
        return category.toString();
    }
    
    public void saveRun(double distanceKm, String date, String duration, int avgCadence, String gpxFilePath, List<String> runCategories) {
        List<Category> categories = new ArrayList<>();
        for (String category: runCategories) {
            int primaryKey = catDao.getPrimaryKey(category);
            Category runCategory = catDao.read(primaryKey);
            categories.add(runCategory);
        }
        Run newRun = new Run(distanceKm, getLocalDateObject(date), getLocalTimeObject(duration), avgCadence, gpxFilePath, categories);
        runDao.create(newRun);
    }
    
    public String readRun(int id) {
        Run run = runDao.read(id);
        return run.toString();
    }
    
    public void resetDatabase() {           
        catDao.resetTable();;
        catAttributeDao.resetTable();
        runDao.resetTable();        
    }
    
    public void ensureDataBaseExists() {
        catDao.ensureTableExists();
        catAttributeDao.ensureTableExists();
    }
    
    public LocalDate getLocalDateObject (String date) {        
        String [] timeUnits = date.split("-");
        int year = Integer.valueOf(timeUnits[0]);
        int month = Integer.valueOf(timeUnits[1]);
        int day = Integer.valueOf(timeUnits[2]);
        LocalDate dateObject = new LocalDate(year, month, day);
        return dateObject;
    }
    
    public LocalTime getLocalTimeObject (String time) { 
        String [] timeUnits = time.split(":");
        int hours = Integer.valueOf(timeUnits[0]);
        int minutes = Integer.valueOf(timeUnits[1]);
        int secs = Integer.valueOf(timeUnits[2]);        
        LocalTime timeObject = new LocalTime(hours, minutes, secs);
        return timeObject;
    }
}
