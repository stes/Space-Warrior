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
package sw.server;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */

public class ServerGUI extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2967561560162321268L;
	private JTextArea _area;
	private JList/* <Client> */_clientList;
	private JScrollPane _scroll;
	private JButton _kickButton;
	private JTextField _nameField;
	private SWServer _server;

	/**
	 * erstellt eine GUI für die Serverkonsole
	 * 
	 * @param width
	 * @param height
	 */
	public ServerGUI(int width, int height)
	{
		super("SW Server");

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(0, 0, width, height);
		JPanel panel = (JPanel) this.getContentPane();
		panel.setPreferredSize(new Dimension(width, height));
		panel.setLayout(null);

		_area = new JTextArea();
		_area.setEditable(false);

		_scroll = new JScrollPane(_area);
		_scroll.setBounds(10, 10, width / 3 * 2 - 20, height - 90);

		_clientList = new JList/* <Client> */();
		_clientList.setBounds(20 + width / 3 * 2 - 20, 10, width / 3 - 30,
				height - 90);

		_kickButton = new JButton("Kick");
		_kickButton.setBounds(20 + width / 3 * 2 - 20, height - 70, 100, 20);
		_kickButton.addActionListener(this);

		_nameField = new JTextField();
		_nameField.setBounds(10, height - 70, width / 3 * 2 - 20, 20);
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
			public void write(byte[] b, int off, int len)
			{
				addMessage(new String(b, off, len));
			}

			@Override
			public void write(int b) throws IOException
			{
				addMessage(String.valueOf((char) b));
			}
		};

		System.setOut(new PrintStream(output, true));
		System.setErr(new PrintStream(output, true));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Kick"))
		{
			Client cl = (Client) _clientList.getSelectedValue();
			if (_server != null && cl != null)
			{
				_server.drop(cl, "You were kicked");
			}
		}
		else
		{
			_server.setServerName(e.getActionCommand());
			_nameField.setText("");
		}
	}

	/**
	 * setzt die Clientliste
	 * 
	 * @param data
	 */
	public void setClientList(Vector<Client> data)
	{
		_clientList.setListData(data);
	}

	/**
	 * setzt den Netserver
	 * 
	 * @param sWServer
	 */
	public void setNetServer(SWServer server)
	{
		_server = server;
	}

	/**
	 * fügt Systemnachrichten ein
	 * 
	 * @param str
	 *            neue Textnachricht
	 */
	private void addMessage(String str)
	{
		_area.append(str);
		_area.setCaretPosition(_area.getDocument().getLength());
	}
}
