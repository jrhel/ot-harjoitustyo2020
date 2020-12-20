/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.ui.stage;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.domain.Category;
import main.domain.CategoryAttribute;
import main.domain.Logic;
import main.domain.Run;
import main.ui.map.RunMap;

/**
 *
 * @author J
 */
public class MainStage {
    private Stage mainStage;
    private List<Category> selectedCategories;

    public MainStage() {
        selectedCategories = new ArrayList<>();
    }  
    
    public void main(Logic logic, Stage stage) {
        mainStage = stage;
        stage.setTitle("RunView");        
        
        updateScene(logic);
              
        stage.show();
    }
    
    public VBox getLeftDispaly(Logic logic) {        
        Button addRun = getAddRunButton(logic);
        
        Label latestRuns = new Label("\nLatest runs:");
        VBox runDisplay = new VBox();
        runDisplay.getChildren().add(addRun);
        runDisplay.getChildren().add(latestRuns);
        
        VBox runs = new VBox();        
        for (Run run: logic.listRuns()) {            
            Button runButton = getRunButton(logic, run);
            runs.getChildren().add(runButton);
        }        
        runDisplay.getChildren().add(runs);
        
        return runDisplay;
    }
    
    private Button getAddRunButton(Logic logic) {
        Button addRun = new Button("Add a new run:");
        addRun.setOnAction((event) -> {
            NewRunStage newRunStage = new NewRunStage();
            newRunStage.newRun(logic, this);             
        });
        
        return addRun;
    }
    
    private Button getRunButton(Logic logic, Run run) {
        Button runButton = new Button(getRunInfo(run));
        runButton.setOnAction((event) -> {
            RunStage runStage = new RunStage();
            runStage.showRun(run, logic, this);
        });
        
        return runButton;
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
    
    private BorderPane getRightDisplay(Logic logic) {
        BorderPane rightDisplay = new BorderPane();
        Button addCategory = getAddCategoryButton(logic);            
        VBox categoryList = new VBox();
        Label categories = new Label("\nCategories: ");
        VBox categoryButtons = listCategoryButtons(logic.listCategories());
        Label emptyLabel = new Label();        
        Button showRuns = new Button("Show runs");
        categoryList.getChildren().addAll(categories, categoryButtons, emptyLabel, showRuns);
        showRuns.setOnAction((event) -> {
            if (selectedCategories.isEmpty()) {
                MessageStage messageStage = new MessageStage("You hadn't selected any categories. Please select at least one category, in order to show any matching runs.");
            } else {
                OverviewStage overviewStage = new OverviewStage(selectedCategories, logic, this);
            }
        });
        
        assembleRightDisplay(rightDisplay, logic, addCategory, categoryList);      
        return rightDisplay;
    }
    
    public void assembleRightDisplay(BorderPane rightDisplay, Logic logic, Button addCategory, VBox categoryList) {
        Button resetApp = getResetButton(logic);
        rightDisplay.setTop(addCategory);
        rightDisplay.setBottom(resetApp);
        rightDisplay.setCenter(categoryList);
    }
    
    private Button getResetButton(Logic logic) {
        Button resetApp = new Button("Reset application");
        resetApp.setOnAction((event) -> {
            resetApplication(logic);
        });
        
        return resetApp;
    }
    
    private void resetApplication(Logic logic) {
        VBox messageDisplay = new VBox();
        Stage resetStage = new Stage();
        Label question = new Label("All data stored in this application will be deleted. Do you really want this?");
        HBox choices = new HBox();
        Button yes = new Button("Yes");
        yes.setOnAction((event) -> {
            logic.resetDatabase();
        });
        Button no = new Button("No");
        no.setOnAction((event) -> {
            resetStage.close();
        });
        choices.getChildren().addAll(yes, no);
        messageDisplay.getChildren().addAll(question, choices);
        choices.setAlignment(Pos.CENTER);
        Scene scene = new Scene(messageDisplay);
        resetStage.setScene(scene);       
        resetStage.show();
    }
    
    private Button getAddCategoryButton(Logic logic) {
        Button addCategory = new Button("Add a new category");
        addCategory.setOnAction((event) -> {
            Stage newCategoryStage = new Stage();
            List<String> attributes = new ArrayList<>();
            String parent = "";
            String name = "";
            NewCategoryStage newCategoryScene = new NewCategoryStage();
            newCategoryScene.newCategory(logic, newCategoryStage, name, attributes, parent, this);
        });
        
        return addCategory;
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
            categoryButton.setStyle("-fx-background-color: #CDCDCD");
            selectedCategories.add(category);
        });
        return categoryButton;
    }
    
    public StackPane getMap(Logic logic) {
        List<String> gpxFilePaths = new ArrayList<>();
        for (Run run: logic.listRuns()) {
            gpxFilePaths.add(run.getGpxFilePath());
        }
        RunMap runMap = new RunMap();
        StackPane mapPane = runMap.getMap(logic, gpxFilePaths);
        return mapPane;
    }
    
    public void updateScene(Logic logic) {
        HBox display = new HBox();
        display.getChildren().add(getLeftDispaly(logic));
        display.getChildren().add(getMap(logic));
        display.getChildren().add(getRightDisplay(logic));   
        display.minHeight(756);
        display.minWidth(1344);
        
        Scene scene = new Scene(display);
        mainStage.setScene(scene); 
    }
}
