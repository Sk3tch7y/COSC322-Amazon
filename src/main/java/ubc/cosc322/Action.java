package ubc.cosc322;
import java.util.LinkedList;

public class Action {
    private int[][] move;
    private int[][] arrow;
    private int[][] board;
    private int[][] theirQueens;
    private int[][] myQueens;
    private boolean isMax; //true if it's the AI's turn, false if it's the opponent's turn
    private int movePossibilityGain; //variable for move possibility gain/opponent move possibility gain
    private LinkedList<Action> possibleActions;
    
    public Action(int[][] move, int[][] arrow, int[][] board, int[][] myQueens, int[][] theirQueens){
        this.move = move;
        this.arrow = arrow;
        this.board = board;
        this.myQueens = myQueens;
        this.theirQueens = theirQueens;
        movePossibilityGain = 0;
    }
    public void explore(){
        if(possibleActions == null){
            generateChildren();
        }
        if(this.possibleActions.size() == 0){
            //if you win, set movePossibilityGain to 1
            //if you lose, set movePossibilityGain to -1
            //if you draw, set movePossibilityGain to 0


        }
        else{
            //pick a random action and explore it
            
            
        }
        
    }
    public void generateChildren(){
        possibleActions = new LinkedList<Action>();
        possibleActions.addAll(AFactory.allMoves(this.board, this.myQueens, theirQueens));
    }
    public int[][] getMove(){
        return move;
    }
    public int[][] getArrow(){
        return arrow;
    }
    public String toString(){
        return "Move: " + move[1][0] + ", " + move[1][1] + " to " + move[0][0] + ", " + move[0][1] + " Arrow: " + arrow[0][0] + ", " + arrow[0][1] + "\n";
    }
}
