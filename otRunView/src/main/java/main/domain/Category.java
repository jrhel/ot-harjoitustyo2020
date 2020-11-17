/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.domain;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author J
 */
public class Category {
    
    private Integer id;
    private String name;
    private Category parent;
    // private Map<String, String> attributes;
    
    public Category(String name, Category parent) {
        this.id = -1;
        this.name = name;
        this.parent = parent;
    }

    public Category(int id, String name, Category parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }    
    /*   
    public Category(String name, HashMap<String, String> attributes) {
        this.name = name;
        this.id = -1;
        this.parent = null;
        this.attributes = attributes;
    }
    
    public Category(int id, String name, Category parent, HashMap<String, String> attributes) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.attributes = attributes;
    }
    */
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
    /*
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
    */

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getParent() {
        return parent;
    }

    @Override
    public String toString() {
        String a_id = "id: " + this.id;
        String b_cat = ", category: " + this.name;
        String c_parent = ", parent: " + this.parent.getName();
        return a_id + b_cat + c_parent;
        // return "id: " + this.id + ", category: " + this.name + ", parent: " + this.parent.getName();
    }
    
    
    
    
    
}
