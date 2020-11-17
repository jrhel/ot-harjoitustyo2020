/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.domain;

import java.util.List;
import org.joda.time.*;

/**
 *
 * @author J
 */
public class Run {
    
    private double distanceKm;
    private LocalDate date;
    private LocalTime duration;
    private double speedKmH;
    private int cadence;
    private String gpxFilePath;
    private List<Category> categories;
    
}
