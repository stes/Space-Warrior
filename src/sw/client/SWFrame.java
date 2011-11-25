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

import sw.shared.Spielkonstanten;
import sw.shared.Pakettype;
import sw.shared.Paket;
import sw.shared.PlayerDataSet;

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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
* @author Redix, stes, Abbadonn
* @version 25.11.11
*/
public class SWFrame extends JFrame implements WindowListener, ClientListener
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
    
    private PlayingFieldGraphics _spielfeld;
    private GameController _spielController;
    
    //private NetClient _client;
    private ServerFinder _serverSucher;
    
    // Attribute
    
    /**
     * Konstruktor
     */
    public SWFrame()
    {
        //Initialisierung der Oberklasse
        super("Space Warrior");
        this.setSize(1400, 800);
        this.initKomponenten();
        
        this.setVisible(true);
        this.toFront();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        this.initEastereggs();
        //this.bildschirm().addWindowListener(this);
        this.aktualisiereServerListe();
    }
    
    /**
     * Die Variablen werden mit den Serverdaten angepasst
     */
    private void initNachVerbindung()
    {
        //_spielController = new SpielController(_client);  
        //_client.fuegeClientListenerHinzu(_spielController);
        //_client.fuegeClientListenerHinzu(this);
        _spielfeld = new PlayingFieldGraphics(_spielController.getPlayerList());
    }
    
    /**
     * Die Variablen der einzelnen Elemente der GUI werden gesetzt.
     */
    private void initKomponenten()
    {
        int chat = 700;
        
        _txtIPAdresse = new JTextField();
        _txtIPAdresse.setBounds(220, 10, 400, 25);
        this.add(_txtIPAdresse);
        
        _txtName = new JTextField();
        _txtName.setBounds(220, 50, 400, 25);
        this.add(_txtName);
        
        _txtChatnachricht = new JTextField("");
        _txtChatnachricht.setBounds(100, chat+100, 520, 25);
        ////_txtChatnachricht.eventhandlerblablabla("_txtChatnachrichtEingabeBestaetigt");
        this.add(_txtChatnachricht);
        
        _btnVerbinden = new JButton("Verbinden");
        _btnVerbinden.setBounds(640, 10, 100, 25);
        //_btnVerbinden.setzeBearbeiterGeklickt("_btnVerbindenGeklickt");
        this.add(_btnVerbinden);
        
        _btnChat = new JButton("Chat");
        _btnChat.setBounds(640, chat+100, 100, 25);
        //_btnChat.setzeBearbeiterGeklickt("_btnChatGeklickt");
        this.add(_btnChat);
        
        _btnAktualisieren = new JButton("Aktualisieren");
        _btnAktualisieren.setBounds(1100, 620, 100, 25);
        //_btnAktualisieren.setzeBearbeiterGeklickt("_btnAktualisierenGeklickt");
        this.add(_btnAktualisieren);
        
        _lblIPAdresse = new JLabel("IP-Adresse");
        _lblIPAdresse.setBounds(100, 10, 100, 25);
        this.add(_lblIPAdresse);
        
        _lblName = new JLabel("Name");
        _lblName.setBounds(100, 50, 100, 25);
        this.add(_lblName);
        
        _lstChatverlauf = new JTextArea();
        _lstChatverlauf.setBounds(100, chat, 645, 90);
        this.add(_lstChatverlauf);
        
        _punkteListe = new JTable(Spielkonstanten.MAX_SPIELERZAHL, 2);
        _punkteListe.setBounds(1100, 100, 200, 150);
        _punkteListe.getColumnModel().getColumn(0).setHeaderValue("Spieler");
        _punkteListe.getColumnModel().getColumn(1).setHeaderValue("Punkte");
        this.add(_punkteListe);
        
        _serverListe = new JTable(0, 3);
        _serverListe.setBounds(1100, 300, 200, 300);
        _serverListe.getColumnModel().getColumn(0).setHeaderValue("Server");
        _serverListe.getColumnModel().getColumn(1).setHeaderValue("Spieler/Max");
        _serverListe.getColumnModel().getColumn(0).setWidth(110);
        this.add(_serverListe);
        //_serverListe.setzeBearbeiterMarkierungGeaendert("_tblMarkierungGeaendert");
    }
    
    /**
    * guckt ob die Eingabe des Chats gedrückt wurde
    */
    public void _txtChatnachrichtEingabeBestaetigt()
    {
        this.verarbeiteEingabe();
    }
    
    /**
    * guckt ob der Verbindenbutton geklickt wurde
    */
    /*public void _btnVerbindenGeklickt()
    {
        if ( !_txtName.getText().isEmpty())
        {
            this.verbinde(_txtIPAdresse.getText(), _txtName.getText());
        }
 
    }*/
    
    /**
    * guckt ob der Chatbutton gedrückt wurde
    */
    public void _btnChatGeklickt()
    {
        this.verarbeiteEingabe();
    }
    
    /**
     * aktualisiert die Serverliste
     */
    private void aktualisiereServerListe()
    {
        if(_serverSucher == null)
        {
            //_serverListe.setzeZeilenanzahl(0);
            _btnAktualisieren.setEnabled(true);
            _serverSucher = new ServerFinder(this);
            _serverSucher.start();
        }
    }
    
    /**
    * guckt ob der Aktuilisierungsbutton geklickt wurde
    */
    public void _btnAktualisierenGeklickt()
    {
        this.aktualisiereServerListe();
    }
    
    private void initEastereggs()
    {
        OutputStream output = new OutputStream()
        {
            @Override
            public void write(int b) throws IOException
            {
                _lstChatverlauf.append(String.valueOf((char) b));
            }
            
            @Override
            public void write(byte[] b, int off, int len)
            {
                _lstChatverlauf.append(new String(b, off, len));
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
        String nachricht = _txtChatnachricht.getText();
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
                    _txtChatnachricht.setText("");
                }
            }
        }
        _txtChatnachricht.setText("");
    }
    
    /**
     * ändert die Markierung der angeklickten IPAddresse
     */
    public void _tblMarkierungGeaendert()
    {
        for(int i = 0; i <= _serverListe.getRowCount(); i++)
        {
            if(_serverListe.isRowSelected(i))
            {
                _txtIPAdresse.setText((String) _serverListe.getValueAt(i, 2));
            }
        }
    }
    
    /**
     * Die eingegebene Nachricht wird an den Chatverlauf gesendet.
     */
    public void sendeNachricht(String chatNachricht)
    {
        Paket chat = new Paket(Pakettype.CL_CHAT_NACHRICHT);
        chat.fuegeStringAn(chatNachricht);
        //_client.sendeNachricht(chat);
    }
    
    /**
     * Verbindet den Client mit einem Server.
     */
    /*private void verbinde(String ip, String name)
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
            _lstChatverlauf.append("Fehler: konnte nicht zum Server verbinden");
            _client.gibFrei();
            _client = null;
        }
    }*/
    
    private void deaktiviereElemente()
    {
        _txtIPAdresse.setEnabled(false);
        _txtName.setEnabled(false);
        _btnVerbinden.setEnabled(false);
        _lblIPAdresse.setEnabled(false);
        _lblName.setEnabled(false);
        _punkteListe.setVisible(true);
        _serverListe.setVisible(false);
        _btnAktualisieren.setEnabled(false);
    }
    
    private void aktiviereElemente()
    {
        _txtIPAdresse.setEnabled(true);
        _txtName.setEnabled(true);
        _btnVerbinden.setEnabled(true);
        _lblIPAdresse.setEnabled(true);
        _lblName.setEnabled(true);
        _punkteListe.setVisible(false);
        _serverListe.setVisible(true);
        _btnAktualisieren.setEnabled(true);
    }
    
    //@Override
    public void bearbeiteLeerlauf()
    {
        if (_spielfeld != null)
        {
            _spielfeld.repaint();
        }
        if(_serverSucher != null /*&& _uhr.verstricheneZeit() > 5000*/)
        {
            _serverSucher.gibFrei();
            _serverSucher = null;
            _btnAktualisieren.setEnabled(true);
        }
    }
    
    @Override
    public void windowClosing(WindowEvent e)
    {
        //if (_client != null)
        {
            //_client.gibFrei();
        }
        if (_serverSucher != null)
        {
            _serverSucher.gibFrei();
        }
    }
    
    public void bearbeiteServerGefunden(String serverIp, String serverName, int maxSpielerZahl, int spielerZahl)
    {
        //_serverListe.();  new line
        _serverListe.setValueAt(serverName, _serverListe.getRowCount()-1, 0);
        _serverListe.setValueAt(spielerZahl + "/" + maxSpielerZahl, _serverListe.getRowCount()-1, 1);
        _serverListe.setValueAt(serverIp, _serverListe.getRowCount()-1, 2);
    }
    
    @Override
    public void bearbeiteTrennung(String grund)
    {
        //if (_client != null)
        {
            //_client.gibFrei();
            //_client = null;
            //_spielfeld.verstecke();
            //_spielfeld.gibFrei();
        }
        this.aktiviereElemente();
        _lstChatverlauf.append(grund);
    }
    
    @Override
    public void bearbeiteChatNachricht(String name, String text)
    {
        _lstChatverlauf.append(name + ": " + text);
    }
    
    @Override
    public void editSnapshot(Paket paket)
    {
        if(_spielController.getPlayerList().zaehle() == 0)
            return;
        PlayerDataSet[] spielerScore = new PlayerDataSet[_spielController.getPlayerList().zaehle()];
        int anzahl = 0;
        for(int i = 0; i < Spielkonstanten.MAX_SPIELERZAHL; i++)
        {
            if(_spielController.getPlayerList().elementAn(i) != null)
            {
                spielerScore[anzahl] = _spielController.getPlayerList().elementAn(i);
                anzahl++;
            }
        }
        
        java.util.Arrays.sort(spielerScore);
        
        for(int i = 0; i < spielerScore.length; i++)
        {
            _punkteListe.setValueAt(spielerScore[spielerScore.length-i].name(), i, 0);
            _punkteListe.setValueAt(spielerScore[spielerScore.length-i].punkte(), i, 1);
        }
        for(int i = spielerScore.length; i < _punkteListe.getRowCount(); i++)
        {
            _punkteListe.setValueAt("", i, 0);
            _punkteListe.setValueAt("", i, 1);
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

