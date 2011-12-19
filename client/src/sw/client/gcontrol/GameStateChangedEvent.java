package sw.client.gcontrol;

import java.util.EventObject;

import sw.shared.data.PlayerData;
import sw.shared.data.PlayerList;

/**
 * 
 * @author Redix stes Abbadonn
 * @version 02.12.2011
 */
public class GameStateChangedEvent extends EventObject
{
	private static final long serialVersionUID = -6781713802525662904L;

	private PlayerData _localDataSet;
	private PlayerList _playerList;
	private PlayerData _winner;
	private PlayerData _loser;

	public GameStateChangedEvent(Object source)
	{
		super(source);
	}

	
	// TODO format getters and setters
	/**
	 * @return the _localDataSet
	 */
	public PlayerData getLocalDataSet()
	{
		return _localDataSet;
	}

	/**
	 * @param _localDataSet the _localDataSet to set
	 */
	public void setLocalDataSet(PlayerData _localDataSet)
	{
		this._localDataSet = _localDataSet;
	}

	/**
	 * @return the _playerList
	 */
	public PlayerList getPlayerList()
	{
		return _playerList;
	}

	/**
	 * @param _playerList the _playerList to set
	 */
	public void setPlayerList(PlayerList _playerList)
	{
		this._playerList = _playerList;
	}

	/**
	 * @return the _winner
	 */
	public PlayerData getWinner()
	{
		return _winner;
	}

	/**
	 * @param _winner the _winner to set
	 */
	public void setWinner(PlayerData _winner)
	{
		this._winner = _winner;
	}

	/**
	 * @return the _loser
	 */
	public PlayerData getLoser()
	{
		return _loser;
	}

	/**
	 * @param _loser the _loser to set
	 */
	public void setLoser(PlayerData _loser)
	{
		this._loser = _loser;
	}
}