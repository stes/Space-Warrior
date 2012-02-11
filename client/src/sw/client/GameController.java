/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011 Redix stes Abbadonn
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

import java.io.File;
import java.util.ArrayList;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.GameStateChangedListener;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.HumanPlayer;
import sw.client.player.Player;
import sw.client.plugins.AIPlayerLoader;
import sw.shared.data.GameWorld;
import sw.shared.data.entities.players.SpaceShip;

/**
 * Controls all gameplay related issues
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public abstract class GameController implements IGameStateManager
{
	private static File _aiPlugin;
	private static boolean _runAI = false;

	public static void setAIPlugin(File source)
	{
		GameController._aiPlugin = source;
		GameController._runAI = true;
	}

	private GameWorld _prevWorld;
	private GameWorld _world;
	private SpaceShip[] _players;
	private Player _localPlayer;

	private long _prevLastSnap;
	private long _lastSnap;

	private ArrayList<GameStateChangedListener> _gameStateChangedListener;

	private boolean _rendering;

	/**
	 * creates an new GameController
	 */
	public GameController()
	{
		_prevWorld = new GameWorld();
		setWorld(new GameWorld());
		_gameStateChangedListener = new ArrayList<GameStateChangedListener>();
		_players = new SpaceShip[0];
		_rendering = false;
	}

	@Override
	public void addGameStateChangedListener(GameStateChangedListener l)
	{
		_gameStateChangedListener.add(l);
	}

	@Override
	public synchronized GameWorld getGameWorld()
	{
		return getWorld();
	}

	public boolean getIsPlayerHuman()
	{
		return (getLocalPlayer() instanceof HumanPlayer);
	}

	@Override
	public Player getLocalPlayer()
	{
		return _localPlayer;
	}

	@Override
	public synchronized SpaceShip[] getPlayerList()
	{
		return getPlayers();
	}

	@Override
	public synchronized GameWorld getPrevGameWorld()
	{
		return _prevWorld;
	}

	public void init()
	{
		Player local = null;
		if (GameController._runAI && GameController._aiPlugin.exists())
		{
			try
			{
				local = AIPlayerLoader.load(GameController._aiPlugin, this);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				SWFrame.out.println("Unable to load AI Player. Loading default player instead");
			}
		}
		if (local == null)
		{
			SWFrame.out.println("Using default player");
			setLocalPlayer(new HumanPlayer(""));
		}
		else
		{
			SWFrame.out.println("Successfully loaded AI Player.");
			setLocalPlayer(local);
		}
		getLocalPlayer().setStateManager(this);
		this.invokePlayerInit(new GameStateChangedEvent(this));
	}

	public void removeGameStateChangedListener(GameStateChangedListener l)
	{
		_gameStateChangedListener.remove(l);
	}

	public synchronized void setGameworld(GameWorld world)
	{
		while (_rendering)
		{
			try
			{
				this.wait();
			}
			catch (InterruptedException e1)
			{}
		}
		_prevLastSnap = _lastSnap;
		_lastSnap = System.currentTimeMillis();
		_prevWorld = getWorld();
		setWorld(world);
		_players = getWorld().getPlayers();
	}

	@Override
	public synchronized void setRendering(boolean render)
	{
		_rendering = render;
		if (_rendering == false)
		{
			this.notify();
		}
	}

	@Override
	public synchronized double snapTime() // TODO: find a better name
	{
		if (_lastSnap == 0 || _prevLastSnap == 0)
		{
			return 1;
		}
		return (System.currentTimeMillis() - _lastSnap) / (double) (_lastSnap - _prevLastSnap);
	}

	private void invokeNewRound(GameStateChangedEvent e)
	{
		if (_gameStateChangedListener == null || _gameStateChangedListener.size() == 0)
		{
			return;
		}
		for (GameStateChangedListener l : _gameStateChangedListener)
		{
			l.newRound(e);
		}
	}

	private void invokePlayerInit(GameStateChangedEvent e)
	{
		if (_gameStateChangedListener == null || _gameStateChangedListener.size() == 0)
		{
			return;
		}
		for (GameStateChangedListener l : _gameStateChangedListener)
		{
			l.playerInit(e);
		}
	}

	protected void invokeStateChanged(GameStateChangedEvent e)
	{
		if (_gameStateChangedListener == null || _gameStateChangedListener.size() == 0)
		{
			return;
		}
		for (GameStateChangedListener l : _gameStateChangedListener)
		{
			l.gameStateChanged(e);
		}
	}

	protected void newRound()
	{
		// TODO improve, add loser/winner to event
		GameStateChangedEvent e = new GameStateChangedEvent(this);
		this.invokeNewRound(e);
	}

	protected SpaceShip[] getPlayers()
	{
		return _players;
	}

	protected void setLocalPlayer(Player localPlayer)
	{
		this._localPlayer = localPlayer;
	}

	public GameWorld getWorld()
	{
		return _world;
	}

	private void setWorld(GameWorld world)
	{
		this._world = world;
	}
}
