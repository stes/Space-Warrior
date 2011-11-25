package sw.client;

import sw.shared.Paket;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */

public interface ClientListener
{
    // Dienste
    /**
     * Wird ausgeführt, wenn der Server die Verbindung beendet
     * 
     * @param grund Der Trenngrund
     */
    public void bearbeiteTrennung(String grund);
    
    /**
     * Wird ausgeführt, wenn eine Chatnachricht erhalten wurde
     * 
     * @param name Der Name des Absenders
     * @param text Der Text, der gesendet wurde
     */
    public void bearbeiteChatNachricht(String name, String text);
    
    /**
     * Wird ausgeführt, wenn der Client einen Snapshot empfängt
     * 
     * @param paket Der Snapshot
     */
    public void bearbeiteSnapshot(Paket paket);
    
    /**
     * Wird ausgeführt, wenn der Client einen Schuss empfängt
     * 
     * @param paket Der Schuss
     */
    public void bearbeiteSchuss(Paket paket);
}
