package sw.client;

import sw.shared.Paket;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */

public interface IClient
{
    // Dienste
    /**
     * Sendet eine Nachricht an den Server
     * 
     * @param nachricht Die Nachricht
     */
    public void sendeNachricht(Paket nachricht);
}
