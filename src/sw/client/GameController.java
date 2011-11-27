/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 *     Copyright (C) 2011 Redix stes Abbadonn
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.client;

import sw.client.components.ShotPool;
import sw.client.player.HumanPlayer;
import sw.client.player.Player;
import sw.shared.GameConstants;
import sw.shared.data.Packet;
import sw.shared.data.PlayerDataSet;
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

    private boolean _isReady;
    
    /**
     * creates an new GameController
     */
    public GameController(IClient client)
    {
    	_localPlayer = new HumanPlayer(this);
        _playerList = new PlayerList(GameConstants.MAX_PLAYERS);
        _client = client;
        _isReady = true;
    }
    
    @Override
    public void connected() {}
    
    @Override
    public void disconnected() {}
        
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
}
