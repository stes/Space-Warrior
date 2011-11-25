/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 *     Copyright (C) 2011 Redix stes
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

import sw.shared.Punkt;
import sw.shared.Spielkonstanten;
import sw.shared.Nachrichtentyp;
import sw.shared.Paket;
import sw.shared.SpielerDaten;

import sw.eastereggs.bf.*;
import sw.eastereggs.fortytwo.*;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
* @author Alex Belke, Dennis Sternberg, Steffen Schneider
* @version 15.11.11
*/
public class SWAnwendung extends JFrame implements WindowListener, ClientListener
{
    // Objekte    
    private BfInterpreter _bfInterpreter;
    private JTextField _txtIPAdresse;
    private JTextField _txtName;
    private JTextField _txtChatnachricht;
    private AbstractButton _btnVerbinden;
    private AbstractButton _btnChat;
    private AbstractButton _btnAktualisieren;
    private JLabel _lblIPAdresse;
    private JLabel _lblName;
    private JTextArea _lstChatverlauf;
    private JTable _punkteListe;
    private JTable _serverListe;
    
    private Spielfeld _spielfeld;
    private SpielController _spielController;
    
    private NetClient _client;
    private ServerSucher _serverSucher;
    
    // Attribute
    
    /**
     * Konstruktor
     */
    public SWAnwendung()
    {
        //Initialisierung der Oberklasse
        super();
        this.initKomponenten();
        this.initEastereggs();
        //this.bildschirm().addWindowListener(this);
        this.aktulisiereServerListe();
    }
    
    /**
     * Die Variablen werden mit den Serverdaten angepasst
     */
    private void initNachVerbindung()
    {
        _spielController = new SpielController(_client);  
        _client.fuegeClientListenerHinzu(_spielController);
        _client.fuegeClientListenerHinzu(this);
        _spielfeld = new Spielfeld(_spielController.spielerListe());
    }
    
    /**
     * Die Variablen der einzelnen Elemente der GUI werden gesetzt.
     */
    private void initKomponenten()
    {
        int chat = 700;
        
        _txtIPAdresse = new JTextField();
        _txtIPAdresse.setBounds(220, 10, 400, 25);
        
        _txtName = new JTextField();
        _txtName.setBounds(220, 50, 400, 25);
        
        _txtChatnachricht.setBounds(100, chat+100, 520, 25);
        
        _txtChatnachricht.eventhandlerblablabla("_txtChatnachrichtEingabeBestaetigt");
        _btnVerbinden = new JButton(640, 10, 100, 25, "Verbinden");
        _btnVerbinden.setzeBearbeiterGeklickt("_btnVerbindenGeklickt");
        _btnChat = new JButton(640, chat+100, 100, 25, "Chat");
        _btnChat.setzeBearbeiterGeklickt("_btnChatGeklickt");
        _btnAktualisieren = new JButton(1100, 620, 100, 25, "Aktualisieren");
        _btnAktualisieren.setzeBearbeiterGeklickt("_btnAktualisierenGeklickt");
        _lblIPAdresse = new Etikett(100, 10, 100, 25, "IP-Adresse");
        // Ausrichtung
        _lblIPAdresse.setzeAusrichtung(Ausrichtung.LINKS);
        
        _lblName = new Etikett(100, 50, 100, 25, "Name");
        // Ausrichtung
        _lblName.setzeAusrichtung(Ausrichtung.LINKS);
        
        _lstChatverlauf = new Zeilenbereich(100, chat, 645, 90, "");
        _lstChatverlauf.deaktiviere();
        
        _punkteListe = new Tabelle(1100, 100, 200, 150, Spielkonstanten.MAX_SPIELERZAHL, 2);
        _punkteListe.deaktiviere();
        _punkteListe.setzeSpaltentitelAn("Spieler", 1);
        _punkteListe.setzeSpaltentitelAn("Punkte", 2);
        _punkteListe.verstecke();
        
        _serverListe = new Tabelle(1100, 300, 200, 300, 0, 3);
        _serverListe.setzeSpaltentitelAn("Server", 1);
        _serverListe.setzeSpaltentitelAn("Spieler/Max", 2);
        _serverListe.setzeSpaltentitelAn("IP-Adresse", 3);
        _serverListe.setzeSpaltenbreite(110);
        _serverListe.setzeBearbeiterMarkierungGeaendert("_tblMarkierungGeaendert");
    }
    
