/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.List;
import main.dao.CategoryDAO;
import main.domain.Category;
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
    
    @Test
    public void categoryCanBeRead() {
        Logic logic = new Logic();
        logic.resetDatabase();
        CategoryDAO catDao = new CategoryDAO();
        Category category = new Category(1, "child", "parent");
        catDao.create(category);
        assertEquals(category.toString(), logic.readCategory("child"));
    }
    
    @Test
    public void databaseExists() {
        Logic logic = new Logic();
        assertTrue(logic.ensureDataBaseExists());
    }
    
    @Test
    public void databaseCanBeReset() {
        Logic logic = new Logic();
        assertTrue(logic.resetDatabase());
    }
    
    @Test
    public void localDateObjectCanBeObtained() {
        Logic logic =  new Logic();
        String date = "2020-11-23";
        assertEquals(date, logic.getLocalDateObject(date).toString());
    }
    
    @Test
    public void localTimeObjectCanBeObtained() {
        Logic logic =  new Logic();
        String time = "12:59:23";
        assertEquals(time + ".000", logic.getLocalTimeObject(time).toString());
    }
}
