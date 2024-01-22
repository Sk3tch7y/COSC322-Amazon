
package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GamePlayer;

/**
 * An example illustrating how to implement a GamePlayer
 * @author Yong Gao (yong.gao@ubc.ca)
 * Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer{

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private static String userName = "COSC322";
    private static String passwd = "COSC322";
 
	
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
	
    public static void main(String[] args) {				 
    	COSC322Test player = new COSC322Test(userName, passwd);
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
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
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	gamegui = new BaseGameGUI(this);
    }
 


    @Override
    public void onLogin() {
    	userName = gameClient.getUserName();
		if(gamegui != null && gameClient != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}	
    }

    @Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    	//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
		/*
		 GameMessage.GAME_STATE_BOARD (call BaseGameGui.setGameState(...) to set the game board)
		GameMessage.GAME_ACTION_MOVE (call BaseGameGui.updateGaemState(...) to update the game board)
		In a game player, upon receiving this message about your opponent's move, you will also need to calculate your move and send your move to the server using the method GameClient.sendMoveMessage(...) (these are the core tasks of this project you will have to by the middle of March)

		 */
		if(messageType.equals(msgDetails.equals("GAME_ACTION_MOVE"))) {
			gamegui.updateGameState(msgDetails);
			sendNextMove();
		}
		else if(messageType.equals(msgDetails.equals("GAME_STATE_BOARD"))){
			ArrayList<Integer> Gameboard = (ArrayList<Integer>)msgDetails.get("game-state");
			gamegui.setGameState(Gameboard);
		}
    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document. 
    	System.out.println(msgDetails);
    	return true;   	
    }
    
    
    private void sendNextMove() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'sendNextMove'");
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
		// TODO Auto-generated method stub
		return  this.gamegui;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
    	gameClient = new GameClient(userName, passwd, this);
			
	}
 
}//end of class
