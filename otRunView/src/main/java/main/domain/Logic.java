/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.domain;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import main.dao.CategoryAttributeDAO;
import main.dao.CategoryDAO;
import main.dao.RunCategoryDAO;
import main.dao.RunDAO;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.jxmapviewer.viewer.GeoPosition;

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
        int categoryId = catDao.create(newCategory);
        for (String attribute: attributes) {
            CategoryAttribute newAttribute = new CategoryAttribute(-1, attribute, categoryId);
            catAttributeDao.create(newAttribute);
        }
        
    }
    
    public Category readCategory(int id) {        
        Category category = catDao.read(id);
        category.setAttributes(catAttributeDao.list(id));
        return category;
    }
    
    public int saveRun(double distanceKm, String date, String duration, int steps, String gpxFilePath, List<String> runCategories) {        
        List<Category> categories = new ArrayList<>();
        for (String category: runCategories) {
            int categoryId = catDao.getPrimaryKey(category);
            Category runCategory = catDao.read(categoryId);
            categories.add(runCategory);
        }
        
        Run newRun = new Run(distanceKm, getLocalDateObject(date), getLocalTimeObject(duration), steps, gpxFilePath, categories);
        int runId = runDao.create(newRun);
                
        for (Category runCategory: newRun.getCategories()) {
            int categoryId = catDao.getPrimaryKey(runCategory.getName());
            runCatDao.create(runId, categoryId);
        }
        return runId;
    }
    
    public Run readRun(int id) {        
        Run run = runDao.read(id);
        
        for (int categoryId: runCatDao.listCategoryIds(run.getId())) {
            Category runCategory = readCategory(categoryId);
            run.getCategories().add(runCategory);
        }
        return run;
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
            for (int categoryId: runCatDao.listCategoryIds(run.getId())) {
                Category runCategory = catDao.read(categoryId);
                runCategories.add(runCategory);
            }
            
            run.setCategories(runCategories);            
        }
        return allRuns;
    }
    
    public boolean deleteRun(Run run) {        
        boolean result = false;
        
        runCatDao.delete(run.getId());
        runDao.delete(run.getId());
        
        return result;
    }
    
    public List<Category> listCategories() {        
        List<Category> categories = catDao.list();
        for (Category category: categories) {
            category.setAttributes(catAttributeDao.list(category.getId()));
        }        
        return categories;
    }
    
    public List<String> readGpxFile(String filePath) {        
        List<String> lines = new ArrayList<>();
        
        try (Scanner fileScanner = new Scanner(Paths.get(filePath))) {

            while (fileScanner.hasNextLine()) {
                lines.add(fileScanner.nextLine());
            }
            
        } catch (Exception e) {
            
        }
        return lines;
    }
    
    public List<GeoPosition> readCoordinates(List<String> lines) {        
        List<String> uniqueCoordinates = new ArrayList<>();
        List<GeoPosition> coordinates = new ArrayList<>();
        
        for (String line: lines) {
            if (line.contains("lat=")) {
                if (!uniqueCoordinates.contains(line)) {
                    uniqueCoordinates.add(line);
                    String trimmedLine = line.trim();
                    String [] lineCoordinates = trimmedLine.split(" ");
                    String lat = lineCoordinates[1].replace("lat=", "");
                    lat = lat.replace("\"", "");
                    String lon = lineCoordinates[2].replace("lon=\"", "");
                    lon = lon.replace("\">", "");
                    GeoPosition linePosition = new GeoPosition(Double.valueOf(lat), Double.valueOf(lon));
                    coordinates.add(linePosition);
                }
            }            
        }        
        return coordinates;
    }
    
    public GeoPosition getMapCenter(List<List<GeoPosition>> positionLists) {        
        double maxLat = -90.0;
        double minLat = 90.0;
        double maxLon = -90.0;
        double minLon = 90.0;
        
        for (List<GeoPosition> coordinates: positionLists) {
            for (GeoPosition gp: coordinates) {
                double lat = gp.getLatitude();
                double lon = gp.getLongitude();
                maxLat = getLargerNr(maxLat, lat);               
                minLat = getSmallerNr(minLat, lat);
                maxLon = getLargerNr(maxLon, lon);
                minLon = getSmallerNr(minLon, lon);
            }
        }
        
        return setCenterPosition(maxLat, minLat, maxLon, minLon);
    }
    
    private double getLargerNr(double a, double b) {
        if (b > a) {
            return b;
        } else {
            return a;
        }
    }
    
    private double getSmallerNr(double a, double b) {
        if (b < a) {
            return b;
        } else {
            return a;
        }
    }
    
    private GeoPosition setCenterPosition(double maxLat, double minLat, double maxLon, double minLon) {
        double centralLat = (maxLat + minLat) / 2;
        double centralLon = (maxLon + minLon) / 2;
        GeoPosition center = new GeoPosition(centralLat, centralLon);
        
        return center;
    }   
    
    public List<Run> listSelectedRuns(List<Category> selectedCategories) {        
        List<Run> runs = new ArrayList<>();
        
        List<Integer> categoryIds = new ArrayList<>();
        for (Category category: selectedCategories) {
            categoryIds.add(category.getId());
        }
        
        List<Integer> runIds = runCatDao.listRunIds(categoryIds);
        for (Integer id: runIds) {
            Run run = runDao.read(id);
            runs.add(run);
        }        
        return runs;        
    }
    
    public AggregatedRunData getAggregatedRunData(List<Category> selectedCategories) {       
        List<Run> runs = new ArrayList<>();
        List<Integer> runIds = getRunIds(selectedCategories);     
        for (Integer id: runIds) {
            runs.add(readRun(id));
        }        
        AggregatedRunData aggregatedData = new AggregatedRunData(runs);  
        
        return aggregatedData;
    }
    
    public boolean checkIfNumbers(List<String> numbers) {
        boolean areNumbers = true;
        for (String nr: numbers) {
            try {
                Integer i = Integer.valueOf(nr);
            } catch (Exception e) {
                areNumbers = false;
            }
        }
        
        return areNumbers;
    }
    
    private  List<Integer> getRunIds(List<Category> selectedCategories) {
        List<Integer> categoryIds = new ArrayList<>();
        for (Category category: selectedCategories) {
            categoryIds.add(category.getId());
        }        
        List<Integer> runIds = runCatDao.listRunIds(categoryIds);
        
        return runIds;
    }
}
