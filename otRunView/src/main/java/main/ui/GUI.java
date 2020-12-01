/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.ui;

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
import main.domain.Logic;
import main.domain.Run;

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
        
        HBox display = new HBox();
        display.getChildren().add(runDisplay);
        
        Scene scene = new Scene(display);
        stage.setScene(scene);       
        stage.show();
    }
    
    public void getRuns() {
        
    }
    
    public String getRunDate(Run run) {
        int day = run.getDate().getDayOfMonth();
        String month = run.getDate().monthOfYear().getAsShortText();
        int year = run.getDate().getYear();
        String date = month + " " + day + " " + year;
        return date;
    }
    
    public String getRunInfo(Run run) {
        String date = getRunDate(run);
        
        String distance = run.getDistanceKm() + " Km, ";
        
        String categories = "";
        for (Category category: run.getCategories()) {
            categories = categories + ", " + category.getName();
        }

        String runInfo = date + ", " + distance + run.getDuration() + categories;
        return runInfo;
    }
    
    public void newRun(Logic logic) {
        Stage newRunStage = new Stage();
        newRunStage.setTitle("Add a new run");
        
        VBox runData = new VBox();
        
        FlowPane datePane = getDatePane();        
        FlowPane distancePane = getDistancePane();
        FlowPane timePane = getTimePane();
        FlowPane cadencePane = getCadencePane();
        
        runData.getChildren().addAll(datePane, distancePane, timePane, cadencePane);
        Scene newRunScene = new Scene(runData);
        newRunStage.setScene(newRunScene);       
        newRunStage.show();
    }
    
    public Button saveButton(Logic logic, Run run) {
        Button save = new Button("Save run");
        save.setOnAction((event) -> {
            
        });
        
        return save;
    }
    
    public void showRun(Run run, Logic logic) {
        Stage runStage = new Stage();
        String title = run.getDateAsText() + ", " + run.getDistanceKm() + ", " + run.getDuration() + ", " + run.getAvgSpeedKmH() + ", " + run.getAvgCadence();
        runStage.setTitle(title);
        
        Button deleteRun = deleteRun(logic, run);
        
        Scene runScene = new Scene(deleteRun);
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
    
    public FlowPane getDatePane() {
        FlowPane datePane = new FlowPane();
        Label dateLabel = new Label("Date: ");
        TextField date = new TextField();
        date.setMaxWidth(32);
        Label dashOne = new Label(" / ");
        TextField month =  new TextField();
        month.setMaxWidth(32);
        Label dashTwo = new Label(" / ");
        TextField year = new TextField();
        year.setMaxWidth(64);
        datePane.getChildren().addAll(dateLabel, date, dashOne, month, dashTwo, year);
        return datePane;
    }
    
    public FlowPane getDistancePane() {
        FlowPane distancePane = new FlowPane();
        Label distanceLabel = new Label("Distance: ");
        TextField distanceInt = new TextField();
        distanceInt.setMaxWidth(32);
        Label dot = new Label(" . ");
        TextField distanceDec = new TextField();
        distanceDec.setMaxWidth(32);
        Label distanceUnit = new Label(" Km");
        distancePane.getChildren().addAll(distanceLabel, distanceInt, dot, distanceDec, distanceUnit);
        return distancePane;
    }
    
    public FlowPane getTimePane() {
        FlowPane timePane = new FlowPane();
        Label timeLabel = new Label("Time: ");
        TextField hours = new TextField();
        hours.setMaxWidth(32);
        Label commaOne = new Label(" : ");
        TextField minutes = new TextField();
        minutes.setMaxWidth(32);
        Label commaTwo = new Label(" : ");
        TextField seconds = new TextField();
        seconds.setMaxWidth(32);
        timePane.getChildren().addAll(timeLabel, hours, commaOne, minutes, commaTwo, seconds);
        return timePane;
    }
    
    public FlowPane getCadencePane() {
        FlowPane cadencePane = new FlowPane();
        Label cadenceLabel = new Label("Avg. cadence: ");
        TextField cadenceInput = new TextField();
        cadenceInput.setMaxWidth(64);
        cadencePane.getChildren().addAll(cadenceLabel, cadenceInput);
        return cadencePane;
    }
    
}
