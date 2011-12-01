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
package sw.server;

import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.Packer;
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
     * @param input Die Eingabe des Spielers
     */
    public void processPlayerInput(String name, PlayerInput input)
    {
        _activePlayers.trySetInput(name, input);
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
            PlayerDataSet data = _connectedPlayers.dataAt(i);
            if (data != null)
            {
                Packer snapshot = _activePlayers.createSnapshot(data.getName());
                _server.sendPacket(data.getName(), snapshot);
            }
        }
    }
    /**
     * A player left the game
     * 
     * @param name the player's name
     */
    public void playerLeft(String name, String reason)
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
                    PlayerDataSet data = _activePlayers.dataAt(i);
                    if (data != null)
                    {
                        Packer info = new Packer(Packettype.SV_CHAT_MESSAGE);
                        info.writeUTF("Server");
                        info.writeUTF(data.getName() + " hat die Runde gewonnen!");
                        _server.sendBroadcast(info);
                        break;
                    }
                }
            }
            this.startGame();
        }
    }
    
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
            PlayerDataSet data = _connectedPlayers.dataAt(i);
            if (data != null)
            {
                data.init();
                _activePlayers.insert(data, null);
            }
        }
        Packer info = new Packer(Packettype.SV_CHAT_MESSAGE);
        info.writeUTF("Server");
        info.writeUTF("New round");
        _server.sendBroadcast(info);
        System.out.println("New round");
    }
    
    private void updateData()
    {
        for(int i = 0; i < _activePlayers.size(); i++)
        {
            PlayerDataSet data = _activePlayers.dataAt(i);
            PlayerInput input = _activePlayers.inputAt(i);
            if (data != null)
            {
                if (input.shot() > 0)
                {
                    Shot s = data.shoot(input.shot() == 2);
                    if (s != null)
                    {
                        this.addDamage(data, s);
                        Packer p = s.write();
                        _server.sendBroadcast(p);
                    }
                }
                data.accelerate(GameConstants.ACCELERATION * input.moveDirection());
                data.rotate(GameConstants.ANGEL_OF_ROTATION * Math.signum(input.turnDirection()));
                data.ladeNach();
                data.move();
            }
        }
    }
    
    /**
     * Der getroffen Spieler kriegt schaden und somit Leben abgezogen
     * Wenn ein Spieler kein Leben mehr hat kriegt der Angreifer einen
     * Punkt
     * 
     * @param attacker Daten des angreifenden Spielers
     * @param shot abgegebener Schuss
     */
    private void addDamage(PlayerDataSet attacker, Shot shot)
    {
        for (int i = 0; i < _activePlayers.size(); i++)
        {
            PlayerDataSet data = _activePlayers.dataAt(i);
            if (data != null && !data.getName().equals(attacker.getName()))
            {
                if(shot.distanceTo(data.getPosition()) < GameConstants.PLAYER_SIZE/2)
                {
                    data.setLifepoints(data.getLifepoints() - shot.getDamage());
                    if(data.getLifepoints() <= 0)
                    {
                        _activePlayers.tryRemove(data.getName());
                        attacker.setScore(attacker.getScore() + 1);
                    }
                }
            }
        }
    }
}
