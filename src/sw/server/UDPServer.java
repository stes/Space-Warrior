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

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import sw.shared.NetworkListener;
import sw.shared.NetworkReceiver;
import sw.shared.UDPConnection;
import sw.shared.UDPConnection.CTRLMSG;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class UDPServer extends Thread implements NetworkListener
{
    private DatagramSocket _socket;
    private NetworkReceiver _receiver;
    
    private UDPConnection[] _clients;
    
    private ArrayList<NetworkServerListener> _networkServerListener;
    
    public UDPServer(int port, int maxPlayers)
    {
        try
        {
        	_clients = new UDPConnection[maxPlayers];
            _socket = new DatagramSocket(port);
            _networkServerListener = new ArrayList<NetworkServerListener>();
            _receiver = new NetworkReceiver(_socket);
            _receiver.addNetworkListener(this);
            _receiver.start();
            System.out.println("server started on port " + port);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void addNetworkServerListener(NetworkServerListener listener)
	{
    	_networkServerListener.add(listener);
	}
    
    @Override
	public void run()
    {
    	while(true)
    	{
	    	for(int i = 0; i < _clients.length; i++)
	    	{
	    		UDPConnection cl = _clients[i];
	    		if(cl != null)
	    		{
	    			if(cl.timeout())
	    			{
	    				_clients[i] = null;
	    				for (NetworkServerListener l : _networkServerListener)
	    				{
	    					l.serverClientDisconnected(i);
	    				}
	    			}
	    			else
	    			{
	    				cl.update();
	    			}
	    		}
	    	}
	    	Thread.yield();
    	}
    }
    
    public void send(int clientID, byte[] data, int len)
    {
    	if(clientID == -1)
    	{
    		for(int i = 0; i < _clients.length; i++)
	    	{
	    		if(_clients[i] != null)
	    		{
	    			_clients[i].send(data, len);
	    		}
	    	}
    	}
    	else if(_clients[clientID] != null)
		{
			_clients[clientID].send(data, len);
		}
    }
    
    public void drop(int clientID)
    {
    	if(_clients[clientID] != null)
		{
    		System.out.println("drop: " + _clients[clientID]);
    		_clients[clientID].disconnect();
			_clients[clientID] = null;
			for (NetworkServerListener l : _networkServerListener)
			{
				l.serverClientDisconnected(clientID);
			}
		}
    }
    
    @Override
    public void messageReceived(InetSocketAddress addr, byte flag, byte[] data, int len)
    {
    	for(int i = 0; i < _clients.length; i++)
    	{
    		UDPConnection cl = _clients[i];
    		if(cl != null && cl.equals(addr))
    		{
    			cl.messageReceived();
    			if(flag == (byte)CTRLMSG.NONE.ordinal())
    			{
    				for (NetworkServerListener l : _networkServerListener)
    				{
    					l.serverReceivedMessage(i, data, len);
    				}
    			}
				else if(flag == (byte)CTRLMSG.CLOSE.ordinal())
				{
					System.out.println("disconnect: " + _clients[i]);
					_clients[i] = null;
					for (NetworkServerListener l : _networkServerListener)
    				{
    					l.serverClientDisconnected(i);
    				}
				}
    			return;
    		}
    	}
    	
    	if(flag == (byte)CTRLMSG.CONNECT.ordinal())
		{
    		for(int i = 0; i < _clients.length; i++)
        	{
        		if(_clients[i] == null)
        		{
        			_clients[i] = new UDPConnection(_socket, addr);
        			_clients[i].setConnected();
        			_clients[i].sendControl(CTRLMSG.CONNECTACCEPT);
        			for (NetworkServerListener l : _networkServerListener)
    				{
    					l.serverClientConnected(i);
    				}
        			return;
        		}
        	}
		}
    }
    
    @Override
    public void messageReceivedConnless(InetSocketAddress addr, byte[] data, int len)
    {
    }
}
