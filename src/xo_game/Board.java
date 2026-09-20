/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xo_game;

/**
 * Board:
 * Encapsulates a 3×3 char grid. Provides methods to clear it,
 * place a symbol, check for a winner, and test for a full board.
 */
public class Board {
    private static final int SIZE = 3;    // board dimension
    private final char[][] cells;         // stores 'X', 'O', or ' ' (empty)

    public Board() {
        cells = new char[SIZE][SIZE];
        clear();  // initialize all cells to ' '
    }

    /** Clears the board (sets all cells to ' '). */
    public void clear() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                cells[r][c] = ' ';
            }
        }
    }

    /**
     * Attempts to place symbol at (row,col).
     * @return true if the cell was empty and placement succeeded.
     */
    public boolean setCell(int row, int col, char symbol) {
        if (cells[row][col] == ' ') {
            cells[row][col] = symbol;
            return true;
        }
        return false;
    }

    /**
     * Checks if symbol occupies any full row, column, or diagonal.
     * @return true if symbol has three in a line.
     */
    public boolean hasWinner(char symbol) {
        // rows & columns
        for (int i = 0; i < SIZE; i++) {
            if (cells[i][0] == symbol && cells[i][1] == symbol && cells[i][2] == symbol) return true;
            if (cells[0][i] == symbol && cells[1][i] == symbol && cells[2][i] == symbol) return true;
        }
        // diagonals
        if (cells[0][0] == symbol && cells[1][1] == symbol && cells[2][2] == symbol) return true;
        if (cells[0][2] == symbol && cells[1][1] == symbol && cells[2][0] == symbol) return true;
        return false;
    }

    /** @return true if no empty cells remain. */
    public boolean isFull() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (cells[r][c] == ' ') return false;
            }
        }
        return true;
    }
}