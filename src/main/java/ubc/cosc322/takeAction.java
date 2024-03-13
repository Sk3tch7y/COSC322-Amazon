package ubc.cosc322;

import java.util.ArrayList; // Import ArrayList class
import java.util.Map;

public class takeAction<E> {

    public takeAction(Map<String, Object> msgDetails, boolean color, int turn) {
        
        ArrayList<Integer> current = (ArrayList<Integer>) msgDetails.get("game-state");
        int state[][] = new int[11][11];
        for(int i = 0;  i < current.size(); i++){
            state[i%11][(int)Math.floor(i/11)] = current.get(i);
        }
        
        ArrayList<Integer> possibleMoves = ActionFactory.Action(state, color, turn); 

    } 
    
}
