package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
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

	private GameClient gameClient = null;
	private BaseGameGUI gamegui = null;

	private static String userName = "Adam";
	private static String passwd = "WoahAPassword";
	
	private static boolean color = true; // true for black, false for white
	private static int turn = 0;
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
		while(true){
			System.out.println();
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
			System.out.println("Game Board: " + gameS);
			gamegui.setGameState(gameS);
		} else if (messageType.equals(GameMessage.GAME_ACTION_START)) {

			this.setColor();
			System.out.println("Game is starting");
			turn = 0;
			//move if you are black
			if (color){
				
			}
			else{
				turn++;
			}
			
		} else if (messageType.equals(GameMessage.GAME_ACTION_MOVE)) {
			System.out.println(msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR));
			gamegui.updateGameState(msgDetails);

			handleOpponentMove(msgDetails);

		} else if (messageType.equals("user-count-change")) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}
		return true;
	}

	private void handleOpponentMove(Map<String, Object> msgDetails) {
		takeAction action = new takeAction(msgDetails, color, turn);
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
	
	public void setColor(){
		if (userName.equals("black")){
			color = true;
		}
		else{
			color = false;
		}
	}

}// end of class
