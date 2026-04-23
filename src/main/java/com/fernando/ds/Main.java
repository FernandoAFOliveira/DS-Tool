package com.fernando.ds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.fernando.ds.engine.InputCollector;
import com.fernando.ds.engine.ScoringEngine;
import com.fernando.ds.model.DSArrayDeque;
import com.fernando.ds.model.DSArrayList;
import com.fernando.ds.model.DSHashMap;
import com.fernando.ds.model.DSHashSet;
import com.fernando.ds.model.DSPriorityQueue;
import com.fernando.ds.model.DSQueue;
import com.fernando.ds.model.DSStack;
import com.fernando.ds.model.DSTreeMap;
import com.fernando.ds.model.DSTreeSet;
import com.fernando.ds.model.DataStructure;
import com.fernando.ds.requirements.UserRequirements;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize the library ONCE before the loop
        List<DataStructure> library = new ArrayList<>();
        library.add(new DSArrayList());
        library.add(new DSStack());
        library.add(new DSQueue());
        library.add(new DSPriorityQueue());
        library.add(new DSArrayDeque());
        library.add(new DSHashSet());
        library.add(new DSTreeSet());
        library.add(new DSHashMap());
        library.add(new DSTreeMap());

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("--- Welcome to the Data Structure Advisor ---");

        while (running) {
            // 2. Collect input
            InputCollector collector = new InputCollector(scanner);
            UserRequirements req = collector.collect();

            // 3. Score and Rank
            ScoringEngine engine = new ScoringEngine();
            for (DataStructure ds : library) {
                double score = engine.calculate(ds, req);
                ds.setLastCalculatedScore(score);
            }
            
            Collections.sort(library);
            
            for (DataStructure ds : library) {
                System.out.println(ds.getName() + " Score: " + ds.getLastCalculatedScore());
            }

            System.out.print("\nWould you like to run another query? (y/n): ");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                running = false;
            }
        }
        System.out.println("Goodbye!");
    }
}