    /**
    * guckt ob die Eingabe des Chats gedr�ckt wurde
    */
    public void _txtChatnachrichtEingabeBestaetigt()
    {
        this.verarbeiteEingabe();
    }
    
    /**
    * guckt ob der Verbindenbutton geklickt wurde
    */
    public void _btnVerbindenGeklickt()
    {
        if ( !_txtName.inhaltAlsText().isEmpty())
        {
            this.verbinde(_txtIPAdresse.inhaltAlsText(), _txtName.inhaltAlsText());
        }
 
    }
    
    /**
    * guckt ob der Chatbutton gedr�ckt wurde
    */
    public void _btnChatGeklickt()
    {
        this.verarbeiteEingabe();
    }
    
    /**
     * aktualisiert die Serverliste
     */
    private void aktulisiereServerListe()
    {
        if(_serverSucher == null)
        {
            _serverListe.setzeZeilenanzahl(0);
            _btnAktualisieren.deaktiviere();
            _uhr.starte();
            _serverSucher = new ServerSucher(this);
            _serverSucher.start();
        }
    }
    
    /**
    * guckt ob der Aktuilisierungsbutton geklickt wurde
    */
    public void _btnAktualisierenGeklickt()
    {
        this.aktulisiereServerListe();
    }
    
    private void initEastereggs()
    {
        OutputStream output = new OutputStream()
        {
            @Override
            public void write(int b) throws IOException
            {
                _lstChatverlauf.haengeAn(String.valueOf((char) b));
            }
            
            @Override
            public void write(byte[] b, int off, int len)
            {
                _lstChatverlauf.haengeAn(new String(b, off, len));
            }
        };
        
        InputStream input = new InputStream()
        {
            @Override
            public int read()
            {
                return 0;
            }
        };
        
        System.setOut(new PrintStream(output, true));
        System.setIn(input);
        
        _bfInterpreter = new BfInterpreter();
    }
	
    private void verarbeiteEingabe()
    {
        String nachricht = _txtChatnachricht.inhaltAlsText();
        if ( !nachricht.isEmpty())
        {
            if (!FortyTwo.answer(nachricht))
            {
                if (nachricht.startsWith("/"))
                {
                    if (nachricht.startsWith("/bf"))
                    {
                        System.out.println("[BF]" + nachricht);
                        _bfInterpreter.readCode(nachricht);
                    }
                    if (nachricht.startsWith("/exe"))
                    {
                        System.out.println("[BF] Execute");
                        _bfInterpreter.execute();
                    }
                }
                else
                {
                    sendeNachricht(nachricht);
                    _txtChatnachricht.loescheAlles();
                }
            }
        }
        _txtChatnachricht.loescheAlles();
    }
    
    /**
     * �ndert die Markierung der angeklickten IPAddresse
     */
    public void _tblMarkierungGeaendert()
    {
        for(int i = 1; i <= _serverListe.zeilenanzahl(); i++)
        {
            if(_serverListe.istZeileMarkiert(i))
            {
                _txtIPAdresse.setzeInhalt(_serverListe.inhaltAlsTextAn(i, 3));
            }
        }
    }
    
    /**
     * Die eingegebene Nachricht wird an den Chatverlauf gesendet.
     */
    public void sendeNachricht(String chatNachricht)
    {
        Paket chat = new Paket(Nachrichtentyp.CL_CHAT_NACHRICHT);
        chat.fuegeStringAn(chatNachricht);
        _client.sendeNachricht(chat);
    }
    
    /**
     * Verbindet den Client mit einem Server.
     */
    private void verbinde(String ip, String name)
    {
        if(_client != null)
            return;
        
        _client = new NetClient(ip, Spielkonstanten.STANDARD_PORT, name);
        
        if(_client.vorhanden())
        {
            this.deaktiviereElemente();
            this.initNachVerbindung();
        }
        else
        {
            _lstChatverlauf.haengeAn("Fehler: konnte nicht zum Server verbinden");
            _client.gibFrei();
            _client = null;
        }
    }
    
