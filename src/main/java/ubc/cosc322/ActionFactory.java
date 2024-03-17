package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;

public class ActionFactory {
    // TODO: implement the depth scaling factor
    public static int depth = 5;
    public static int depthCalc(int turn) {
        /*
         * since each turn the maximum number of possible moves decreases by a minimum
         * of one we
         * can make calculate our branching factor on each turn. since each player has a
         * maximum
         * possible 40 moves in all direction based on the queen, 10 on each axis and
         * diagonal
         * from the center of the board(this is 1 greater than the actual realty of the
         * player having to move one queen)
         * each turn we can subract one square from the possible move set in the future
         * as the arrow
         * blocks at least one possible move
         * thus 4(40)-t is equal to the number of possible branches from a specific
         * outcome assuming the worst case of the player not blocking any more than one
         * possible move for each
         * 
         * We are going to use this number for our scaling factor to increase the depth
         * of our search
         * as the number of possible branches decreases. since this is an upper limit
         * and the odds of a minimal move being the opponents
         * first choice, we can adjust this more to expect less of a branching factor
         * 2 is black and 1 is white, I think 3 is arrows but I cant find it in the
         * documentation
         * for example
         * +---+---+---+---+---+---+---+---+---+---+
         * | | | | 2 | | | 2 | | | |
         * +---+---+---+---+---+---+---+---+---+---+
         * | | | | | | | | | | |
         * +---+---+---+---+---+---+---+---+---+---+
         * | | | | | | | | | | |
         * +---+---+---+---+---+---+---+---+---+---+
         * | 2 | | | 3 | | | | | | 2 |
         * +---+---+---+---+---+---+---+---+---+---+
         * | | | | | | | | | | |
         * +---+---+---+---+---+---+---+---+---+---+
         * | | | | | | | | | | |
         * +---+---+---+---+---+---+---+---+---+---+
         * | 1 | | | | | | | | | 1 |
         * +---+---+---+---+---+---+---+---+---+---+
         * | | | | 1 | | | | | | |
         * +---+---+---+---+---+---+---+---+---+---+
         * | | | | | | | | | | |
         * +---+---+---+---+---+---+---+---+---+---+
         * | | | | | | | 1 | | | |
         * +---+---+---+---+---+---+---+---+---+---+
         * 
         * As the arrow eliminates major pathways for the top left and left queen
         * restricting them to only two moves on one axis leaving the next branch to be
         * 2(5 + 2 + 3 + 2) + (9 + 2+ 3+ 4) + (2+3+5+2+6) which is far less than the 160
         * possible
         * branches we predicted on a terrible move. We treat the placement on arrows as
         * a separate
         * move as it is a separate action and thus we can expect the branching factor
         * to be less than 160
         * since this is a relaxed action generator we must check the possibility of the
         * actions.
         */

        return 5;
    }
    public static int monteCarlos(int[][] gameState, int[][] nextQueens, int[][] previousQueens, int depth){
        //assess depth to search until
        //use actions to find a queen move and an arrow placement
        /*
        if(depth == -1){
            return;
        }
         */


        return 0;
    }
    public static void ArrowMove(int[][] gameState, int[][] yourQueens, int[][] theirQueens, int depth) {
        
        ArrayList<int[][]> movement = Actions(gameState, yourQueens, theirQueens) ;
        int possibleMoves = movement.size();
        ArrayList<Action> actions = new ArrayList<Action>();
        for(int[][] move : movement){
            makeMove(move, gameState);
            ArrayList<int[][]> arrows = Actions(gameState, yourQueens, theirQueens);
            for(int[][] arrow : arrows){
                actions.add(new Action(move, arrow));
            }
            undoMove(move, gameState);

        }
        //for each action, generate the possible arrows and generate your possible moves minus your opponents possible moves
        
    }
    
    public static ArrayList<int[][]> Actions(int[][] gameState, int[][] yourQueens, int[][] theirQueens) {
        ArrayList<int[][]> result = new ArrayList<int[][]>();
        for (int i = 0; i < yourQueens.length; i++) {
            for (int x = 0; x <= 9; x++) {
                for (int y = 0; y <= 9; y++) {
                    int[] move = { x, y };
                    if (isValidMove(move, yourQueens[i], gameState)) {
                        result.add(new int[][]{move, yourQueens[i]});
                    }
                }
            }
        }
        return result;
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
        if(move[0] > queen[0] && move[1] > queen[1]){
            for(int i = 1; i < move[0] - queen[0]; i++){
                if(gameState[queen[0] + i][queen[1] + i] != 0){
                    return false;
                }
            }
        } else if(move[0] > queen[0] && move[1] < queen[1]){
            for(int i = 1; i < move[0] - queen[0]; i++){
                if(gameState[queen[0] + i][queen[1] - i] != 0){
                    return false;
                }
            }
        } else if(move[0] < queen[0] && move[1] > queen[1]){
            for(int i = 1; i < queen[0] - move[0]; i++){
                if(gameState[queen[0] - i][queen[1] + i] != 0){
                    return false;
                }
            }
        } else if(move[0] < queen[0] && move[1] < queen[1]){
            for(int i = 1; i < queen[0] - move[0]; i++){
                if(gameState[queen[0] - i][queen[1] - i] != 0){
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

    public static int[][] generateActionOutcome(int[][] move, int[][] gameState, int depth) {
        return gameState;
    }

    // evaluate benefit ratio: your moves/their moves
    public int evaluateBenefits(int[][] gameState, int[][] yourQueens, int[][] theirQueens) {
        return 0;
    }

    public static void main(String[] args) {
        // tests
        int[][] gameState = {                   //First
                { 0, 0, 0, 2, 0, 0, 2, 0, 0, 0 },//0
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//1
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//2
                { 2, 0, 0, 1, 0, 0, 3, 0, 0, 2 },//3
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//4
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//5
                { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },//6
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//7
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//8
                { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 }//9
        };//Sec   0  1  2  3  4  5  6  7  8  9

        int[][] yourQueens = { { 3, 3 },{ 0, 3 },  { 6, 0 }, { 9, 3 } };
        int[][] theirQueens = { { 0, 6 }, { 3, 9 }, { 6, 9 }, { 9, 6 } };
        // test actions
        ArrayList<int[][]> result = Actions(gameState, yourQueens, theirQueens);
        System.out.println(Arrays.deepToString(result.toArray()));
    }
}
