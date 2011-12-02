/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011 Redix stes Abbadonn
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.client.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import sw.client.ClientListener;
import sw.client.GameController;
import sw.shared.data.Unpacker;

public class LoginPanel extends JPanel implements ClientListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2058618925690808825L;
	
	private LoginPanel _self;
	
	private JTextField _txtIPAdresse;
    private JTextField _txtName;
    private AbstractButton _btnConnect;
    private AbstractButton _btnUpdate;
    private JFileChooser _fileChooser;
    private AbstractButton _btnChooseAI;
    private JLabel _lblIPAdress;
    private JLabel _lblName;
    private JTable _tblServers;
    
    private ArrayList<LoginListener> _loginListener;
	
	public LoginPanel(int width, int height)
	{
		super();
		_self = this;
		this.setLayout(null);
		this.setSize(width, height);
		this.setBackground(Color.GREEN);
		
		_loginListener = new ArrayList<LoginListener>();
		
		this.initComponents();
		_txtName.setText("test");
		_txtIPAdresse.setText("localhost");
		this.repaint();
	}
	
	public void addLoginListener(LoginListener l)
	{
		_loginListener.add(l);
	}
	
	public void removeLoginListener(LoginListener l)
	{
		_loginListener.remove(l);
	}
	
	protected void invokeLogin(LoginEvent e)
	{
		if (_loginListener.size() == 0)
			return;
		for (LoginListener l : _loginListener)
			l.login(e);
			
	}
	
    public void foundServer(String serverIp, String serverName, int maxPlayers, int playerCount)
    {
        //_serverListe.();  new line
        _tblServers.setValueAt(serverName, _tblServers.getRowCount()-1, 0);
        _tblServers.setValueAt(playerCount + "/" + maxPlayers, _tblServers.getRowCount()-1, 1);
        _tblServers.setValueAt(serverIp, _tblServers.getRowCount()-1, 2);
    }
	
    public String getIP()
    {
    	return _txtIPAdresse.getText();
    }
    
    @Override
	public String getName()
    {
    	return _txtName.getText();
    }

    
    /**
     * Initializes the GUI components
     */
    private void initComponents()
    {       
        _txtIPAdresse = new JTextField();
        _txtIPAdresse.setBounds(220, 10, 400, 25);
        
        
        this.add(_txtIPAdresse);
        
        _txtName = new JTextField();
        _txtName.setBounds(220, 50, 400, 25);
        this.add(_txtName);

        
        _btnConnect = new JButton("Verbinden");
        _btnConnect.setBounds(640, 10, 100, 25);
        _btnConnect.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
		        if ( !_txtName.getText().isEmpty())
		        {
		        	System.out.println("connect");
		            _self.invokeLogin(new LoginEvent(this, _txtIPAdresse.getText(), _txtName.getText()));
		        }
			}
		});
        this.add(_btnConnect);
        
        _btnUpdate = new JButton("Aktualisieren");
        _btnUpdate.setBounds(1100, 620, 100, 25);
        _btnUpdate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				for (LoginListener l : _loginListener)
					l.scan();
			}
		});
        this.add(_btnUpdate);
        
        _lblIPAdress = new JLabel("IP-Adresse");
        _lblIPAdress.setBounds(100, 10, 100, 25);
        this.add(_lblIPAdress);
        
        _lblName = new JLabel("Name");
        _lblName.setBounds(100, 50, 100, 25);
        this.add(_lblName);
               
        _tblServers = new JTable(0, 3);
        _tblServers.setBounds(1100, 300, 200, 300);
        _tblServers.getColumnModel().getColumn(0).setHeaderValue("Server");
        _tblServers.getColumnModel().getColumn(1).setHeaderValue("Players/Max");
        _tblServers.getColumnModel().getColumn(0).setWidth(110);
        
        this.add(_tblServers);
        //_serverListe.setzeBearbeiterMarkierungGeaendert("_tblMarkierungGeaendert");
        
        _fileChooser = new JFileChooser(System.getProperty("user.dir"));
        
        _btnChooseAI = new JButton("Choose AI");
        _btnChooseAI.setBounds(100, 500, 100, 25);
        _btnChooseAI.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
			    //Handle open button action.
		        int returnVal = _fileChooser.showOpenDialog(_self);

		        if (returnVal == JFileChooser.APPROVE_OPTION)
		        {
		            GameController.setAIPlugin(_fileChooser.getSelectedFile());
		        }
			}
		});
        this.add(_btnChooseAI);
    }
    
    @Override
	public void serverInfo(Unpacker packet)
	{
    	String name = packet.readUTF();
    	int maxPlayers = packet.readShort();
    	int playerCount = packet.readShort();
    	
    	System.out.println(name + " - " + maxPlayers + " - " + playerCount);
	}

	@Override
	public void connected() {}
	@Override
	public void disconnected(String reason) {}
	@Override
	public void chatMessage(String name, String text) {}
	@Override
	public void shot(Unpacker packet) {}
	@Override
	public void snapshot(Unpacker packet) {}
}
