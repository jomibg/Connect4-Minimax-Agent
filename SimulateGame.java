import java.util.List;
import java.util.ArrayList;

public class SimulateGame {
    private static final int BLANK = 0;
    private static final int RED = 1;
    private static final int YELLOW = 2;
    private static final int MAXROW = 6;
    private static final int MAXCOL = 7;

    private int[][] board;
    private int activeColor;
    private boolean gameEnd;
    private int redAgentType;
    private int yellowAgentType;
    private MinimaxAb minimaxAgentAb;
    private Minimax minimaxAgent;
    private RandomAgent randomAgent;
    private List<Long> moveTimes;

    public SimulateGame(int redAgentType, int yellowAgentType) {
        this.redAgentType = redAgentType;
        this.yellowAgentType = yellowAgentType;
        this.minimaxAgentAb = new MinimaxAb(MAXCOL,MAXROW,BLANK,RED,YELLOW);
        this.minimaxAgent = new Minimax(MAXCOL,MAXROW,BLANK,RED,YELLOW);
        this.randomAgent = new RandomAgent(MAXCOL,MAXROW,BLANK,RED,YELLOW);
        this.moveTimes = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        board = new int[MAXROW][MAXCOL];
        for (int row = 0; row < MAXROW; row++)
            for (int col = 0; col < MAXCOL; col++)
                board[row][col] = BLANK;
        activeColor = RED;
        gameEnd = false;
    }

    public int simulateGame() {
        while (!gameEnd) {
            long startTime = System.currentTimeMillis();
        	int column = makeAgentMove();
        	long endTime = System.currentTimeMillis();
        	long duration = endTime - startTime;
        	moveTimes.add(duration);
            if (column != -1) {
                putDisk(column);
                if (checkWin()) {
                    return activeColor == RED ? YELLOW : RED; // Return the winner
                }
                if (isBoardFull()) {
                    return BLANK; // Draw
                }
            } else {
                // Invalid move, current player loses
                return activeColor == RED ? YELLOW : RED;
            }
        }
        return BLANK; // This should not be reached
    }

    private int makeAgentMove() {
        int agentType = (activeColor == RED) ? redAgentType : yellowAgentType;
        switch (agentType) {
            case 1:
                return minimaxAgent.findBestMove(board, activeColor);
            case 2:
                return minimaxAgentAb.findBestMove(board, activeColor);
            case 3:
                return randomAgent.findBestMove(board);
            default:
                throw new IllegalStateException("Invalid agent type");
        }
    }

    private void putDisk(int column) {
        for (int row = MAXROW - 1; row >= 0; row--) {
            if (board[row][column] == BLANK) {
                board[row][column] = activeColor;
                activeColor = (activeColor == RED) ? YELLOW : RED;
                return;
            }
        }
    }

    private boolean checkWin() {
        // Check horizontal
        for (int row = 0; row < MAXROW; row++) {
            for (int col = 0; col < MAXCOL - 3; col++) {
                if (board[row][col] != BLANK &&
                    board[row][col] == board[row][col+1] &&
                    board[row][col] == board[row][col+2] &&
                    board[row][col] == board[row][col+3]) {
                    return true;
                }
            }
        }

        // Check vertical
        for (int row = 0; row < MAXROW - 3; row++) {
            for (int col = 0; col < MAXCOL; col++) {
                if (board[row][col] != BLANK &&
                    board[row][col] == board[row+1][col] &&
                    board[row][col] == board[row+2][col] &&
                    board[row][col] == board[row+3][col]) {
                    return true;
                }
            }
        }

        // Check diagonal (top-left to bottom-right)
        for (int row = 0; row < MAXROW - 3; row++) {
            for (int col = 0; col < MAXCOL - 3; col++) {
                if (board[row][col] != BLANK &&
                    board[row][col] == board[row+1][col+1] &&
                    board[row][col] == board[row+2][col+2] &&
                    board[row][col] == board[row+3][col+3]) {
                    return true;
                }
            }
        }

        // Check diagonal (bottom-left to top-right)
        for (int row = 3; row < MAXROW; row++) {
            for (int col = 0; col < MAXCOL - 3; col++) {
                if (board[row][col] != BLANK &&
                    board[row][col] == board[row-1][col+1] &&
                    board[row][col] == board[row-2][col+2] &&
                    board[row][col] == board[row-3][col+3]) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int col = 0; col < MAXCOL; col++) {
            if (board[0][col] == BLANK) {
                return false;
            }
        }
        return true;
    }

    public double getAverageTime() {
        if (moveTimes.isEmpty()) {
            return 0;
        }
        long sum = 0;
        for (long time : moveTimes) {
            sum += time;
        }
        return (double) sum / moveTimes.size();
    }
}
