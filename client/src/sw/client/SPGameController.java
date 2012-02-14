/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011-2012 Redix stes Abbadonn
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.client;

import java.util.ArrayList;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.player.Player;
import sw.client.player.ai.AIPlayer;
import sw.client.player.ai.bots.RandomBot;
import sw.shared.GameConstants.Images;
import sw.shared.Packettype;
import sw.shared.data.PlayerInput;
import sw.shared.data.entities.GameState;
import sw.shared.data.entities.players.SpaceShip;

/**
 * Controls all gameplay related issues in a singleplayer game
 * 
 * @author Redix, stes
 * @version 25.11.11
 */
public class SPGameController extends GameController
{
	private static SPGameEngine _gameEngine = new SPGameEngine();
	
	public static void reset()
	{
		_gameEngine = new SPGameEngine();
	}
	
	private ArrayList<Player> _opponents;
	
	public SPGameController()
	{
		super();
		_opponents = new ArrayList<Player>();
		_gameEngine.addListener(this);
	}
	
	private SPGameController(AIPlayer player)
	{
		super();
		setLocalPlayer(player);
		player.setStateManager(this);
		_gameEngine.addListener(this);
	}
	
	@Override
	public void init()
	{
		this.init("noname", Images.SHIP_1.getID());
	}
	
	public void init(String name, int imageID)
	{
		super.init(name, imageID);
		_opponents.add(new SPGameController(new RandomBot("Killer", Images.SHIP_2.getID())).getLocalPlayer());
		_opponents.add(new SPGameController(new RandomBot("Simple", Images.SHIP_3.getID())).getLocalPlayer());
		_opponents.add(new SPGameController(new RandomBot("Simple2", Images.SHIP_4.getID())).getLocalPlayer());
		
		_gameEngine.addPlayer(name, imageID, true);
		for (Player p : _opponents)
		{
			_gameEngine.addPlayer(p.getDataSet().getName(), p.getDataSet().getImageID());
		}
		_gameEngine.startGame();
		_gameEngine.start();
	}

	@Override
	public void stateUpdated(PlayerInput input)
	{
		_gameEngine.playerInput(getLocalPlayer().getDataSet().getName(), input);
	}

	public void snapshot()
	{
		this.setGameworld(_gameEngine.getWorld());
		for (SpaceShip pl : getPlayers())
		{
			if (pl.getName().equals(getLocalPlayer().getDataSet().getName()))
			{
				getLocalPlayer().setDataSet(pl);
			}
		}
		GameState[] state = getWorld().getEntitiesByType(Packettype.SNAP_GAMESTATE, new GameState[] {});
		if (state.length >= 1)
		{
			if (state[0].isNewRoundStarted())
			{
				this.newRound();
			}
		}
		GameStateChangedEvent e = new GameStateChangedEvent(this);
		e.setLocalDataSet(getLocalPlayer().getDataSet());
		// TODO only pass a copy!
		e.setGameWorld(getWorld());
		this.invokeStateChanged(e);
	}

	@Override
	public boolean isReady()
	{
		return true;
	}
}
