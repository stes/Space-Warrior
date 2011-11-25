package sw.server;

import sw.shared.Spielkonstanten;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class ServerKonsole
{
    public static void main(String[] args)
    {
        NetServer _netServer = new NetServer(Spielkonstanten.STANDARD_PORT);
        ServerInfo _serverInfo = new ServerInfo(_netServer);
        _serverInfo.start();
        
        while (true)
        {
            _netServer.tick();
        }
    }
}
