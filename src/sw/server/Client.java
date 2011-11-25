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
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class Client
{
    // Bezugsobjekte

    // Attribute
    private String _ip;
    private int _port;
    private String _name;
    private boolean _isPlaying;

    // Konstruktor
    /**
     * Ein neuer Client wird auf dem Server hinzugefügt
     * 
     * @param ip Ip des Clients
     * @param port Port des Clients
     * @param name Name des Clients
     */
    public Client(String ip, int port, String name)
    {
        _ip = ip;
        _port = port;
        _name = name;
        _isPlaying = false;
    }

    /**
     * wahr, wenn der Client auf den Server verbindet
     */
    public void enterGame()
    {
        _isPlaying = true;
    }
    
    // Dienste
    /**
     * @return ip IPAddresse mit dazugehörigem Port
     */
    public String getAdress()
    {
        return _ip + ":" + _port;
    }
    
    /**
     * @return imSpiel gibt zurück ob der Client im Spiel ist
     */
    public boolean getIsPlaying()
    {
        return _isPlaying;
    }
    
    /**
     * @return port Port des Clients
     */
    public int getPort()
    {
        return _port;
    }
    
    /**
     * @return ip IPAddresse des Clients
     */
    public String ip()
    {
        return _ip;
    }
    
    /**
     * @return name Name des Clients
     */
    public String name()
    {
        return _name;
    }
    
    /**
     * setzt den vom Client eingegebenen Namen
     * 
     * @param name Name des Clients
     */
    public void setName(String name)
    {
        _name = name;
    }
    
    @Override
    public String toString()
    {
        return _name;
    }
}
