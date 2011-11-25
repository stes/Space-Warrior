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
package sw.server;

import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import sw.shared.Spielkonstanten;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class Program implements WindowListener
{
    private ServerGUI _serverGUI;
    private NetServer _netServer;
    private ServerInfo _serverInfo;
    
    public Program()
    {
        _serverGUI = new ServerGUI(800, 400);
        _netServer = new NetServer(Spielkonstanten.STANDARD_PORT);
        _serverGUI.setNetServer(_netServer);
        _serverInfo = new ServerInfo(_netServer);
        _serverInfo.start();
        
        _serverGUI.addWindowListener(this);
    }
    
    public void run()
    {
        int letzteLaenge = 0;
        while (true)
        {
            ArrayList<Client> clientListe = _netServer.clListe();
            if(clientListe.size() != letzteLaenge)
            {
                Vector<Client> liste = new Vector<Client>();
                for(int i = 1; i <= clientListe.size(); i++)
                {
                    Client cur = clientListe.get(i);
                    liste.add(cur);
                }
                _serverGUI.setClientList(liste);
                letzteLaenge = clientListe.size();
            }
            _netServer.tick();
        }
    }
    
    public void windowClosing(WindowEvent e)
    {
        if (_netServer != null)
        {
            //_netServer.gibFrei();
            _serverInfo.gibFrei();
        }
    }
    
    public void windowActivated(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowOpened(WindowEvent e) { }

    public static void main(String[] args)
    {
        (new Program()).run();
    }
}
