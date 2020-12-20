/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import main.dao.CategoryDAO;
import main.domain.Category;
import main.domain.Logic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author J
 */
public class CategoryDAOTest {
    
    @Test
    public void categoryCanBeSavedToDB() {
        CategoryDAO catDao = new CategoryDAO("TestCategory");
        catDao.resetTable();
        
        Category category = new Category("newCategory");
        assertEquals(1, catDao.create(category).longValue());
    }
    
    @Test
    public void categoryPrimaryKeyCanBeObtained() {
        CategoryDAO catDao = new CategoryDAO("TestCAtegory");
        catDao.resetTable();
        
        Category category = new Category("category");
        catDao.create(category);
        
        int categoryPK = catDao.getPrimaryKey("category");
        assertEquals(1, categoryPK);
    }
    
    @Test
    public void categoryCanBeReadFromDB() {
        CategoryDAO catDao = new CategoryDAO("TestCategory");
        catDao.resetTable();
        
        Category category = new Category("category");
        catDao.create(category);
        
        int categoryPK = catDao.getPrimaryKey("category");
        Category read = catDao.read(categoryPK);
        
        assertEquals("1category", read.getId() + read.getName());
    }       
}
