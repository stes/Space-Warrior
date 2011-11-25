package sw.server;

import sw.shared.Paket;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */

public interface IServer
{
    // Dienste
     /**
     * Sendet eine Nachricht an einen Client
     * 
     * @param name Name des Empfängers
     * @param nachricht Die Nachricht
     */
    public void sendeNachricht(String name, Paket nachricht);
    
    /**
     * Sendet eine Nachricht an alle verbundenen Clients
     * 
     * @param nachricht Die Nachricht
     */
    public void sendeRundnachricht(Paket nachricht);
}
