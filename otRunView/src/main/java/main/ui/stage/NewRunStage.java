/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.ui.stage;

import java.util.ArrayList;
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
public class NewRunStage {
      
    public void newRun(Logic logic, MainStage mainStage) {   
        Stage stage = new Stage();
        
        List<Category> runCategories = new ArrayList<>();        
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
        HBox lowerScene = getLowerNewRunScene(logic, stage, day, month, year, kmInt, kmDec, hours, minutes, seconds, steps, runCategories, mainStage);
        compileNewRunStage(runData, categoryData, lowerScene, stage);
    }
    
    private HBox getLowerNewRunScene(Logic logic, Stage stage, TextField day, TextField month, TextField year, TextField kmInt, TextField kmDec, TextField hours, TextField minutes, TextField seconds, TextField steps, List<Category> runCategories, MainStage mainStage) {
        HBox lowerScene = new HBox();
        Label gpxLabel = new Label("Please enter the file path for the .gpx-file of your run, if you wish: ");
        TextField gpxFilePath = new TextField();
        Button saveButton = saveButton(logic, stage, day, month, year, kmInt, kmDec, hours, minutes, seconds, steps, gpxFilePath, runCategories, mainStage);
        lowerScene.getChildren().addAll(gpxLabel, gpxFilePath, saveButton);
        
        return lowerScene;
    }
    
    private void compileNewRunStage(VBox runData, VBox categoryData, HBox lowerScene, Stage stage) {
        stage.setTitle("Add a new run");
        
        HBox upperScene = new HBox();
        upperScene.getChildren().addAll(runData, categoryData);
        
        VBox fullScene = new VBox();
        fullScene.getChildren().addAll(upperScene, lowerScene);
        
        Scene newRunScene = new Scene(fullScene);
        stage.setScene(newRunScene);       
        stage.show();
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
            categoryButton.setStyle("-fx-background-color: #CDCDCD");
        });
        return categoryButton;
    }
    
    public Button saveButton(Logic logic, Stage stage, TextField day, TextField month, TextField year, TextField kmInt, TextField kmDec, TextField hours, TextField minutes, TextField seconds, TextField steps, TextField gpxFilePath, List<Category> categories, MainStage mainStage) {
        Button save = new Button("Save run");
        save.setOnAction((event) -> {
            if (logic.checkIfNumbers(populateNumberList(day, month, year, kmInt, kmDec, hours, minutes, seconds, steps))) {
                double distanceKm = Double.valueOf(kmInt.getText() + "." + kmDec.getText());
                String date = year.getText() + "-" + month.getText() + "-" + day.getText();
                String duration = hours.getText() + ":" + minutes.getText() + ":" + seconds.getText();
                List<String> runCategories = new ArrayList<>();
                for (Category category: categories) {
                    runCategories.add(category.getName());
                }
                logic.saveRun(distanceKm, date, duration, Integer.valueOf(steps.getText()), gpxFilePath.getText(), runCategories);
                mainStage.updateScene(logic);
                stage.close();
            } else {
                MessageStage messageStage = new MessageStage("Make sure your inputs only contain numbers.");
            }
        });
        
        return save;
    }
    
    private List<String> populateNumberList(TextField day, TextField month, TextField year, TextField kmInt, TextField kmDec, TextField hours, TextField minutes, TextField seconds, TextField steps) {
        List<String> inputs = new ArrayList<>();
        inputs.add(day.getText());
        inputs.add( month.getText());
        inputs.add(year.getText());
        inputs.add(kmInt.getText());
        inputs.add(kmDec.getText());
        inputs.add(hours.getText());
        inputs.add(minutes.getText());
        inputs.add(seconds.getText());
        inputs.add(steps.getText());
        
        return inputs;
    }
}
