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

import sw.shared.net.UDPConnection;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class Packet
{
    private byte[] _data;
    private int _cur;
    
    public Packet(byte type)
    {
    	_data = new byte[UDPConnection.MAX_PACKET_LENGTH-UDPConnection.PACKET_HEADER_LENGTH];
    	_data[0] = type;
    	_cur = 1;
    }
    
    public Packet(byte[] data, int len)
    {
    	_data = data;
    	_cur = 1;
    }
    
    public byte getType()
    {
        return _data[0];
    }
    
    public byte[] getData()
    {
        return java.util.Arrays.copyOf(_data, _cur);
    }
    
    public void addByte(byte data)
    {
    	_data[_cur] = data;
    	_cur++;
    }
    
    public void addBytes(byte[] data, int len)
    {
    	int size = Math.min(len, _data.length-_cur);
    	this.addNumber(size);
    	System.arraycopy(data, 0, _data, _cur, size);
    	_cur += size;
    }
    
    public void addNumber(int zahl)
    {
    	this.addByte((byte) ( (zahl >> 24) & 0xFF ));
    	this.addByte((byte) ( (zahl >> 16) & 0xFF ));
    	this.addByte((byte) ( (zahl >> 8) & 0xFF ));
    	this.addByte((byte) ( zahl & 0xFF ));
    }
    
    public void addBoolean(boolean bool)
    {
    	this.addByte((byte) (bool ? 1 : 0));
    }
    
    public void addPacket(Packet p)
    {
    	byte[] data = p.getData();
        this.addBytes(data, data.length);
    }
    
    public void addString(String text)
    {
    	byte[] data = text.getBytes();
    	this.addBytes(data, data.length);
    }
    
    public byte getByte()
    {
    	_cur++;
    	return _data[_cur-1];
    }
    
    public byte[] getBytes()
    {
    	int size = this.getNumber();
    	_cur += size;
    	return java.util.Arrays.copyOfRange(_data, _cur-size, _cur);
    }
    
    public int getNumber()
    {
        int zahl = (getByte() & 0xFF) << 24;
        zahl |= (getByte() & 0xFF) << 16;
        zahl |= (getByte() & 0xFF) << 8;
        zahl |= (getByte() & 0xFF);
        
        return zahl;
    }
    
    public boolean getBoolean()
    {
        return getByte() == (byte)1;
    }
    
    public Packet getPacket()
    {
    	byte[] data = this.getBytes();
        return new Packet(data, data.length);
    }
    
    public String getString()
    {
    	byte[] data = this.getBytes();
        return new String(data, 0, data.length);
    }
}
