package sw.client;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import sw.shared.Spielkonstanten;
import sw.shared.Paket;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class ServerSucher extends Thread
{
    private SWAnwendung _anwendung;
    private DatagramSocket _socket;
    private boolean _laeuft;
    
    public ServerSucher(SWAnwendung anwendung)
    {
        try
        {
            _anwendung = anwendung;
            _socket = new DatagramSocket();
            _socket.setBroadcast(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
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
            String local = InetAddress.getLocalHost().getHostName();
            for(InetAddress a : InetAddress.getAllByName(local))
            {
                byte[] addr = a.getAddress();
                if(addr.length != 4 || addr[0] != (byte)192 || addr[1] != (byte)168)
                    continue;
                addr[3] = (byte)255;
                byte[] buffer = Spielkonstanten.SERVER_INFO_ANFRAGE.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                packet.setSocketAddress(new InetSocketAddress(InetAddress.getByAddress(addr), Spielkonstanten.STANDARD_PORT));
                _socket.send(packet);
            }
            
            while(_laeuft)
            {
                byte[] buf = new byte[1024];
                DatagramPacket response = new DatagramPacket(buf, buf.length);
                _socket.receive(response);
                int len = Math.min(response.getLength(), Spielkonstanten.SERVER_INFO_ANTWORT.length());
                String msg = new String(buf, 0, len);
                if(msg.equals(Spielkonstanten.SERVER_INFO_ANTWORT))
                {
                    Paket info = new Paket(new String(buf, len, response.getLength()));
                    _anwendung.bearbeiteServerGefunden(response.getAddress().getHostAddress().toString(),
                        info.holeString(), info.holeZahl(), info.holeZahl());
                }
            }
        }
        catch (Exception e)
        {
        }
    }
}
