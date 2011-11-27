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
package sw.shared;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class Packet
{
	// TODO: use byte array here
    private StringBuilder _stringBuilder;
    private String _content;
    private int _position;
    private byte _type;


    /**
     * Paket um Daten verschiedener Typen zu übermitteln
     * 
     * @param typ Typ des Pakets
     */
    public Packet(byte type)
    {
        _stringBuilder = new StringBuilder();
        _content = new String();
        _type = type;
        _stringBuilder.append((char)_type);
    }
    
    /**
     * Paket um Strings zu übermitteln
     * 
     * @param text Text als String
     */
    public Packet(byte[] data, int len)
    {
        _stringBuilder = new StringBuilder();
        _content = new String(data, 0, len);
        _type = data[0];
        _position++;
    }
    
    /**
     * fügt ein bool an
     * 
     * @param bool
     */
    public void fuegeBooleanAn(boolean bool)
    {
    	byte data = (byte) (bool ? 1 : 0);
        _stringBuilder.append(data);
    }

    /**
     * fügt ein weiteres Paket an
     * 
     * @param p 
     */
    public void fuegePaketAn(Packet p)
    {
    	byte[] data = p.getData();
        this.fuegeStringAn(new String(data, 0, data.length));
    }
    
    /**
     * fügt einen Text an
     * 
     * @param text
     */
    public void fuegeStringAn(String text)
    {
        this.fuegeZahlAn(text.length());
        _stringBuilder.append(text);
    }
    
    /**
     * fügt Zahlen als Typ an
     * 
     * @param zahl
     */
    public void fuegeZahlAn(int zahl)
    {
    	char[] data = new char[4];
        
        data[0] = (char) ( (zahl >> 24) & 0xFF);
        data[1] = (char) ( (zahl >> 16) & 0xFF);
        data[2] = (char) ( (zahl >> 8) & 0xFF);
        data[3] = (char) ( zahl & 0xFF);
        
        _stringBuilder.append(data);
    }
    
    /**
     * holt einen bool aus einem Paket
     * 
     * @return bool
     */
    public boolean holeBoolean()
    {
        _position++;
        return _content.charAt(_position-1) == 1;
    }
    
    /**
     * holt ein weiteres Paket 
     * 
     * @return Paket
     */
    public Packet holePaket()
    {
    	byte[] data = this.holeString().getBytes();
        return new Packet(data, data.length);
    }
    
    /**
     * holt einen String aus einem Paket
     * 
     * @return string
     */
    public String holeString()
    {
        int laenge = this.holeZahl();
        _position += laenge;
        return _content.substring(_position-laenge, _position);
    }
    
    /**
     * holt eine Zahl aus einem paket
     * 
     * @return zahl
     */
    public int holeZahl()
    {
        int zahl = (_content.charAt(_position) & 0xFF) << 24;
        zahl |= (_content.charAt(_position+1) & 0xFF) << 16;
        zahl |= (_content.charAt(_position+2) & 0xFF) << 8;
        zahl |= (_content.charAt(_position+3) & 0xFF);
        
        _position += 4;
        return zahl;
    }
    
    public byte[] getData()
    {
        return _stringBuilder.toString().getBytes();
    }
    
    /**
     * @return gibt den Typ zurück
     */
    public byte type()
    {
        return _type;
    }
}
