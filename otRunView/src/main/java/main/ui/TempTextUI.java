/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import main.domain.Logic;

/**
 *
 * @author J
 */
public class TempTextUI {
    
    public static void main(String[] args) {
        
        Logic logic = new Logic();
                
        Scanner kbInput = new Scanner(System.in);                 
        
        while (true) {
            String userCommand = getCommandAndInstructions(kbInput);
            if (userCommand.equals("x")) {
                break;
            } else if (userCommand.equals("reset db")) {
                resetDatabase(logic);
            } else if (userCommand.equals("add run")) {
                addRun(kbInput, logic);
            } else if (userCommand.equals("add cat")) {
                addCategory(kbInput, logic);
            } else if (userCommand.equals("read cat")) {
                read(kbInput, logic);
            } else if (userCommand.equals("read run")) {
                readRun(kbInput, logic);
            }
        }
        
    }
    
    public static String getCommandAndInstructions(Scanner kbInput) {
        System.out.println("To create a new category for your runs,  (e.g. \"Pavement\", \"Trail\", \"Track\", \"5K\", \"10K\", \"tempo\". \"easy\" etc.), type \"add cat\",");
        System.out.println("To read a category, type \"read cat\"");;
        System.out.println("To add a new run, type \"add run\",");
        System.out.println("To read a run, type \"read run\"");
        System.out.println("To delete all your data, type \"reset db\".");
        String userCommand = kbInput.nextLine();
        return userCommand;
    }
    
    private static void addRun(Scanner kbInput, Logic logic) {
        System.out.println("Plese provide the date of your run, in the form \"YYYY-MM-DD\":");
        String date = kbInput.nextLine();
        System.out.println("Please provide the distance (Km) of your run: ");
        float distance = Float.valueOf(kbInput.nextLine());
        System.out.println("Please provide the duration of your run, in the form \"h:mm:ss\", (e.g. 0:44:12 for a run of 44 minutes & 12 seconds):");
        String duration = kbInput.nextLine();
        System.out.println("Please provide your avg. cadence on your run, if you wish: ");
        int avgCadence = Integer.valueOf(kbInput.nextLine());
        System.out.println("Please enter the file path for a .gpx-file of your run, if you wish: ");
        String gpxPath = kbInput.nextLine();
        List<String> categories = new ArrayList<>();
        while (true) {
            System.out.println("Please provide any category you wish to associate your run with, or type \"x\" to continue: ");
            String category = kbInput.nextLine();
            if (category.equals("x")) {
                break;
            } else {
                categories.add(category);
            }
        }
        
        logic.saveRun(distance, date, duration, avgCadence, gpxPath, categories);
    }
    
    private static void readRun(Scanner kbInput, Logic logic) {
        System.out.println("Enter the primary key for the run: ");
        int pk = Integer.valueOf(kbInput.nextLine());
        System.out.println(logic.readRun(pk));
    }
    
    private static void addCategory(Scanner kbInput, Logic logic) {
        
        System.out.println("Give a name to your new category, (e.g. \"Distance\", \"5K\", \"tempo\" etc.):");
        String newCategory = kbInput.nextLine();
        
        System.out.println("If your categry is a subcategory (e.g.\"5K\"), name the parent category (e.g. \"Distance\"):");
        String parentCategory = kbInput.nextLine();
        
        List<String> attributes = new ArrayList<>();
        while (true) {
            System.out.println("Give an attribute to your category, or type \"x\" to continue:");
            String attribute = kbInput.nextLine();
            if (attribute.equals("x")) {
                break;
            } else {                
                attributes.add(attribute);
            }
        }
        logic.saveCategory(newCategory, attributes, parentCategory);
    }
    
    private static void read(Scanner kbInput, Logic logic) {
        
        System.out.println("What do you want to read:");
        String toBeRead = kbInput.nextLine();
        if (!(toBeRead.equals(""))) {
            System.out.println(logic.readCategory(toBeRead));
        }
        System.out.println("You entered an empty string. This is not a valid input.");
    }
    
    private static void resetDatabase(Logic logic) {
        logic.resetDatabase();
    }
}
