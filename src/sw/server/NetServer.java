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
package sw.server;

import java.util.ArrayList;

import sw.shared.Paket;
import sw.shared.Pakettype;
import sw.shared.Spielkonstanten;
import sw.shared.SpielerEingabe;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */

public class NetServer implements IServer
{
    private GameController _controller;
    private ArrayList<Client> _clientListe;
    private double _letztesSnapshot;
    private double _letzteAktualisierung;
    private String _serverName;
    
    /**
     * Initialisiert den NetServer
     * 
     * @param port Der Port
     */
    public NetServer(int port)
    {
        //super(port, false);
        _controller = new GameController(this);
        _clientListe = new ArrayList<Client>();
        _letztesSnapshot = System.currentTimeMillis();
        _letzteAktualisierung = System.currentTimeMillis();
        this.setzeServerName("Server");
        System.out.println("Server wird gestartet an Port " + port);
    }
    
    /**
     * Der gesuchte Client wird zurückgegeben
     * 
     * @param ip Ip des Clients, welcher gesucht werden soll
     * @param port Port des Clients, welcher gesucht werden soll
     * @return der gesuchte Client oder null, wenn dieser nicht vorhanden ist
     */
    private Client sucheClient(String ip, int port)
    {
        String adresse = ip + ":" + port;
        for(int i = 1; i <= _clientListe.size(); i++)
        {
            Client cur = _clientListe.get(i);
            if(adresse.equals(cur.adresse()))
                return cur;
        }
        return null;
    }
    
    /**
     * Der gesuchte Client wird zurückgegeben
     * 
     * @param name Name des Clients, welcher gesucht werden soll
     * @return der gesuchte Client oder null, wenn dieser nicht vorhanden ist
     */
    private Client sucheClient(String name)
    {
        for(int i = 1; i <= _clientListe.size(); i++)
        {
        	Client cur = _clientListe.get(i);
            if(name.equals(cur.name()))
                return cur;
        }
        return null;
    }
    
    /**
     * Entfernt einen Client
     * 
     * @param ip Ip des Clients, welcher entfernt werden soll
     * @param port Port des Clients, welcher entfernt werden soll
     */
    private void entferneClient(String ip, int port)
    {
        String adresse = ip + ":" + port;
        for(int i = 1; i <= _clientListe.size(); i++)
        {
        	Client cur = _clientListe.get(i);
            if(adresse.equals(cur.adresse()))
            {
                if(cur.istImSpiel())
                {
                    _controller.bearbeiteSpielerVerlaesst(cur.name());
                }
                System.out.println("'" + cur.name() + "' hat die Verbindung zum Server getrennt");
                _clientListe.remove(i);
            }
        }
    }
    
    /**
     * Sendet eine Nachricht an einen Client
     * 
     * @param ip Ip des Empfängers
     * @param port Port des Empfängers
     * @param nachricht Die Nachricht
     */
    private void sendeNachricht(String ip, int port, Paket nachricht)
    {
        //this.sendeAnEinen(ip, port, nachricht.toString());
    }
    
    /**
     * Sendet eine Nachricht an einen Client
     * 
     * @param name Name des Empfängers
     * @param nachricht Die Nachricht
     */
    @Override
    public void sendeNachricht(String name, Paket nachricht)
    {
        Client client = this.sucheClient(name);
        if(client != null)
        {
            this.sendeNachricht(client.ip(), client.port(), nachricht);
        }
    }
    
    /**
     * Sendet eine Nachricht an alle verbundenen Clients
     * 
     * @param nachricht Die Nachricht
     */
    @Override
    public void sendeRundnachricht(Paket nachricht)
    {
        this.sendeAnAlle(nachricht.toString());
    }
    
