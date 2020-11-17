/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.ui;

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
                addRun(kbInput);
            } else if (userCommand.equals("add cat")) {
                addCategory(kbInput, logic);
            } else if (userCommand.equals("read")) {
                read(kbInput, logic);
            }
        }
        
    }
    
    public static String getCommandAndInstructions(Scanner kbInput) {
        System.out.println("To create a new category for your runs,  (e.g. \"Pavement\", \"Trail\", \"Track\", \"5K\", \"10K\", \"tempo\". \"easy\" etc.), type \"add cat\",");
        System.out.println("To read a category, type \"read\"");;
        System.out.println("To add a new run, type \"add run\".");
        String userCommand = kbInput.nextLine();
        return userCommand;
    }
    
    private static void addRun(Scanner kbInput) {
        
    }
    
    private static void addCategory(Scanner kbInput, Logic logic) {
        
        System.out.println("Give a name to your new category, (e.g. \"Distance\", \"5K\", \"tempo\" etc.):");
        String newCategory = kbInput.nextLine();
        System.out.println("If your categry is a subcategory (e.g.\"5K\"), name the parent category (e.g. \"Distance\"):");
        String parentCategory = kbInput.nextLine();
        
        logic.saveCategory(newCategory, parentCategory);
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