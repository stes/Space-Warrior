package sw.server;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */

public class ServerGUI extends JFrame implements ActionListener
{
	private JTextArea _area;
	private JList _clientList;
	private JScrollPane _scroll;
	private JButton _kickButton;
	private JTextField _nameField;
	private NetServer _netServer;
	
	/**
	 * erstellt eine GUI f�r die Serverkonsole
	 * 
	 * @param width 
	 * @param height
	 */
	public ServerGUI(int width, int height)
	{
		super("SW Server");
		
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setBounds(0,0,width,height);
		JPanel panel = (JPanel) this.getContentPane();
        panel.setPreferredSize(new Dimension(width,height));
        panel.setLayout(null);
        
		_area = new JTextArea();
		_area.setEditable(false);
		
		_scroll = new JScrollPane(_area);
		_scroll.setBounds(10, 10, width/3*2-20, height-90);
		
		_clientList = new JList();
		_clientList.setBounds(20+width/3*2-20, 10, width/3-30, height-90);
		
		_kickButton = new JButton("Kick");
		_kickButton.setBounds(20+width/3*2-20, height-70, 100, 20);
		_kickButton.addActionListener(this);
		
		_nameField = new JTextField();
		_nameField.setBounds(10, height-70, width/3*2-20, 20);
		_nameField.addActionListener(this);
		
	    panel.add(_scroll);
	    panel.add(_clientList);
	    panel.add(_kickButton);
	    panel.add(_nameField);
	    
	    this.setVisible(true);
	    this.toFront();
	    
		OutputStream output = new OutputStream()
		{
			@Override
			public void write(int b) throws IOException
			{
				addMessage(String.valueOf((char) b));
			}
			
			@Override
			public void write(byte[] b, int off, int len)
			{
				addMessage(new String(b, off, len));
			}
		};
	    
	    System.setOut(new PrintStream(output, true));
	    System.setErr(new PrintStream(output, true));
	}
	
	/**
	 * setzt den Netserver
	 * 
	 * @param netServer
	 */
	public void setNetServer(NetServer netServer)
	{
	    _netServer = netServer;
	}
	
	/**
	 * f�gt Systemnachrichten ein
	 * 
	 * @param str neue Textnachricht
	 */
	private void addMessage(String str)
	{
		_area.append(str);
		_area.setCaretPosition(_area.getDocument().getLength());
	}
	
	/**
	 * setzt die Clientliste
	 * 
	 * @param data
	 */
	public void setClientList(Vector<?> data)
	{
		_clientList.setListData(data);
	}
	
    @Override
    public void actionPerformed(ActionEvent e)
    {
    	if(e.getActionCommand().equals("Kick"))
    	{
    		Client cl = (Client)_clientList.getSelectedValue();
    		if(_netServer != null && cl != null)
    		{
    		    _netServer.beendeVerbindung(cl.ip(), cl.port());
    		}
    	}
    	else
    	{
    	    _netServer.setzeServerName(e.getActionCommand());
    	    _nameField.setText("");
        }
    }
}