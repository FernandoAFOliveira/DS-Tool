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
        boolean keyValue = askYesNo("Do you need Key-Value mapping? (y/n): ");
        boolean allowDuplicates = askYesNo("Do you allow duplicate elements? (y/n): ");
        boolean sorted = askYesNo("Do you need elements sorted (e.g., natural order)? (y/n): ");

        System.out.println("\n--- Performance Priorities (1-5, where 5 is critical) ---");

        int lookup = askPriority("Importance of Lookup Speed: ", 1, 5);
        int addDelete = askPriority("Importance of Add/Delete Speed: ", 1, 5);
        int memory = askPriority("Importance of Memory Efficiency: ", 1, 5);

        DSRequirements req = new DSRequirements();

        // Convert boolean → Preference
        req.setKeyValuePreference(
            keyValue ? Preference.YES : Preference.NO
        );

        req.setDuplicatePreference(
            allowDuplicates ? Preference.YES : Preference.NO
        );

        req.setSortedPreference(
            sorted ? Preference.YES : Preference.NO
        );

        // Set weights
        req.setLookupWeight(lookup);
        req.setAddDeleteWeight(addDelete);
        req.setMemoryWeight(memory);

        return req;
    }

    private boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no")) return false;

            System.out.println("Invalid input. Please enter y or n.");
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