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
package sw.shared.data;

import javax.management.AttributeNotFoundException;

import sw.shared.Packettype;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayerList
{
    /**
     * Creates a new Playerlist out of a paket
     * 
     * @param p The paket
     * @return The new instance
     */
    private static PlayerList fromSnapshot(Packet p)
    {
        PlayerList liste = new PlayerList(p.getNumber());
        int n = p.getNumber();
        for (int i = 0; i < n; i++)
        {
           liste.insert(PlayerDataSet.hole(p.getPacket()), null);
        }
        return liste;
    }
    private PlayerDataSet[] _liste;
    private PlayerInput[] _eingaben;
    
    /**
     * playerlist creates a new playerlist
     * 
     * @param size size of the list 
     */
    public PlayerList(int size)
    {
        _liste = new PlayerDataSet[size];
        _eingaben = new PlayerInput[size];
    }
    /**
     * @return input
     */
    public PlayerInput inputAt(int index)
    {
        return _eingaben[index];
    }

    /**
     * @return list
     */
    public PlayerDataSet dataAt(int index)
    {
        return _liste[index];
    }
    
    /**
     * removes the player with the specified name from the
     * list
     * 
     * @param name playername
     * @return the record belongs to the player
     */
    public void remove(String name) throws AttributeNotFoundException
    {
        if (tryRemove(name))
            return;
        throw new AttributeNotFoundException();
    }
    
    /**
     * Writes the list of players in a snapshot and returns
     * 
     * @return the packet
     */
    public Packet createSnapshot(String lokalerName)
    {
        Packet p = new Packet(Packettype.SV_SNAPSHOT);
        p.addNumber(this.size());
        p.addNumber(this.count());
        for (PlayerDataSet s : _liste)
        {
            if (s != null)
            {
                boolean lokal = s.name().equals(lokalerName);
                p.addPacket(s.pack(lokal));
            }
        }
        return p;
    }
    
    /**
     * adds a data record to the list
     * 
     * @param player the data record
     * @param input the current player input, null for default
     */
    public void insert(PlayerDataSet spieler, PlayerInput eingabe)
    {
        int index = findEmptyPlace();
        if (index == -1)
        {
            throw new ArrayIndexOutOfBoundsException("Kein Platz in der Liste");
        }
        _liste[index] = spieler;
        _eingaben[index] = (eingabe == null ? new PlayerInput() : eingabe);
    }
    
    /**
     * returns the current size of the list
     * 
     * @return the size
     */
    public int size()
    {
        return _liste.length;
    }
    
    // Dienste
    /**
     * the player data is deleted from the list
     */
    public void clear()
    {
        _liste = new PlayerDataSet[_liste.length];
        _eingaben = new PlayerInput[_eingaben.length];
    }
    
    /**
     * searching for the name of the player in the list
     * 
     * @param name name of the player
     * @return the data record belongs to the player
     */
    public PlayerDataSet find(String name) throws AttributeNotFoundException
    {
        PlayerDataSet s = tryFind(name);
        if (s != null)
            return s;
        throw new AttributeNotFoundException();
    }
    
    /**
     * Updated the packet
     * 
     * @param p the packet
     */
    public void update(Packet p)
    {
        PlayerList liste = PlayerList.fromSnapshot(p);
        _liste = liste._liste;
        _eingaben = liste._eingaben;
    }
    
    /**
     * deletes the player with the given name from the list
     * 
     * @param name name of the player
     * @return the data record that belongs to the player 
     * or null if no player is found
     */
    public boolean tryRemove(String name)
    {
        for (int i = 0; i < _liste.length; i++)
        {
            if (_liste[i] != null && _liste[i].name().equals(name))
            {
                _liste[i] = null;
                return true;
            }
        }
        return false;
    }
    
    /**
     * sets the player input
     * 
     * @param name name of the player
     * @param input player input
     * @return the data record that belongs to the player 
     * or null if no player is found
     */
    public boolean trySetInput(String name, PlayerInput eingabe)
    {
        for (int i = 0; i < _liste.length; i++)
        {
            PlayerDataSet s = _liste[i];
            if (s != null && s.name().equals(name))
            {
                _eingaben[i] = eingabe;
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * looking for the player with the given name in the list
     * 
     * @param name name of the player
     * @return the data record that belongs to the player 
     * or null if no player is found
     */
    public PlayerDataSet tryFind(String name)
    {
        for (PlayerDataSet s : _liste)
        {
            if (s != null && s.name().equals(name))
                return s;
        }
        return null;
    }
    
    /**
     * counts the number of occupied elements in the list
     * 
     * @return number of occupied elements
     */
    public int count()
    {
        int n = 0;
        for (int i = 0; i < _liste.length; i++)
        {
            if (_liste[i] != null)
            {
                n++;
            }
        }
        return n;
    }
    
    /**
     * @return next free space in the list
     * @return -1 , if no free space is available
     */
    private int findEmptyPlace()
    {
        for (int i = 0; i < _liste.length; i++)
        {
            if (_liste[i] == null)
            {
                return i;
            }
        }
        return -1;
    }
}
