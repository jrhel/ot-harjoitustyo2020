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
        CategoryDAO catDao = new CategoryDAO();
        catDao.resetTable();
        
        Category parent = new Category("parent", null);
        Category category = new Category("childCategory", parent);
        assertTrue(catDao.create(category));
    }
    
    @Test
    public void categoryCanBeReadFromDB() {
        CategoryDAO catDao = new CategoryDAO();
        catDao.resetTable();
        
        int parentPK = catDao.getPrimaryKey("parent");
        Category parentCategory = catDao.read(parentPK);
        Category child = new Category("child", parentCategory);
        catDao.create(child);
        
        int childPK = catDao.getPrimaryKey("child");
        Category newParent = catDao.read(childPK);
        Category grandChild = new Category("Fenix", newParent);
        catDao.create(grandChild);
        assertEquals("id: 2, category: Fenix, parent: child", catDao.read(2).toString());
    }
}
