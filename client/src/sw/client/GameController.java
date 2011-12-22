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
import sw.client.gui.ShotPool;
import sw.client.player.HumanPlayer;
import sw.client.player.Player;
import sw.client.player.ai.AIPlayerLoader;
import sw.shared.Packer;
import sw.shared.Packettype;
import sw.shared.Unpacker;
import sw.shared.data.GameWorld;
import sw.shared.data.PlayerData;
import sw.shared.data.PlayerInput;
import sw.shared.data.ServerInfo;
import sw.shared.data.Shot;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class GameController implements ClientListener, IGameStateManager
{
	private static File _aiPlugin;
	private static boolean _runAI = false;

	public static void setAIPlugin(File source)
	{
		_aiPlugin = source;
		_runAI = true;
	}

	private GameWorld _world;
	private IClient _client;
	private PlayerData[] _players;
	private Player _localPlayer;

	private ArrayList<GameStateChangedListener> _gameStateChangedListener;

	private boolean _isConnected;

	/**
	 * creates an new GameController
	 */
	public GameController(IClient client)
	{
		_world = new GameWorld();
		_gameStateChangedListener = new ArrayList<GameStateChangedListener>();
		_client = client;
		_players = new PlayerData[0];
		_localPlayer = new HumanPlayer(this);
	}

	public void addGameStateChangedListener(GameStateChangedListener l)
	{
		_gameStateChangedListener.add(l);
	}
	
	public void removeGameStateChangedListener(GameStateChangedListener l)
	{
		_gameStateChangedListener.remove(l);
	}
	
	@Override
	public void chatMessage(String name, String text)
	{
	}

	@Override
	public void connected()
	{
		setIsConnected(true);
		this.init();
	}

	@Override
	public void disconnected(String reason)
	{
		setIsConnected(false);
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
	public GameWorld getGameWorld()
	{
		return _world;
	}
	
	@Override
	public PlayerData[] getPlayerList()
	{
		return _players;
	}

	public void init()
	{
		if (_runAI && _aiPlugin.exists())
		{
			try
			{
				System.out.println("Successfully loaded AI Player.");
				_localPlayer = AIPlayerLoader.load(_aiPlugin, this);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.out
						.println("Unable to load AI Player. Loading default player instead");
				_localPlayer = new HumanPlayer(this);
			}
		}
		else
		{
			System.out.println("no AI player selected, using default player");
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
		return this.isConnected();
	}

	@Override
	public void serverInfo(ServerInfo info)
	{
	}

	@Override
	public void shot(Unpacker packet)
	{
		Shot s = Shot.read(packet);
		ShotPool.addShot(s);
	}

	@Override
	public void snapshot(Unpacker snapshot)
	{
		_world.fromSnap(snapshot);
		_players = _world.getEntitiesByType(Packettype.SNAP_PLAYERDATA, _players);
		for (PlayerData pl : _players)
		{
			if (pl.isLocal())
				_localPlayer.setDataSet(pl);
		}
		GameStateChangedEvent e = new GameStateChangedEvent(this);
		e.setLocalDataSet(_localPlayer.getDataSet());
		// TODO only pass a copy!
		e.setGameWorld(_world);
		this.invokeStateChanged(e);
	}

	@Override
	public void stateUpdated(PlayerInput input)
	{
		Packer p = input.pack();
		_client.sendPacket(p);
	}

	@SuppressWarnings("unused")
	private void invokeNewRound(GameStateChangedEvent e)
	{
		if (_gameStateChangedListener == null
				|| _gameStateChangedListener.size() == 0)
			return;
		for (GameStateChangedListener l : _gameStateChangedListener)
			l.newRound(e);
	}

	private void invokeStateChanged(GameStateChangedEvent e)
	{
		if (_gameStateChangedListener == null
				|| _gameStateChangedListener.size() == 0)
			return;
		for (GameStateChangedListener l : _gameStateChangedListener)
			l.gameStateChanged(e);
	}
	
	private void invokePlayerInit(GameStateChangedEvent e)
	{
		if (_gameStateChangedListener == null
				|| _gameStateChangedListener.size() == 0)
			return;
		for (GameStateChangedListener l : _gameStateChangedListener)
			l.playerInit(e);
	}

	private void setIsConnected(boolean _isConnected)
	{
		this._isConnected = _isConnected;
	}
}
