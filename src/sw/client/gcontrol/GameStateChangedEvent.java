package sw.client.gcontrol;

import java.util.EventObject;

import sw.shared.data.PlayerDataSet;
import sw.shared.data.PlayerList;

/**
 * 
 * @author Redix stes Abbadonn
 * @version 02.12.2011
 */
public class GameStateChangedEvent extends EventObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6781713802525662904L;
	
	private PlayerDataSet _localDataSet;
	private PlayerList _playerList;
	
	public GameStateChangedEvent(
			Object source,
			PlayerDataSet localDataSet,
			PlayerList playerList)
	{
		super(source);
		_localDataSet = localDataSet;
		_playerList = playerList;
	}

	public PlayerDataSet getLocalDataSet()
	{
		return _localDataSet;
	}

	public PlayerList getPlayerList()
	{
		return _playerList;
	}
}
