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
        PlayerList liste = new PlayerList(p.getInt());
        int n = p.getInt();
        for (int i = 0; i < n; i++)
        {
           liste.insert(PlayerDataSet.hole(p.getPacket()), null);
        }
        return liste;
    }
    private PlayerDataSet[] _liste;
    private PlayerInput[] _eingaben;
    
    /**
     * Spielerliste Eine neue Spielerliste wird erstellt
     * 
     * @param size Die groesse der Liste 
     */
    public PlayerList(int size)
    {
        _liste = new PlayerDataSet[size];
        _eingaben = new PlayerInput[size];
    }
    /**
     * @return eingabe
     */
    public PlayerInput inputAt(int index)
    {
        return _eingaben[index];
    }

    /**
     * @return liste
     */
    public PlayerDataSet dataAt(int index)
    {
        return _liste[index];
    }
    
    /**
     * Entfernt den Spieler mit dem angegebenen Namen aus der
     * Liste
     * 
     * @param name Der Spielername
     * @return Der Datensatz der zum Spieler gehört
     */
    public void remove(String name) throws AttributeNotFoundException
    {
        if (tryRemove(name))
            return;
        throw new AttributeNotFoundException();
    }
    
    /**
     * Schreibt die Spielerliste in ein Snapshot und gibt dieses zurueck
     * 
     * @return Das Paket
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
     * Fügt der Liste einen neuen Datensatz hinzu
     * 
     * @param spieler Der Datensatz
     * @param eingabe Die aktuelle Spielereingabe, null fuer Standardwert
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
     * Gibt die Groesse der Liste zurueck
     * 
     * @return Die Listengroesse
     */
    public int size()
    {
        return _liste.length;
    }
    
    // Dienste
    /**
     * Die Spielerdaten werden aus der Liste geloescht
     */
    public void clear()
    {
        _liste = new PlayerDataSet[_liste.length];
        _eingaben = new PlayerInput[_eingaben.length];
    }
    
    /**
     * Sucht den Spieler mit dem angegebenen Namen in der
     * Liste
     * 
     * @param name Der Spielername
     * @return Der Datensatz der zum Spieler gehört
     */
    public PlayerDataSet find(String name) throws AttributeNotFoundException
    {
        PlayerDataSet s = tryFind(name);
        if (s != null)
            return s;
        throw new AttributeNotFoundException();
    }
    
    /**
     * Updated das Paket
     * 
     * @param p Das Paket
     */
    public void update(Packet p)
    {
        PlayerList liste = PlayerList.fromSnapshot(p);
        _liste = liste._liste;
        _eingaben = liste._eingaben;
    }
    
    /**
     * Entfernt den Spieler mit dem angegebenen Namen aus der
     * Liste
     * 
     * @param name Der Spielername
     * @return Der Datensatz der zum Spieler gehört oder null wenn kein Spieler gefunden wird
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
     * Setzt die Eingabe des Spielers
     * 
     * @param name Der Spielername
     * @param eingabe Eingabe des Spielers
     * @return Der Datensatz der zum Spieler gehört oder null wenn kein Spieler gefunden wurde
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
     * Sucht den Spieler mit dem angegebenen Namen in der
     * Liste
     * 
     * @param name Der Spielername
     * @return Der Datensatz der zum Spieler gehört oder null wenn kein Spieler gefunden wurde
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
     * Zaehlt die Anzahl der belegten Elemente in der Liste
     * 
     * @return Die Anzahl der belegten Elemente
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
     * @return nächster freier Platz in der Liste
     * @return -1 , wenn kein freier Platz mehr verfügbar ist
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
