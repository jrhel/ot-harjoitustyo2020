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
    private String runTableName;
    private CategoryDAO catDao;
    private String categoryTableName;
    private CategoryAttributeDAO catAttributeDao;
    private String categoryAttributeTableName;
    private RunCategoryDAO runCatDao;
    private String runCategoryTableName;

    public Logic() {
        this.runDao = new RunDAO("Run");
        this.catDao = new CategoryDAO("Category");
        this.catAttributeDao = new CategoryAttributeDAO("CategoryAttribute", "Category");
        this.runCatDao = new RunCategoryDAO("RunCategory", "Run", "Category");
        this.ensureDataBaseExists();
    }
    
    public Logic(String runTableName, String categoryTableName, String runCategoryTableName, String categoryAttributeTableName) {
        this.runDao = new RunDAO(runTableName);
        this.catDao = new CategoryDAO(categoryTableName);
        this.catAttributeDao = new CategoryAttributeDAO(categoryAttributeTableName, categoryTableName);
        this.runCatDao = new RunCategoryDAO(runCategoryTableName, runTableName, categoryTableName);
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
        return category.toString();
    }
    
    public int saveRun(double distanceKm, String date, String duration, int steps, String gpxFilePath, List<String> runCategories) {
        List<Category> categories = new ArrayList<>();
        for (String category: runCategories) {
            int categoryID = catDao.getPrimaryKey(category);
            Category runCategory = catDao.read(categoryID);
            categories.add(runCategory);
        }
        Run newRun = new Run(distanceKm, getLocalDateObject(date), getLocalTimeObject(duration), steps, gpxFilePath, categories);
        int runID = runDao.create(newRun);
                
        for (Category runCategory: newRun.getCategories()) {
            int categoryID = catDao.getPrimaryKey(runCategory.getName());
            runCatDao.create(runID, categoryID);
        }        
        
        return runID;
    }
    
    public String readRun(int id) {
        Run run = runDao.read(id);
        
        String date = run.getDateAsText();
        String distance = run.getDistanceKm() + " Km, ";
        
        for (int categoryID: runCatDao.listCategoryIDs(run.getID())) {
            Category runCategory = catDao.read(categoryID);
            run.getCategories().add(runCategory);
        }

        String runCategories = "";
        for (Category category: run.getCategories()) {
            runCategories = runCategories + ", " + category.getName();
        }
        
        String runInfo = date + ", " + distance + run.getDuration() + runCategories;
        return runInfo;
    }
    
    public boolean resetDatabase() {  
        boolean result = true;
        if (!(catDao.resetTable())) {
            result = false;
        }
        if (!(catAttributeDao.resetTable())) {
            result = false;
        }
        if (!(runDao.resetTable())) {
            result = false;
        }  
        if (!(runCatDao.resetTable())) {
            result = false;
        }
        return result;
    }
    
    public boolean ensureDataBaseExists() {
        boolean result = true;
        if (!(catDao.ensureTableExists())) {
            result = false;
        }
        if (!(catAttributeDao.ensureTableExists())) {
            result = false;
        }
        if (!(runDao.ensureTableExists())) {
            result = false;
        }
        if (!(runCatDao.ensureTableExists())) {
            result = false;
        }
        return result;
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
    
    public List<Category> listCategories() {
        List<Category> categories = catDao.list();
        for (Category category: categories) {
            category.setAttributes(catAttributeDao.list(category.getId()));
        }
        return categories;
    }
}
