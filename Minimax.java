import java.util.ArrayList;
import java.util.Collections;

class Minimax{
	private static int winScore = 1000;
    private static int loseScore = -1000;
    private static int drawScore = 0;
    private static long timeMs = 0;
    private static int maxDepth = 6;
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

    public Minimax(int maxcol, int maxrow, int blank, int red, int yellow){
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


    private int minimax(int[][] board, int depth, int player) {

        if (depth == maxDepth || isTerminal(board)) {
            return evaluate(board, player);
        }
        
    	boolean isMaximizing = (player == RED) ? true : false;
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int opponent = (player == RED) ? YELLOW : RED;

        for (int col = 0; col < MAXCOL; col++) {
            if (isValidMove(board, col)) {
                int[][] newBoard = makeMove(board, col, player);
                int score = minimax(newBoard, depth + 1, opponent);

                if (isMaximizing) {
                    bestScore = Math.max(bestScore, score);
                } else {
                    bestScore = Math.min(bestScore, score);
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
	
        Collections.shuffle(columns);
        for (int col: columns) {
            if (isValidMove(board, col)) {
                int[][] newBoard = makeMove(board, col, player);
                int score = minimax(newBoard, 0, opponent);
                if (isMaximizing) {
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = col;
                    }
                } else {
                    if (score < bestScore) {
                        bestScore = score;
                        bestMove = col;
                    }
                }
            }
        }
        //System.out.println("Best score: " + bestScore);
        long endTime = System.currentTimeMillis();
        // Time to make move
        this.timeMs = (endTime - startTime);
        return bestMove;
    }
}
