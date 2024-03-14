package ubc.cosc322;

import java.lang.reflect.Array;
import java.util.ArrayList;
import org.apache.commons.lang.math.IntRange;

public class ActionFactory {
    
    //depth formula:
    public static int turnNum = 0;
    public static int depthCalc(int turn){
        /*
         * since each turn the maximum number of possible moves decreases by a minimum of one we
         * can make calculate our branching factor on each turn. since each player has a maximum 
         * possible 40 moves in all direction based on the queen, 10 on each axis and diagonal 
         * from the center of the board(this is 1 greater than the actual realty of the player having to move one queen)
         * each turn we can subract one square from the possible move set in the future as the arrow 
         * blocks at least one possible move
         * thus 4(40)-t is equal to the number of possible branches from a specific 
         * outcome assuming the worst case of the player not blocking any more than one possible move for each
         * 
         * We are going to use this number for our scaling factor to increase the depth of our search  
         * as the number of possible branches decreases. since this is an upper limit and the odds of a minimal move being the opponents
         * first choice, we can adjust this more to expect less of a branching factor 
         *  2 is black and 1 is white, I think 3 is arrows but I cant find it in the documentation
         * for example
            +---+---+---+---+---+---+---+---+---+---+
            |   |   |   | 2 |   |   | 2 |   |   |   |
            +---+---+---+---+---+---+---+---+---+---+
            |   |   |   |   |   |   |   |   |   |   |
            +---+---+---+---+---+---+---+---+---+---+
            |   |   |   |   |   |   |   |   |   |   |
            +---+---+---+---+---+---+---+---+---+---+
            | 2 |   |   | 3 |   |   |   |   |   | 2 |
            +---+---+---+---+---+---+---+---+---+---+
            |   |   |   |   |   |   |   |   |   |   |
            +---+---+---+---+---+---+---+---+---+---+
            |   |   |   |   |   |   |   |   |   |   |
            +---+---+---+---+---+---+---+---+---+---+
            | 1 |   |   |   |   |   |   |   |   | 1 |
            +---+---+---+---+---+---+---+---+---+---+
            |   |   |   | 1 |   |   |   |   |   |   |
            +---+---+---+---+---+---+---+---+---+---+
            |   |   |   |   |   |   |   |   |   |   |
            +---+---+---+---+---+---+---+---+---+---+
            |   |   |   |   |   |   | 1 |   |   |   |
            +---+---+---+---+---+---+---+---+---+---+

            As the arrow eliminates major pathways for the top left and left queen
            restricting them to only two moves on one axis leaving the next branch to be 
            2(5 + 2 + 3 + 2) + (9 + 2+ 3+ 4) +  (2+3+5+2+6) which is far less than the 160 possible 
            branches we predicted on a terrible move. We treat the placement on arrows as a separate
            move as it is a separate action and thus we can expect the branching factor to be less than 160
            since this is a relaxed action generator we must check the possibility of the actions. 
         */
    
        return 5; 
    }
    //relaxed move set
    public static int[][] startAction(int[][] gameState, boolean turn, int[][] yourQueens, int[][] theirQueens){
        int depth = depthCalc(turnNum);
        return Action(gameState, depth, yourQueens, theirQueens);
    }
    
    //search and select from possible moves for black
    public static int[][] Action(int[][] gameState, int depth, int[][] yourQueens, int[][] theirQueens){
        //iterate through depth until 0
        depth = depth - 1;
        if(depth == 0){
            return gameState;
        }
        //generate possible moves
        
        //uses the formula for the number of possible moves where a queen can move to any {x+cx, y+cy} where |cx| = |cy|
        for(int i = 0; i < yourQueens.length; i++){
            int[] queen = yourQueens[i];
            //TODO: implement back propagation for each state using a heuristic function
            //might have to add a turn dependent variable to help distinguish between when the algorithm should add the 
            //max or min value based on alpha beta pruning
            int benefit = 0;
            //repeats each value of x and y for each queen
            for(int x = 0; x < 10; x++){
                // this might cause the player to not move if removed(This stops the system from considering not moving as a move
                // which shouldn't be a problem if our game board is properly assessed for possible moves)
                if (x != queen[0] && gameState[x][queen[1]] == 0) {
                    //this makes a move and then undoes it after the recursive call to save on memory
                    //creating new arrays every time is expensive so we just manipulate one array
                    int[] move = new int[]{x, queen[1]};
                    makeMove(move, queen, gameState);
                    Action(gameState, depth, theirQueens, yourQueens);
                    undoMove(move, queen, gameState);
                }
                for (int y = 0; y < 10; y++) {
                    
                    if (y != queen[1] && gameState[queen[0]][y] == 0) {
                        int[] move = new int[]{queen[0], y};
                        makeMove(move, queen, gameState);
                        Action(gameState, depth, theirQueens, yourQueens);
                        undoMove(move, queen, gameState);
                    }
                    if (x-y == queen[0]-queen[1] && gameState[x][y] == 0) {
                        int[] move = new int[]{x, y};
                        makeMove(move, queen, gameState);
                        Action(gameState, depth, theirQueens, yourQueens);
                        undoMove(move, queen, gameState);
                    }
                }
            }
        }
        return gameState;
    }
    public static void makeMove(int[] move, int[] queen, int[][] gameState){
        gameState[move[0]][move[1]] = gameState[queen[0]][queen[1]];
        gameState[queen[0]][queen[1]] = 0;
    }
    public static void undoMove(int[] move, int[] queen, int[][] gameState){
        gameState[queen[0]][queen[1]] = gameState[move[0]][move[1]];
        gameState[move[0]][move[1]] = 0;
    }
    
    public static int[][] generateActionOutcome(int[][] move, int[][] gameState, int depth){
        return gameState;
    }
    
    //evaluate benefit ratio: your moves/their moves
    public int evaluateBenefits(int[][] gameState, int[][] yourQueens, int[][] theirQueens){
        return 0;
    }
}
