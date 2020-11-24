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
    private int category_id;

    public CategoryAttribute(int id, String attribute, int category_id) {
        this.id = id;
        this.attribute = attribute;
        this.category_id = category_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
    
    public int getId() {
        return id;
    }

    public String getAttribute() {
        return attribute;
    }

    public int getCategory_id() {
        return category_id;
    }

    @Override
    public String toString() {
        return attribute;
    }
    
    
}
