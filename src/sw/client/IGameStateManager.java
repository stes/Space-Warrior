package sw.client;

import sw.shared.PlayerList;

public interface IGameStateManager
{
	/**
	 * @return a list of all players currently participating in the game
	 */
	public PlayerList getPlayerList();
	
	//TODO further methods that can be generalized for easy connection
	// between player and controller
}
