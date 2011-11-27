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
    private int _clientID;
    private String _name;
    private boolean _isPlaying;

    public Client(int clientID, String name)
    {
    	_clientID = clientID;
        _name = name;
        _isPlaying = false;
    }
    
    public void enterGame()
    {
        _isPlaying = true;
    }
    
    public int getClientID()
    {
        return _clientID;
    }
    
    public boolean isPlaying()
    {
        return _isPlaying;
    }
    
    public String name()
    {
        return _name;
    }
    
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
