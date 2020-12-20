/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.ui.stage;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.domain.AggregatedRunData;
import main.domain.Category;
import main.domain.Logic;
import main.domain.Run;
import main.ui.map.RunMap;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author J
 */
public class OverviewStage {
    
    private List<Category> selectedCategories;
    
    public OverviewStage(List<Category> selectedCategories, Logic logic, MainStage mainStage) {
        this.selectedCategories = selectedCategories;
        showOverview(logic, mainStage);
    }
    
    public void showOverview(Logic logic, MainStage mainStage) {
        Stage stage = new Stage();
        stage.setTitle("Overview of runs for selected categories");
        
        AggregatedRunData aggregatedRunData = logic.getAggregatedRunData(selectedCategories);
                
        Label aggregatedData = new Label(aggregatedRunData.getTotalRuns() + " runs, Combined distance: " + aggregatedRunData.getCombinedDistanceKm() + " Km, Avg. distance: " + aggregatedRunData.getAvgDistanceKm() + " Km, Combined time: " + aggregatedRunData.getCombinedDuration() + ", Avg. time: " + aggregatedRunData.getAvgDuration() + ", Avg. speed: " + aggregatedRunData.getAvgSpeedKmH() + ", Avg. cadence: " + aggregatedRunData.getAvgCadence());
        Label pb = new Label("Personal best: " + aggregatedRunData.getPb().getDuration() + ", " + aggregatedRunData.getPb().getDateAsText() + ", Distance: " + aggregatedRunData.getPb().getDistanceKm() + " Km, Avg. speed: " + aggregatedRunData.getPb().getAvgSpeedKmH() + " Km/h, Avg. cadence:" + aggregatedRunData.getPb().getAvgCadence() + ", Categories: " + aggregatedRunData.getPb().printCategories().substring(2));
        VBox upperScene = new VBox();
        upperScene.getChildren().addAll(aggregatedData, pb);
        
        BorderPane fullScene = setUpOverview(aggregatedRunData, logic, upperScene, mainStage);
        
        Scene scene = new Scene(fullScene);
        stage.setScene(scene);       
        stage.show();
    }
    
    private BorderPane setUpOverview(AggregatedRunData aggregatedRunData, Logic logic, VBox upperScene, MainStage mainStage) {
        VBox graphs = new VBox();
        graphs.getChildren().add(getAvgTimeGraph(aggregatedRunData.getRuns()));
        
        VBox gpxMap = new VBox();
        gpxMap.getChildren().add(getMap(logic, aggregatedRunData));
        
        VBox runList = listRuns(logic, aggregatedRunData, mainStage);
        
        BorderPane fullScene = new BorderPane();
        fullScene.setTop(upperScene);        
        fullScene.setLeft(graphs);        
        fullScene.setCenter(gpxMap);
        fullScene.setRight(runList);
        
        return fullScene;
    }
    
    public VBox listRuns(Logic logic, AggregatedRunData aggregatedRunData, MainStage mainStage) {
        Stage stage = new Stage();
        stage.setTitle("Runs matching your selected categories: ");
        
        VBox runs = getRuns(logic, aggregatedRunData, mainStage);
        
        return runs;
    }
    
    public VBox getRuns(Logic logic, AggregatedRunData aggregatedRunData, MainStage mainStage) {
        VBox runButtons = new VBox();
        
        List<Run> runs = logic.listSelectedRuns(selectedCategories);
        
        for (Run run: aggregatedRunData.getRuns()) {
            String runInfo = getRunInfo(run);
            
            Button runButton = new Button(runInfo);
            runButton.setOnAction((event) -> {
                RunStage runStage = new RunStage();
                runStage.showRun(run, logic, mainStage);
            });
            
            runButtons.getChildren().add(runButton);
        }
        
        return runButtons;
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
    
    public LineChart<String, String> getAvgTimeGraph(List<Run> runs) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        CategoryAxis yAxis = new CategoryAxis();
        yAxis.setLabel("Time (duration of run)");
        LineChart<String, String> chart = new LineChart(xAxis, yAxis);
        chart.setTitle("Time per run for selected categories");
        XYChart.Series data = new XYChart.Series();
        for (Run run: runs) {
            data.getData().add(new XYChart.Data(run.getDate().toString(), run.getDuration().toString()));
        }
        chart.getData().add(data);
        chart.setLegendVisible(false);
        return chart;
    }    
    
    public StackPane getMap(Logic logic, AggregatedRunData aggregatedRunData) {
        List<String> gpxFilePaths = new ArrayList<>();
        for (Run run: aggregatedRunData.getRuns()) {
            gpxFilePaths.add(run.getGpxFilePath());
        }
        RunMap runMap = new RunMap();
        StackPane mapPane = runMap.getMap(logic, gpxFilePaths);
        return mapPane;
    }
}
