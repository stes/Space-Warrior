package sw.shared;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class Paket
{
    // Bezugsobjekte
    private StringBuilder _stringBuilder;
    private String _string;
    private int _position;
    private char _typ;

    // Attribute

    // Konstruktor
    /**
     * Paket um Daten verschiedener Typen zu übermitteln
     * 
     * @param typ Typ des Pakets
     */
    public Paket(char typ)
    {
        _stringBuilder = new StringBuilder();
        _string = new String();
        _typ = (char)(((int)typ) & 0x3F | 0x40);
        _stringBuilder.append(_typ);
    }
    
    /**
     * Paket um Strings zu übermitteln
     * 
     * @param text Text als String
     */
    public Paket(String text)
    {
        _stringBuilder = new StringBuilder();
        _string = text;
        _typ = (char)(((int) _string.charAt(0)) & 0x3F);
        _position++;
    }
    
    /**
     * @return gibt den Typ zurück
     */
    public char Typ()
    {
        return _typ;
    }

    // Dienste
    /**
     * fügt Zahlen als Typ an
     * 
     * @param zahl
     */
    public void fuegeZahlAn(int zahl)
    {
        char[] data = new char[6];
        
        // workaround: '\n' und '\r' dürfen nicht enthalten sein
        data[0] = (char) ( (zahl >> 30) & 0x03 | 0x40);
        data[1] = (char) ( (zahl >> 24) & 0x3F | 0x40);
        data[2] = (char) ( (zahl >> 18) & 0x3F | 0x40);
        data[3] = (char) ( (zahl >> 12) & 0x3F | 0x40);
        data[4] = (char) ( (zahl >> 6) & 0x3F | 0x40);
        data[5] = (char) ( zahl & 0x3F | 0x40);
        
        _stringBuilder.append(data);
    }
    
    /**
     * fügt ein bool an
     * 
     * @param bool
     */
    public void fuegeBooleanAn(boolean bool)
    {
        char data = (char) (bool ? 1 : 0);
        _stringBuilder.append(data);
    }
    
    /**
     * fügt einen Text an
     * 
     * @param text
     */
    public void fuegeStringAn(String text)
    {
        text = text.replace("\n", "");
        text = text.replace("\r", "");
        this.fuegeZahlAn(text.length());
        _stringBuilder.append(text);
    }
    
    /**
     * fügt ein weiteres Paket an
     * 
     * @param p 
     */
    public void fuegePaketAn(Paket p)
    {
        this.fuegeStringAn(p.toString());
    }
    
    /**
     * holt eine Zahl aus einem paket
     * 
     * @return zahl
     */
    public int holeZahl()
    {
        int zahl = (_string.charAt(_position) & 0x03) << 30;
        zahl |= (_string.charAt(_position+1) & 0x3F) << 24;
        zahl |= (_string.charAt(_position+2) & 0x3F) << 18;
        zahl |= (_string.charAt(_position+3) & 0x3F) << 12;
        zahl |= (_string.charAt(_position+4) & 0x3F) << 6;
        zahl |= (_string.charAt(_position+5) & 0x3F);
        
        _position += 6;
        return zahl;
    }
    
    /**
     * holt einen bool aus einem Paket
     * 
     * @return bool
     */
    public boolean holeBoolean()
    {
        _position++;
        return _string.charAt(_position-1) == 1;
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
        return _string.substring(_position-laenge, _position);
    }
    
    /**
     * holt ein weiteres Paket 
     * 
     * @return Paket
     */
    public Paket holePaket()
    {
        return new Paket(this.holeString());
    }
    
    @Override
    public String toString()
    {
        return _stringBuilder.toString();
    }
}