    private void deaktiviereElemente()
    {
        _txtIPAdresse.deaktiviere();
        _txtName.deaktiviere();
        _btnVerbinden.deaktiviere();
        _lblIPAdresse.deaktiviere();
        _lblName.deaktiviere();
        _punkteListe.zeige();
        _serverListe.verstecke();
        _btnAktualisieren.deaktiviere();
    }
    
    private void aktiviereElemente()
    {
        _txtIPAdresse.aktiviere();
        _txtName.aktiviere();
        _btnVerbinden.aktiviere();
        _lblIPAdresse.aktiviere();
        _lblName.aktiviere();
        _punkteListe.verstecke();
        _serverListe.zeige();
        _btnAktualisieren.aktiviere();
    }
    
    @Override
    public void bearbeiteLeerlauf()
    {
        if (_spielfeld != null)
        {
            _spielfeld.repaint();
        }
        if(_serverSucher != null && _uhr.verstricheneZeit() > 5000)
        {
            _serverSucher.gibFrei();
            _serverSucher = null;
            _btnAktualisieren.aktiviere();
        }
    }
    
    @Override
    public void windowClosing(WindowEvent e)
    {
        if (_client != null)
        {
            _client.gibFrei();
        }
        if (_serverSucher != null)
        {
            _serverSucher.gibFrei();
        }
    }
    
    public void bearbeiteServerGefunden(String serverIp, String serverName, int maxSpielerZahl, int spielerZahl)
    {
        _serverListe.haengeNeueZeileAn();
        _serverListe.setzeInhaltAn(serverName, _serverListe.zeilenanzahl(), 1);
        _serverListe.setzeInhaltAn(spielerZahl + "/" + maxSpielerZahl, _serverListe.zeilenanzahl(), 2);
        _serverListe.setzeInhaltAn(serverIp, _serverListe.zeilenanzahl(), 3);
    }
    
    @Override
    public void bearbeiteTrennung(String grund)
    {
        if (_client != null)
        {
            _client.gibFrei();
            _client = null;
            _spielfeld.verstecke();
            _spielfeld.gibFrei();
        }
        this.aktiviereElemente();
        _lstChatverlauf.haengeAn(grund);
    }
    
    @Override
    public void bearbeiteChatNachricht(String name, String text)
    {
        _lstChatverlauf.haengeAn(name + ": " + text);
    }
    
    @Override
    public void bearbeiteSnapshot(Paket paket)
    {
        if(_spielController.spielerListe().zaehle() == 0)
            return;
        SpielerDaten[] spielerScore = new SpielerDaten[_spielController.spielerListe().zaehle()];
        int anzahl = 0;
        for(int i = 0; i < Spielkonstanten.MAX_SPIELERZAHL; i++)
        {
            if(_spielController.spielerListe().elementAn(i) != null)
            {
                spielerScore[anzahl] = _spielController.spielerListe().elementAn(i);
                anzahl++;
            }
        }
        
        java.util.Arrays.sort(spielerScore);
        
        for(int i = 1; i <= spielerScore.length; i++)
        {
            _punkteListe.setzeInhaltAn(spielerScore[spielerScore.length-i].name(), i, 1);
            _punkteListe.setzeInhaltAn(spielerScore[spielerScore.length-i].punkte(), i, 2);
        }
        for(int i = spielerScore.length+1; i <= _punkteListe.zeilenanzahl(); i++)
        {
            _punkteListe.setzeInhaltAn("", i, 1);
            _punkteListe.setzeInhaltAn("", i, 2);
        }
    }
    
    @Override
    public void bearbeiteSchuss(Paket paket) {}

    @Override
    public void windowActivated(WindowEvent e) { }
    @Override
    public void windowClosed(WindowEvent e) { }
    @Override
    public void windowDeactivated(WindowEvent e) { }
    @Override
    public void windowDeiconified(WindowEvent e) { }
    @Override
    public void windowIconified(WindowEvent e) { }
    @Override
    public void windowOpened(WindowEvent e) { }
}

