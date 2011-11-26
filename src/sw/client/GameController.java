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

import sw.shared.GameConstants;
import sw.shared.PlayerDataSet;
import sw.shared.PlayerList;
import sw.shared.PlayerInput;
import sw.shared.Shot;
import sw.shared.Packet;

import java.awt.event.AWTEventListener;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */ 
public class GameController implements AWTEventListener, ClientListener
{
    private PlayerList _playerList;
    private PlayerDataSet _localPlayer;
    private IClient _client;
    private PlayerInput _currentState;
    private PlayerInput _oldState;
    private boolean _isReady;
    
    /**
     * creates an new GameController
     */
    public GameController(IClient client)
    {
        _currentState = new PlayerInput();
        _oldState = new PlayerInput();
        _playerList = new PlayerList(GameConstants.MAX_SPIELERZAHL);
        _client = client;
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
        _isReady = true;
    }
        
    @Override
    public void chatMessage(String name, String text) {}

    @Override
    public void connectionLost(String grund) {}
    
    @Override
    public void eventDispatched(AWTEvent e)
    {
        if (e.getID() == KeyEvent.KEY_TYPED)
        {
            KeyEvent keyEvent = (KeyEvent)e;
            char eingabe = ((KeyEvent)e).getKeyChar();
            switch (eingabe)
            {
                case 'w':
                {
                    // vorwärts
                    _currentState.setzeBewegung(1);
                    break;
                }
                case 's':
                {
                    // rückwärts
                    _currentState.setzeBewegung(-1);
                    break;
                }
                case 'a':
                {
                    // links
                    _currentState.setzeDrehung(1);
                    break;
                }
                case 'd':
                {
                    // rechts
                    _currentState.setzeDrehung(-1);
                    break;
                }
                case 'n':
                {
                    // normaler Schuss
                    _currentState.setzeSchuss(1);
                    break;
                }
                case 'm':
                {
                    // Masterschuss
                    _currentState.setzeSchuss(2);
                    break;
                }
            }
        }
        else if (e.getID() == KeyEvent.KEY_RELEASED)
        {
            KeyEvent keyEvent = (KeyEvent)e;
            char eingabe = ((KeyEvent)e).getKeyChar();
            switch (eingabe)
            {
                case 'w':
                {
                    // vorwärts
                    _currentState.setzeBewegung(-1);
                    break;
                }
                case 's':
                {
                    // rückwärts
                    _currentState.setzeBewegung(0);
                    break;
                }
                case 'a':
                {
                    // links
                    _currentState.setzeDrehung(0);
                    break;
                }
                case 'd':
                {
                    // rechts
                    _currentState.setzeDrehung(0);
                    break;
                }
                case 'm': case 'n':
                {
                    _currentState.setzeSchuss(0);
                    break;
                }
            }
        }
        if (!_oldState.equals(_currentState))
        {
            _oldState = new PlayerInput(_currentState);
            Packet p = _currentState.pack();
            //_client.sendeNachricht(p);
        }
    }
    /**
     * @return the Playerlist
     */
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
    /**
     * send a Snapshot to every player
     */
    @Override
	public void snapshot(Packet snapshot)
    {
        _playerList.update(snapshot);
        for (int i = 0; i < _playerList.size(); i++)
        {
            PlayerDataSet d = _playerList.dataAt(i);
            if (d != null && d.lokal())
            {
                _localPlayer = d;
            }
        }
    }
}
