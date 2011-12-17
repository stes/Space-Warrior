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
import sw.shared.GameConstants;
import sw.shared.data.Packer;
import sw.shared.data.PlayerData;
import sw.shared.data.PlayerInput;
import sw.shared.data.PlayerList;
import sw.shared.data.ServerInfo;
import sw.shared.data.Shot;
import sw.shared.data.Unpacker;

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

	private PlayerList _playerList;
	private IClient _client;
	private Player _localPlayer;

	private ArrayList<GameStateChangedListener> _gameStateChangedListener;

	private boolean _isConnected;

	/**
	 * creates an new GameController
	 */
	public GameController(IClient client)
	{
		_playerList = new PlayerList(GameConstants.MAX_PLAYERS);
		_gameStateChangedListener = new ArrayList<GameStateChangedListener>();
		_client = client;
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
	public PlayerList getPlayerList()
	{
		return _playerList;
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
		_playerList.update(snapshot);
		for (int i = 0; i < _playerList.size(); i++)
		{
			PlayerData d = _playerList.dataAt(i);
			if (d != null && d.isLocal())
			{
				_localPlayer.setDataSet(d);
			}
		}
		GameStateChangedEvent e = new GameStateChangedEvent(this);
		e.setLocalDataSet(_localPlayer.getDataSet());
		// TODO only pass a copy!
		e.setPlayerList(_playerList);
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
