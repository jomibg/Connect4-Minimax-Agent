import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class GameSimulator {
    private static final int NUM_GAMES = 100;
    private static final int RED = 1;
    private static final int YELLOW = 2;
    private static final int DRAW = 0;
    private static int originalType1;
    private static int originalType2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter agent type for 1st player (1: Minimax, 2: MinimaxAB, 3: Random):");
        originalType1 = scanner.nextInt();
        int firstAgentType = originalType1;

        System.out.println("Enter agent type for 2nd player (1: Minimax, 2: MinimaxAB, 3: Random):");
        originalType2 = scanner.nextInt();
        int secondAgentType = originalType2;

        scanner.nextLine(); // Consume the newline

        System.out.println("Enter the output folder path:");
        String outputFolder = scanner.nextLine().trim();

        scanner.close();

        int firstWins = 0;
        int secondWins = 0;
        int draws = 0;
        double totalAverageTime = 0;

        for (int i = 0; i < NUM_GAMES; i++) {
            SimulateGame game = new SimulateGame(firstAgentType, secondAgentType);
            System.out.println(String.format("Starting game: %d",i));
            int result = game.simulateGame();
            System.out.println(String.format("Game finished: %d",i));

            String winner = "DRAW";
            switch (result) {
                case RED:
                    winner = "RED";
                    if (firstAgentType == originalType1)
                        firstWins++;
                    else
                        secondWins++;
                    break;
                case YELLOW:
                    winner = "YELLOW";
                    if (secondAgentType == originalType2)
                        secondWins++;
                    else
                        firstWins++;
                    break;
                case DRAW:
                    draws++;
                    break;
            }
            System.out.println("Reult/Winner: " + winner);
            System.out.println(String.format("First agent type: %d Original type: %d", firstAgentType, originalType1));
            totalAverageTime += game.getAverageTime();
            // Switch agent color
            int temp = firstAgentType;
            firstAgentType = secondAgentType;
            secondAgentType = temp;
        }

        double firstWinProportion = (double) firstWins / NUM_GAMES;
        double overallAverageTime = totalAverageTime / NUM_GAMES;

        String resultString = String.format(
            "Simulation Results:\n" +
            "Agen type encoding:\n" +
            "1 -- Minimax, 2 -- MinimaxAB, 3 -- Random\n"+
            "First Agent Type: %d\n" +
            "Second Agent Type: %d\n" +
            "Number of Games: %d\n" +
            "First Agent Wins: %d\n" +
            "Second Agent Wins: %d\n" +
            "Draws: %d\n" +
            "Proportion of first agent wins: %.2f\n" +
            "Overall Average Move Time: %.2f ms",
            firstAgentType, secondAgentType, NUM_GAMES, firstWins, secondWins, draws, firstWinProportion, overallAverageTime
        );

        System.out.println(resultString);

        // Save results to file
        File folder = new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        String fileName = String.format("simulation_results_F%d_S%d.txt", firstAgentType, secondAgentType);
        File outputFile = new File(folder, fileName);

        try (PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {
            out.println(resultString);
            System.out.println("Results saved to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
