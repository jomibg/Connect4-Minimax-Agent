import java.util.ArrayList;
import java.util.Collections;

class MinimaxAb{
	private static int winScore = 1000;
	private static int loseScore = -1000;
	private static int drawScore = 0;
	private static long timeMs = 0;
    private static int maxDepth = 8;
	// RED: maximizer; YELLOW: minimizer
    // Matrix for evaluation function
    private int[][] evalMatrix = {
            {3, 4, 5, 7, 5, 4, 3},
            {4, 6, 8, 10, 8, 6, 4},
            {5, 7, 11, 13, 11, 7, 5},
            {5, 7, 11, 13, 11, 7, 5},
            {4, 6, 8, 10, 8, 6, 4},
            {3, 4, 5, 7, 5, 4, 3}
        };

    private int MAXCOL;
    private int MAXROW;
    private int BLANK;
    private int RED;
    private int YELLOW;

    private ArrayList<Integer> columns = new ArrayList<Integer>();

    public MinimaxAb(int maxcol, int maxrow, int blank, int red, int yellow){
            this.MAXCOL = maxcol;
            this.MAXROW = maxrow;
            this.BLANK = blank;
            this.RED = red;
            this.YELLOW = yellow;
            for(int i = 0; i< MAXCOL; i++){
                this.columns.add(i);
            }
    }

    public void setDepth(int depth){
        this.maxDepth = depth;
    }

	public long getTimeMs(){
		return this.timeMs;
	}


	private boolean checkWin(int[][] board, int row, int col, int player) {
        // Check horizontal
        if (col + 3 < MAXCOL &&
            board[row][col] == player &&
            board[row][col+1] == player &&
            board[row][col+2] == player &&
            board[row][col+3] == player) {
            return true;
        }

        // Check vertical
        if (row + 3 < MAXROW &&
            board[row][col] == player &&
            board[row+1][col] == player &&
            board[row+2][col] == player &&
            board[row+3][col] == player) {
            return true;
        }

        // Check diagonal (down-right)
        if (row + 3 < MAXROW && col + 3 < MAXCOL &&
            board[row][col] == player &&
            board[row+1][col+1] == player &&
            board[row+2][col+2] == player &&
            board[row+3][col+3] == player) {
            return true;
        }

        // Check diagonal (up-right)
        if (row - 3 >= 0 && col + 3 < MAXCOL &&
            board[row][col] == player &&
            board[row-1][col+1] == player &&
            board[row-2][col+2] == player &&
            board[row-3][col+3] == player) {
            return true;
        }

        return false;
    }


	private boolean playerWon(int[][] board, int player){
		for (int row = 0; row < MAXROW; row++) {
			for (int col = 0; col < MAXCOL; col++) {
				if (checkWin(board, row, col, player)) {
                    return true;
                }
			}
		}
		return false;
	}


	private boolean isBoardFull(int[][] board) {
        for (int col = 0; col < MAXCOL; col++) {
            if (board[0][col] == BLANK) {
                return false;
            }
        }
        return true;
	}



	private boolean boardTerminal(int[][] board) {
        return playerWon(board, RED) || playerWon(board, YELLOW) || isBoardFull(board);
    }


    private int evaluate(int[][] board, int player) {
        if (playerWon(board, RED)) {
            return winScore;
        } else if (playerWon(board, YELLOW )) {
            return loseScore;
        } else if (isBoardFull(board)) {
            return drawScore;
        }

        // Evaluation function
        int score = 0;

        // Check for potential wins/blocks in the next move
        for (int col = 0; col < MAXCOL; col++) {
            if (isValidMove(board, col)) {
                int[][] newBoard = makeMove(board, col, player);
                if (playerWon(newBoard, RED)) {
                    score += 100;
                }
                if (playerWon(newBoard, YELLOW)) {
                    score -= 100;
                }
            }
        }

        for (int col = 0; col < MAXCOL; col++) {
            for (int row = 0; row< MAXROW; row++) {
                if (board[row][col] == RED)
                    score += evalMatrix[row][col];
                else if (board[row][col] == YELLOW)
                    score -= evalMatrix[row][col];
            }
        }

        return score;
    }



    private boolean isTerminal(int[][] board) {
        return playerWon(board, RED) || playerWon(board, YELLOW) || isBoardFull(board);
    }


    private boolean isValidMove(int[][] board, int col) {
        return board[0][col] == BLANK;
    }


    private int[][] makeMove(int[][] board, int col, int player) {
        int[][] newBoard = new int[MAXROW][MAXCOL];
        for (int i = 0; i < MAXROW; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, MAXCOL);
        }

        for (int row = MAXROW - 1; row >= 0; row--) {
            if (newBoard[row][col] == BLANK) {
                newBoard[row][col] = player;
                break;
            }
        }
        return newBoard;
    }


    private int minimax(int[][] board, int player, int alpha, int beta, int depth) {

        if (depth == maxDepth || isTerminal(board)) {
            return evaluate(board, player);
        }

    	boolean isMaximizing = (player == RED) ? true : false;
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int opponent = (player == RED) ? YELLOW : RED;

        for (int col = 0; col < MAXCOL; col++) {
            if (isValidMove(board, col)) {
                int[][] newBoard = makeMove(board, col, player);
                int score = minimax(newBoard, opponent, alpha, beta, depth+1);

                if (isMaximizing) {
                    bestScore = Math.max(bestScore, score);
                    alpha = Math.max(alpha, score);
                } else {
                    bestScore = Math.min(bestScore, score);
                    beta = Math.min(beta, score);
                }
                if (beta <= alpha) {
                    break;
                }

            }
        }

        return bestScore;
    }


    public int findBestMove(int[][] board, int player) {
        long startTime = System.currentTimeMillis();
        int bestMove = -1;
        int bestScore = (player == RED) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int opponent = (player == RED) ? YELLOW : RED;
        boolean isMaximizing = (player == RED) ? true : false;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        Collections.shuffle(columns);
        for (int col : columns) {
            if (isValidMove(board, col)) {
                int[][] newBoard = makeMove(board, col, player);
                int score = minimax(newBoard, opponent, alpha, beta, 0);
                if (isMaximizing) {
                    alpha = Math.max(alpha, score);
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = col;
                    }
                } else {
                    beta = Math.min(beta, score);
                    if (score < bestScore) {
                        bestScore = score;
                        bestMove = col;
                    }
                }
            }
        }
        // Log score for debuging
        //System.out.println(bestScore);
        long endTime = System.currentTimeMillis();
        // Count time in ms for making the move
        this.timeMs = (endTime - startTime);
        return bestMove;
    }
}