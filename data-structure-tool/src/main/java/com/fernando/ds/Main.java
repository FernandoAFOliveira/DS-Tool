package com.fernando.ds;

import java.util.*;

import com.fernando.ds.engine.*;
import com.fernando.ds.model.*;
import com.fernando.ds.requirements.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("--- Welcome to the Data Structure Advisor ---");

        while (running) {
            // 1. Create a fresh requirements object for each session
            InputCollector collector = new InputCollector(new Scanner(System.in));
            UserRequirements req = collector.collect();

            ScoringEngine engine = new ScoringEngine();
            for (DataStructure ds : library) {
                double score = engine.calculate(ds, req);
                ds.setLastCalculatedScore(score); // Add this field to your DataStructure model!
            }
            // Sort by the scores you just set
            Collections.sort(library);
            for (DataStructure ds : library) {
                System.out.println(ds.getName() + " Score: " + ds.getLastCalculatedScore());
            }
            // 3. Loop check
            System.out.print("\nWould you like to run another query? (y/n): ");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                running = false;
            }
        }
        System.out.println("Goodbye!");
    }
}