package sw.client;

import sum.netz.Clientverbindung;

import java.util.ArrayList;

import sw.shared.Nachrichtentyp;
import sw.shared.Paket;

/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class NetClient extends Clientverbindung implements IClient
{
    private String _letzterTrennGrund;

    private ArrayList<ClientListener> _clientListener;
    
    /**
     * Erzeugt einen neuen NetClient
     * 
     * @param ip Die IP Adresse des Servers
     * @param port Der Port
     */
    public NetClient(String ip, int port, String name)
    {
        super(ip, port, false);
        _clientListener = new ArrayList<ClientListener>();
        this.setzeTrennGrundZurueck();
        Paket start = new Paket(Nachrichtentyp.CL_START_INFO);
        start.fuegeStringAn(name);
        this.sendeNachricht(start);
    }
    
    /**
     * Fügt einen neuen ClientListener hinzu
     * 
     * @param l Der Listener
     */
    public void fuegeClientListenerHinzu(ClientListener l)
    {
        _clientListener.add(l);
    }
    
    private void setzeTrennGrundZurueck()
    {
        _letzterTrennGrund = "Verbindung verloren";
    }
    
    @Override
    public void sendeNachricht(Paket nachricht)
    {
        this.sende(nachricht.toString());
    }
    
    @Override
    public void bearbeiteVerbindungsverlust()
    {
        if (_clientListener != null)
        {
            for (ClientListener l : _clientListener)
            {
                if (l == null) continue;
                l.bearbeiteTrennung(_letzterTrennGrund);
            }
        }
        this.setzeTrennGrundZurueck();
    }
    
    @Override
    public void bearbeiteNachricht(String nachricht)
    {
        Paket paket = new Paket(nachricht);
        
        if (_clientListener == null)
            return;
        
        if(Nachrichtentyp.SV_TRENN_INFO == paket.Typ())
        {
            _letzterTrennGrund = paket.holeString();
        }
        else if(Nachrichtentyp.SV_CHAT_NACHRICHT == paket.Typ())
        {
            String name = paket.holeString();
            String text = paket.holeString();
            for (ClientListener l : _clientListener)
            {
                if (l == null) continue;
                l.bearbeiteChatNachricht(name, text);
            }
        }
        else if(Nachrichtentyp.SV_SNAPSHOT == paket.Typ())
        {
            for (ClientListener l : _clientListener)
            {
                if (l == null) continue;
                l.bearbeiteSnapshot(paket);
            }
        }
        else if(Nachrichtentyp.SV_SCHUSS == paket.Typ())
        {
            for (ClientListener l : _clientListener)
            {
                if (l == null) continue;
                l.bearbeiteSchuss(paket);
            }
        }
    }
}
