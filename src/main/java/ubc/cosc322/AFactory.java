package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;

public class AFactory {


    
    public static int[][] deepCopy2DArray(int[][] original) {
        if (original == null) {
            return null;
        }

        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            // For each row, copy the row itself
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }

        return result;
    }

    
    /**
     * Performs the Monte Carlo search algorithm to determine the best move for the
     * current player.
     * 
     * @param gameState      The current state of the game represented as a 2D
     *                       array.
     * @param nextQueens     The positions of the next queens to be placed
     *                       represented as a 2D array.
     * @param previousQueens The positions of the previous queens that have been
     *                       placed represented as a 2D array.
     * @param depth          The depth of the search algorithm.
     * @param turn           The current player's turn (true for player 1, false for
     *                       player 2).
     * @return The number of possible actions after performing the best move.
     */
    public static int sumOfPos = 0;
    public static Action bestAction = null;
    public static int bestMove = -1;

    

    /**
     * Generates a list of possible actions for the ArrowMove strategy.
     * 
     * @param movement    The list of possible movements for the player.
     * @param gameState   The current state of the game.
     * @param yourQueens  The positions of the player's queens.
     * @param theirQueens The positions of the opponent's queens.
     * @param depth       The depth of the search.
     * @return The list of possible actions.
     */
    public static ArrayList<Action> ArrowMove(ArrayList<int[][]> movement, int[][] gameState, int[] yourQueen, int[][] yourQueens,
            int[][] theirQueens, boolean turn) {

        ArrayList<Action> actions = new ArrayList<Action>();
        for (int[][] move : movement) {
            makeMove(move, gameState);
            ArrayList<int[][]> arrows = Actions(gameState, move[0], theirQueens);
            for (int[][] arrow : arrows) {
                actions.add(new Action(move, arrow, gameState, yourQueens, theirQueens, !turn));
            }
            undoMove(move, gameState);

        }
        return actions;
        // for each action, generate the possible arrows and generate your possible
        // moves minus your opponents possible moves

    }

    private static void placeArrow(int[][] move, int[][] gameState) {
        gameState[move[0][0]][move[0][1]] = 3;
    }

    private static void undoArrow(int[][] move, int[][] gameState) {
        gameState[move[0][0]][move[0][1]] = 0;
    }

    public static ArrayList<int[][]> actionsForAllQueens(int[][] gameState, int[][] yourQueens, int[][] theirQueens) {
        ArrayList<int[][]> result = new ArrayList<int[][]>();
        for (int i = 0; i < yourQueens.length; i++) {
            result.addAll(Actions(gameState, yourQueens[i], theirQueens));

        }
        return result;
    }


    public static ArrayList<int[][]> Actions(int[][] gameState, int[] yourQueen, int[][] theirQueens) {
        ArrayList<int[][]> result = new ArrayList<int[][]>();

        for (int x = 0; x <= 9; x++) {
            for (int y = 0; y <= 9; y++) {
                int[] move = { x, y };
                if (isValidMove(move, yourQueen, gameState)) {
                    result.add(new int[][] { move, yourQueen });
                }
            }
        }

        return result;
    }
    public static ArrayList<Action> allMoves(int[][] gameState, int[][] yourQueens, int[][] theirQueens, boolean turn) {
        ArrayList<Action> moves = new ArrayList<Action>();
        ArrayList<int[][]> movement = actionsForAllQueens(gameState, yourQueens, theirQueens);
        for (int[][] move : movement) {
            moves.addAll(ArrowMove(movement, gameState, move[1], yourQueens, theirQueens, turn));
        }
        return moves;
    }

    public static boolean isValidMove(int[] move, int[] queen, int[][] gameState) {
        // check every square between
        if (gameState[move[0]][move[1]] != 0) {
            return false;
        } else if (move[0] == queen[0] && move[1] == queen[1]) {
            return false;
        } else if (move[0] == queen[0]) {
            // check x direction for obstacles
            return isValidX(move, queen, gameState);
        } else if (move[1] == queen[1]) {
            // check y direction for obstacles
            return isValidY(move, queen, gameState);
        } else if (isDiagonalMove(move, queen, gameState)) {
            // check diagonal for obstacles
            return isValidDiagonal(move, queen, gameState);
        }
        return false;
    }

    private static boolean isValidDiagonal(int[] move, int[] queen, int[][] gameState) {
        if (move[0] > queen[0] && move[1] > queen[1]) {
            for (int i = 1; i < move[0] - queen[0]; i++) {
                if (gameState[queen[0] + i][queen[1] + i] != 0) {
                    return false;
                }
            }
        } else if (move[0] > queen[0] && move[1] < queen[1]) {
            for (int i = 1; i < move[0] - queen[0]; i++) {
                if (gameState[queen[0] + i][queen[1] - i] != 0) {
                    return false;
                }
            }
        } else if (move[0] < queen[0] && move[1] > queen[1]) {
            for (int i = 1; i < queen[0] - move[0]; i++) {
                if (gameState[queen[0] - i][queen[1] + i] != 0) {
                    return false;
                }
            }
        } else if (move[0] < queen[0] && move[1] < queen[1]) {
            for (int i = 1; i < queen[0] - move[0]; i++) {
                if (gameState[queen[0] - i][queen[1] - i] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValidX(int[] move, int[] queen, int[][] gameState) {
        if (move[1] > queen[1]) {
            for (int i = queen[1] + 1; i < move[1]; i++) {
                if (gameState[move[0]][i] != 0) {
                    return false;
                }
            }
        } else {
            for (int i = move[1] + 1; i < queen[1]; i++) {
                if (gameState[move[0]][i] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValidY(int[] move, int[] queen, int[][] gameState) {
        if (move[0] > queen[0]) {
            for (int i = queen[0] + 1; i < move[0]; i++) {
                if (gameState[i][move[1]] != 0) {
                    return false;
                }
            }
        } else {
            for (int i = move[0] + 1; i < queen[0]; i++) {
                if (gameState[i][move[1]] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // check if the move is diagonal
    public static boolean isDiagonalMove(int[] move, int[] queen, int[][] gameState) {
        if ((Math.abs(move[0] - queen[0]) == Math.abs(move[1] - queen[1]))) {
            return true;
        }
        return false;
    }

    public static void makeMove(int[][] move, int[][] gameState) {
        int queen = gameState[move[1][0]][move[1][1]];
        gameState[move[0][0]][move[0][1]] = queen;
        gameState[move[1][0]][move[1][1]] = 0;
    }

    public static void undoMove(int[][] move, int[][] gameState) {
        int queen = gameState[move[0][0]][move[0][1]];
        gameState[move[1][0]][move[1][1]] = queen;
        gameState[move[0][0]][move[0][1]] = 0;
    }
    // for action type objects

    public static void main(String[] args) {
        // tests
        int[][] gameState = { // First
                { 0, 0, 0, 2, 0, 0, 2, 0, 0, 0 }, // 0
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 1
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 2
                { 2, 0, 0, 0, 0, 0, 0, 0, 0, 2 }, // 3
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 4
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 5
                { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, // 6
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 7
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 8
                { 0, 0, 0, 1, 0, 0, 1, 0, 0, 0 }// 9
        };// Sec 0 1 2 3 4 5 6 7 8 9

        int[][] yourQueens = { { 3, 0 }, { 0, 3 }, { 0, 6 }, { 3, 9 } };
        int[][] theirQueens = { { 6, 0 }, { 9, 3 }, { 9, 6 }, { 6, 9 } };
        // test actions
        
        // ArrowMove(result, gameState, yourQueens, theirQueens, depth);
        // System.out.println(Arrays.deepToString(gameState));
        long startTime = System.currentTimeMillis();
        boolean turn = true;
        takeAction t = new takeAction();
        Action a = new Action(gameState, yourQueens, theirQueens, turn, 0);
        Action best = t.findBestAction(a, 11);
        System.out.println("Time in seconds: " + (System.currentTimeMillis() - startTime)/1000);
        System.out.println(best.toString());
        makeMove(best, gameState);
        System.out.println(Arrays.deepToString(gameState));

    }

    private static void makeMove(Action a, int[][] gameState) {
        int[][] move = a.getMove();
        int[][] arrow = a.getArrow();
        int queen = gameState[move[1][0]][move[1][1]];
        gameState[move[0][0]][move[0][1]] = queen;
        gameState[move[1][0]][move[1][1]] = 0;
        gameState[arrow[0][0]][arrow[0][1]] = -1;
    }
    private static void undoMove(Action a, int[][] gameState) {
        int[][] move = a.getMove();
        int[][] arrow = a.getArrow();
        int queen = gameState[move[0][0]][move[0][1]];
        gameState[move[1][0]][move[1][1]] = queen;
        gameState[move[0][0]][move[0][1]] = 0;
        gameState[arrow[0][0]][arrow[0][1]] = 0;
    }
}
