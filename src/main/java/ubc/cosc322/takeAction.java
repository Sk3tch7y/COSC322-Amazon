package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class takeAction {

    private Random random = new Random();

    public takeAction(){
        random.setSeed(System.currentTimeMillis());
    }

    public double exploreState(Action action, int depth){
        if(depth < 0){
            makeMove(action, action.board);
            double score = evaluate(action);
            undoMove(action, action.board);
            return score;
        }
        if(action.possibleActions == null){
            generateChildren(action);
        }
        if(action.possibleActions.size() == 0){
            return Double.POSITIVE_INFINITY;
        }
        Action temp = action.possibleActions.get(random.nextInt(action.possibleActions.size()));
        depth = depth - 1;
        return exploreState(temp, depth);
    }
    //get the best action from the possible actions
    public Action findBestAction(Action a, int depth){
        double bestScore = Double.POSITIVE_INFINITY;
        int numMoves = 15;
        Action bestAction = null;
        if(a.possibleActions == null){
            generateChildren(a);
        }
        if(a.possibleActions.size() == 0){
            return null;
        }
        //if the number of possible actions is less than the depth, we can only search the number of possible actions
        if(a.possibleActions.size() <= numMoves){
            numMoves = a.possibleActions.size();
        }
        
        for(int i = 0; i < numMoves; i++){
            Action action = a.possibleActions.get(random.nextInt(a.possibleActions.size()));
            double score = exploreState(action, depth);
            //System.out.println("Action:" + action.toString() + " Score: " + score);
            if(score <= bestScore){
                bestScore = score;
                bestAction = action;
            }
        }
        return bestAction;
    }
    public void generateChildren(Action a){
        a.possibleActions = new LinkedList<Action>();
        a.possibleActions.addAll(AFactory.allMoves(a.board, a.myQueens, a.theirQueens, !a.turn));
    }
    public double evaluate(Action a){
        //evaluate the board
        ArrayList<Action> yourActions = AFactory.allMoves(a.board, a.myQueens, a.theirQueens, a.turn);
        ArrayList<Action> theirActions = AFactory.allMoves(a.board, a.theirQueens, a.myQueens, !a.turn);
        if(yourActions.size() == 0){
            return Double.NEGATIVE_INFINITY;
        }
        else if(theirActions.size() == 0){
            return Double.POSITIVE_INFINITY;
        }
        
        //System.out.println("Your Actions: " + yourActions.size() + " Their Actions: " + theirActions.size());
        return (yourActions.size()/theirActions.size());
        //return the ratio of their possible moves to your possible moves
        //game wants the lowest value possible
        
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
        gameState[arrow[0][0]][arrow[0][1]] = 0;
        gameState[move[1][0]][move[1][1]] = queen;
        gameState[move[0][0]][move[0][1]] = 0;
        
    }

    // TODO: implement the depth scaling factor
    
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
}
