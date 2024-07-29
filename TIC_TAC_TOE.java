#include <iostream>
#include <vector>
#include <limits>

using namespace std;

class TicTacToe {
private:
    vector<vector<char>> board;
    char currentPlayer;
    bool againstComputer;

    void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = '-';
            }
        }
    }

    void printBoard() {
        cout << "-------------" << endl;
        for (int row = 0; row < 3; row++) {
            cout << "| ";
            for (int col = 0; col < 3; col++) {
                cout << board[row][col] << " | ";
            }
            cout << endl;
            cout << "-------------" << endl;
        }
    }

    bool isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    bool hasWon(char player) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }
        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        return false;
    }

    bool isValidMove(int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3) {
            return false;
        }
        return board[row][col] == '-';
    }

    bool makeMove(int row, int col) {
        if (isValidMove(row, col)) {
            board[row][col] = currentPlayer;
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            return true;
        }
        return false;
    }

    void makeComputerMove() {
        pair<int, int> bestMove = findBestMove();
        makeMove(bestMove.first, bestMove.second);
    }

    int evaluate(char player) {
        if (hasWon(player)) {
            return 1;
        } else if (hasWon((player == 'X') ? 'O' : 'X')) {
            return -1;
        }
        return 0;
    }

    int minimax(char player, int depth, bool isMaximizingPlayer) {
        int score = evaluate(player);

        if (score == 1 || score == -1) {
            return score;
        }

        if (isBoardFull()) {
            return 0;
        }

        if (isMaximizingPlayer) {
            int bestScore = numeric_limits<int>::min();

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (isValidMove(row, col)) {
                        board[row][col] = player;
                        int currentScore = minimax(player, depth + 1, false);
                        bestScore = max(bestScore, currentScore);
                        board[row][col] = '-';
                    }
                }
            }

            return bestScore;
        } else {
            int bestScore = numeric_limits<int>::max();

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (isValidMove(row, col)) {
                        board[row][col] = player;
                        int currentScore = minimax(player, depth + 1, true);
                        bestScore = min(bestScore, currentScore);
                        board[row][col] = '-';
                    }
                }
            }

            return bestScore;
        }
    }

    pair<int, int> findBestMove() {
        int bestScore = numeric_limits<int>::min();
        pair<int, int> bestMove = {-1, -1};

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (isValidMove(row, col)) {
                    board[row][col] = currentPlayer;
                    int currentScore = minimax(currentPlayer, 0, false);
                    board[row][col] = '-';
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestMove = {row, col};
                    }
                }
            }
        }

        return bestMove;
    }

public:
    TicTacToe(bool againstComputer) : board(3, vector<char>(3)), currentPlayer('X'), againstComputer(againstComputer) {
        initializeBoard();
    }

    void play() {
        while (!hasWon('X') && !hasWon('O') && !isBoardFull()) {
            printBoard();

            if (againstComputer && currentPlayer == 'O') {
                cout << "Computer's turn:" << endl;
                makeComputerMove();
            } else {
                bool validMove = false;
                while (!validMove) {
                    cout << "Player " << currentPlayer << ", enter your move (row column): ";
                    int row, col;
                    cin >> row >> col;
                    validMove = makeMove(row, col);
                    if (!validMove) {
                        cout << "This cell is already taken or invalid. Please choose another one." << endl;
                    }
                }
            }
        }

        printBoard();

        if (hasWon('X')) {
            if (againstComputer) {
                cout << "Player X wins!" << endl;
            } else {
                cout << "Player X wins!" << endl;
            }
        } else if (hasWon('O')) {
            if (againstComputer) {
                cout << "Computer wins!" << endl;
            } else {
                cout << "Player O wins!" << endl;
            }
        } else {
            cout << "It's a draw!" << endl;
        }
    }
};

int main() {
    cout << "Welcome to Tic Tac Toe!" << endl;
    cout << "Do you want to play against another player? (y/n): ";
    char choice;
    cin >> choice;

    bool againstComputer = (choice == 'n' || choice == 'N');

    TicTacToe game(againstComputer);
    game.play();

    return 0;
}
