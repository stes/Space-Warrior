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

import sw.client.gui.ShotPool;
import sw.client.player.HumanPlayer;
import sw.client.player.Player;
import sw.client.player.ai.AIPlayer;
import sw.shared.GameConstants;
import sw.shared.data.Packet;
import sw.shared.data.PlayerDataSet;
import sw.shared.data.PlayerInput;
import sw.shared.data.PlayerList;
import sw.shared.data.Shot;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */ 
public class GameController implements ClientListener, IGameStateManager
{
    private PlayerList _playerList;
    private IClient _client;
    private Player _localPlayer;
    private File _aiPlugin;
    
    private boolean _isConnected;

    /**
     * creates an new GameController
     */
    public GameController(IClient client, File aiPlugin)
    {
        _playerList = new PlayerList(GameConstants.MAX_PLAYERS);
        _client = client;
    }
    
    public void init()
    {
    	if (_aiPlugin.exists())
    	{
    		_localPlayer = AIPlayer.load(_aiPlugin);
    	}
    	else
    	{
    		_localPlayer = new HumanPlayer(this);
    	}
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
        
    @Override
    public void chatMessage(String name, String text) {}
   
    @Override
    public PlayerList getPlayerList()
    {
        return _playerList;
    }
    @Override
    public void shot(Packet packet)
    {
        Shot s = Shot.hole(packet);
        ShotPool.addShot(s);
    }

    @Override
	public void snapshot(Packet snapshot)
    {
        _playerList.update(snapshot);
        for (int i = 0; i < _playerList.size(); i++)
        {
            PlayerDataSet d = _playerList.dataAt(i);
            if (d != null && d.lokal())
            {
                _localPlayer.setDataSet(d);
            }
        }
    }

	@Override
	public void stateUpdated(PlayerInput input)
	{
        Packet p = input.pack();
        _client.sendPacket(p);
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

	private void setIsConnected(boolean _isConnected)
	{
		this._isConnected = _isConnected;
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
}
