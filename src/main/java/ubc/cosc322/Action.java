package ubc.cosc322;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Action {
    private Random random = new Random();
    private int[][] move;
    private int[][] arrow;
    int[][] board;
    int[][] theirQueens;
    int[][] myQueens;
    boolean turn; //true if it's the AI's turn, false if it's the opponent's turn
    private int moveGain; //variable for move possibility gain/opponent move possibility gain
    LinkedList<Action> possibleActions;

    public Action(int[][] board, int[][] myQueens, int[][] theirQueens, boolean turn, int moveGain){
        this.move = null;
        this.arrow = null;
        this.board = board;
        this.myQueens = myQueens;
        this.theirQueens = theirQueens;
        this.turn = turn;
        this.moveGain = moveGain;
        random.setSeed(System.currentTimeMillis());
    }
    
    public Action(int[][] move, int[][] arrow, int[][] board, int[][] myQueens, int[][] theirQueens, boolean turn){
        this.move = move;
        this.arrow = arrow;
        this.board = board;
        this.myQueens = myQueens;
        this.theirQueens = theirQueens;
        this.turn = turn;
        moveGain = 0;
        random.setSeed(System.currentTimeMillis());
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
    public int getDepth(){
        return 6;
    }

    
    
}
