/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: InputCollector.java
 *
 * This file is part of a Java Swing application that helps students compare
 * common Java data structures based on user-selected requirements.
 */

import java.util.Scanner;

public class InputCollector {

    private final Scanner scanner;

    public InputCollector() {
        this.scanner = new Scanner(System.in);
    }

    public InputCollector(Scanner scanner) {
        this.scanner = scanner;
    }

    public DSRequirements collectRequirements() {
        DSRequirements req = new DSRequirements();

        req.setKeyValuePreference(
            askPreference("Do you need key-value mapping?")
        );

        req.setDuplicatePreference(
            askPreference("Do you allow duplicate elements?")
        );

        req.setSortedPreference(
            askPreference("Do you need elements sorted?")
        );

        req.setIndexedPreference(
            askPreference("Do you need indexed access?")
        );

        req.setRemovalOrderPreference(askRemovalOrder());

        System.out.println("\n--- Performance Priorities (0-5, where 5 is critical) ---");

        req.setLookupWeight(askPriority("Importance of Lookup Speed: ", 0, 5));
        req.setAddDeleteWeight(askPriority("Importance of Add/Delete Speed: ", 0, 5));
        req.setMemoryWeight(askPriority("Importance of Memory Efficiency: ", 0, 5));

        return req;
    }

    private Preference askPreference(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n/a): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return Preference.YES;
            }

            if (input.equals("n") || input.equals("no")) {
                return Preference.NO;
            }

            if (input.equals("a") || input.equals("any")) {
                return Preference.ANY;
            }

            System.out.println("Invalid input. Please enter y, n, or a.");
        }
    }


    private int askPriority(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Invalid input. Enter a number from " + min + " to " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    public int askChoiceFromList(int maxOption) {
        while (true) {
            System.out.print("Select an option (1-" + maxOption + "): ");
            String input = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);

                if (choice >= 1 && choice <= maxOption) {
                    return choice;
                }

                System.out.println("Invalid choice. Try again.");

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private RemovalOrder askRemovalOrder() {
        System.out.println("\nRemoval order preference:");
        System.out.println("1. Any");
        System.out.println("2. FIFO");
        System.out.println("3. LIFO");
        System.out.println("4. Double-ended");
        System.out.println("5. Priority");

        int choice = askPriority("Choose 1-5: ", 1, 5);

        switch (choice) {
            case 1:
                return RemovalOrder.ANY;
            case 2:
                return RemovalOrder.FIFO;
            case 3:
                return RemovalOrder.LIFO;
            case 4:
                return RemovalOrder.DOUBLE_ENDED;
            case 5:
                return RemovalOrder.PRIORITY;
            default:
                return RemovalOrder.ANY;
        }
    }

    public int askMenuChoice() {
        while (true) {
            System.out.println("\n--- Data Structure Advisor ---");
            System.out.println("1. Start advisor");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);

                if (choice == 1 || choice == 2) {
                    return choice;
                }

                System.out.println("Invalid choice. Please enter 1 or 2.");

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

        public void close() {
        scanner.close();
    }

    
}