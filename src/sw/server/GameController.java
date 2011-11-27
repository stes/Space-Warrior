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
package sw.server;

import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.Packet;
import sw.shared.data.PlayerDataSet;
import sw.shared.data.PlayerInput;
import sw.shared.data.PlayerList;
import sw.shared.data.Shot;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */ 

public class GameController
{
    private PlayerList _connectedPlayers;
    private PlayerList _activePlayers;
    private IServer _server;
    
    /**
     * Creates a new game controller
     * 
     * @param server IServer
     */
    public GameController(IServer server)
    {
        _connectedPlayers = new PlayerList(GameConstants.MAX_PLAYERS);
        _activePlayers = new PlayerList(GameConstants.MAX_PLAYERS);
        _server = server;
    }
        
    /**
     * Bearbeitet eine Eingabe vom Server
     * 
     * @param name Der Name des betreffenden Spielers
     * @param eingabe Die Eingabe des Spielers
     */
    public void processPlayerInput(String name, PlayerInput eingabe)
    {
        _activePlayers.trySetInput(name, eingabe);
    }
    
    public void tick()
    {
        this.checkTurn();
        this.updateData();
    }
    /**
     * A new player joined the game
     * 
     * @param name Name des Spielers
     */
    public void bearbeiteNeuenSpieler(String name)
    {
        PlayerDataSet newDataSet = new PlayerDataSet(name, true);
        newDataSet.init();
        _connectedPlayers.insert(newDataSet, null);
    }
    /**
     * Sends a snapshot to every player
     */
    public void broadcastSnapshots()
    {
        for (int i = 0; i < _connectedPlayers.size(); i++)
        {
            PlayerDataSet daten = _connectedPlayers.dataAt(i);
            if (daten != null)
            {
                Packet snapshot = _activePlayers.createSnapshot(daten.name());
                _server.sendPacket(daten.name(), snapshot);
            }
        }
    }
    /**
     * A player left the game
     * 
     * @param name the player's name
     */
    public void playerLeft(String name)
    {
        //PlayerDataSet suchObjekt = new PlayerDataSet(name, true);
        _connectedPlayers.tryRemove(name);
        _activePlayers.tryRemove(name);
    }
    
    private void checkTurn()
    {
        if((_activePlayers.count() == 1 && _connectedPlayers.count() > 1) ||
            (_activePlayers.count() == 0 && _connectedPlayers.count() == 1))
        {
            if(_activePlayers.count() == 1)
            {
                for (int i = 0; i < _activePlayers.size(); i++)
                {
                    PlayerDataSet daten = _activePlayers.dataAt(i);
                    if (daten != null)
                    {
                        Packet info = new Packet(Packettype.SV_CHAT_NACHRICHT);
                        info.addString("Server");
                        info.addString(daten.name() + " hat die Runde gewonnen!");
                        _server.sendBroadcast(info);
                        break;
                    }
                }
            }
            this.startGame();
        }
    }
    
    // Dienste
    /**
     * startet ein neues Spiel und fügt alle verbundenen Spieler
     * zu den aktiven Spielern hinzu und gibt dem Client eine
     * Nachricht aus
     */
    public void startGame()
    {
        // Liste der aktiven Spieler leeren
        _activePlayers.clear();
        // alle verbundenen Spieler in das Spiel einfügen
        for(int i = 0; i < _connectedPlayers.size(); i++)
        {
            PlayerDataSet daten = _connectedPlayers.dataAt(i);
            if (daten != null)
            {
                daten.init();
                _activePlayers.insert(daten, null);
            }
        }
        Packet info = new Packet(Packettype.SV_CHAT_NACHRICHT);
        info.addString("Server");
        info.addString("Neue Runde");
        _server.sendBroadcast(info);
        System.out.println("Neue Runde");
    }
    
    private void updateData()
    {
        for(int i = 0; i < _activePlayers.size(); i++)
        {
            PlayerDataSet daten = _activePlayers.dataAt(i);
            PlayerInput eingabe = _activePlayers.inputAt(i);
            if (daten != null)
            {
                if (eingabe.schuss() > 0)
                {
                    Shot s = daten.schiesse(eingabe.schuss() == 2);
                    if (s != null)
                    {
                        this.addDamage(daten, s);
                        Packet p = s.pack();
                        _server.sendBroadcast(p);
                    }
                }
                daten.beschleunige(GameConstants.ACCELERATION * eingabe.moveDirection());
                daten.dreheUm(GameConstants.ANGEL_OF_ROTATION * Math.signum(eingabe.turnDirection()));
                daten.ladeNach();
                daten.bewege();
            }
        }
    }
    
    /**
     * Der getroffen Spieler kriegt schaden und somit Leben abgezogen
     * Wenn ein Spieler kein Leben mehr hat kriegt der Angreifer einen
     * Punkt
     * 
     * @param angreifer Daten des angreifenden Spielers
     * @param shot abgegebener Schuss
     */
    private void addDamage(PlayerDataSet angreifer, Shot shot)
    {
        for (int i = 0; i < _activePlayers.size(); i++)
        {
            PlayerDataSet daten = _activePlayers.dataAt(i);
            if (daten != null && !daten.name().equals(angreifer.name()))
            {
                if(shot.abstandZu(daten.position()) < GameConstants.PLAYER_SIZE/2)
                {
                    daten.setzeLeben(daten.leben() - shot.schaden());
                    if(daten.leben() <= 0)
                    {
                        _activePlayers.tryRemove(daten.name());
                        angreifer.setzePunkte(angreifer.punkte() + 1);
                    }
                }
            }
        }
    }
}
