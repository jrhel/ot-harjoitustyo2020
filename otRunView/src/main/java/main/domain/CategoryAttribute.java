
package main.domain;

/**
 * CategoryAttribute is an object representing attributes belonging to a specific user generated category * 
 */
public class CategoryAttribute {    
    private int id;
    private String attribute;
    private int categoryId;
    
    /**
     * Constructor of initiating a CategoryAttribute object
     *
     * @param   id   As integer functioning as a unique identifier for the attribute. 
     * @param   attribute   A string representing the attribute, or its value, as defined by the user
     * @param   categoryId   The unique identifier for the category the attribute belongs to
     */ 
    public CategoryAttribute(int id, String attribute, int categoryId) {
        this.id = id;
        this.attribute = attribute;
        this.categoryId = categoryId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public int getId() {
        return id;
    }

    public String getAttribute() {
        return attribute;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
