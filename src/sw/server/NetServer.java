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

import sw.shared.GameConstants;
import sw.shared.Packet;
import sw.shared.Packettype;
import sw.shared.PlayerInput;

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
    
    @Override
    public void bearbeiteNachricht(String pClientIP, int pPartnerPort, String pNachricht)
    {
        Client client = this.sucheClient(pClientIP, pPartnerPort);
        if(client != null)
        {
            Packet packet = new Packet(pNachricht);
            
            if(Packettype.CL_START_INFO == packet.Typ() && !client.getIsPlaying())
            {
                String name = packet.holeString();
                Client cl = this.sucheClient(name);
                if(cl == null)
                {
                    client.setName(name);
                    client.enterGame();
                    _controller.bearbeiteNeuenSpieler(client.name());
                    System.out.println("'" + client.name() + "' hat das Spiel betreten");
                }
                else
                {
                    Packet info = new Packet(Packettype.SV_TRENN_INFO);
                    info.fuegeStringAn("Der Name \"" + name + "\" wird bereits verwendet.");
                    this.sendeNachricht(pClientIP, pPartnerPort, info);
                    this.beendeVerbindung(pClientIP, pPartnerPort);
                }
            }
            else if(Packettype.CL_CHAT_NACHRICHT == packet.Typ() && client.getIsPlaying())
            {
                String text = packet.holeString();
                Packet antwort = new Packet(Packettype.SV_CHAT_NACHRICHT);
                antwort.fuegeStringAn(client.name());
                antwort.fuegeStringAn(text);
                this.sendeRundnachricht(antwort);
                System.out.println(client.name() + ": " + text);
            }
            else if(Packettype.CL_EINGABE == packet.Typ() && client.getIsPlaying())
            {
                _controller.processPlayerInput(client.name(), new PlayerInput(packet));
            }
        }
    }
    
    @Override
    public void bearbeiteVerbindungsaufbau(String pClientIP, int pPartnerPort)
    {
        System.out.println("Ein neuer Client versucht zu verbinden (" + pClientIP + ")");
        if(_clientListe.size() < GameConstants.MAX_SPIELERZAHL)
        {
            Client neuerClient = new Client(pClientIP, pPartnerPort, "unbekannt");
            _clientListe.add(neuerClient);
        }
        else
        {
            Packet info = new Packet(Packettype.SV_TRENN_INFO);
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
    
    /**
     * Gibt ein Paket mit den Serverinfos zurueck
     *
     * @return Paket mit Serverinfos
     */
    public Packet holeServerInfos()
    {
        Packet info = new Packet((char)0);
        info.fuegeStringAn(_serverName);
        info.fuegeZahlAn(GameConstants.MAX_SPIELERZAHL);
        info.fuegeZahlAn(_clientListe.size());
        return info;
    }
    
    /**
     * Sendet eine Nachricht an einen Client
     * 
     * @param name Name des Empfängers
     * @param nachricht Die Nachricht
     */
    @Override
    public void sendeNachricht(String name, Packet nachricht)
    {
        Client client = this.sucheClient(name);
        if(client != null)
        {
            this.sendeNachricht(client.ip(), client.getPort(), nachricht);
        }
    }
    
    /**
     * Sendet eine Nachricht an alle verbundenen Clients
     * 
     * @param nachricht Die Nachricht
     */
    @Override
    public void sendeRundnachricht(Packet nachricht)
    {
        this.sendeAnAlle(nachricht.toString());
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
     * Bearbeitet das Leerlauf-Ereignis
     */
    public void tick()
    {
        if (_controller == null)
        {
            return;
        }
        double aktZeit = System.currentTimeMillis();
        if(aktZeit - _letzteAktualisierung > GameConstants.SPIELER_AKTUALISIERUNGS_INTERVALL)
        {
            _controller.tick();
            _letzteAktualisierung = aktZeit;
        }
        if(aktZeit - _letztesSnapshot > GameConstants.SNAPSHOT_INTERVALL)
        {
            _controller.broadcastSnapshots();
            _letztesSnapshot = aktZeit;
        }
    }
    
    protected ArrayList<Client> clListe()
    {
        return _clientListe;
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
            if(adresse.equals(cur.getAdress()))
            {
                if(cur.getIsPlaying())
                {
                    _controller.playerLeft(cur.name());
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
    private void sendeNachricht(String ip, int port, Packet nachricht)
    {
        //this.sendeAnEinen(ip, port, nachricht.toString());
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
            if(adresse.equals(cur.getAdress()))
                return cur;
        }
        return null;
    }
}
