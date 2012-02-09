package sw.client;

import sw.shared.GameEngine;
import sw.shared.data.entities.players.SpaceShip;

/**
 * A local game engine used in singleplayer games
 * @author Redix stes
 *
 */
public class ClientGameEngine extends GameEngine
{
	public ClientGameEngine()
	{
		super();
	}
	
	@Override
	public void invokePlayerWon(SpaceShip pl)
	{
		// TODO add functionality
	}

}
