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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.domain.Category;
import main.domain.Logic;
import main.domain.Run;
import main.ui.map.RunMap;

/**
 *
 * @author J
 */
public class RunStage {
    
    public void showRun(Run run, Logic logic, MainStage mainStage) {
        Stage runStage = new Stage();
        String title = "Your run " + run.getDateAsText() + " :";
        runStage.setTitle(title);
        
        VBox runInfo = getRunInfo(logic, run, runStage, mainStage);
        
        VBox mapView = new VBox();
        Label gpxLabel = new Label(".gpx file path: " + run.getGpxFilePath());
        RunMap runMap = new RunMap();
        List<String> gpxFilePath = new ArrayList<>();
        gpxFilePath.add(run.getGpxFilePath());
        mapView.getChildren().addAll(runMap.getMap(logic, gpxFilePath), gpxLabel);
        
        HBox fullScene = new HBox();
        fullScene.getChildren().addAll(runInfo, mapView);
        
        Scene runScene = new Scene(fullScene);
        runStage.setScene(runScene);       
        runStage.show();
        
    }
    
    public VBox getRunInfo(Logic logic, Run run, Stage stage, MainStage mainStage) {
        VBox runInfo = new VBox();
        
        VBox runData = new VBox();
        Label distance = new Label("Distance: " + run.getDistanceKm() + " Km");
        Label duration = new Label("Time: " + run.getDuration());
        Label speed = new Label("Avg. speed: " + run.getAvgSpeedKmH() + " Km/h");
        Label cadence = new Label("Avg. cadence: " + run.getAvgCadence() + " steps/minute");
        Label emptyLabelOne = new Label();
        Label emptyLabelTwo = new Label();
        VBox categories = listRunCategories(run);
        runData.getChildren().addAll(distance, duration, speed, cadence, emptyLabelOne, categories, emptyLabelTwo);
        runData.setAlignment(Pos.TOP_LEFT);
        
        VBox buttons = new VBox();
        buttons.getChildren().add(editRun(logic, run, mainStage));
        buttons.getChildren().add(deleteRun(logic, run, stage, mainStage));
        buttons.setAlignment(Pos.BOTTOM_LEFT);
        
        runInfo.getChildren().addAll(runData, buttons);
        
        return runInfo;
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
    
    public Button editRun(Logic logic, Run run, MainStage mainStage) {
        Button deleteRun = new Button("Edit data");
        deleteRun.setOnAction((event) -> {
            NewRunStage newRunStage = new NewRunStage();
            newRunStage.newRun(logic, mainStage);
            logic.deleteRun(run);
        });
        
        return deleteRun;
    }
    
    public Button deleteRun(Logic logic, Run run, Stage stage, MainStage mainStage) {
        Button deleteRun = new Button("Delete run");
        deleteRun.setOnAction((event) -> {
            logic.deleteRun(run);
            mainStage.updateScene(logic);
            stage.close();
        });
        
        return deleteRun;
    }
}
