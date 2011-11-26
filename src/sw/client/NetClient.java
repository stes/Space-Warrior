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
package sw.client;

import java.util.ArrayList;

import sw.shared.Nachrichtentyp;
import sw.shared.Packet;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class NetClient implements IClient
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
        //super(ip, port, false);
        _clientListener = new ArrayList<ClientListener>();
        this.setzeTrennGrundZurueck();
        Packet start = new Packet(Nachrichtentyp.CL_START_INFO);
        start.fuegeStringAn(name);
        this.sendeNachricht(start);
    }
    
    /**
     * F�gt einen neuen ClientListener hinzu
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
    public void sendeNachricht(Packet nachricht)
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
        Packet packet = new Packet(nachricht);
        
        if (_clientListener == null)
            return;
        
        if(Nachrichtentyp.SV_TRENN_INFO == packet.Typ())
        {
            _letzterTrennGrund = packet.holeString();
        }
        else if(Nachrichtentyp.SV_CHAT_NACHRICHT == packet.Typ())
        {
            String name = packet.holeString();
            String text = packet.holeString();
            for (ClientListener l : _clientListener)
            {
                if (l == null) continue;
                l.bearbeiteChatNachricht(name, text);
            }
        }
        else if(Nachrichtentyp.SV_SNAPSHOT == packet.Typ())
        {
            for (ClientListener l : _clientListener)
            {
                if (l == null) continue;
                l.bearbeiteSnapshot(packet);
            }
        }
        else if(Nachrichtentyp.SV_SCHUSS == packet.Typ())
        {
            for (ClientListener l : _clientListener)
            {
                if (l == null) continue;
                l.bearbeiteSchuss(packet);
            }
        }
    }
}
