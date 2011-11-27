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

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import sw.client.components.PlayingFieldGraphics;
import sw.client.gui.GamePanel;
import sw.client.gui.LoginEvent;
import sw.client.gui.LoginListener;
import sw.client.gui.LoginPanel;
import sw.eastereggs.bf.BfInterpreter;
import sw.eastereggs.fortytwo.FortyTwo;
import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.Packet;
import sw.shared.data.PlayerDataSet;


/**
* @author Redix, stes, Abbadonn
* @version 25.11.11
*/
public class SWFrame extends JFrame implements WindowListener, ClientListener, LoginListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1575599799999464878L;
	
	// Objekte    
//    private BfInterpreter _bfInterpreter;
    
//    private JTextField _txtIPAdresse;
//    private JTextField _txtName;
//    private JTextField _txtChatmessage;
//    private AbstractButton _btnConnect;
//    private AbstractButton _btnChat;
//    private AbstractButton _btnUpdate;
//    private JLabel _lblIPAdress;
//    private JLabel _lblName;
//    private JTextArea _lstChathistory;
//    private JTable _tblPoints;
//    private JTable _tblServers;
    
//    private PlayingFieldGraphics _playingField;
    private GameController _controller;
    

    
    private SWClient _client;

    private GamePanel _gamePanel;
    private LoginPanel _loginPanel;
    private JPanel _activePanel;
    
    // Attribute
    
    /**
     * Konstruktor
     */
    public SWFrame()
    {
        //Initialisierung der Oberklasse
        super("Space Warrior");
        
        _client = new SWClient();
        _controller = new GameController(_client);
        _client.addClientListener(this);
        _client.addClientListener(_controller);
        
        this.setSize(1400, 900);
        
        _gamePanel = new GamePanel(1400, 900, _controller);
        _loginPanel = new LoginPanel(1400, 900);
        
        _loginPanel.addLoginListener(this);
      
        this.setGUIMode(GUIMode.LOGIN);

        //this.initComponents();
        //this.initAfterConnection(); for test purposes
        this.setVisible(true);
        this.toFront();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        try
        {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}

//        this.initEastereggs();
        this.updateServerList();
    }
      

    
//    /**
//	    * invoked after connect button is pressed
//	    */
//	    public void btnConnect_Action(ActionEvent e)
//	    {
//	        if ( !_txtName.getText().isEmpty())
//	        {
//	        	System.out.println("connect");
//	            this.verbinde(_txtIPAdresse.getText(), _txtName.getText());
//	        }
//	 
//	    }
//	    
//	    public void btnUpdate_Action(ActionEvent e)
//	    {
//	    	if (e.getID() == ActionEvent.ACTION_PERFORMED)
//	    	{
//	    		System.out.println("update server list");
//	    	}
//	    }
//    
//    @Override
//    public void chatMessage(String name, String text)
//    {
//        _lstChathistory.append(name + ": " + text);
//    }
//    
//    @Override
//    public void connected()
//    {
//    	this.disableComponents();
//        this.initAfterConnection();
//        
//        // ugly!!!
//        // indeed.
//        new Thread(new Runnable(){public void run() {while(true) { tick(); }}}).start();
//    }
    
    @Override
    public void disconnected()
    {
        /*if (_client != null)
        {
            _client.gibFrei();
            _client = null;
            _spielfeld.verstecke();
            _spielfeld.gibFrei();
        }*/
//        this.enableComponents();
//        _lstChathistory.append("connection lost");
    	System.out.println("connection lost");
    }
    

    
    /**
     * Die eingegebene Nachricht wird an den Chatverlauf gesendet.
     */
    public void sendChatmessage(String chatNachricht)
    {
        Packet chat = new Packet(Packettype.CL_CHAT_MSG);
        chat.addString(chatNachricht);
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
        this.repaint();
        
        /*for(int i = 0; i < spielerScore.length; i++)
        {
            _tblPoints.setValueAt(spielerScore[spielerScore.length-i].name(), i, 0);
            _tblPoints.setValueAt(spielerScore[spielerScore.length-i].punkte(), i, 1);
        }
        for(int i = spielerScore.length; i < _tblPoints.getRowCount(); i++)
        {
            _tblPoints.setValueAt("", i, 0);
            _tblPoints.setValueAt("", i, 1);
        }*/
    }
    
    public void tick()
    {
    }
    
    
    /**
    * guckt ob die Eingabe des Chats gedrückt wurde
    */
    public void txtChatmessage_Action(ActionEvent e)
    {
        System.out.println("chat textfeld");
    }
    
    @Override
    public void windowActivated(WindowEvent e) { }
    
    @Override
    public void windowClosed(WindowEvent e) { }
    
    @Override
    public void windowClosing(WindowEvent e) { } // TODO: shutdown
    
    @Override
    public void windowDeactivated(WindowEvent e) { }
    
    @Override
    public void windowDeiconified(WindowEvent e) { }
    
    @Override
    public void windowIconified(WindowEvent e) { }
    
    @Override
    public void windowOpened(WindowEvent e) { }
    
//    private void disableComponents()
//    {
//        _txtIPAdresse.setEnabled(false);
//        _txtName.setEnabled(false);
//        _btnConnect.setEnabled(false);
//        _lblIPAdress.setEnabled(false);
//        _lblName.setEnabled(false);
//        _tblPoints.setVisible(true);
//        _tblServers.setVisible(false);
//        _btnUpdate.setEnabled(false);
//    }
//    
//    private void enableComponents()
//    {
//        _txtIPAdresse.setEnabled(true);
//        _txtName.setEnabled(true);
//        _btnConnect.setEnabled(true);
//        _lblIPAdress.setEnabled(true);
//        _lblName.setEnabled(true);
//        _tblPoints.setVisible(false);
//        _tblServers.setVisible(true);
//        _btnUpdate.setEnabled(true);
//    }

     
//    private void initEastereggs()
//    {
//        OutputStream output = new OutputStream()
//        {
//            @Override
//            public void write(byte[] b, int off, int len)
//            {
//                _lstChathistory.append(new String(b, off, len));
//            }
//            
//            @Override
//            public void write(int b) throws IOException
//            {
//                _lstChathistory.append(String.valueOf((char) b));
//            }
//        };
//        
//        InputStream input = new InputStream()
//        {
//            @Override
//            public int read()
//            {
//                return 0;
//            }
//        };
//        
//        //System.setOut(new PrintStream(output, true));
//        // -.-
//        System.setIn(input);
//        
//        _bfInterpreter = new BfInterpreter();
//    }
    

    
    /**
     * aktualisiert die Serverliste
     */
    private void updateServerList()
    {
    }
    /**
     * Verbindet den Client mit einem Server.
     */
    private void verbinde(String ip, String name)
    {
        _client.connect(ip, GameConstants.STANDARD_PORT, name);
    }

	@Override
	public void connected()
	{
		this.setGUIMode(GUIMode.GAME);
	}

	@Override
	public void chatMessage(String name, String text)
	{
		// TODO Auto-generated method stub
		
	}    
	
	private enum GUIMode {LOGIN, GAME}
	private void setGUIMode(GUIMode mode)
	{
		if (_activePanel != null)
			this.remove(_activePanel);
		if (mode == GUIMode.LOGIN)
		{
			_activePanel = _loginPanel;
		}
		else if (mode == GUIMode.GAME)
		{
			_activePanel = _gamePanel;
		}
		this.add(_activePanel);
		this.setVisible(true);
		this.repaint();
	}



	@Override
	public void login(LoginEvent e)
	{
		this.verbinde(e.getIPAdress(), e.getLoginName());
	}
}
