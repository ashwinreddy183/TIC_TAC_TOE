import java.util.Scanner;

public class TicTacToe {
    private char[][] board;
    private char currentPlayer;
    private boolean againstComputer;

    public TicTacToe(boolean againstComputer) {
        board = new char[3][3];
        currentPlayer = 'X';
        this.againstComputer = againstComputer;
        initializeBoard();
    }

    public void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = '-';
            }
        }
    }

    public void printBoard() {
        System.out.println("-------------");
        for (int row = 0; row < 3; row++) {
            System.out.print("| ");
            for (int col = 0; col < 3; col++) {
                System.out.print(board[row][col] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }

    public boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasWon(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }

            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }

        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }

        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    public boolean isValidMove(int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3) {
            return false;
        }
        return board[row][col] == '-';
    }

    public boolean makeMove(int row, int col) {
        if (isValidMove(row, col)) {
            board[row][col] = currentPlayer;
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            return true;
        } else {
            return false;
        }
    }

    public void makeComputerMove() {
        int[] bestMove = findBestMove();
        makeMove(bestMove[0], bestMove[1]);
    }

    private int evaluate(char player) {
        if (hasWon(player)) {
            return 1;
        } else if (hasWon((player == 'X') ? 'O' : 'X')) {
            return -1;
        } else {
            return 0;
        }
    }

    private int minimax(char player, int depth, boolean isMaximizingPlayer) {
        int score = evaluate(player);

        if (score == 1 || score == -1) {
            return score;
        }

        if (isBoardFull()) {
            return 0;
        }

        if (isMaximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (isValidMove(row, col)) {
                        board[row][col] = player;
                        int currentScore = minimax(player, depth + 1, false);
                        bestScore = Math.max(bestScore, currentScore);
                        board[row][col] = '-';
                    }
                }
            }

            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (isValidMove(row, col)) {
                        board[row][col] = player;
                        int currentScore = minimax(player, depth + 1, true);
                        bestScore = Math.min(bestScore, currentScore);
                        board[row][col] = '-';
                    }
                }
            }

            return bestScore;
        }
    }

    private int[] findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[] { -1, -1 };

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (isValidMove(row, col)) {
                    board[row][col] = currentPlayer;
                    int currentScore = minimax(currentPlayer, 0, false);
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestMove[0] = row;
                        bestMove[1] = col;
                    }
                    board[row][col] = '-';
                }
            }
        }

        return bestMove;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Tic Tac Toe!");
        System.out.print("Do you want to play against another player? (y/n): ");
        String choice = scanner.nextLine();

        boolean againstComputer = !choice.equalsIgnoreCase("y");

        TicTacToe game = new TicTacToe(againstComputer);

        while (!game.hasWon('X') && !game.hasWon('O') && !game.isBoardFull()) {
            game.printBoard();

            if (game.againstComputer && game.currentPlayer == 'O') {
                System.out.println("Computer's turn:");
                game.makeComputerMove();
            } else {
                boolean validMove = false;
                while (!validMove) {
                    System.out.print("Player " + game.currentPlayer + ", enter your move (row column): ");
                    int row = scanner.nextInt();
                    int col = scanner.nextInt();
                    validMove = game.makeMove(row, col);
                    if (!validMove) {
                        System.out.println("This cell is already taken. Please choose another one.");
                    }
                }
            }
        }

        game.printBoard();

        if (game.hasWon('X')) {
            if (game.againstComputer) {
                System.out.println("You win!");
            } else {
                System.out.println("Player X wins!");
            }
        } else if (game.hasWon('O')) {
            if (game.againstComputer) {
                System.out.println("Computer wins!");
            } else {
                System.out.println("Player O wins!");
            }
        } else {
            System.out.println("It's a draw!");
        }

        scanner.close();
    }
}
