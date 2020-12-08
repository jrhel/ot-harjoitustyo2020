/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.ui;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
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
import main.domain.Run;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author J
 */
public class GUI extends Application {
    
    private Logic logic;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        logic = new Logic();
        logic.ensureDataBaseExists();
        
        stage.setTitle("RunView");        
        
        HBox display = new HBox();
        display.getChildren().add(getRunDispaly(logic));
        display.getChildren().add(getCategoryDisplay(logic));   
        
        Scene scene = new Scene(display);
        stage.setScene(scene);       
        stage.show();
    }
    
    public VBox getRunDispaly(Logic logic) {
        
        Button addRun = new Button("Add a new run:");
        addRun.setOnAction((event) -> {
                newRun(logic);                
            });
        
        Label latestRuns = new Label("Latest runs:");
        
        VBox runDisplay = new VBox();
        runDisplay.getChildren().add(addRun);
        runDisplay.getChildren().add(latestRuns);
        
        VBox runs = new VBox();
        
        for (Run run: logic.listRuns()) {
            String runInfo = getRunInfo(run);
            
            Button runButton = new Button(runInfo);
            runButton.setOnAction((event) -> {
                showRun(run, logic);
            });
            
            runs.getChildren().add(runButton);
        }
        
        runDisplay.getChildren().add(runs);
        return runDisplay;
    }
    
    public String getRunInfo(Run run) {
        String date = run.getDateAsText();
        
        String distance = run.getDistanceKm() + " Km, ";
        
        String categories = "";
        for (Category category: run.getCategories()) {
            categories = categories + ", " + category.getName();
        }

        String runInfo = date + ", " + distance + run.getDuration() + categories;
        return runInfo;
    }
    
    public void newRun(Logic logic) {
        
        List<Category> runCategories = new ArrayList<>();
        
        Stage newRunStage = new Stage();
        newRunStage.setTitle("Add a new run");
        
        TextField day = new TextField();
        TextField month = new TextField();
        TextField year = new TextField();
        TextField kmInt = new TextField();
        TextField kmDec = new TextField();
        TextField hours = new TextField();
        TextField minutes = new TextField();
        TextField seconds = new TextField();
        TextField steps = new TextField();
        
        VBox runData = newRunDataForm(day, month, year, kmInt, kmDec, hours, minutes, seconds, steps);
        VBox categoryData = listRunCategoryButtons(logic.listCategories(), runCategories);
        HBox upperScene = new HBox();
        upperScene.getChildren().addAll(runData, categoryData);
        
        HBox lowerScene = new HBox();
        Label gpxLabel = new Label("Please enter the file path for the .gpx-file of your run, if you wish: ");
        TextField gpxFilePath = new TextField();
        Button saveButton = saveButton(logic, day, month, year, kmInt, kmDec, hours, minutes, seconds, steps, gpxFilePath, runCategories);
        lowerScene.getChildren().addAll(gpxLabel, gpxFilePath, saveButton);
        
        VBox fullScene = new VBox();
        fullScene.getChildren().addAll(upperScene, lowerScene);
        
        Scene newRunScene = new Scene(fullScene);
        newRunStage.setScene(newRunScene);       
        newRunStage.show();
    }
    
    public Button saveButton(Logic logic, TextField day, TextField month, TextField year, TextField kmInt, TextField kmDec, TextField hours, TextField minutes, TextField seconds, TextField steps, TextField gpxFilePath, List<Category> categories) {
        Button save = new Button("Save run");
        save.setOnAction((event) -> {
            // double distanceKm, LocalDate date, LocalTime duration, int steps, String gpxFilePath, List<Category> categories
            double distanceKm = Double.valueOf(kmInt.getText() + "." + kmDec.getText());
            String date = year.getText() + "-" + month.getText() + "-" + day.getText();
            String duration = hours.getText() + ":" + minutes.getText() + ":" + seconds.getText();
            List<String> runCategories = new ArrayList<>();
            for (Category category: categories) {
                runCategories.add(category.getName());
            }
            logic.saveRun(distanceKm, date, duration, Integer.valueOf(steps.getText()), gpxFilePath.getText(), runCategories);
        });
        
        return save;
    }
    
    public void showRun(Run run, Logic logic) {
        Stage runStage = new Stage();
        String title = "Your run, " + run.getDateAsText();
        runStage.setTitle(title);
        
        Button edit = editRun(logic, run);
        Label runData = new Label("Avg. cadence: " +  run.getAvgCadence() + ", Avg. Speed: " + run.getAvgSpeedKmH() + ", Distance: " + run.getDistanceKm() + " Km, Time: " + run.getDuration());
        Button deleteRun = deleteRun(logic, run);
        HBox upperScene = new HBox();
        upperScene.getChildren().addAll(edit, runData, deleteRun);
        
        HBox lowerScene = new HBox();
        VBox runCategories = listRunCategories(run);
        lowerScene.getChildren().addAll(runCategories);
        
        VBox fullScene = new VBox();
        fullScene.getChildren().addAll(upperScene, lowerScene);
        
        Scene runScene = new Scene(fullScene);
        runStage.setScene(runScene);       
        runStage.show();
        
    }
    
    public Button deleteRun(Logic logic, Run run) {
        Button deleteRun = new Button("Delete run");
        deleteRun.setOnAction((event) -> {
            logic.deleteRun(run);
        });
        
        return deleteRun;
    }
    
    public Button editRun(Logic logic, Run run) {
        Button deleteRun = new Button("Edit");
        deleteRun.setOnAction((event) -> {
            newRun(logic);
            logic.deleteRun(run);
        });
        
        return deleteRun;
    }
    
    public VBox listRunCategories(Run run) {
        VBox runCategories = new VBox();
        Label categoriesLabel = new Label("Categories: ");
        runCategories.getChildren().add(categoriesLabel);
        for (Category category: run.getCategories()) {
            Label categoryName = new Label(category.getName());
            runCategories.getChildren().add(categoryName);
        }
        return runCategories;
    }
    
    public FlowPane getDatePane(TextField day, TextField month, TextField year) {
        FlowPane datePane = new FlowPane();
        Label dateLabel = new Label("Date: ");
        day.setMaxWidth(32);
        Label dashOne = new Label(" / ");
        month.setMaxWidth(32);
        Label dashTwo = new Label(" / ");
        year.setMaxWidth(64);
        datePane.getChildren().addAll(dateLabel, day, dashOne, month, dashTwo, year);
        return datePane;
    }
    
    public FlowPane getDistancePane(TextField kmInt, TextField kmDec) {
        FlowPane distancePane = new FlowPane();
        Label distanceLabel = new Label("Distance: ");
        kmInt.setMaxWidth(32);
        Label dot = new Label(" . ");
        kmDec.setMaxWidth(32);
        Label distanceUnit = new Label(" Km");
        distancePane.getChildren().addAll(distanceLabel, kmInt, dot, kmDec, distanceUnit);
        return distancePane;
    }
    
    public FlowPane getTimePane(TextField hours, TextField minutes, TextField seconds) {
        FlowPane timePane = new FlowPane();
        Label timeLabel = new Label("Time: ");
        hours.setMaxWidth(32);
        Label commaOne = new Label(" : ");
        minutes.setMaxWidth(32);
        Label commaTwo = new Label(" : ");
        seconds.setMaxWidth(32);
        timePane.getChildren().addAll(timeLabel, hours, commaOne, minutes, commaTwo, seconds);
        return timePane;
    }
    
    public FlowPane getCadencePane(TextField steps) {
        FlowPane cadencePane = new FlowPane();
        Label stepLabel = new Label("Steps: ");
        steps.setMaxWidth(64);
        cadencePane.getChildren().addAll(stepLabel, steps);
        return cadencePane;
    }
    
    public VBox newRunDataForm(TextField day, TextField month, TextField year, TextField kmInt, TextField kmDec, TextField hours, TextField minutes, TextField seconds, TextField steps) {
        VBox dataForm = new VBox();
        
        FlowPane datePane = getDatePane(day, month, year);        
        FlowPane distancePane = getDistancePane(kmInt, kmDec);
        FlowPane timePane = getTimePane(hours, minutes, seconds);
        FlowPane cadencePane = getCadencePane(steps);
        
        dataForm.getChildren().addAll(datePane, distancePane, timePane, cadencePane);
        
        return dataForm;
    }
    
    public VBox listRunCategoryButtons(List<Category> allCategories, List<Category> runCategories) {
        VBox categoryButtons = new VBox();
        for (Category category: allCategories) {
            categoryButtons.getChildren().add(getRunCategoryButton(category, runCategories));
        }        
        return categoryButtons;
    }
    
    public Button getRunCategoryButton(Category category, List<Category> runCategories) {
        String attributes = "";
        for (CategoryAttribute catAttribute: category.getAttributes()) {
            attributes = attributes + ", " + catAttribute.getAttribute();
        }
        String categoryInfo = category.getName() + attributes;
        if (!category.getParentName().equals("")) {
            categoryInfo = categoryInfo + ", (" + category.getParentName() + ")";
        }
        Button categoryButton = new Button(categoryInfo);
        categoryButton.setOnAction((event) -> {
            runCategories.add(category);
        });
        return categoryButton;
    }
    
    public VBox getCategoryDisplay(Logic logic) {
        VBox categoryDisplay = new VBox();
        Button addCategory = new Button("Add a new category");
        addCategory.setOnAction((event) -> {
            Stage newCategoryStage = new Stage();
            List<String> attributes = new ArrayList<>();
            String parent = "";
            String name ="";
            newCategory(logic, newCategoryStage, name, attributes, parent);
        });
        
        Label categories = new Label("Categories: ");
        VBox categoryButtons = listCategoryButtons(logic.listCategories());
        
        categoryDisplay.getChildren().addAll(addCategory, categories, categoryButtons);
        return categoryDisplay;
    }
    
    public VBox listCategoryButtons(List<Category> categories) {
        VBox categoryButtons = new VBox();
        for (Category category: categories) {
            categoryButtons.getChildren().add(getCategoryButton(category));
        }        
        return categoryButtons;
    }
    
    public Button getCategoryButton(Category category) {
        String attributes = "";
        for (CategoryAttribute catAttribute: category.getAttributes()) {
            attributes = attributes + ", " + catAttribute.getAttribute();
        }
        String categoryInfo = category.getName() + attributes;
        if (!category.getParentName().equals("")) {
            categoryInfo = categoryInfo + ", (" + category.getParentName() + ")";
        }
        Button categoryButton = new Button(categoryInfo);
        categoryButton.setOnAction((event) -> {
            
        });
        return categoryButton;
    }
    
    public void newCategory(Logic logic, Stage newCategoryStage, String name, List<String> attributes, String parent) {
        
        newCategoryStage.setTitle("Add a new category");
        
        VBox categoryData = new VBox();
        
        FlowPane namePane = new FlowPane();
        Label nameLabel = new Label("Name: ");
        TextField nameInput = new TextField();
        nameInput.setText(name);
        Button saveCategory = new Button("Save category");
        saveCategory.setOnAction((event) -> {
            logic.saveCategory(nameInput.getText(), attributes, parent);
        });
        namePane.getChildren().addAll(nameLabel, nameInput, saveCategory);
        
        categoryData.getChildren().add(namePane);
        HBox lowerDisplay = new HBox();
        lowerDisplay.getChildren().add(getAttributeComponent(logic, nameInput, attributes, newCategoryStage, parent));
        lowerDisplay.getChildren().add(getCategoryComponent(logic, nameInput, attributes, newCategoryStage, parent));
        
        categoryData.getChildren().add(lowerDisplay);
        
        Scene newCategoryScene = new Scene(categoryData);
        newCategoryStage.setScene(newCategoryScene);       
        newCategoryStage.show();
    }
    
    public Button getParentCategoryButton(Logic logic, Category category, TextField nameInput, List<String> newCategoryAttributes, Stage newCategoryStage, String parent) {
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
            newCategory(logic, newCategoryStage, nameInput.getText(), newCategoryAttributes, category.getName());
        });
        return categoryButton;
    }
    
    public VBox listNewCategoryButtons(Logic logic, List<Category> categories, TextField nameInput, List<String> attributes, Stage newCategoryStage, String parent) {
        VBox categoryButtons = new VBox();
        for (Category category: categories) {
            categoryButtons.getChildren().add(getParentCategoryButton(logic, category, nameInput, attributes, newCategoryStage, parent));
        }        
        return categoryButtons;
    }
    
    public VBox getCategoryComponent(Logic logic, TextField nameInput, List<String> attributes, Stage newCategoryStage, String parent) {
        VBox component = new VBox();
        Label selectParent = new Label("Select a parent category, if you wish: ");
        component.getChildren().add(selectParent);
        component.getChildren().add(listNewCategoryButtons(logic, logic.listCategories(), nameInput, attributes,newCategoryStage, parent));
        return component;
    }
    
    public VBox getAttributeComponent(Logic logic, TextField nameInput, List<String> attributes, Stage newCategoryStage, String parent) {
        VBox component = new VBox();
        VBox addedAttributes = updateAttributes(attributes);
        TextField attributeInput = new TextField();
        Button newAttribute = new Button("Add attribute: ");
        newAttribute.setOnAction((event) -> {
            attributes.add(attributeInput.getText());
            newCategory(logic, newCategoryStage, nameInput.getText(), attributes, parent);
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
}
