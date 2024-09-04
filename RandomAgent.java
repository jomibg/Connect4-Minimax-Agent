import java.util.Random;

class RandomAgent{
        private int MAXCOL;
        private int MAXROW;
        private int BLANK;
        private int RED;
        private int YELLOW;

        public RandomAgent(int maxcol, int maxrow, int blank, int red, int yellow){
                this.MAXCOL = maxcol;
                this.MAXROW = maxrow;
                this.BLANK = blank;
                this.RED = red;
                this.YELLOW = yellow;
        }

        private Random random = new Random();

	public int findBestMove(int[][] board) {
                int column;
                do {
                        column = random.nextInt(MAXCOL);
                } while (!isValidMove(column, board));

                return column; 
        }

        private boolean isValidMove(int column, int[][] board) {
                return board[0][column] == BLANK;
        }
}