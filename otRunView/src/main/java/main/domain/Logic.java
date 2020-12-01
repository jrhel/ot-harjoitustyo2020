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
import main.dao.RunCategoryDAO;
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
    private RunCategoryDAO runCatDao;

    public Logic() {
        this.runDao = new RunDAO();
        this.catDao = new CategoryDAO();
        this.catAttributeDao = new CategoryAttributeDAO();
        this.runCatDao = new RunCategoryDAO();
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
    
    public int saveRun(double distanceKm, String date, String duration, int avgCadence, String gpxFilePath, List<String> runCategories) {
        List<Category> categories = new ArrayList<>();
        for (String category: runCategories) {
            int categoryID = catDao.getPrimaryKey(category);
            Category runCategory = catDao.read(categoryID);
            categories.add(runCategory);
        }
        Run newRun = new Run(distanceKm, getLocalDateObject(date), getLocalTimeObject(duration), avgCadence, gpxFilePath, categories);
        int runID = runDao.create(newRun);
                
        for (Category runCategory: newRun.getCategories()) {
            int categoryID = catDao.getPrimaryKey(runCategory.getName());
            runCatDao.create(runID, categoryID);
        }        
        
        return runID;
    }
    
    public String readRun(int id) {
        Run run = runDao.read(id);
        return run.toString();
    }
    
    public void resetDatabase() {           
        catDao.resetTable();;
        catAttributeDao.resetTable();
        runDao.resetTable();       
        runCatDao.resetTable();
    }
    
    public void ensureDataBaseExists() {
        catDao.ensureTableExists();
        catAttributeDao.ensureTableExists();
        runDao.ensureTableExists();
        runCatDao.ensureTableExists();
    }
    
    public LocalDate getLocalDateObject(String date) {        
        String [] timeUnits = date.split("-");
        int year = Integer.valueOf(timeUnits[0]);
        int month = Integer.valueOf(timeUnits[1]);
        int day = Integer.valueOf(timeUnits[2]);
        LocalDate dateObject = new LocalDate(year, month, day);
        return dateObject;
    }
    
    public LocalTime getLocalTimeObject(String time) { 
        String [] timeUnits = time.split(":");
        int hours = Integer.valueOf(timeUnits[0]);
        int minutes = Integer.valueOf(timeUnits[1]);
        int secs = Integer.valueOf(timeUnits[2]);        
        LocalTime timeObject = new LocalTime(hours, minutes, secs);
        return timeObject;
    }
    
    public List<Run> listRuns() {
        
        List<Run> allRuns = runDao.list();
        for (Run run: allRuns) {
            
            List<Category> runCategories = new ArrayList<>();
            for (int categoryID: runCatDao.listCategoryIDs(run.getID())) {
                Category runCategory = catDao.read(categoryID);
                runCategories.add(runCategory);
            }
            
            run.setCategories(runCategories);            
        }
        
        return allRuns;
    }
    
    public boolean deleteRun(Run run) {
        
        boolean result = false;
        
        runCatDao.delete(run.getID());
        runDao.delete(run.getID());
        
        return result;
    }
}
