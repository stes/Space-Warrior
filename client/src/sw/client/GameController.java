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
import sw.shared.Packettype;
import sw.shared.data.GameWorld;
import sw.shared.data.PlayerInput;
import sw.shared.data.entities.GameState;
import sw.shared.data.entities.players.SpaceShip;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class GameController implements ClientConnectionListener, ClientMessageListener,
		IGameStateManager
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
//	private IClient _client;
//	private HashMap<String, SpaceShip> _players;
	private Player _localPlayer;

	private long _prevLastSnap;
	private long _lastSnap;

	private ArrayList<GameStateChangedListener> _gameStateChangedListener;

	private boolean _isConnected;

	private boolean _rendering;

	/**
	 * creates an new GameController
	 */
//	public GameController(IClient client)
//	{
//		_prevWorld = new GameWorld();
//		_world = new GameWorld();
//		_gameStateChangedListener = new ArrayList<GameStateChangedListener>();
//		_client = client;
//		_players = new SpaceShip[0];
//		_rendering = false;
//	}

	@Override
	public void addGameStateChangedListener(GameStateChangedListener l)
	{
		_gameStateChangedListener.add(l);
	}

	@Override
	public void chatMessage(String name, String text)
	{}

	@Override
	public void connected()
	{
		this.setIsConnected(true);
		this.init();
	}

	@Override
	public void disconnected(String reason)
	{
		this.setIsConnected(false);
	}

	@Override
	public synchronized GameWorld getGameWorld()
	{
		return _world;
	}

	public boolean getIsPlayerHuman()
	{
		return (_localPlayer instanceof HumanPlayer);
	}

	@Override
	public Player getLocalPlayer()
	{
		return _localPlayer;
	}

	@Override
	public synchronized SpaceShip[] getPlayerList()
	{
		return _world.getPlayers();
	}

	@Override
	public synchronized GameWorld getPrevGameWorld()
	{
		return _prevWorld;
	}

	public void init()
	{
		System.out.println("init");
//		_localPlayer = new SampleAI(this);
		if (GameController._runAI && GameController._aiPlugin.exists())
		{
			try
			{
				SWFrame.out.println("Successfully loaded AI Player.");
				_localPlayer = AIPlayerLoader.load(GameController._aiPlugin, this);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				SWFrame.out.println("Unable to load AI Player. Loading default player instead");
				_localPlayer = new HumanPlayer(this);
			}
		}
		else
		{
			SWFrame.out.println("no AI player selected, using default player");
			_localPlayer = new HumanPlayer(this);
		}
		this.invokePlayerInit(new GameStateChangedEvent(this));
	}

	public boolean isConnected()
	{
		return _isConnected;
	}

	@Override
	public boolean isReady()
	{
		return true;
//		return this.isConnected();
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
		_prevWorld = _world;
		_world = world;
//		_players = _world.getPlayers();
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
	public void snapshot(Unpacker snapshot)
	{
//		GameWorld world = new GameWorld();
//		world.fromSnap(snapshot);
		this.setGameworld(_world);
		for (SpaceShip pl : _world.getPlayers())
		{
			if (pl.isLocal())
			{
				_localPlayer.setDataSet(pl);
			}
		}
		GameState[] state = _world.getEntitiesByType(Packettype.SNAP_GAMESTATE, new GameState[] {});
		if (state.length >= 1)
		{
			if (state[0].isNewRoundStarted())
			{
				this.newRound();
			}
		}
		GameStateChangedEvent e = new GameStateChangedEvent(this);
		e.setLocalDataSet(_localPlayer.getDataSet());
		// TODO only pass a copy!
		e.setGameWorld(_world);
		this.invokeStateChanged(e);
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

	@Override
	public void stateUpdated(PlayerInput input)
	{
//		Packer p = input.pack();
//		_client.sendPacket(p);
		this.processPlayerInput(_localPlayer.getDataSet().getName(), input);
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

	private void invokeStateChanged(GameStateChangedEvent e)
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

	private void newRound()
	{
		// TODO improve, add loser/winner to event
		GameStateChangedEvent e = new GameStateChangedEvent(this);
		this.invokeNewRound(e);
	}

	private void setIsConnected(boolean _isConnected)
	{
		this._isConnected = _isConnected;
	}

	private GameState _gameState;

	/**
	 * Creates a new game controller
	 * 
	 * @param server
	 *            IServer
	 */
	public GameController()
	{
		_world = new GameWorld();
		//_players = new HashMap<String, SpaceShip>();
		_gameState = new GameState();
		_world.insert(_gameState);
		
		_prevWorld = new GameWorld();
		_world = new GameWorld();
		_gameStateChangedListener = new ArrayList<GameStateChangedListener>();
//		_client = client;
//		_players = new SpaceShip[0];
		_rendering = false;
	}

	public void broadcastSnapshots()
	{
		for (SpaceShip pl : _world.getPlayers())
		{
			Packer snapshot = new Packer(Packettype.SV_SNAPSHOT);
			_world.snap(snapshot, pl.getName());
//			_server.sendPacket(pl.getName(), snapshot);
			this.snapshot(new Unpacker(snapshot.toByteArray()));
		}
	}

	public void playerConnected(String name, int imageID)
	{
		SpaceShip newPl = new SpaceShip(name, imageID);
		newPl.setLocal(true);
		_world.insert(newPl);
	}

	public void playerLeft(String name, String reason)
	{
//		_players.get(name).destroy();
//		_players.remove(name);
	}

	public void processPlayerInput(String name, PlayerInput input)
	{
//		_players.get(name).setInput(input);
		SpaceShip s = ((SpaceShip)_world.getEntity(0));
		s.setInput(input);
//		_localPlayer.setDataSet(((SpaceShip)_world.getEntity(name)));
	}

	/**
	 * starts a new game
	 */
	public void startGame()
	{
		_world.removeShotEntities();
		for (SpaceShip pl : _world.getPlayers())
		{
			do
			{
				pl.respawn();
			}
			while(_world.checkCollision(pl));
		}
		_gameState.startNewRound();
	}


	public void tick()
	{
		this.checkTurn();
		_world.tick();
	}

	private void checkTurn()
	{
		int alive = 0;
		
		SpaceShip[] players = _world.getPlayers();
		for (SpaceShip pl : players)
		{
			if (pl.isAlive())
			{
				alive++;
			}
		}

		if ((alive == 1 && players.length > 1) || (alive == 0 && players.length == 1))
		{
			if (alive == 1)
			{
				for (SpaceShip pl : players)
				{
					Packer info = new Packer(Packettype.SV_CHAT_MESSAGE);
					info.writeUTF("Server");
					info.writeUTF(pl.getName() + " has won the round!");
					break;
				}
			}
			this.startGame();
		}
	}
}
