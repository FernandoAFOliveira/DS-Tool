/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: Main.java
 *
 * Main is the console-based entry point of the program.
 * It runs the Data Structure Advisor in the terminal using user input.
 * This version is useful for testing the core logic without the GUI.
 */

public class Main {
    public static void main(String[] args) {
        InputCollector inputCollector = new InputCollector();
        ScoringEngine scoringEngine = new ScoringEngine();

        boolean running = true;

        while (running) {
            int choice = inputCollector.askMenuChoice();

            if (choice == 1) {
                DSRequirements requirements = inputCollector.collectRequirements();
                scoringEngine.evaluateAndPrint(requirements, inputCollector);
            } else if (choice == 2) {
                running = false;
            }
        }

        inputCollector.close();
        System.out.println("Goodbye!");
    }
}