    @Override
    public void bearbeiteVerbindungsaufbau(String pClientIP, int pPartnerPort)
    {
        System.out.println("Ein neuer Client versucht zu verbinden (" + pClientIP + ")");
        if(_clientListe.size() < Spielkonstanten.MAX_SPIELERZAHL)
        {
            Client neuerClient = new Client(pClientIP, pPartnerPort, "unbekannt");
            _clientListe.add(neuerClient);
        }
        else
        {
            Paket info = new Paket(Pakettype.SV_TRENN_INFO);
            info.fuegeStringAn("Der Server ist voll.");
            this.sendeNachricht(pClientIP, pPartnerPort, info);
            this.beendeVerbindung(pClientIP, pPartnerPort);
        }
    }
    
    @Override
    public void bearbeiteVerbindungsende(String pClientIP, int pPartnerPort)
    {
        entferneClient(pClientIP, pPartnerPort);
    }
   
    @Override
    public void bearbeiteVerbindungsverlust(String pClientIP, int pPartnerPort)
    {
        entferneClient(pClientIP, pPartnerPort);
    }
    
    @Override
    public void bearbeiteNachricht(String pClientIP, int pPartnerPort, String pNachricht)
    {
        Client client = this.sucheClient(pClientIP, pPartnerPort);
        if(client != null)
        {
            Paket paket = new Paket(pNachricht);
            
            if(Pakettype.CL_START_INFO == paket.Typ() && !client.istImSpiel())
            {
                String name = paket.holeString();
                Client cl = this.sucheClient(name);
                if(cl == null)
                {
                    client.setzeName(name);
                    client.betrittSpiel();
                    _controller.bearbeiteNeuenSpieler(client.name());
                    System.out.println("'" + client.name() + "' hat das Spiel betreten");
                }
                else
                {
                    Paket info = new Paket(Pakettype.SV_TRENN_INFO);
                    info.fuegeStringAn("Der Name \"" + name + "\" wird bereits verwendet.");
                    this.sendeNachricht(pClientIP, pPartnerPort, info);
                    this.beendeVerbindung(pClientIP, pPartnerPort);
                }
            }
            else if(Pakettype.CL_CHAT_NACHRICHT == paket.Typ() && client.istImSpiel())
            {
                String text = paket.holeString();
                Paket antwort = new Paket(Pakettype.SV_CHAT_NACHRICHT);
                antwort.fuegeStringAn(client.name());
                antwort.fuegeStringAn(text);
                this.sendeRundnachricht(antwort);
                System.out.println(client.name() + ": " + text);
            }
            else if(Pakettype.CL_EINGABE == paket.Typ() && client.istImSpiel())
            {
                _controller.bearbeiteEingabe(client.name(), new SpielerEingabe(paket));
            }
        }
    }
    
    /**
     * Bearbeitet das Leerlauf-Ereignis
     */
    public void tick()
    {
        if (_controller == null)
        {
            return;
        }
        double aktZeit = System.currentTimeMillis();
        if(aktZeit - _letzteAktualisierung > Spielkonstanten.SPIELER_AKTUALISIERUNGS_INTERVALL)
        {
            _controller.bearbeiteLeerlauf();
            _letzteAktualisierung = aktZeit;
        }
        if(aktZeit - _letztesSnapshot > Spielkonstanten.SNAPSHOT_INTERVALL)
        {
            _controller.bearbeiteSnapshot();
            _letztesSnapshot = aktZeit;
        }
    }
    
    /**
     * Setzt den Name des Servers
     *
     * @param name neuer Server Name
     */
    public void setzeServerName(String name)
    {
        System.out.println("Server Name: " + name);
        _serverName = name;
    }
    
    /**
     * Gibt ein Paket mit den Serverinfos zurueck
     *
     * @return Paket mit Serverinfos
     */
    public Paket holeServerInfos()
    {
        Paket info = new Paket((char)0);
        info.fuegeStringAn(_serverName);
        info.fuegeZahlAn(Spielkonstanten.MAX_SPIELERZAHL);
        info.fuegeZahlAn(_clientListe.size());
        return info;
    }
    
    protected ArrayList<Client> clListe()
    {
        return _clientListe;
    }
}
