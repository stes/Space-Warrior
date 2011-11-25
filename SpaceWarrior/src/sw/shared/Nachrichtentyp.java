package sw.shared;
/**
 * Eine Sammlung von Konstanten fuer den Netzwerkverkehr
 * 
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public final class Nachrichtentyp
{
    // Client
    public final static char CL_START_INFO = 0;
    public final static char CL_CHAT_NACHRICHT = 1;
    public final static char CL_EINGABE = 2;
    
    // Server
    public final static char SV_TRENN_INFO = 0;
    public final static char SV_CHAT_NACHRICHT = 1;
    public final static char SV_SNAPSHOT = 2;
    public final static char SV_SCHUSS = 3;
    
    // Snapshot Typen
    public final static char SNAP_SPIELERDATEN = 4;
}
