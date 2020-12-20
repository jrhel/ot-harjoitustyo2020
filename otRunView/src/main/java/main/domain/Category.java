/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author J
 */
public class Category {    
    private Integer id;
    private String name;
    private List<CategoryAttribute> attributes;
    private String parentName;

    public Category(String name) {
        this.id = -1;
        this.name = name;
        this.attributes = new ArrayList<>();
        this.parentName = "";
    }   
    
    public Category(Integer id, String name, String parentName) {
        this.id = id;
        this.name = name;
        this.attributes = new ArrayList<>();
        this.parentName = parentName;
    }    

    public Category(Integer id, String name, List<CategoryAttribute> attributes, String parentName) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
        this.parentName = parentName;
    }    
        
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setAttributes(List<CategoryAttribute> attributes) {
        this.attributes = attributes;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }    

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CategoryAttribute> getAttributes() {
        return attributes;
    }

    public String getParentName() {
        return parentName;
    }
}
