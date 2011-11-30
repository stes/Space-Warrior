package sw.client;

import sw.client.player.Player;
import sw.shared.data.PlayerInput;
import sw.shared.data.PlayerList;

public interface IGameStateManager
{
	/**
	 * @return a list of all players currently participating in the game
	 */
	public PlayerList getPlayerList();
	
	public Player getLocalPlayer();
	
	public boolean isReady();
	
	//TODO further methods that can be generalized for easy connection
	// between player and controller
	
	public void stateUpdated(PlayerInput input);
}
