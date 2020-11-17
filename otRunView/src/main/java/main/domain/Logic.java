/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.domain;

import main.dao.CategoryDAO;
import main.dao.RunDAO;

/**
 *
 * @author J
 */
public class Logic {
    
    private RunDAO runDao;
    private CategoryDAO catDao;

    public Logic() {
        this.runDao = new RunDAO();
        this.catDao = new CategoryDAO();
    }
    
    public void saveCategory(String child, String parent) {
        int parentPK = catDao.getPrimaryKey(parent);
        Category parentCategory = catDao.read(parentPK);
        Category newCategory = new Category(child, parentCategory);
        catDao.create(newCategory);
    }
    
    public String readCategory(String name) {
        int primaryKey = catDao.getPrimaryKey(name);
        Category category = catDao.read(primaryKey);
        return category.toString();
    }
    
    public void resetDatabase() {
        catDao.resetTable();;
        runDao.resetTable();
    }
}
