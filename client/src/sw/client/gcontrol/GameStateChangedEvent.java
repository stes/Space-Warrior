package sw.client.gcontrol;

import java.util.EventObject;

import sw.shared.data.GameWorld;
import sw.shared.data.PlayerData;

/**
 * @author Redix stes Abbadonn
 * @version 02.12.2011
 */
public class GameStateChangedEvent extends EventObject
{
	private static final long serialVersionUID = -6781713802525662904L;

	private PlayerData _localDataSet;
	private GameWorld _world;
	private PlayerData _winner;
	private PlayerData _loser;

	public GameStateChangedEvent(Object source)
	{
		super(source);
	}

	/**
	 * @return the _localDataSet
	 */
	public PlayerData getLocalDataSet()
	{
		return _localDataSet;
	}

	/**
	 * @param _localDataSet
	 *            the _localDataSet to set
	 */
	public void setLocalDataSet(PlayerData localDataSet)
	{
		this._localDataSet = localDataSet;
	}

	/**
	 * @return the _playerList
	 */
	public GameWorld getGameWorld()
	{
		return _world;
	}

	/**
	 * @param _playerList
	 *            the _playerList to set
	 */
	public void setGameWorld(GameWorld world)
	{
		_world = world;
	}

	/**
	 * @return the _winner
	 */
	public PlayerData getWinner()
	{
		return _winner;
	}

	/**
	 * @param _winner
	 *            the _winner to set
	 */
	public void setWinner(PlayerData winner)
	{
		_winner = winner;
	}

	/**
	 * @return the _loser
	 */
	public PlayerData getLoser()
	{
		return _loser;
	}

	/**
	 * @param _loser
	 *            the _loser to set
	 */
	public void setLoser(PlayerData loser)
	{
		_loser = loser;
	}
}
