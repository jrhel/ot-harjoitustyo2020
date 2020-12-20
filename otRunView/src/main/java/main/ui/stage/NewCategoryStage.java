/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.ui.stage;

import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.domain.Category;
import main.domain.CategoryAttribute;
import main.domain.Logic;

/**
 *
 * @author J
 */
public class NewCategoryStage {    
    
    public void newCategory(Logic logic, Stage stage, String name, List<String> attributes, String parent, MainStage mainStage) {
        stage.setTitle("Add a new category");        
        VBox categoryData = new VBox();
        
        FlowPane namePane = new FlowPane();
        Label nameLabel = new Label("Name: ");
        TextField nameInput = new TextField();
        nameInput.setText(name);
        Button saveCategory = getSaveCategoryButton(nameInput, attributes, parent, logic, stage, mainStage);
        namePane.getChildren().addAll(nameLabel, nameInput, saveCategory);
        
        HBox lowerDisplay = new HBox();
        lowerDisplay.getChildren().add(getAttributeComponent(logic, nameInput, attributes, stage, parent, mainStage));
        lowerDisplay.getChildren().add(getCategoryComponent(logic, nameInput, attributes, stage, parent, mainStage));
        
        categoryData.getChildren().addAll(namePane, lowerDisplay);
        
        Scene newCategoryScene = new Scene(categoryData);
        stage.setScene(newCategoryScene);       
        stage.show();
    }
    
    private Button getSaveCategoryButton(TextField nameInput, List<String> attributes, String parent, Logic logic, Stage stage, MainStage mainStage) {
        Button saveCategory = new Button("Save category");
        saveCategory.setOnAction((event) -> {
            if (nameInput.getText().equals("")) {
                MessageStage messageStage = new MessageStage("You didn't enter a name for your category. Please enter a valid name, in order to save your new category.");
            } else {
                logic.saveCategory(nameInput.getText(), attributes, parent);
                mainStage.updateScene(logic);
                stage.close();
            }
        });
        
        return saveCategory;
    }
    
    public VBox getAttributeComponent(Logic logic, TextField nameInput, List<String> attributes, Stage stage, String parent, MainStage mainStage) {
        VBox component = new VBox();
        VBox addedAttributes = updateAttributes(attributes);
        TextField attributeInput = new TextField();
        Button newAttribute = new Button("Add attribute: ");
        newAttribute.setOnAction((event) -> {
            attributes.add(attributeInput.getText());
            newCategory(logic, stage, nameInput.getText(), attributes, parent, mainStage);
        });
        
        component.getChildren().addAll(newAttribute, attributeInput, addedAttributes);
        
        return component;
    }
    
    public VBox updateAttributes(List<String> attributes) {
        VBox updatedAttributes = new VBox();
        for (String attribute: attributes) {
            Label attrubuteLabel = new Label(" - " + attribute);
            updatedAttributes.getChildren().add(attrubuteLabel);
        }
        return updatedAttributes;
    }    
    
    public VBox getCategoryComponent(Logic logic, TextField nameInput, List<String> attributes, Stage stage, String parent, MainStage mainStage) {
        VBox component = new VBox();
        Label selectParent = new Label("Select a parent category, if you wish: ");
        component.getChildren().add(selectParent);
        component.getChildren().add(listNewCategoryButtons(logic, logic.listCategories(), nameInput, attributes, stage, parent, mainStage));
        return component;
    }
    
    public VBox listNewCategoryButtons(Logic logic, List<Category> categories, TextField nameInput, List<String> attributes, Stage stage, String parent, MainStage mainStage) {
        VBox categoryButtons = new VBox();
        for (Category category: categories) {
            categoryButtons.getChildren().add(getParentCategoryButton(logic, category, nameInput, attributes, stage, parent, mainStage));
        }        
        return categoryButtons;
    }

    public Button getParentCategoryButton(Logic logic, Category category, TextField nameInput, List<String> newCategoryAttributes, Stage stage, String parent, MainStage mainStage) {
        String attributes = "";
        for (CategoryAttribute catAttribute: category.getAttributes()) {
            attributes = attributes + ", " + catAttribute.getAttribute();
        }
        String parentInfo = category.getName() + attributes;
        if (!category.getParentName().equals("")) {
            parentInfo = parentInfo + ", (" + category.getParentName() + ")";
        }
        Button categoryButton = new Button(parentInfo);
        categoryButton.setOnAction((event) -> {
            newCategory(logic, stage, nameInput.getText(), newCategoryAttributes, category.getName(), mainStage);
        });
        return categoryButton;
    }    
}
