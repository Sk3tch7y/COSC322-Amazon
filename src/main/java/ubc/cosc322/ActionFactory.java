package ubc.cosc322;

import java.lang.reflect.Array;
import java.util.ArrayList;
import org.apache.commons.lang.math.IntRange;

public class ActionFactory {
    
    //depth formula:
    public static int depth = 0;
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
    //relaxed actions
    public static ArrayList<Integer> Action(int[][] gameState, boolean turn, int depth){
        ArrayList<Integer> actions = new ArrayList<Integer>();

        ArrayList<Integer> possibleActions = new ArrayList<Integer>();
        depthCalc(turnNum);
        if(turn){
            possibleActions = blackAction(gameState, possibleActions);
        }else{
            possibleActions = whiteAction(gameState, possibleActions);
        }
        return null;
    }
    
    //search and select from possible moves for black
    public static ArrayList<Integer> blackAction(int[][] gameState, ArrayList<Integer> possibleActions){
        /*
         * 
         */
        for(int i =1; i<10; i++){
            int[] lastMoveY;
            int current = 0;
            for(int j = 1; j<10; j++){
                current = gameState[i][j];
                if(current ==3 || current == 1){
                    lastMoveY = {i,j};
                }
                else{
                    
                }
            }

        }
        return gameState;
    }
    //search and select from possible moves for white
    public static ArrayList<Integer> whiteAction(int[][] gameState, ArrayList<Integer> possibleActions){
        
        return gameState;
    }
}
