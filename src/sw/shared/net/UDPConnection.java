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
package sw.shared.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class UDPConnection
{
	public final static int MAX_PACKET_LENGTH = 4*1024;
	public final static int PACKET_HEADER_LENGTH = 1;
	
	public final static byte CTRL_NONE = 0;
	public final static byte CTRL_KEEPALIVE = 1;
	public final static byte CTRL_CONNECT = 2;
	public final static byte CTRL_CONNECTACCEPT = 3;
	public final static byte CTRL_CLOSE = 4;
	
    private DatagramSocket _socket;
    private InetSocketAddress _addr;
    private long _lastRecvTime;
    private long _lastSendTime;
    private boolean _timeout;
    private boolean _connected;

    public UDPConnection(DatagramSocket socket, InetSocketAddress addr)
    {
    	_socket = socket;
    	_addr = addr;
    	_timeout = false;
    	_connected = false;
    	this.messageReceived();
    	System.out.println("new connection: " + _addr);
    }
    
    public void setConnected()
    {
    	_connected = true;
    }
    
    public boolean timeout()
    {
    	return _timeout;
    }
    
    public void messageReceived()
    {
    	_lastRecvTime = System.currentTimeMillis();
    }
    
    public void update()
    {
    	long now = System.currentTimeMillis();
		
		if(!_timeout)
		{
			if((now - _lastRecvTime) > 1000*10)
			{
				System.out.println("timeout: " + _addr);
				_timeout = true;
			}
			
			if(now - _lastSendTime > 1000 && _connected)
			{
				//System.out.println("send keepalive: " + _addr);
				sendControl(CTRL_KEEPALIVE);
			}
		}
    }
    
    private void send(byte[] data, int len, byte flag)
    {
    	try
    	{
	    	if(!_timeout)
	    	{
	            byte[] buf = new byte[MAX_PACKET_LENGTH];
	            buf[0] = flag;
	            if(data != null)
	            {
	            	int size = Math.min(data.length, buf.length-PACKET_HEADER_LENGTH);
	            	System.arraycopy(data, 0, buf, PACKET_HEADER_LENGTH, size);
	            }
	            DatagramPacket packet = new DatagramPacket(buf, buf.length, _addr);
				_socket.send(packet);
	    		_lastSendTime = System.currentTimeMillis();
	    	}
    	}
    	catch (IOException e)
    	{
			e.printStackTrace();
		}
    }
    
    public void send(byte[] data, int len)
    {
    	this.send(data, data.length, CTRL_NONE);
    }
    
    public void sendControl(byte flag)
    {
    	this.send(null, 0, flag);
    }
    
    public void disconnect()
    {
    	System.out.println("disconnect: " + _addr);
    	sendControl(CTRL_CLOSE);
    }
    
    @Override
    public boolean equals(Object obj)
    {
    	if(obj instanceof InetSocketAddress)
    	{
    		InetSocketAddress addr = (InetSocketAddress) obj;
    		return _addr.equals(addr);
    	}
    	else
    	{
    		return false;
    	}
    }
    
    @Override
    public String toString()
    {
    	return _addr.toString();
    }
}
