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