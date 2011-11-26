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

import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.Packet;
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
    private JTextField _txtChatmessage;
    private AbstractButton _btnConnect;
    private AbstractButton _btnChat;
    private AbstractButton _btnUpdate;
    private JLabel _lblIPAdress;
    private JLabel _lblName;
    private JTextArea _lstChathistory;
    private JTable _tblPoints;
    private JTable _tblServers;
    
    private PlayingFieldGraphics _playingField;
    private GameController _controller;
    
    //private NetClient _client;
    private ServerFinder _serverFinder;
    
    // Attribute
    
    /**
     * Konstruktor
     */
    public SWFrame()
    {
        //Initialisierung der Oberklasse
        super("Space Warrior");
        this.setSize(1400, 800);
        this.initComponents();
        
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
        this.updateServerList();
    }
    
    /**
    * guckt ob der Aktuilisierungsbutton geklickt wurde
    */
    public void _btnAktualisierenGeklickt()
    {
        this.updateServerList();
    }
    
    /**
    * guckt ob der Chatbutton gedrückt wurde
    */
    public void _btnChatGeklickt()
    {
        this.processInput();
    }
    
    /**
     * ändert die Markierung der angeklickten IPAddresse
     */
    public void _tblMarkierungGeaendert()
    {
        for(int i = 0; i <= _tblServers.getRowCount(); i++)
        {
            if(_tblServers.isRowSelected(i))
            {
                _txtIPAdresse.setText((String) _tblServers.getValueAt(i, 2));
            }
        }
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
    * guckt ob die Eingabe des Chats gedrückt wurde
    */
    public void _txtChatnachrichtEingabeBestaetigt()
    {
        this.processInput();
    }
    
    @Override
    public void chatMessage(String name, String text)
    {
        _lstChathistory.append(name + ": " + text);
    }
    
    @Override
    public void connectionLost(String grund)
    {
        //if (_client != null)
        {
            //_client.gibFrei();
            //_client = null;
            //_spielfeld.verstecke();
            //_spielfeld.gibFrei();
        }
        this.enableComponents();
        _lstChathistory.append(grund);
    }
    
    public void foundServer(String serverIp, String serverName, int maxSpielerZahl, int spielerZahl)
    {
        //_serverListe.();  new line
        _tblServers.setValueAt(serverName, _tblServers.getRowCount()-1, 0);
        _tblServers.setValueAt(spielerZahl + "/" + maxSpielerZahl, _tblServers.getRowCount()-1, 1);
        _tblServers.setValueAt(serverIp, _tblServers.getRowCount()-1, 2);
    }
	
    /**
     * Die eingegebene Nachricht wird an den Chatverlauf gesendet.
     */
    public void sendChatmessage(String chatNachricht)
    {
        Packet chat = new Packet(Packettype.CL_CHAT_MSG);
        chat.fuegeStringAn(chatNachricht);
        //_client.sendeNachricht(chat);
    }
    
    @Override
    public void shot(Packet packet) {}
    
    @Override
    public void snapshot(Packet packet)
    {
        if(_controller.getPlayerList().count() == 0)
            return;
        PlayerDataSet[] spielerScore = new PlayerDataSet[_controller.getPlayerList().count()];
        int anzahl = 0;
        for(int i = 0; i < GameConstants.MAX_PLAYERS; i++)
        {
            if(_controller.getPlayerList().dataAt(i) != null)
            {
                spielerScore[anzahl] = _controller.getPlayerList().dataAt(i);
                anzahl++;
            }
        }
        
        java.util.Arrays.sort(spielerScore);
        
        for(int i = 0; i < spielerScore.length; i++)
        {
            _tblPoints.setValueAt(spielerScore[spielerScore.length-i].name(), i, 0);
            _tblPoints.setValueAt(spielerScore[spielerScore.length-i].punkte(), i, 1);
        }
        for(int i = spielerScore.length; i < _tblPoints.getRowCount(); i++)
        {
            _tblPoints.setValueAt("", i, 0);
            _tblPoints.setValueAt("", i, 1);
        }
    }
    
    //@Override
    public void tick()
    {
        if (_playingField != null)
        {
            _playingField.repaint();
        }
        if(_serverFinder != null /*&& _uhr.verstricheneZeit() > 5000*/)
        {
            _serverFinder.dispose();
            _serverFinder = null;
            _btnUpdate.setEnabled(true);
        }
    }
    
    @Override
    public void windowActivated(WindowEvent e) { }
    
    @Override
    public void windowClosed(WindowEvent e) { }
    
    @Override
    public void windowClosing(WindowEvent e)
    {
        //if (_client != null)
        {
            //_client.gibFrei();
        }
        if (_serverFinder != null)
        {
            _serverFinder.dispose();
        }
    }
    
    @Override
    public void windowDeactivated(WindowEvent e) { }
    
    @Override
    public void windowDeiconified(WindowEvent e) { }
    
    @Override
    public void windowIconified(WindowEvent e) { }
    
    @Override
    public void windowOpened(WindowEvent e) { }
    
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
    
    private void disableComponents()
    {
        _txtIPAdresse.setEnabled(false);
        _txtName.setEnabled(false);
        _btnConnect.setEnabled(false);
        _lblIPAdress.setEnabled(false);
        _lblName.setEnabled(false);
        _tblPoints.setVisible(true);
        _tblServers.setVisible(false);
        _btnUpdate.setEnabled(false);
    }

    private void enableComponents()
    {
        _txtIPAdresse.setEnabled(true);
        _txtName.setEnabled(true);
        _btnConnect.setEnabled(true);
        _lblIPAdress.setEnabled(true);
        _lblName.setEnabled(true);
        _tblPoints.setVisible(false);
        _tblServers.setVisible(true);
        _btnUpdate.setEnabled(true);
    }
    /**
     * Die Variablen werden mit den Serverdaten angepasst
     */
    private void initAfterConnection()
    {
        //_spielController = new SpielController(_client);  
        //_client.fuegeClientListenerHinzu(_spielController);
        //_client.fuegeClientListenerHinzu(this);
        _playingField = new PlayingFieldGraphics(_controller.getPlayerList());
        this.add(_playingField);
    }
    /**
     * Die Variablen der einzelnen Elemente der GUI werden gesetzt.
     */
    private void initComponents()
    {
        int chat = 700;
        
        _txtIPAdresse = new JTextField();
        _txtIPAdresse.setBounds(220, 10, 400, 25);
        this.add(_txtIPAdresse);
        
        _txtName = new JTextField();
        _txtName.setBounds(220, 50, 400, 25);
        this.add(_txtName);
        
        _txtChatmessage = new JTextField("");
        _txtChatmessage.setBounds(100, chat+100, 520, 25);
        ////_txtChatnachricht.eventhandlerblablabla("_txtChatnachrichtEingabeBestaetigt");
        this.add(_txtChatmessage);
        
        _btnConnect = new JButton("Verbinden");
        _btnConnect.setBounds(640, 10, 100, 25);
        //_btnVerbinden.setzeBearbeiterGeklickt("_btnVerbindenGeklickt");
        this.add(_btnConnect);
        
        _btnChat = new JButton("Chat");
        _btnChat.setBounds(640, chat+100, 100, 25);
        //_btnChat.setzeBearbeiterGeklickt("_btnChatGeklickt");
        this.add(_btnChat);
        
        _btnUpdate = new JButton("Aktualisieren");
        _btnUpdate.setBounds(1100, 620, 100, 25);
        //_btnAktualisieren.setzeBearbeiterGeklickt("_btnAktualisierenGeklickt");
        this.add(_btnUpdate);
        
        _lblIPAdress = new JLabel("IP-Adresse");
        _lblIPAdress.setBounds(100, 10, 100, 25);
        this.add(_lblIPAdress);
        
        _lblName = new JLabel("Name");
        _lblName.setBounds(100, 50, 100, 25);
        this.add(_lblName);
        
        _lstChathistory = new JTextArea();
        _lstChathistory.setBounds(100, chat, 645, 90);
        this.add(_lstChathistory);
        
        _tblPoints = new JTable(GameConstants.MAX_PLAYERS, 2);
        _tblPoints.setBounds(1100, 100, 200, 150);
        _tblPoints.getColumnModel().getColumn(0).setHeaderValue("Spieler");
        _tblPoints.getColumnModel().getColumn(1).setHeaderValue("Punkte");
        this.add(_tblPoints);
        
        _tblServers = new JTable(0, 3);
        _tblServers.setBounds(1100, 300, 200, 300);
        _tblServers.getColumnModel().getColumn(0).setHeaderValue("Server");
        _tblServers.getColumnModel().getColumn(1).setHeaderValue("Spieler/Max");
        _tblServers.getColumnModel().getColumn(0).setWidth(110);
        this.add(_tblServers);
        //_serverListe.setzeBearbeiterMarkierungGeaendert("_tblMarkierungGeaendert");
    }
    private void initEastereggs()
    {
        OutputStream output = new OutputStream()
        {
            @Override
            public void write(byte[] b, int off, int len)
            {
                _lstChathistory.append(new String(b, off, len));
            }
            
            @Override
            public void write(int b) throws IOException
            {
                _lstChathistory.append(String.valueOf((char) b));
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
    private void processInput()
    {
        String nachricht = _txtChatmessage.getText();
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
                    sendChatmessage(nachricht);
                    _txtChatmessage.setText("");
                }
            }
        }
        _txtChatmessage.setText("");
    }
    /**
     * aktualisiert die Serverliste
     */
    private void updateServerList()
    {
        if(_serverFinder == null)
        {
            //_serverListe.setzeZeilenanzahl(0);
            _btnUpdate.setEnabled(true);
            _serverFinder = new ServerFinder(this);
            _serverFinder.start();
        }
    }
}

