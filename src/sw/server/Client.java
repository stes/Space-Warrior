package sw.server;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class Client
{
    // Bezugsobjekte

    // Attribute
    private String _ip;
    private int _port;
    private String _name;
    private boolean _imSpiel;

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
        _imSpiel = false;
    }

    // Dienste
    /**
     * @return ip IPAddresse mit dazugehörigem Port
     */
    public String adresse()
    {
        return _ip + ":" + _port;
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
    public void setzeName(String name)
    {
        _name = name;
    }
    
    /**
     * @return ip IPAddresse des Clients
     */
    public String ip()
    {
        return _ip;
    }
    
    /**
     * @return port Port des Clients
     */
    public int port()
    {
        return _port;
    }
    
    /**
     * wahr, wenn der Client auf den Server verbindet
     */
    public void betrittSpiel()
    {
        _imSpiel = true;
    }
    
    /**
     * @return imSpiel gibt zurück ob der Client im Spiel ist
     */
    public boolean istImSpiel()
    {
        return _imSpiel;
    }
    
    @Override
    public String toString()
    {
        return _name;
    }
}
