/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import main.dao.RunCategoryDAO;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author J
 */
public class RunCategoryDAOTest {
    
    @Test
    public void tableExists() {
        RunCategoryDAO runCatDao = new RunCategoryDAO("TestRunCategory", "TestRun", "TestCategory");
        runCatDao.resetTable();
        assertTrue(runCatDao.ensureTableExists());
    }
    
    @Test
    public void tabelCanBeReset() {
        RunCategoryDAO runCatDao = new RunCategoryDAO("TestRunCategory", "TestRun", "TestCategory");
        assertTrue(runCatDao.resetTable());
    }
    
}
