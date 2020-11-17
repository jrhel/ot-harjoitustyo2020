/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import main.domain.Category;
import main.domain.Logic;
import static org.junit.Assert.assertEquals;
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
        
        logic.saveCategory("parent", "");
        assertEquals("id: 1, category: parent, parent: ", logic.readCategory("parent"));
    }
    
    @Test
    public void childCategoryCanBeSaved() {
        
        Logic logic = new Logic();
        logic.resetDatabase();
        
        logic.saveCategory("parent", "");
        logic.saveCategory("child", "parent");
        assertEquals("id: 2, category: child, parent: parent", logic.readCategory("child"));
    }
    
}
