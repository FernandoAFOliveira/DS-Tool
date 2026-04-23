package com.fernando.ds.engine;

import java.util.Scanner;

import com.fernando.ds.requirements.UserRequirements;

public class InputCollector {
    private final Scanner scanner;

    public InputCollector(Scanner scanner) {
        this.scanner = scanner;
    }

    public UserRequirements collect() {
        UserRequirements req = new UserRequirements();

        System.out.println("\n--- Data Structure Advisor ---");
        
        // 1. Map vs Collection
        System.out.print("Do you need Key-Value mapping? (y/n): ");
        boolean needsKeys = scanner.nextLine().equalsIgnoreCase("y");
        req.setHaveKeys(needsKeys);

        // 2. Uniqueness
        System.out.print("Must elements be unique? (y/n): ");
        req.setBeUnique(scanner.nextLine().equalsIgnoreCase("y"));

        // 3. Sorting
        System.out.print("Do you need elements sorted (e.g., natural order)? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            req.setSortWeight(5); // Assign default high weight
        }

        // 4. Performance Weights
        System.out.println("\n--- Performance Priorities (1-5, where 5 is critical) ---");
        System.out.print("Importance of Lookup Speed: ");
        req.setLookupWeight(Integer.parseInt(scanner.nextLine()));
        
        System.out.print("Importance of Add/Delete Speed: ");
        req.setInsertWeight(Integer.parseInt(scanner.nextLine()));

        System.out.print("Importance of Memory Efficiency: ");
        req.setMemoryWeight(Integer.parseInt(scanner.nextLine()));

        return req;
    }
}