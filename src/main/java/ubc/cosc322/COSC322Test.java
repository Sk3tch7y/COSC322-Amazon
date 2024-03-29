package ubc.cosc322;

import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsBoard;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

/**
 * An example illustrating how to implement a GamePlayer
 *
 * @author Yong Gao (yong.gao@ubc.ca)
 *         Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer {

	private static GameClient gameClient = null;
	private static BaseGameGUI gamegui = null;

	private static String userName = "Adam" + Math.round(Math.random()* 1000);
	private static String passwd = "WoahAPassword";
	
	private static boolean color = true; // true for black, false for white
	private static int turn = 0;
	//starting queens
	public static int[][] whiteQueens = { { 3, 0 }, { 0, 3 }, { 0, 6 }, { 3, 9 } };
	public static int[][] blackQueens = { { 6, 0 }, { 9, 3 }, { 9, 6 }, { 6, 9 } };
	public static int[][] state = new int[10][10];

	static AmazonsBoard board = new AmazonsBoard();

	public static void main(String[] args) {
		COSC322Test player = new COSC322Test(userName, passwd);
		//HumanPlayer player2 = new HumanPlayer();
		
		if (player.getGameGUI() == null) {
			player.Go();
		} else {
			BaseGameGUI.sys_setup();
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					player.Go();
				}
			});
		}
	}

	/**
	 * Any name and passwd
	 * @param userName
	 * @param passwd
	 */
	public COSC322Test(String userName, String passwd) {
		this.userName = userName;
		this.passwd = passwd;

		this.gamegui = new BaseGameGUI(this);
	}

	@Override
	public void onLogin() {
		userName = gameClient.getUserName();
		if (gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}
	}

	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		System.out.println(messageType);
		System.out.println(msgDetails);

		if (messageType.equals(GameMessage.GAME_STATE_BOARD)) {
			ArrayList<Integer> gameS = (ArrayList<Integer>) msgDetails.get("game-state");
			System.out.println("Game Board: " + gameS + "\n");
			gamegui.setGameState(gameS);
			board.setGameState(gameS);
			translateArr(gameS);
			System.out.println(Arrays.deepToString(state));
		} else if (messageType.equals(GameMessage.GAME_ACTION_START)) {

			this.setColor((String)msgDetails.get(AmazonsGameMessage.PLAYER_BLACK));
			System.out.println("Game is starting");
			//move if you are black
			if (color){
				System.out.println("Making a move");
				handleOpponentMove(msgDetails);
				//getGameClient().sendMoveMessage( );
			}
			
			
		} else if (messageType.equals(GameMessage.GAME_ACTION_MOVE)) {
			//System.out.println(msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR));
			gamegui.updateGameState(msgDetails);
			board.updateGameState(msgDetails);
			makeMove(msgDetails);
			translateArr(state);
			handleOpponentMove(msgDetails);

		} else if (messageType.equals("user-count-change")) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}
		return true;
	}

	

	
	public static void handleOpponentMove(Map<String, Object> msgDetails) {
		//Find an action to take and send it
		//int[][] gameState = translateArr((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
		//Action action = new Action(gameState, msgDetails.get(AmazonsGameMessage.ARROW_POS), blackQueens, whiteQueens, blackQueens, color);
		System.out.println("Making a move");
		takeAction t = new takeAction();
		Action action;
		if(!color){
			action = new Action(state, blackQueens, whiteQueens, color, 0);
		}else{
			action = new Action(state, whiteQueens, blackQueens, color, 0);
		}
		Action bestAction = t.findBestAction(action, 4);
		if(bestAction == null){
			System.out.println("No moves left");
			return;
		}
		//send the move
		int[] next = bestAction.getMove()[0];
		int[] move = bestAction.getMove()[1];
		int[] arrow = bestAction.getArrow()[0];
		ArrayList<Integer> queenFrom = convertToList(move);
		ArrayList<Integer> queenTo = convertToList(next);
		ArrayList<Integer> arrowPos = convertToList(arrow);
		System.out.println(Arrays.toString(move));
		System.out.println(Arrays.toString(next));
		System.out.println(Arrays.toString(arrow));
		makeMove(new int[][]{move, next}, arrow, color);
		board.updateGameState(queenFrom, queenTo, arrowPos);
		gameClient.sendMoveMessage(queenFrom, queenTo, arrowPos);
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put(AmazonsGameMessage.QUEEN_POS_CURR, queenFrom);
		msg.put(AmazonsGameMessage.QUEEN_POS_NEXT, queenTo);
		msg.put(AmazonsGameMessage.ARROW_POS, arrowPos);
		gamegui.updateGameState(msg);
		board.updateGameState(msg);
	}
	public static ArrayList<Integer> convertToList(int[] a){
		ArrayList<Integer> list = new ArrayList<>();
		for (int num : a) {
			list.add(num+1);
		}
		return list;
	}
	public static ArrayList<Integer> convertToList(){
		ArrayList<Integer> list = new ArrayList<>();
		for (int[] row : state) {
			for (int num : row) {
				list.add(num);
			}
		}
		return list;
	}
	public static void translateArr(ArrayList<Integer> arr){
		int count = 12;
		int wQueens = 0;
		int bQueens = 0;
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				if(count %11 == 0){
					count++;
				}
				state[i][j] = arr.get(count);
				if(state[i][j] == 1){
					if(wQueens < 4){
						whiteQueens[wQueens] = new int[]{i, j};
						wQueens++;
					}
				}
				else if(state[i][j] == 2){
					if(bQueens < 4){
						blackQueens[bQueens] = new int[]{i, j};
						bQueens++;
					}	
				}
				count++;
			}
		}
		System.out.println(Arrays.deepToString(state));
		System.out.println("White: " + Arrays.deepToString(whiteQueens));
		System.out.println("Black: " + Arrays.deepToString(blackQueens));
	}
	public static void translateArr(int[][] arr){
		
		int wQueens = 0;
		int bQueens = 0;
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				if(state[i][j] == 1){
					if(wQueens < 4){
						whiteQueens[wQueens] = new int[]{i, j};
						wQueens++;
					}
				}
				else if(state[i][j] == 2){
					if(bQueens < 4){
						blackQueens[bQueens] = new int[]{i, j};
						bQueens++;
					}	
				}
			}
		}
		System.out.println(Arrays.deepToString(state));
		System.out.println("White: " + Arrays.deepToString(whiteQueens));
		System.out.println("Black: " + Arrays.deepToString(blackQueens));
	}

	private static void makeMove(Map<String, Object> msgDetails) {
		ArrayList<Integer> QueenCur = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
		ArrayList<Integer> QueenNext = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
		ArrayList<Integer> arrowPos = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.ARROW_POS);
		int[] queenFrom = new int[] {QueenCur.get(0)-1, QueenCur.get(1)-1};
		int[][] move = new int[][] {queenFrom, new int[] {QueenNext.get(0)-1, QueenNext.get(1)-1}};
		int[] arrow = new int[]{arrowPos.get(0)-1, arrowPos.get(1)-1};
		int queen = state[move[1][0]][move[1][1]];
		state[move[0][0]][move[0][1]] = queen;
		state[move[1][0]][move[1][1]] = 0;
		state[arrow[0]][arrow[1]] = -1;
	}
	private static void makeMove(int[][]move , int[] arrow, boolean color) {
		int queen = state[move[1][0]][move[1][1]];
		state[move[0][0]][move[0][1]] = queen;
		state[move[1][0]][move[1][1]] = 0;
		state[arrow[0]][arrow[1]] = -1;
	}
	
	@Override
	public String userName() {
		return userName;
	}
	
	@Override
	public GameClient getGameClient() {
		// TODO Auto-generated method stub
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		return this.gamegui;
	}

	@Override
	public void connect() {
		gameClient = new GameClient(userName, passwd, this);
	}
	public void setColor(String blackPlayer){

		if (userName.equals(blackPlayer)){
			color = true;
		}
		else{
			color = false;
		}
	}
	

}// end of class
