package sw.server;

import java.net.DatagramSocket;
import java.net.DatagramPacket;

import sw.shared.Spielkonstanten;
import sw.shared.Paket;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class ServerInfo extends Thread
{
    private DatagramSocket _socket;
    private NetServer _netServer;
    private boolean _laeuft;

    // Konstruktor
    public ServerInfo(NetServer netServer)
    {
        _netServer = netServer;
        try
        {
            _socket = new DatagramSocket(Spielkonstanten.STANDARD_PORT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Dienste
    public void gibFrei()
    {
        _laeuft = false;
        try
        {
            _socket.close();
            _socket = null;
        }
        catch (Exception e)
        {
        }
    }
    
    public void run()
    {
        _laeuft = true;
        try
        {
            while(_laeuft)
            {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                _socket.receive(packet);
                String msg = new String(buffer, 0, packet.getLength());
                if(msg.equals(Spielkonstanten.SERVER_INFO_ANFRAGE))
                {
                    Paket info = _netServer.holeServerInfos();
                    byte[] buf = (Spielkonstanten.SERVER_INFO_ANTWORT + info.toString()).getBytes();
                    DatagramPacket response = new DatagramPacket(buf, buf.length);
                    response.setSocketAddress(packet.getSocketAddress());
                    _socket.send(response);
                }
            }
        }
        catch (Exception e)
        {
        }
    }
}
