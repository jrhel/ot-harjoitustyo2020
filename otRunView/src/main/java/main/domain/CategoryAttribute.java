/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.domain;

/**
 *
 * @author J
 */
public class CategoryAttribute {
    
    private int id;
    private String attribute;
    private int categoryID;

    public CategoryAttribute(int id, String attribute, int categoryID) {
        this.id = id;
        this.attribute = attribute;
        this.categoryID = categoryID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
    
    public int getId() {
        return id;
    }

    public String getAttribute() {
        return attribute;
    }

    public int getCategoryID() {
        return categoryID;
    }

    @Override
    public String toString() {
        return attribute;
    }
    
    
}
